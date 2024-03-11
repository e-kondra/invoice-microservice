package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.CashReceiptMapStructMapper;
import com.invoiceservice.invoiceservice.business.mappers.InvoiceMapStructMapper;
import com.invoiceservice.invoiceservice.business.mappers.OrderDetailsMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.CashReceiptRepository;
import com.invoiceservice.invoiceservice.business.repository.OrderDetailsRepository;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.business.repository.model.OrderDetailsDAO;
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
class OrderDetailsServiceImplTest {

    @Mock
    OrderDetailsRepository mockRepository;
    @InjectMocks
    private OrderDetailsServiceImpl service;
    @Mock
    private OrderDetailsMapStructMapper mapper;
    @Mock
    private InvoiceMapStructMapper invoiceMapper;

    Invoice invoice;
    InvoiceDAO invoiceDAO;
    OrderDetails orderDetails;
    OrderDetailsDAO orderDetailsDAO;
    List<OrderDetailsDAO> orderDetailsDAOS;

    @BeforeEach
    public void init(){
        invoice = createInvoice();
        invoiceDAO = createInvoiceDAO();
        orderDetailsDAO = createOrderDetailsDAO();
        orderDetails = createOrderDetails();
        orderDetailsDAOS = createOrderDetailsDAOS();
    }

    private List<OrderDetailsDAO> createOrderDetailsDAOS() {
        List<OrderDetailsDAO> listDAOS = new ArrayList<>();
        listDAOS.add(orderDetailsDAO);
        listDAOS.add(orderDetailsDAO);
        return listDAOS;
    }

    private OrderDetailsDAO createOrderDetailsDAO() {
        OrderDetailsDAO detailsDAO = new OrderDetailsDAO();
        detailsDAO.setId(1L);
        detailsDAO.setInvoice(invoiceDAO);
        detailsDAO.setDescription("car repair");
        detailsDAO.setPrice(220);
        detailsDAO.setQuantity(1);
        return detailsDAO;
    }

    private OrderDetails createOrderDetails() {
        OrderDetails details = new OrderDetails();
        details.setId(1L);
        details.setInvoice(invoice);
        details.setDescription("car repair");
        details.setPrice(220);
        details.setQuantity(1);
        return details;
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
    void findOrderDetailsByInvoice() {
        when(invoiceMapper.invoiceToInvoiceDAO(invoice)).thenReturn(invoiceDAO);
        when(mockRepository.findByInvoice(invoiceDAO)).thenReturn(orderDetailsDAOS);
        when(mapper.orderDetailsDAOToOrderDetails(orderDetailsDAO)).thenReturn(orderDetails);

        List<OrderDetails> orderDetailsList  = service.findOrderDetailsByInvoice(invoice);
        assertEquals(2, orderDetailsList.size());
        verify(mockRepository,times(1)).findByInvoice(invoiceDAO);
    }

    @Test
    void findOrderDetailsById() {
        when(mockRepository.findById(anyLong())).thenReturn(Optional.of(orderDetailsDAO));
        when(mapper.orderDetailsDAOToOrderDetails(orderDetailsDAO)).thenReturn(orderDetails);

        Optional<OrderDetails> returnedOrderDetails= service.findOrderDetailsById(orderDetails.getId());
        assertEquals(orderDetails.getId(), returnedOrderDetails.get().getId());
        assertEquals(orderDetails.getDescription(), returnedOrderDetails.get().getDescription());
        verify(mockRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveOrderDetails() {
        when(mockRepository.save(orderDetailsDAO)).thenReturn(orderDetailsDAO);
        when(mapper.orderDetailsDAOToOrderDetails(orderDetailsDAO)).thenReturn(orderDetails);
        when(mapper.orderDetailsToOrderDetailsDAO(orderDetails)).thenReturn(orderDetailsDAO);

        OrderDetails savedOrderDetails = service.saveOrderDetails(orderDetails);
        assertTrue(service.hasNoMatch(orderDetails));
        assertEquals(orderDetails, savedOrderDetails);
        assertEquals(savedOrderDetails.getDescription(), orderDetails.getDescription());
        verify(mockRepository,times(1)).save(orderDetailsDAO);
    }

    @Test
    void saveCashReceiptInvalidDuplicate() {
        OrderDetails orderDetailsForTest = createOrderDetails();
        orderDetailsForTest.setDescription("car repair");
        orderDetailsForTest.setId(2L);
        when(mockRepository.findAll()).thenReturn(orderDetailsDAOS);
        assertThrows(HttpClientErrorException.class, () -> service.saveOrderDetails(orderDetailsForTest));
        verify(mockRepository, times(0)).save(orderDetailsDAO);
    }

    @Test
    void deleteOrderDetailsById() {
        service.deleteOrderDetailsById(anyLong());
        verify(mockRepository, times(1)).deleteById(anyLong());
    }

}