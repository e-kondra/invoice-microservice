package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.InvoiceMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.InvoiceRepository;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.model.CashReceipt;
import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.model.OrderDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @InjectMocks
    InvoiceServiceImpl service;
    @Mock
    InvoiceRepository invoiceRepository;
    @Mock
    InvoiceMapStructMapper invoiceMapper;
    @Mock
    DocumentNumberServiceImpl documentNumberService;

    Invoice invoice;
    InvoiceDAO invoiceDAO;
    List<InvoiceDAO> invoiceDAOList;

    @BeforeEach
    public void init(){
        invoice = createInvoice();
        invoiceDAO = createInvoiceDAO();
        invoiceDAOList = createInvoiceDAOList();
    }
    private Invoice createInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setCar("TOYOTA COROLLA AB123C");
        invoice.setDate("2024-03-07");
        invoice.setNumber("TET24 Nr.10");
        invoice.setClientCode("38008181111");
        invoice.setClientAddress("Vilnius, Vilniaus g.1");
        invoice.setClientName("Vytautas Brangiausias");
        return invoice;
    }

    private InvoiceDAO createInvoiceDAO() {
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        invoiceDAO.setId(1L);
        invoiceDAO.setCar("TOYOTA COROLLA AB123C");
        invoiceDAO.setDate("2024-03-07");
        invoiceDAO.setNumber("TET24 Nr.10");
        invoiceDAO.setClientCode("38008181111");
        invoiceDAO.setClientAddress("Vilnius, Vilniaus g.1");
        invoiceDAO.setClientName("Vytautas Brangiausias");
        return invoiceDAO;
    }

    private List<InvoiceDAO> createInvoiceDAOList() {
        List<InvoiceDAO> invoiceDAOS = new ArrayList<>();
        invoiceDAOS.add(invoiceDAO);
        invoiceDAOS.add(invoiceDAO);
        return invoiceDAOS;
    }

    @Test
    void findAllInvoices() {
        when(invoiceRepository.findAll()).thenReturn(invoiceDAOList);
        when(invoiceMapper.invoiceDAOToInvoice(invoiceDAO)).thenReturn(invoice);

        List<Invoice> invoiceList = service.findAllInvoices();
        assertEquals(2, invoiceList.size());
        assertEquals(1L, invoiceList.get(0).getId());
        assertEquals("TOYOTA COROLLA AB123C", invoiceList.get(1).getCar());
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void saveInvoice() {
        when(invoiceRepository.save(invoiceDAO)).thenReturn(invoiceDAO);
        when(invoiceMapper.invoiceDAOToInvoice(invoiceDAO)).thenReturn(invoice);
        when(invoiceMapper.invoiceToInvoiceDAO(invoice)).thenReturn(invoiceDAO);

        Invoice savedInvoice = service.saveInvoice(invoice);
        assertTrue(service.hasNoMatch(invoice));
        assertEquals(invoice, savedInvoice);
        assertEquals(savedInvoice.getNumber(), invoice.getNumber());
        verify(invoiceRepository,times(1)).save(invoiceDAO);
    }

    @Test
    void saveInvoiceInvalidDuplicate() {
        Invoice invoiceTest = createInvoice();
        invoiceTest.setNumber("TET24 Nr.10");
        invoiceTest.setId(2L);
        when(invoiceRepository.findAll()).thenReturn(invoiceDAOList);
        assertThrows(HttpClientErrorException.class, () -> service.saveInvoice(invoiceTest));
        verify(invoiceRepository, times(0)).save(invoiceDAO);
    }

    @Test
    void updateInvoice() {
        when(invoiceRepository.save(invoiceDAO)).thenReturn(invoiceDAO);
        when(invoiceMapper.invoiceDAOToInvoice(invoiceDAO)).thenReturn(invoice);
        when(invoiceMapper.invoiceToInvoiceDAO(invoice)).thenReturn(invoiceDAO);

        Invoice savedInvoice = service.updateInvoice(invoice);
        assertTrue(service.hasNoMatch(invoice));
        assertEquals(invoice, savedInvoice);
        assertEquals(savedInvoice.getNumber(), invoice.getNumber());
        verify(invoiceRepository,times(1)).save(invoiceDAO);
    }

    @Test
    void updateInvoiceInvalidDuplicate() {
        Invoice invoiceTest = createInvoice();
        invoiceTest.setNumber("TET24 Nr.10");
        invoiceTest.setId(2L);
        when(invoiceRepository.findAll()).thenReturn(invoiceDAOList);
        assertThrows(HttpClientErrorException.class, () -> service.updateInvoice(invoiceTest));
        verify(invoiceRepository, times(0)).save(invoiceDAO);
    }

    @Test
    void findInvoiceById() {
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(invoiceDAO));
        when(invoiceMapper.invoiceDAOToInvoice(invoiceDAO)).thenReturn(invoice);

        Optional<Invoice> returnedInvoice = service.findInvoiceById(invoice.getId());
        assertEquals(invoice.getId(), returnedInvoice.get().getId());
        assertEquals(invoice.getNumber(), returnedInvoice.get().getNumber());
        verify(invoiceRepository, times(1)).findById(anyLong());
    }

    @Test
    void deleteInvoiceById() {
        service.deleteInvoiceById(anyLong());
        verify(invoiceRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void isItLastInvoice() {
        when(documentNumberService.buildPreviousInvoiceNumber()).thenReturn("TET24 Nr.10");
        assertTrue(service.isItLastInvoice(invoice));
    }
}