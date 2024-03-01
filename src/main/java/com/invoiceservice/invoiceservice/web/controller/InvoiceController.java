package com.invoiceservice.invoiceservice.web.controller;


import com.invoiceservice.invoiceservice.business.service.InvoiceService;
import com.invoiceservice.invoiceservice.business.service.impl.InvoiceServiceImpl;
import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.swagger.DescriptionVariables;
import com.invoiceservice.invoiceservice.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {DescriptionVariables.INVOICE})
@Log4j2
@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceController {

    @Autowired
    InvoiceServiceImpl invoiceService;


    @GetMapping
    @ApiOperation(value = "Finds all invoices",
            notes = "Returns the entire list of invoices",
            response = Invoice.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<Invoice>> findAllClients() {
        log.info("Retrieve list of invoices");
        List<Invoice> invoiceList = invoiceService.findAllInvoices();
        if (invoiceList.isEmpty()) log.warn("Invoice list is empty! {}", invoiceList);
        else log.debug("Invoice list is found. Size: {}", invoiceList::size);
        return ResponseEntity.ok(invoiceList);
    }

}
