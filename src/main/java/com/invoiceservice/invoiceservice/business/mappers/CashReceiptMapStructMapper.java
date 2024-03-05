package com.invoiceservice.invoiceservice.business.mappers;

import com.invoiceservice.invoiceservice.business.repository.CashReceiptRepository;
import com.invoiceservice.invoiceservice.business.repository.model.CashReceiptDAO;
import com.invoiceservice.invoiceservice.model.CashReceipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        InvoiceMapStructMapper.class})
public interface CashReceiptMapStructMapper {

    CashReceipt cashReceiptDAOToCashReceipt (CashReceiptDAO cashReceiptDAO);

    CashReceiptDAO cashReceiptToCashReceiptDAO (CashReceipt cashReceipt);
}
