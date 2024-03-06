package com.invoiceservice.invoiceservice.business.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoice")
public class InvoiceDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @Column(name = "number")
    @NonNull
    private String number;

    @Column(name = "date")
    @NonNull
    private String date;

    @Column(name = "client_name")
    @NonNull
    private String clientName;

    @Column(name = "client_address")
    private String clientAddress;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "client_pvm")
    private String clientPvm;

    @Column(name = "client_representative")
    private String clientRepresentative;

    @Column(name = "car")
    @NonNull
    private String car;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<OrderDetailsDAO> OrderDetailsIds;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<CashReceiptDAO> CashReceiptIds;
}