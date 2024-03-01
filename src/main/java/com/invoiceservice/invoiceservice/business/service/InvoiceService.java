package com.invoiceservice.invoiceservice.business.service;

import com.invoiceservice.invoiceservice.model.Invoice;

import java.util.List;

public interface InvoiceService {
    List<Invoice> findAllInvoices();
}
