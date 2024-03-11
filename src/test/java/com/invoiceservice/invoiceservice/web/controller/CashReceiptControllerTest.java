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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CashReceiptController.class)
class CashReceiptControllerTest {

    public static String URL = "/api/v1/cash";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CashReceiptController controller;

    @MockBean
    private CashReceiptServiceImpl service;
    @MockBean
    DocumentNumberServiceImpl documentNumberServiceImpl;
    @MockBean
    private InvoiceServiceImpl invoiceService;
    @MockBean
    private OrderDetailsServiceImpl orderDetailsServiceImpl;



    @Test
    void findCashReceiptsByInvoiceId() throws Exception{
        List<CashReceipt> receiptList = createCashReceiptList();
        Invoice invoice = createInvoice();
        when(invoiceService.findInvoiceById(anyLong())).thenReturn(Optional.of(invoice));
        when(service.findCashReceiptsByInvoice(invoice)).thenReturn(receiptList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/invoice/" + invoice.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value("T24 Nr.10"))
                .andExpect(status().isOk());

        verify(service,times(1)).findCashReceiptsByInvoice(invoice);
    }

    @Test
    void findCashReceiptsByInvoiceIdInvalidInvoice() throws Exception{
        Invoice invoice = null;
        when(invoiceService.findInvoiceById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(URL + "/invoice/" + 1 ))
                .andExpect(status().isNotFound());
        verify(service,times(0)).findCashReceiptsByInvoice(invoice);
    }
    @Test
    void findAllCashReceipts() throws Exception {
        List<CashReceipt> receiptList = createCashReceiptList();

        when(service.findAllCashReceipts()).thenReturn(receiptList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number").value("T24 Nr.10"))
                .andExpect(status().isOk());

        verify(service, times(1)).findAllCashReceipts();
    }

    @Test
    void findCashReceiptById() throws Exception {
        Optional<CashReceipt> optionalCashReceipt = Optional.of(createCashReceipt());

        when(service.findCashReceiptById(anyLong())).thenReturn(optionalCashReceipt);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2024-03-07"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value("T24 Nr.10"))
                .andExpect(status().isOk());

        verify(service, times(1)).findCashReceiptById(anyLong());
    }

    @Test
    void findCashReceiptByIdInvalid() throws Exception {

        when(service.findCashReceiptById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findCashReceiptById(null);
    }

    @Test
    void saveCashReceipt() throws Exception{
        CashReceipt cashReceipt = createCashReceipt();
        cashReceipt.setId(null);

        when(service.saveCashReceipt(cashReceipt)).thenReturn(cashReceipt);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(cashReceipt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveCashReceipt(cashReceipt);
    }

    @Test
    void saveCashReceiptIncorrectBindingResult() throws Exception{
        CashReceipt cashReceipt = createCashReceipt();
        cashReceipt.setId(null);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<CashReceipt> noResult = controller.saveCashReceipt(cashReceipt, bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, noResult.getStatusCode());
        Assertions.assertNull(noResult.getBody());
        verifyNoInteractions(service);
    }


    @Test
    void updateCashReceiptById() throws Exception{

        CashReceipt cashReceipt = createCashReceipt();

        when(service.findCashReceiptById(cashReceipt.getId())).thenReturn(Optional.of(cashReceipt));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(cashReceipt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(status().isCreated());

        verify(service, times(1)).updateCashReceipt(cashReceipt);
    }

    @Test
    void updateCashReceiptByIdInvalid() throws Exception{

        CashReceipt cashReceipt = createCashReceipt();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<CashReceipt> noResult = controller
                .updateCashReceiptById(1L, cashReceipt, bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, noResult.getStatusCode());
        Assertions.assertNull(noResult.getBody());
        verifyNoInteractions(service);
    }

    @Test
    void deleteCashReceiptById() throws Exception {
        Optional<CashReceipt> cashReceipt = Optional.of(createCashReceipt());
        when(service.findCashReceiptById(anyLong())).thenReturn(cashReceipt);
        when(service.isItLastCashReceipt(cashReceipt.get())).thenReturn(true);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(cashReceipt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteCashReceiptById(anyLong());
    }

    @Test
    void deleteCashReceiptByIdInvalidNotLastCashReceipt() throws Exception {
        Optional<CashReceipt> cashReceipt = Optional.of(createCashReceipt());
        when(service.findCashReceiptById(anyLong())).thenReturn(cashReceipt);
        when(service.isItLastCashReceipt(cashReceipt.get())).thenReturn(false);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(cashReceipt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).deleteCashReceiptById(anyLong());
    }
    @Test
    void deleteCashReceiptByIdInvalidId() throws Exception {
        when(service.findCashReceiptById(anyLong())).thenReturn(Optional.empty());
        ResultActions mvcRes = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/333"))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deleteCashReceiptById(anyLong());
    }

    private List<CashReceipt> createCashReceiptList() {
        List<CashReceipt> cashReceiptList = new ArrayList<>();
        cashReceiptList.add(createCashReceipt());
        cashReceiptList.add(createCashReceipt());
        return cashReceiptList;
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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}