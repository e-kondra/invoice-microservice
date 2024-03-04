package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.InvoiceMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.InvoiceRepository;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.business.service.InvoiceService;
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
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoiceMapStructMapper mapper;

    @Override
    public List<Invoice> findAllInvoices() {
        List<InvoiceDAO> invoiceDAOList = invoiceRepository.findAll();
        log.info("Get invoice list. Size is: {}", invoiceDAOList::size);
        return invoiceDAOList.stream().map(mapper::invoiceDAOToInvoice).collect(Collectors.toList());
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        if (!hasNoMatch(invoice)) {
            log.error("Invoice conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        InvoiceDAO invoiceSaved = invoiceRepository.save(mapper.invoiceToInvoiceDAO(invoice));
        log.info("New invoice saved: {}", () -> invoiceSaved);
        return mapper.invoiceDAOToInvoice(invoiceSaved);
    }

    @Override
    public Optional<Invoice> findInvoiceById(Long id) {
        Optional<Invoice> invoiceById = invoiceRepository.findById(id)
                .flatMap(invoiceDAO -> Optional.ofNullable(mapper.invoiceDAOToInvoice(invoiceDAO)));
        log.info("Invoice with id {} is {}", id, invoiceById);
        return invoiceById;
    }

    private boolean hasNoMatch(Invoice invoice) {
        return invoiceRepository.findAll().stream()
                .noneMatch(invoiceDAO -> !invoiceDAO.getId().equals(invoice.getId()) &&
                        invoiceDAO.getNumber().equalsIgnoreCase(invoice.getNumber()));
    }

}
