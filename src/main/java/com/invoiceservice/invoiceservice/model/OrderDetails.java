package com.invoiceservice.invoiceservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of order details data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

    @ApiModelProperty(notes = "The unique id of the order details")
    @NonNull
    private Long id;

    @ApiModelProperty(notes = "job description")
    @NonNull
    @NotEmpty
    private String description;

    @ApiModelProperty(notes = "job price")
    @NotNull
    @Positive
    private float price;

    @ApiModelProperty(notes = "job quantity")
    @NotNull
    @Positive
    private int quantity;

    @ApiModelProperty(notes = "Invoice related cash receipt")
    @NotNull
    private Invoice invoice;
}
