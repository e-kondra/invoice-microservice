package com.invoiceservice.invoiceservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoiceservice.invoiceservice.business.service.impl.CashReceiptServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.DocumentNumberServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.InvoiceServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.OrderDetailsServiceImpl;
import com.invoiceservice.invoiceservice.model.CashReceipt;
import com.invoiceservice.invoiceservice.model.Invoice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private InvoiceController controller;
    public static String URL = "/api/v1/invoice";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InvoiceServiceImpl invoiceService;
    @MockBean
    private CashReceiptServiceImpl cashReceiptServiceImpl;
    @MockBean
    DocumentNumberServiceImpl documentNumberServiceImpl;
    @MockBean
    private OrderDetailsServiceImpl orderDetailsServiceImpl;


    @Test
    void findAllInvoices() throws Exception {
        List<Invoice> invoiceList = createInvoiceList();

        when(invoiceService.findAllInvoices()).thenReturn(invoiceList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value("TET24 Nr.10"))
                .andExpect(status().isOk());

        verify(invoiceService, times(1)).findAllInvoices();
    }


    @Test
    void findInvoiceById() throws Exception {
        Optional<Invoice> optionalInvoice = Optional.of(createInvoice());

        when(invoiceService.findInvoiceById(anyLong())).thenReturn(optionalInvoice);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2024-03-07"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("TET24 Nr.10"))
                .andExpect(status().isOk());

        verify(invoiceService, times(1)).findInvoiceById(anyLong());

    }

    @Test
    void findInvoiceByIdInvalidId() throws Exception {
        Optional<Invoice> optionalInvoice = Optional.of(createInvoice());
        optionalInvoice.get().setId(null);

        when(invoiceService.findInvoiceById(anyLong())).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(invoiceService, times(0)).findInvoiceById(null);
    }

    @Test
    void saveInvoice() throws Exception {
        Invoice invoice = createInvoice();
        invoice.setId(null);

        when(invoiceService.saveInvoice(invoice)).thenReturn(invoice);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(invoice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(invoiceService, times(1)).saveInvoice(invoice);
    }

    @Test
    void saveInvoiceInvalidBindingResult() throws Exception {
        Invoice invoice = createInvoice();
        invoice.setId(null);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Invoice> noResult = controller.saveInvoice(invoice, bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, noResult.getStatusCode());
        Assertions.assertNull(noResult.getBody());
        verifyNoInteractions(invoiceService);
    }


    @Test
    void updateInvoiceById() throws Exception {
        Invoice invoice = createInvoice();
        invoice.setCar("VOLKSWAGEN PASSAT AB123C");

        when(invoiceService.updateInvoice(invoice)).thenReturn(invoice);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(invoice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.car").value("VOLKSWAGEN PASSAT AB123C"))
                .andExpect(status().isCreated());

        verify(invoiceService, times(1)).updateInvoice(invoice);

    }

    @Test
    void updateInvoiceByIdIncorrectBindingResult() throws Exception {
        Invoice invoice = createInvoice();
        invoice.setId(null);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<Invoice> noResult = controller.updateInvoiceById(1l, invoice, bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, noResult.getStatusCode());
        Assertions.assertNull(noResult.getBody());
        verifyNoInteractions(invoiceService);
    }

    @Test
    void deleteInvoiceById() throws Exception {
        Optional<Invoice> invoice = Optional.of(createInvoice());
        when(invoiceService.findInvoiceById(anyLong())).thenReturn(invoice);
        when(invoiceService.isItLastInvoice(invoice.get())).thenReturn(true);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(invoice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(invoiceService, times(1)).deleteInvoiceById(anyLong());
    }

    @Test
    void deleteInvoiceByIdInvalidNotLastInvoice() throws Exception {
        Optional<Invoice> invoice = Optional.of(createInvoice());
        when(invoiceService.findInvoiceById(anyLong())).thenReturn(invoice);
        when(invoiceService.isItLastInvoice(invoice.get())).thenReturn(false);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(invoice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(invoiceService, times(0)).deleteInvoiceById(anyLong());
    }

    @Test
    void deleteInvoiceByIdInvalidId() throws Exception {
        when(invoiceService.findInvoiceById(anyLong())).thenReturn(Optional.empty());
        ResultActions mvcRes = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/333"))
                .andExpect(status().isNotFound());

        verify(invoiceService, times(0)).deleteInvoiceById(anyLong());
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


    private List<Invoice> createInvoiceList() {
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(createInvoice());
        invoices.add(createInvoice());
        return invoices;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}