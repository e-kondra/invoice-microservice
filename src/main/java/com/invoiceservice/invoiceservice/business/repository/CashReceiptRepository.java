package com.invoiceservice.invoiceservice.business.repository;

import com.invoiceservice.invoiceservice.business.repository.model.CashReceiptDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashReceiptRepository extends JpaRepository<CashReceiptDAO, Long>{

}
