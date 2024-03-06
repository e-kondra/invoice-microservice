package com.invoiceservice.invoiceservice.business.mappers;

import com.invoiceservice.invoiceservice.business.repository.model.DocumentNumberDAO;
import com.invoiceservice.invoiceservice.model.DocumentNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentNumberMapStructMapper {

    DocumentNumberDAO documentNumberToDocumentNumberDAO(DocumentNumber documentNumber);

    DocumentNumber documentNumberDAOToDocumentNumber(DocumentNumberDAO documentNumberDAO);

}
