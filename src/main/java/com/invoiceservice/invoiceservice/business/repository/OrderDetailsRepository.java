package com.invoiceservice.invoiceservice.business.repository;

import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.business.repository.model.OrderDetailsDAO;
import com.invoiceservice.invoiceservice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetailsDAO, Long> {

    List<OrderDetailsDAO> findByInvoice(InvoiceDAO invoiceDAO);
}
