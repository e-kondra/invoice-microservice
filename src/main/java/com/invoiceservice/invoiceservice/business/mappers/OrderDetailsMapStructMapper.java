package com.invoiceservice.invoiceservice.business.mappers;

import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.business.repository.model.OrderDetailsDAO;
import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.model.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {
        InvoiceMapStructMapper.class})
public interface OrderDetailsMapStructMapper {

    OrderDetails orderDetailsDAOToOrderDetails(OrderDetailsDAO orderDetailsDAO);

    OrderDetailsDAO orderDetailsToOrderDetailsDAO(OrderDetails orderDetails);

}
