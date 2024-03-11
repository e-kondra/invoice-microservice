package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.DocumentNumberMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.DocumentNumberRepository;
import com.invoiceservice.invoiceservice.business.repository.model.DocumentNumberDAO;
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
        return "T" + String.valueOf(LocalDate.now().getYear() % 100)
                + " Nr." + String.valueOf(documentNumber.getInvoiceNumber());
    }

    @Override
    public String buildCashReceiptNumber(DocumentNumber documentNumber) {
        return "Serija TET" + String.valueOf(LocalDate.now().getYear() % 100)
                + " Nr." + String.valueOf(documentNumber.getCashReceiptNumber());
    }

    public void increaseInvoiceNumber(){
        Optional<DocumentNumber> documentNumber = getDocumentNumber();
        documentNumber.get().setInvoiceNumber(documentNumber.get().getInvoiceNumber() +1);
        repository.save(mapper.documentNumberToDocumentNumberDAO(documentNumber.get()));
    }

    public void decreaseInvoiceNumber(){
        Optional<DocumentNumber> documentNumber = getDocumentNumber();
        documentNumber.get().setInvoiceNumber(documentNumber.get().getInvoiceNumber() -1);
        repository.save(mapper.documentNumberToDocumentNumberDAO(documentNumber.get()));
    }

    public void increaseCashReceiptNumber(){
        Optional<DocumentNumber> documentNumber = getDocumentNumber();
        documentNumber.get().setCashReceiptNumber(documentNumber.get().getCashReceiptNumber() +1);
        repository.save(mapper.documentNumberToDocumentNumberDAO(documentNumber.get()));
    }

    public void decreaseCashReceiptNumber(){
        Optional<DocumentNumber> documentNumber = getDocumentNumber();
        documentNumber.get().setCashReceiptNumber(documentNumber.get().getCashReceiptNumber() -1);
        repository.save(mapper.documentNumberToDocumentNumberDAO(documentNumber.get()));
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
