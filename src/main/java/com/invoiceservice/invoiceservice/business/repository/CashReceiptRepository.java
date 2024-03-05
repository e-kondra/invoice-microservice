package com.invoiceservice.invoiceservice.business.repository;

import com.invoiceservice.invoiceservice.business.repository.model.CashReceiptDAO;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashReceiptRepository extends JpaRepository<CashReceiptDAO, Long>{

    List<CashReceiptDAO> findByInvoice(InvoiceDAO invoiceDAO);
}
