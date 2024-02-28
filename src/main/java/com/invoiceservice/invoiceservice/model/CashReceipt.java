package com.invoiceservice.invoiceservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@ApiModel(description = "Model of cash receipt data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashReceipt {

    private static final String DATE_FORMAT_MESSAGE = "Date format: yyyy-mm-dd";
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    @ApiModelProperty(notes = "The unique id of the cash receipt")
    @NonNull
    private Long id;

    @ApiModelProperty(notes = "Number of cash receipt")
    @NonNull
    @NotEmpty
    private String number;

    @ApiModelProperty(
            notes = "Date of cash receipt. Date format YYYY-MM-DD",
            required = true, example = "2000-12-31")
    @Pattern(regexp = DATE_PATTERN, message = DATE_FORMAT_MESSAGE)
    @NotEmpty
    private String date;

    @ApiModelProperty(notes = "Amount of cash receipt")
    @NonNull
    private Float amount;

    @ApiModelProperty(notes = "Invoice related cash receipt")
    @NotNull
    private Invoice invoice;
}
