package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.CashReceiptMapStructMapper;
import com.invoiceservice.invoiceservice.business.mappers.InvoiceMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.CashReceiptRepository;
import com.invoiceservice.invoiceservice.business.repository.model.CashReceiptDAO;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.model.CashReceipt;
import com.invoiceservice.invoiceservice.model.Invoice;
import org.junit.jupiter.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CashReceiptServiceImplTest {

    @Mock
    CashReceiptRepository mockRepository;
    @InjectMocks
    private CashReceiptServiceImpl service;
    @Mock
    private CashReceiptMapStructMapper mapper;
    @Mock
    private InvoiceMapStructMapper invoiceMapper;
    @Mock
    DocumentNumberServiceImpl documentNumberService;

    CashReceiptDAO receiptDAO;
    CashReceipt  receipt;
    List<CashReceiptDAO> cashReceiptDAOS;
    Invoice invoice;
    InvoiceDAO invoiceDAO;


    @BeforeEach
    public void init(){
        invoice = createInvoice();
        invoiceDAO = createInvoiceDAO();
        receiptDAO = createCashReceiptDAO();
        receipt = createCashReceipt();
        cashReceiptDAOS = createCashReceiptDAOS();
    }

    private List<CashReceiptDAO> createCashReceiptDAOS() {
        List<CashReceiptDAO> listDAOS = new ArrayList<>();
        listDAOS.add(receiptDAO);
        listDAOS.add(receiptDAO);
        return listDAOS;
    }

    private CashReceipt createCashReceipt() {
        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.setId(1L);
        cashReceipt.setDate("2024-03-07");
        cashReceipt.setNumber("T24 Nr.10");
        cashReceipt.setAmount(220f);
        cashReceipt.setInvoice(createInvoice());
        return cashReceipt;
    }

    private CashReceiptDAO createCashReceiptDAO() {
        CashReceiptDAO cashReceiptDAO = new CashReceiptDAO();
        cashReceiptDAO.setId(1L);
        cashReceiptDAO.setDate("2024-03-07");
        cashReceiptDAO.setNumber("T24 Nr.10");
        cashReceiptDAO.setAmount(220f);
        cashReceiptDAO.setInvoice(createInvoiceDAO());
        return cashReceiptDAO;
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


    @Test
    void findCashReceiptsByInvoice() {
        when(invoiceMapper.invoiceToInvoiceDAO(invoice)).thenReturn(invoiceDAO);
        when(mockRepository.findByInvoice(invoiceDAO)).thenReturn(cashReceiptDAOS);
        when(mapper.cashReceiptDAOToCashReceipt(receiptDAO)).thenReturn(receipt);

        List<CashReceipt> receiptList = service.findCashReceiptsByInvoice(invoice);
        assertEquals(2, receiptList.size());
        verify(mockRepository,times(1)).findByInvoice(invoiceDAO);
    }

    @Test
    void findAllCashReceipts() {
        when(mockRepository.findAll()).thenReturn(cashReceiptDAOS);
        when(mapper.cashReceiptDAOToCashReceipt(receiptDAO)).thenReturn(receipt);

        List<CashReceipt> receiptList = service.findAllCashReceipts();
        assertEquals(2, receiptList.size());
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    void findCashReceiptById() {
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(receiptDAO));
        when(mapper.cashReceiptDAOToCashReceipt(receiptDAO)).thenReturn(receipt);

        Optional<CashReceipt> returnedCashReceipt = service.findCashReceiptById(receipt.getId());
        assertEquals(receipt.getId(), returnedCashReceipt.get().getId());
        assertEquals(receipt.getNumber(), returnedCashReceipt.get().getNumber());
        verify(mockRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveCashReceipt() {
        when(mockRepository.save(receiptDAO)).thenReturn(receiptDAO);
        when(mapper.cashReceiptDAOToCashReceipt(receiptDAO)).thenReturn(receipt);
        when(mapper.cashReceiptToCashReceiptDAO(receipt)).thenReturn(receiptDAO);

        CashReceipt savedCashReceipt = service.saveCashReceipt(receipt);
        assertTrue(service.hasNoMatch(savedCashReceipt));
        assertEquals(receipt, savedCashReceipt);
        assertEquals(savedCashReceipt.getNumber(), receipt.getNumber());
    }

    @Test
    void saveCashReceiptInvalid() {
        when(mockRepository.save(receiptDAO)).thenThrow(new IllegalArgumentException());
        when(mapper.cashReceiptToCashReceiptDAO(receipt)).thenReturn(receiptDAO);
            Assertions.assertThrows(IllegalArgumentException.class, () -> service.saveCashReceipt(receipt));
        verify(mockRepository, times(1)).save(receiptDAO);
    }

    @Test
    void saveCashReceiptInvalidDuplicate() {
        CashReceipt cashReceipt = createCashReceipt();
        cashReceipt.setNumber("T24 Nr.10");
        cashReceipt.setId(2L);
        when(mockRepository.findAll()).thenReturn(cashReceiptDAOS);
        assertThrows(HttpClientErrorException.class, () -> service.saveCashReceipt(cashReceipt));
        verify(mockRepository, times(0)).save(receiptDAO);
    }

    @Test
    void updateCashReceipt() {
        when(mockRepository.save(receiptDAO)).thenReturn(receiptDAO);
        when(mapper.cashReceiptDAOToCashReceipt(receiptDAO)).thenReturn(receipt);
        when(mapper.cashReceiptToCashReceiptDAO(receipt)).thenReturn(receiptDAO);

        CashReceipt savedCashReceipt = service.updateCashReceipt(receipt);
        assertTrue(service.hasNoMatch(savedCashReceipt));
        assertEquals(receipt, savedCashReceipt);
        assertEquals(savedCashReceipt.getNumber(), receipt.getNumber());
    }

    @Test
    void updateCashReceiptInvalidDuplicate() {
        CashReceipt cashReceipt = createCashReceipt();
        cashReceipt.setNumber("T24 Nr.10");
        cashReceipt.setId(2L);
        when(mockRepository.findAll()).thenReturn(cashReceiptDAOS);
        assertThrows(HttpClientErrorException.class, () -> service.updateCashReceipt(cashReceipt));
        verify(mockRepository, times(0)).save(receiptDAO);
    }

    @Test
    void deleteCashReceiptById() {
        service.deleteCashReceiptById(anyLong());
        verify(mockRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void isItLastCashReceipt() {
        when(documentNumberService.buildPreviousCashReceiptNumber()).thenReturn("T24 Nr.10");
        assertTrue(service.isItLastCashReceipt(receipt));
    }
}