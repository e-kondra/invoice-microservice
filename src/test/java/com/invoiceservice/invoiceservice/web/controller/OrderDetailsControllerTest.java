package com.invoiceservice.invoiceservice.web.controller;

import com.invoiceservice.invoiceservice.business.service.impl.CashReceiptServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.DocumentNumberServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.InvoiceServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.OrderDetailsServiceImpl;
import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.model.OrderDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderDetailsController.class)
class OrderDetailsControllerTest {

    public static String URL = "/api/v1/details";

    @MockBean
    private OrderDetailsServiceImpl service;
    @MockBean
    private InvoiceServiceImpl invoiceService;
    @MockBean
    private CashReceiptServiceImpl CashReceiptService;
    @MockBean
    private DocumentNumberServiceImpl documentService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void findOrderDetailsByInvoiceId() throws Exception {
        Invoice invoice = createInvoice();
        List<OrderDetails> detailsList = createOrderDetailsList();
        when(invoiceService.findInvoiceById(anyLong())).thenReturn(Optional.of(invoice));
        when(service.findOrderDetailsByInvoice(invoice)).thenReturn(detailsList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/invoice/" + invoice.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("car repair"))
                .andExpect(status().isOk());
        verify(service,times(1)).findOrderDetailsByInvoice(invoice);
    }

    @Test
    void findOrderDetailsById() throws Exception{
        Optional<OrderDetails> optionalDetails = Optional.of(createOrderDetails());

        when(service.findOrderDetailsById(anyLong())).thenReturn(optionalDetails);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("car repair"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(220f))
                .andExpect(status().isOk());

        verify(service, times(1)).findOrderDetailsById(anyLong());
    }

    @Test
    void saveOrderDetails() {
    }

    @Test
    void updateOrderDetailsById() {
    }

    @Test
    void deleteOrderDetailsById() {
    }

    private List<OrderDetails> createOrderDetailsList(){
        List<OrderDetails> details = new ArrayList<>();
        details.add(createOrderDetails());
        details.add(createOrderDetails());
        return details;
    }

    private OrderDetails createOrderDetails() {
        OrderDetails details = new OrderDetails();
        details.setId(1L);
        details.setInvoice(createInvoice());
        details.setDescription("car repair");
        details.setPrice(220f);
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
}