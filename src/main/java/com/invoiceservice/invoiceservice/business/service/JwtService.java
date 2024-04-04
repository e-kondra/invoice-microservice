package com.invoiceservice.invoiceservice.business.service;

public interface JwtService {
    boolean validateToken(String jwtToken);
}
