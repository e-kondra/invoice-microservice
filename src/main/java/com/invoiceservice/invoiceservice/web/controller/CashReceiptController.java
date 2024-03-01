package com.invoiceservice.invoiceservice.web.controller;

import com.invoiceservice.invoiceservice.swagger.DescriptionVariables;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {DescriptionVariables.CASH_RECEIPT})
@Log4j2
@RestController
@RequestMapping("/api/v1/cash_receipt")
public class CashReceiptController {
}
