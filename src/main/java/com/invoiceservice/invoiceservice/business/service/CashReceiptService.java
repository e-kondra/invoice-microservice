package com.invoiceservice.invoiceservice.business.service;

import com.invoiceservice.invoiceservice.model.CashReceipt;
import com.invoiceservice.invoiceservice.model.Invoice;

import java.util.List;
import java.util.Optional;

public interface CashReceiptService {
    List<CashReceipt> findCashReceiptsByInvoice(Invoice invoice);

    List<CashReceipt> findAllCashReceipts();

    Optional<CashReceipt> findCashReceiptById(Long id);

    CashReceipt saveCashReceipt(CashReceipt cashReceipt);

    void deleteCashReceiptById(Long id);

    CashReceipt updateCashReceipt(CashReceipt cashReceipt);

    boolean isItLastCashReceipt (CashReceipt cashReceipt);
}
