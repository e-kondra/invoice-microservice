package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.InvoiceMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.InvoiceRepository;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.business.service.InvoiceService;
import com.invoiceservice.invoiceservice.model.Invoice;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

}
