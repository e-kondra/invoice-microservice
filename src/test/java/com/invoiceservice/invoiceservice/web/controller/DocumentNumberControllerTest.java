package com.invoiceservice.invoiceservice.web.controller;

import com.invoiceservice.invoiceservice.business.service.impl.CashReceiptServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.DocumentNumberServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.InvoiceServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.OrderDetailsServiceImpl;
import com.invoiceservice.invoiceservice.model.DocumentNumber;
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

import java.util.Optional;

import static java.awt.SystemColor.text;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentNumberController.class)
class DocumentNumberControllerTest {
    public static String URL = "/api/v1/document";
    @MockBean
    DocumentNumberServiceImpl service;
    @MockBean
    private OrderDetailsServiceImpl orderDetailsService;
    @MockBean
    private InvoiceServiceImpl invoiceService;
    @MockBean
    private CashReceiptServiceImpl CashReceiptService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void findInvoiceNumber() throws Exception {
        DocumentNumber documentNumber = createDocumentNumber();
        when(service.getDocumentNumber()).thenReturn(Optional.of(documentNumber));
        when(service.buildInvoiceNumber(documentNumber)).thenReturn("TET24 Nr.3");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/invoice"))
                .andExpect(content().string("TET24 Nr.3"))
                .andExpect(status().isOk());
        verify(service, times(1)).buildInvoiceNumber(documentNumber);
    }
    @Test
    void findInvoiceNumberInvalidDocument() throws Exception {
        DocumentNumber documentNumber = createDocumentNumber();
        when(service.getDocumentNumber()).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/invoice"))
                .andExpect(status().isNotFound());
        verify(service, times(0)).buildInvoiceNumber(documentNumber);
    }

    @Test
    void findInvoiceNumberInvalidStr() throws Exception {
        DocumentNumber documentNumber = createDocumentNumber();
        when(service.getDocumentNumber()).thenReturn(Optional.of(documentNumber));
        when(service.buildInvoiceNumber(documentNumber)).thenReturn("");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/invoice"))
                .andExpect(status().isNoContent());
        verify(service, times(1)).buildInvoiceNumber(documentNumber);
    }

    @Test
    void findCashReceiptNumber() throws Exception{
        DocumentNumber documentNumber = createDocumentNumber();
        when(service.getDocumentNumber()).thenReturn(Optional.of(documentNumber));
        when(service.buildCashReceiptNumber(documentNumber)).thenReturn("TET24 Nr.2");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/cash"))
                .andExpect(content().string("TET24 Nr.2"))
                .andExpect(status().isOk());
        verify(service, times(1)).buildCashReceiptNumber(documentNumber);
    }

    @Test
    void findCashReceiptNumberInvalidDocument() throws Exception {
        DocumentNumber documentNumber = createDocumentNumber();
        when(service.getDocumentNumber()).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/cash"))
                .andExpect(status().isNotFound());
        verify(service, times(0)).buildCashReceiptNumber(documentNumber);
    }

    @Test
    void findCashReceiptNumberInvalidStr() throws Exception {
        DocumentNumber documentNumber = createDocumentNumber();
        when(service.getDocumentNumber()).thenReturn(Optional.of(documentNumber));
        when(service.buildCashReceiptNumber(documentNumber)).thenReturn("");

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/cash"))
                .andExpect(status().isNoContent());
        verify(service, times(1)).buildCashReceiptNumber(documentNumber);
    }

    private DocumentNumber createDocumentNumber() {
        DocumentNumber documentNumber = new DocumentNumber();
        documentNumber.setId(1L);
        documentNumber.setInvoiceNumber(3);
        documentNumber.setCashReceiptNumber(2);
        return documentNumber;
    }


}