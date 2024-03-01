package com.invoiceservice.invoiceservice.business.mappers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        InvoiceMapStructMapper.class})
public interface CashReceiptMapStructMapper {
}
