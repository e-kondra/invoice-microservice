package com.invoiceservice.invoiceservice.business.mappers;


import com.invoiceservice.invoiceservice.business.repository.model.OrderDetailsDAO;
import com.invoiceservice.invoiceservice.model.OrderDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        InvoiceMapStructMapper.class})
public interface OrderDetailsMapStructMapper {

    OrderDetails orderDetailsDAOToOrderDetails(OrderDetailsDAO orderDetailsDAO);

    OrderDetailsDAO orderDetailsToOrderDetailsDAO(OrderDetails orderDetails);

}
