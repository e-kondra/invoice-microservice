package com.invoiceservice.invoiceservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@ApiModel(description = "Model of invoice data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private static final String DATE_FORMAT_MESSAGE = "Date format: yyyy-mm-dd";
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    @ApiModelProperty(notes = "The unique id of the invoice")
    @NonNull
    private Long id;

    @ApiModelProperty(notes = "Number of invoice")
    @NonNull
    @NotEmpty
    private String number;

    @ApiModelProperty(
            notes = "Date of invoice. Date format YYYY-MM-DD",
            required = true, example = "2000-12-31")
    @Pattern(regexp = DATE_PATTERN, message = DATE_FORMAT_MESSAGE)
    @NotEmpty
    private String date;

    @ApiModelProperty(notes = "Clients name")
    @NonNull
    @NotEmpty
    private String clientName;

    @ApiModelProperty(notes = "Clients address")
    private String clientAddress;

    @ApiModelProperty(notes = "Clients personal/company code")
    private String clientCode;

    @ApiModelProperty(notes = "Company's pvm code")
    private String clientPvm;

    @ApiModelProperty(notes = "Company's representative person name")
    private String clientRepresentative;

    @ApiModelProperty(notes = "Invoice's car")
    private String car;

    @ApiModelProperty(notes = "Order details which are included in invoice")
    private List<Long> OrderDetailsIds;

    @ApiModelProperty(notes = "Cash receipts which are included in invoice")
    private List<Long> CashReceiptIds;
}
