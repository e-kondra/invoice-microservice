package com.invoiceservice.invoiceservice.business.service.impl;

import com.invoiceservice.invoiceservice.business.mappers.InvoiceMapStructMapper;
import com.invoiceservice.invoiceservice.business.mappers.OrderDetailsMapStructMapper;
import com.invoiceservice.invoiceservice.business.repository.OrderDetailsRepository;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.business.repository.model.OrderDetailsDAO;
import com.invoiceservice.invoiceservice.business.service.OrderDetailsService;
import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.model.OrderDetails;
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
public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Autowired
    OrderDetailsRepository repository;

    @Autowired
    OrderDetailsMapStructMapper mapper;
    @Autowired
    InvoiceMapStructMapper invoiceMapper;


    @Override
    public List<OrderDetails> findOrderDetailsByInvoice(Invoice invoice) {
        InvoiceDAO invoiceDAO = invoiceMapper.invoiceToInvoiceDAO(invoice);
        List<OrderDetailsDAO> detailsDAOList = repository.findByInvoice(invoiceDAO);
        log.info("Get order details list by invoice. Size is: {}", detailsDAOList::size);
        return detailsDAOList.stream().map(mapper::orderDetailsDAOToOrderDetails).collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDetails> findOrderDetailsById(Long id) {
        Optional<OrderDetails> detailsById = repository.findById(id)
                .flatMap(detailsDAO -> Optional.ofNullable(mapper.orderDetailsDAOToOrderDetails(detailsDAO)));
        log.info("Order details with id {} is {}", id, detailsById);
        return detailsById;
    }

    @Override
    public OrderDetails saveOrderDetails(OrderDetails orderDetails) {
        if (!hasNoMatch(orderDetails)) {
            log.error("Order Details conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        OrderDetailsDAO detailsSaved = repository.save(mapper.orderDetailsToOrderDetailsDAO(orderDetails));
        log.info("New Order Details saved: {}", () -> detailsSaved);
        return mapper.orderDetailsDAOToOrderDetails(detailsSaved);
    }

    private boolean hasNoMatch(OrderDetails orderDetails) {
        return repository.findAll().stream()
                .noneMatch(detailsDAO -> !detailsDAO.getId().equals(orderDetails.getId()) &&
                        detailsDAO.getDescription().equalsIgnoreCase(orderDetails.getDescription()));
    }
}
