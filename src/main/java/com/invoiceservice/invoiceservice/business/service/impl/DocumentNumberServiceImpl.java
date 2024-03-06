package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.DocumentNumberMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.DocumentNumberRepository;
import com.invoiceservice.invoiceservice.business.service.DocumentNumberService;
import com.invoiceservice.invoiceservice.model.DocumentNumber;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.Optional;

import static java.time.LocalTime.now;

@Service
@Log4j2
public class DocumentNumberServiceImpl implements DocumentNumberService {

    @Autowired
    DocumentNumberRepository repository;
    @Autowired
    DocumentNumberMapStructMapper mapper;

    @Override
    public Optional<DocumentNumber> getDocumentNumber() {
        Optional<DocumentNumber> docNumberById = repository.findById(1L)
                .flatMap(docNumberDAO -> Optional.ofNullable(mapper.documentNumberDAOToDocumentNumber(docNumberDAO)));
        log.info("Document number is {}", docNumberById);
        return docNumberById;
    }

    @Override
    public String buildInvoiceNumber(DocumentNumber documentNumber) {
        Optional<Integer> invoiceNum = Optional.of(documentNumber.getInvoiceNumber());
        if(!invoiceNum.isEmpty())
            return "T" + String.valueOf(LocalDate.now().getYear() % 100)
                    + " Nr." + String.valueOf(documentNumber.getInvoiceNumber());
        else return null;
    }

    @Override
    public String buildCashReceiptNumber(DocumentNumber documentNumber) {
        Optional<Integer> receiptNum = Optional.of(documentNumber.getCashReceiptNumber());
        if(!receiptNum.isEmpty())
            return "Serija TET" + String.valueOf(LocalDate.now().getYear() % 100)
                    + " Nr." + String.valueOf(documentNumber.getCashReceiptNumber());
        else return null;
    }

    public void increaseInvoiceNumber(){
        Optional<DocumentNumber> documentNumber = getDocumentNumber();
        if(!documentNumber.isEmpty()){
            documentNumber.get().setInvoiceNumber(documentNumber.get().getInvoiceNumber() +1);
        }
    }
    public void decreaseInvoiceNumber(){
        Optional<DocumentNumber> documentNumber = getDocumentNumber();
        if(!documentNumber.isEmpty()){
            documentNumber.get().setInvoiceNumber(documentNumber.get().getInvoiceNumber() -1);
        }
    }

    public void increaseCashReceiptNumber(){
        Optional<DocumentNumber> documentNumber = getDocumentNumber();
        if(!documentNumber.isEmpty()){
            documentNumber.get().setCashReceiptNumber(documentNumber.get().getCashReceiptNumber() +1);
        }
    }

    public void decreaseCashReceiptNumber(){
        Optional<DocumentNumber> documentNumber = getDocumentNumber();
        if(!documentNumber.isEmpty()){
            documentNumber.get().setCashReceiptNumber(documentNumber.get().getCashReceiptNumber() -1);
        }
    }

    public String buildPreviousInvoiceNumber() {
        DocumentNumber documentNumber = getDocumentNumber().get();
        documentNumber.setInvoiceNumber(documentNumber.getInvoiceNumber() -1);
        return buildInvoiceNumber(documentNumber);
    }

    @Override
    public String buildPreviousCashReceiptNumber() {
        DocumentNumber documentNumber = getDocumentNumber().get();
        documentNumber.setCashReceiptNumber(documentNumber.getCashReceiptNumber() -1);
        return buildCashReceiptNumber(documentNumber);
    }
}
