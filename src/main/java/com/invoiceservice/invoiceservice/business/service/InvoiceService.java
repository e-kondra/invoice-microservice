package com.invoiceservice.invoiceservice.business.service;

import com.invoiceservice.invoiceservice.model.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    List<Invoice> findAllInvoices();


    Invoice saveInvoice(Invoice invoice);

    Optional<Invoice> findInvoiceById(Long id);

    void deleteInvoiceById(Long id);
}
