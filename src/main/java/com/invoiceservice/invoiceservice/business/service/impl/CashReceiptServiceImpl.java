package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.CashReceiptMapStructMapper;
import com.invoiceservice.invoiceservice.business.mappers.InvoiceMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.CashReceiptRepository;
import com.invoiceservice.invoiceservice.business.repository.model.CashReceiptDAO;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.business.service.CashReceiptService;
import com.invoiceservice.invoiceservice.business.service.DocumentNumberService;
import com.invoiceservice.invoiceservice.model.CashReceipt;
import com.invoiceservice.invoiceservice.model.Invoice;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CashReceiptServiceImpl implements CashReceiptService {
    @Autowired
    InvoiceMapStructMapper invoiceMapper;
    @Autowired
    CashReceiptMapStructMapper mapper;

    @Autowired
    CashReceiptRepository repository;

    @Autowired
    DocumentNumberService documentService;


    @Override
    public List<CashReceipt> findCashReceiptsByInvoice(Invoice invoice) {
        InvoiceDAO invoiceDAO = invoiceMapper.invoiceToInvoiceDAO(invoice);
        List<CashReceiptDAO> receiptDAOList = repository.findByInvoice(invoiceDAO);
        log.info("Get cash receipt list by invoice. Size is: {}", receiptDAOList::size);
        return receiptDAOList.stream().map(mapper::cashReceiptDAOToCashReceipt).collect(Collectors.toList());
    }

    @Override
    public List<CashReceipt> findAllCashReceipts() {
        List<CashReceiptDAO> receiptDAOList = repository.findAll();
        log.info("Get cash receipt list by invoice. Size is: {}", receiptDAOList::size);
        return receiptDAOList.stream().map(mapper::cashReceiptDAOToCashReceipt).collect(Collectors.toList());
    }

    @Override
    public Optional<CashReceipt> findCashReceiptById(Long id) {
        Optional<CashReceipt> cashReceiptById = repository.findById(id)
                .flatMap(receiptDAO -> Optional.ofNullable(mapper.cashReceiptDAOToCashReceipt(receiptDAO)));
        log.info("Cash receipt with id {} is {}", id, cashReceiptById);
        return cashReceiptById;
    }

    @Override
    public CashReceipt saveCashReceipt(CashReceipt cashReceipt) {
        if (!hasNoMatch(cashReceipt)) {
            log.error("Cash Receipt conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        CashReceiptDAO receiptSaved = repository.save(mapper.cashReceiptToCashReceiptDAO(cashReceipt));
        log.info("New Cash Receipt saved: {}", () -> receiptSaved);
        documentService.increaseCashReceiptNumber();
        return mapper.cashReceiptDAOToCashReceipt(receiptSaved);
    }

    @Override
    public CashReceipt updateCashReceipt(CashReceipt cashReceipt) {
        if (!hasNoMatch(cashReceipt)) {
            log.error("Cash Receipt conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        CashReceiptDAO receiptSaved = repository.save(mapper.cashReceiptToCashReceiptDAO(cashReceipt));
        log.info("New Cash Receipt saved: {}", () -> receiptSaved);
        return mapper.cashReceiptDAOToCashReceipt(receiptSaved);
    }

    @Override
    public void deleteCashReceiptById(Long id) {
        repository.deleteById(id);
        log.info("Cash Receipt with id {} is deleted", id);
        documentService.decreaseCashReceiptNumber();
    }

    @Override
    public boolean isItLastCashReceipt (CashReceipt cashReceipt){
        return cashReceipt.getNumber().equals(documentService.buildPreviousCashReceiptNumber());
    }

    private boolean hasNoMatch(CashReceipt cashReceipt) {
        return repository.findAll().stream()
                    .noneMatch(receiptDAO -> !receiptDAO.getId().equals(cashReceipt.getId()) &&
                            receiptDAO.getNumber().equalsIgnoreCase(cashReceipt.getNumber()));
    }

}
