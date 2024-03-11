package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.DocumentNumberMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.DocumentNumberRepository;
import com.invoiceservice.invoiceservice.business.repository.model.DocumentNumberDAO;
import com.invoiceservice.invoiceservice.model.DocumentNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentNumberServiceImplTest {

    @InjectMocks
    DocumentNumberServiceImpl service;

    @Mock
    DocumentNumberRepository repository;
    @Mock
    DocumentNumberMapStructMapper mapper;

    DocumentNumber documentNumber;
    DocumentNumberDAO documentNumberDAO;

    @BeforeEach
    public void init(){
        documentNumber = createDocumentNumber();
        documentNumberDAO = createDocumentNumberDAO();
    }

    private DocumentNumberDAO createDocumentNumberDAO() {
        DocumentNumberDAO docNum = new DocumentNumberDAO();
        docNum.setId(1L);
        docNum.setInvoiceNumber(10);
        docNum.setCashReceiptNumber(5);
        return docNum;
    }

    private DocumentNumber createDocumentNumber() {
        DocumentNumber docNum = new DocumentNumber();
        docNum.setId(1L);
        docNum.setInvoiceNumber(10);
        docNum.setCashReceiptNumber(5);
        return docNum;
    }

    @Test
    void getDocumentNumber() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(documentNumberDAO));
        when(mapper.documentNumberDAOToDocumentNumber(documentNumberDAO)).thenReturn(documentNumber);

        Optional<DocumentNumber> documentNumberReturned = service.getDocumentNumber();
        assertEquals(documentNumber, documentNumberReturned.get());
        assertEquals(documentNumber.getInvoiceNumber(),documentNumberReturned.get().getInvoiceNumber());
        verify(repository,times(1)).findById(anyLong());
    }

    @Test
    void buildInvoiceNumber() {
        assertEquals("T24 Nr.10", service.buildInvoiceNumber(documentNumber));
    }

    @Test
    void buildCashReceiptNumber() {
        assertEquals("Serija TET24 Nr.5", service.buildCashReceiptNumber(documentNumber));
    }
//    @Test
//    void increaseInvoiceNumber() {
//        documentNumber.setInvoiceNumber(documentNumber.getInvoiceNumber() +1);
//        documentNumberDAO.setInvoiceNumber(documentNumberDAO.getInvoiceNumber() +1);
//        when(mapper.documentNumberToDocumentNumberDAO(documentNumber)).thenReturn(documentNumberDAO);
//        when(repository.save(documentNumberDAO)).thenReturn(documentNumberDAO);
//        when(service.getDocumentNumber()).thenReturn(Optional.of(documentNumber));
//        service.increaseInvoiceNumber();
//        assertEquals(11, service.getDocumentNumber().get().getInvoiceNumber());
//        verify(repository,times(1)).save(documentNumberDAO);
//    }

    @Test
    void decreaseInvoiceNumber() {
    }

    @Test
    void increaseCashReceiptNumber() {
    }

    @Test
    void decreaseCashReceiptNumber() {
    }

    @Test
    void buildPreviousInvoiceNumber() {
    }

    @Test
    void buildPreviousCashReceiptNumber() {
    }
}