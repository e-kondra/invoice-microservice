package com.invoiceservice.invoiceservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoiceservice.invoiceservice.business.service.impl.CashReceiptServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.DocumentNumberServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.InvoiceServiceImpl;
import com.invoiceservice.invoiceservice.business.service.impl.OrderDetailsServiceImpl;
import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.model.OrderDetails;
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
    @Autowired
    OrderDetailsController controller;

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
    void findOrderDetailsByInvoiceIdInvalidInvoiceId() throws Exception {
        Invoice invoice = null;
        when(invoiceService.findInvoiceById(anyLong())).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/invoice/" + 1 ))
                .andExpect(status().isNotFound());
        verify(service,times(0)).findOrderDetailsByInvoice(invoice);
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
    void findOrderDetailsByIdInvalid() throws Exception{

        when(service.findOrderDetailsById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findOrderDetailsById(anyLong());
    }

    @Test
    void saveOrderDetails() throws Exception {
        OrderDetails orderDetails = createOrderDetails();
        orderDetails.setId(null);

        when(service.saveOrderDetails(orderDetails)).thenReturn(orderDetails);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(asJsonString(orderDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveOrderDetails(orderDetails);
    }

    @Test
    void saveOrderDetailsInvalidBindingResult() throws Exception {
        OrderDetails orderDetails = createOrderDetails();
        orderDetails.setId(null);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<OrderDetails> noResult = controller.saveOrderDetails(orderDetails,bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, noResult.getStatusCode());
        Assertions.assertNull(noResult.getBody());
        verifyNoInteractions(invoiceService);
    }

    @Test
    void updateOrderDetailsById() throws Exception {
        OrderDetails orderDetails = createOrderDetails();
        orderDetails.setDescription("preparation for technical expertise");

        when(service.saveOrderDetails(orderDetails)).thenReturn(orderDetails);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(orderDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("preparation for technical expertise"))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveOrderDetails(orderDetails);
    }

    @Test
    void updateOrderDetailsByIdInvalidBindingResult() throws Exception {
        OrderDetails orderDetails = createOrderDetails();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<OrderDetails> noResult = controller
                .updateOrderDetailsById(1L, orderDetails, bindingResult);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, noResult.getStatusCode());
        Assertions.assertNull(noResult.getBody());
        verifyNoInteractions(invoiceService);
    }

    @Test
    void deleteOrderDetailsById() throws Exception {
        Optional<OrderDetails> orderDetails = Optional.of(createOrderDetails());
        when(service.findOrderDetailsById(anyLong())).thenReturn(orderDetails);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(orderDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteOrderDetailsById(anyLong());
    }

    @Test
    void deleteOrderDetailsByIdInvalidId() throws Exception{
        when(service.findOrderDetailsById(anyLong())).thenReturn(Optional.empty());
        ResultActions mvcRes = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/333"))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deleteOrderDetailsById(anyLong());
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
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}