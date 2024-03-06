package com.invoiceservice.invoiceservice.business.service;

import com.invoiceservice.invoiceservice.model.DocumentNumber;

import java.util.Optional;

public interface DocumentNumberService {
    Optional<DocumentNumber> getDocumentNumber();

    String buildInvoiceNumber(DocumentNumber documentNumber);

    String buildCashReceiptNumber(DocumentNumber documentNumber);

    void increaseInvoiceNumber();
    void decreaseInvoiceNumber();

    void increaseCashReceiptNumber();

    void decreaseCashReceiptNumber();

    String buildPreviousInvoiceNumber();

    String buildPreviousCashReceiptNumber();
}
