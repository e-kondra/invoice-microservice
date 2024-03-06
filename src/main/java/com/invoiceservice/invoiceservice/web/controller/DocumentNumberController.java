package com.invoiceservice.invoiceservice.web.controller;

import com.invoiceservice.invoiceservice.business.service.DocumentNumberService;
import com.invoiceservice.invoiceservice.model.DocumentNumber;
import com.invoiceservice.invoiceservice.model.OrderDetails;
import com.invoiceservice.invoiceservice.swagger.DescriptionVariables;
import com.invoiceservice.invoiceservice.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Api(tags = {DescriptionVariables.DOCUMENT_NUMBER})
@Log4j2
@RestController
@RequestMapping("/api/v1/document")
public class DocumentNumberController {

    @Autowired
    DocumentNumberService service;

    @GetMapping("/invoice")
    @ApiOperation(value = "Get invoice number",
            notes = "Getting specific invoice number in database",
            response = DocumentNumber.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<String> findInvoiceNumber() {
        log.info("Finding invoice number");
        Optional<DocumentNumber> documentNumber = service.getDocumentNumber();
        if (documentNumber.isEmpty()) {
            log.warn("Document number is not found.");
            return ResponseEntity.notFound().build();
        }
        else {
            log.debug("Document number is found {}", documentNumber);
            String invoiceNumberStr = service.buildInvoiceNumber(documentNumber.get());
            if(invoiceNumberStr.isEmpty())
                return ResponseEntity.noContent().build();
            return new ResponseEntity<>(invoiceNumberStr, HttpStatus.OK);
        }
    }

    @GetMapping("/cash")
    @ApiOperation(value = "Get cash receipt number",
            notes = "Getting specific cash receipt number in database",
            response = DocumentNumber.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<String> findCashReceiptNumber() {
        log.info("Finding invoice number");
        Optional<DocumentNumber> documentNumber = service.getDocumentNumber();
        if (documentNumber.isEmpty()) {
            log.warn("Document number is not found.");
            return ResponseEntity.notFound().build();
        }
        else {
            log.debug("Document number is found {}", documentNumber);
            String receiptNumberStr = service.buildCashReceiptNumber(documentNumber.get());
            if(receiptNumberStr.isEmpty())
                return ResponseEntity.noContent().build();
            return new ResponseEntity<>(receiptNumberStr, HttpStatus.OK);
        }

    }

}
