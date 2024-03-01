package com.invoiceservice.invoiceservice.business.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_number")
public class DocumentNumberDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_number_id")
    private Long id;

    @Column(name = "invoice_number")
    @Positive
    private int invoiceNumber;

    @Column(name = "cash_receipt_number")
    @Positive
    private int cashReceiptNumber;

}
