package com.invoiceservice.invoiceservice.business.service;

import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.model.OrderDetails;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsService {
    List<OrderDetails> findOrderDetailsByInvoice(Invoice invoice);

    Optional<OrderDetails> findOrderDetailsById(Long id);

    OrderDetails saveOrderDetails(OrderDetails orderDetails);

    void deleteOrderDetailsById(Long id);
}
