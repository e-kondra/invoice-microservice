package com.invoiceservice.invoiceservice.business.mappers;

import com.invoiceservice.invoiceservice.business.repository.model.CashReceiptDAO;
import com.invoiceservice.invoiceservice.business.repository.model.InvoiceDAO;
import com.invoiceservice.invoiceservice.business.repository.model.OrderDetailsDAO;
import com.invoiceservice.invoiceservice.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;


@Mapper(componentModel = "spring", uses = {
        OrderDetailsMapStructMapper.class,
        CashReceiptMapStructMapper.class})
public interface InvoiceMapStructMapper {

    @Mappings({
            @Mapping(target = "orderDetailsIds", source = "orderDetailsIds", qualifiedByName = "orderDetailsIds"),
            @Mapping(target = "cashReceiptIds", source = "cashReceiptIds", qualifiedByName = "CashReceiptIds")
    })
    Invoice invoiceDAOToInvoice(InvoiceDAO invoiceDAO);

    @Mappings({
            @Mapping(target = "orderDetailsIds", source = "orderDetailsIds", qualifiedByName = "orderDetailsIds"),
            @Mapping(target = "cashReceiptIds", source = "cashReceiptIds", qualifiedByName = "CashReceiptIds")
    })
    InvoiceDAO invoiceDAOToInvoice(Invoice invoice);

    @Named("CashReceiptIds")
    default List<Long>  cashReceiptDAOSToCashReceiptIds(List<CashReceiptDAO> cashReceiptDAOS) {
        List<Long> cashReceiptIds = new ArrayList<>();
        if (isNotEmpty(cashReceiptDAOS))
            cashReceiptDAOS.forEach( cashReceiptDAO -> cashReceiptIds.add(cashReceiptDAO.getId()));
        return cashReceiptIds;
    }

    @Named("CashReceiptIds")
    default List<CashReceiptDAO> CashReceiptIdsToCashReceiptDAOS(List<Long> cashReceiptIds) {
        List<CashReceiptDAO> cashReceiptDAOS = new ArrayList<>();
        if (isNotEmpty(cashReceiptIds))
            cashReceiptIds.forEach( cashReceiptId -> cashReceiptDAOS.add(new CashReceiptDAO(cashReceiptId)));
        return cashReceiptDAOS;
    }

    @Named("orderDetailsIds")
    default List<Long>  orderDetailsDAOSToOrderDetailsIds(List<OrderDetailsDAO> orderDetailsDAOS) {
        List<Long> orderDetailsIds = new ArrayList<>();
        if (isNotEmpty(orderDetailsDAOS))
            orderDetailsDAOS.forEach( orderDetailsDAO -> orderDetailsIds.add(orderDetailsDAO.getId()));
        return orderDetailsIds;
    }

    @Named("orderDetailsIds")
    default List<OrderDetailsDAO> orderDetailsIdsToOrderDetailsDAOS(List<Long> orderDetailsIds) {
        List<OrderDetailsDAO> orderDetailsDAOS = new ArrayList<>();
        if (isNotEmpty(orderDetailsIds))
            orderDetailsIds.forEach( cashReceiptId -> orderDetailsDAOS.add(new OrderDetailsDAO(cashReceiptId)));
        return orderDetailsDAOS;
    }
}
