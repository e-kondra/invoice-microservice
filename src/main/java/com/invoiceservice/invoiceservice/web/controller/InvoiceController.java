package com.invoiceservice.invoiceservice.web.controller;


import com.invoiceservice.invoiceservice.business.service.InvoiceService;
import com.invoiceservice.invoiceservice.business.service.impl.InvoiceServiceImpl;
import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.swagger.DescriptionVariables;
import com.invoiceservice.invoiceservice.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<Invoice>> findAllInvoices() {
        log.info("Retrieve list of invoices");
        List<Invoice> invoiceList = invoiceService.findAllInvoices();
        if (invoiceList.isEmpty()) log.warn("Invoice list is empty! {}", invoiceList);
        else log.debug("Invoice list is found. Size: {}", invoiceList::size);
        return ResponseEntity.ok(invoiceList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find the invoice by id",
            notes = "Provide an id to search specific invoice in database",
            response = Invoice.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<Invoice> findInvoiceById(@ApiParam(value = "id of the invoice", required = true)
                                                 @NonNull @PathVariable Long id) {
        log.info("Find Invoice by passing ID of invoice, where invoice ID is :{} ", id);
        Optional<Invoice> invoice = (invoiceService.findInvoiceById(id));
        if (invoice.isEmpty()) log.warn("Invoice with id {} is not found.", id);
        else log.debug("Invoice with id {} is found: {}", id, invoice);
        return invoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves the invoice into the database",
            notes = "If provided valid invoice saves it",
            response = Invoice.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Invoice> saveInvoice(@Valid @RequestBody Invoice invoice, BindingResult bindingResult) throws Exception {
        log.info("Create new invoice by passing : {}", invoice);
        if (bindingResult.hasErrors()) {
            log.error("New invoice is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Invoice invoiceSaved = invoiceService.saveInvoice(invoice);
        log.debug("New invoice is created: {}", invoiceSaved);
        return new ResponseEntity<>(invoiceSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the invoice by id",
            notes = "Updates the invoice if provided id exists",
            response = Invoice.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Invoice> updateInvoiceById(@ApiParam(value = "The id of the invoice", required = true)
                                                   @NonNull @PathVariable Long id,
                                                   @Valid @RequestBody Invoice invoice, BindingResult bindingResult) throws Exception{
        log.info("Update existing invoice with ID: {} and new body: {}", id, invoice);
        if (bindingResult.hasErrors() || !id.equals(invoice.getId())) {
            log.warn("Invoice for update with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        invoiceService.saveInvoice(invoice);
        log.debug("Client with id {} is updated: {}", id, invoice);
        return new ResponseEntity<>(invoice, HttpStatus.CREATED);
    }

}
