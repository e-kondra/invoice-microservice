package com.invoiceservice.invoiceservice.web.controller;

import com.invoiceservice.invoiceservice.business.service.InvoiceService;
import com.invoiceservice.invoiceservice.business.service.OrderDetailsService;
import com.invoiceservice.invoiceservice.model.CashReceipt;
import com.invoiceservice.invoiceservice.model.Invoice;
import com.invoiceservice.invoiceservice.model.OrderDetails;
import com.invoiceservice.invoiceservice.swagger.DescriptionVariables;
import com.invoiceservice.invoiceservice.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Api(tags = {DescriptionVariables.ORDER_DETAILS})
@Log4j2
@RestController
@RequestMapping("/api/v1/details")
public class OrderDetailsController {

    @Autowired
    OrderDetailsService service;
    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/invoice/{id}")
    @ApiOperation(value = "Finds all order details",
            notes = "Returns the entire list of order details",
            response = OrderDetails.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<OrderDetails>> findOrderDetailsByInvoiceId(@ApiParam(value = "id of the invoice", required = true)
                                                                              @NonNull @PathVariable Long id) {
        Optional<Invoice> invoice = (invoiceService.findInvoiceById(id));
        if (invoice.isEmpty()) {
            log.warn("Invoice with id {} is not found.", id);
            ResponseEntity.notFound().build();
        }
        else log.debug("Invoice with id {} is found: {}", id, invoice);

        log.info("Retrieve list of order details by Invoice id");
        List<OrderDetails> detailsList = service.findOrderDetailsByInvoice(invoice.get());
        if (detailsList.isEmpty()) log.warn("Order details list is empty! {}", detailsList);
        else log.debug("Order details list is found. Size: {}", detailsList::size);
        return ResponseEntity.ok(detailsList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find the order details by id",
            notes = "Provide an id to search specific order details in database",
            response = OrderDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<OrderDetails> findOrderDetailsById(@ApiParam(value = "id of the order details", required = true)
                                                   @NonNull @PathVariable Long id) {
        log.info("Find order details by passing ID of order details, where order details ID is :{} ", id);
        Optional<OrderDetails> details = service.findOrderDetailsById(id);
        if (details.isEmpty()) log.warn("Order details with id {} is not found.", id);
        else log.debug("Order details with id {} is found: {}", id, details);
        return details.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves the Order details into the database",
            notes = "If provided valid Order details saves it",
            response = OrderDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderDetails> saveOrderDetails(@Valid @RequestBody OrderDetails orderDetails, BindingResult bindingResult) throws Exception {
        log.info("Create new Order details by passing : {}", orderDetails);
        if (bindingResult.hasErrors()) {
            log.error("New Order details is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        OrderDetails detailsSaved = service.saveOrderDetails(orderDetails);
        log.debug("New Order details is created: {}", detailsSaved);
        return new ResponseEntity<>(detailsSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the Order details by id",
            notes = "Updates the Order details if provided id exists",
            response = OrderDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderDetails> updateOrderDetailsById(@ApiParam(value = "The id of the Order details", required = true)
                                                   @NonNull @PathVariable Long id,
                                                   @Valid @RequestBody OrderDetails orderDetails, BindingResult bindingResult) throws Exception{
        log.info("Update existing Order details with ID: {} and new body: {}", id, orderDetails);
        if (bindingResult.hasErrors() || !id.equals(orderDetails.getId())) {
            log.warn("Order details for update with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        service.saveOrderDetails(orderDetails);
        log.debug("Order details with id {} is updated: {}", id, orderDetails);
        return new ResponseEntity<>(orderDetails, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the Order details  by id",
            notes = "Deletes the Order details if provided id exists",
            response = OrderDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITHOUT_DATA),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOrderDetailsById(@ApiParam(value = "The id of the Order details", required = true)
                                                      @NonNull @PathVariable Long id) {
        log.info("Delete Order details by passing ID, where ID is:{}", id);
        Optional<OrderDetails> orderDetails = service.findOrderDetailsById(id);
        if (!orderDetails.isPresent()) {
            log.warn("Order details for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        service.deleteOrderDetailsById(id);
        log.debug("Order details with id {} is deleted: {}", id, orderDetails);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
