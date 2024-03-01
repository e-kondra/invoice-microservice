package com.invoiceservice.invoiceservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of document number data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentNumber {

    @ApiModelProperty(notes = "The unique id of document number")
    @NonNull
    private Long id;

    @ApiModelProperty(notes = "Number of invoice")
    @NotEmpty
    private int invoiceNumber;

    @ApiModelProperty(notes = "Number of cash receipt")
    @NotEmpty
    private int cashReceiptNumber;
}
