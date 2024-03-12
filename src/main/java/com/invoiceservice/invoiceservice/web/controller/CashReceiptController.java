package com.invoiceservice.invoiceservice.web.controller;

import com.invoiceservice.invoiceservice.business.service.CashReceiptService;
import com.invoiceservice.invoiceservice.business.service.InvoiceService;
import com.invoiceservice.invoiceservice.model.CashReceipt;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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

@Api(tags = {DescriptionVariables.CASH_RECEIPT})
@Log4j2
@RestController
@RequestMapping("/api/v1/cash")
public class CashReceiptController {

    @Autowired
    CashReceiptService service;
    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/invoice/{id}")
    @ApiOperation(value = "Finds all cash receipts by invoice id",
            notes = "Returns the entire list of cash receipts by invoice id ",
            response = CashReceipt.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<CashReceipt>> findCashReceiptsByInvoiceId(@ApiParam(value = "id of the invoice", required = true)
                                                                          @NonNull @PathVariable Long id) {
        Optional<Invoice> invoice = (invoiceService.findInvoiceById(id));
        if (invoice.isEmpty()) {
            log.warn("Invoice with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        else log.debug("Invoice with id {} is found: {}", id, invoice);
        log.info("Retrieve list of cash receipts by Invoice id");
        List<CashReceipt> receiptsList = service.findCashReceiptsByInvoice(invoice.get());
        if (receiptsList.isEmpty()) log.warn("Cash receipts list is empty! {}", receiptsList);
        else log.debug("Cash receipts list is found. Size: {}", receiptsList::size);
        return ResponseEntity.ok(receiptsList);
    }

    @GetMapping
    @ApiOperation(value = "Finds all cash receipts",
            notes = "Returns the entire list of cash receipts",
            response = CashReceipt.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<CashReceipt>> findAllCashReceipts() {
        log.info("Retrieve list of all cash receipts");
        List<CashReceipt> receiptsList = service.findAllCashReceipts();
        if (receiptsList.isEmpty()) log.warn("Cash receipts list is empty! {}", receiptsList);
        else log.debug("Cash receipts list is found. Size: {}", receiptsList::size);
        return ResponseEntity.ok(receiptsList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find the cash receipt by id",
            notes = "Provide an id to search specific cash receipt in database",
            response = CashReceipt.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<CashReceipt> findCashReceiptById(@ApiParam(value = "id of the cash receipt", required = true)
                                                             @NonNull @PathVariable Long id) {
        log.info("Find cash receipt by passing ID of cash receipt, where cash receipt ID is :{} ", id);
        Optional<CashReceipt> cashReceipt = service.findCashReceiptById(id);
        if (cashReceipt.isEmpty()) log.warn("CashReceipt with id {} is not found.", id);
        else log.debug("CashReceipt with id {} is found: {}", id, cashReceipt);
        return cashReceipt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves the cash receipt into the database",
            notes = "If provided valid cash receipt saves it",
            response = CashReceipt.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CashReceipt> saveCashReceipt(@Valid @RequestBody CashReceipt cashReceipt, BindingResult bindingResult) throws Exception {
        log.info("Create new cash receipt by passing : {}", cashReceipt);
        if (bindingResult.hasErrors()) {
            log.error("New cash receipt is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        CashReceipt receiptSaved = service.saveCashReceipt(cashReceipt);
        log.debug("New cash receipt is created: {}", receiptSaved);
        return new ResponseEntity<>(receiptSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the cash receipt by id",
            notes = "Updates the cash receipt if provided id exists",
            response = CashReceipt.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CashReceipt> updateCashReceiptById(@ApiParam(value = "The id of the cash receipt", required = true)
                                                               @NonNull @PathVariable Long id,
                                                               @Valid @RequestBody CashReceipt cashReceipt, BindingResult bindingResult) throws Exception{
        log.info("Update existing cash receipt with ID: {} and new body: {}", id, cashReceipt);
        if (bindingResult.hasErrors() || !id.equals(cashReceipt.getId())) {
            log.warn("Cash receipt for update with id {} not found", id);
            return ResponseEntity.badRequest().build();
        }
        service.updateCashReceipt(cashReceipt);
        log.debug("Cash receipt with id {} is updated: {}", id, cashReceipt);
        return new ResponseEntity<>(cashReceipt, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the cash receipt  by id",
            notes = "Deletes the cash receipt  if provided id exists",
            response = CashReceipt.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITHOUT_DATA),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCashReceiptById(@ApiParam(value = "The id of the cash receipt", required = true)
                                              @NonNull @PathVariable Long id) {
        log.info("Delete cash receipt by passing ID, where ID is:{}", id);
        Optional<CashReceipt> receipt = service.findCashReceiptById(id);
        if (!receipt.isPresent()) {
            log.warn("Cash receipt for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        if (service.isItLastCashReceipt(receipt.get())) {
            service.deleteCashReceiptById(id);
            log.debug("Cash receipt with id {} is deleted: {}", id, receipt);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.debug("Cash receipt with id {}, {} couldn't be deleted", id, receipt.get().getNumber());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
