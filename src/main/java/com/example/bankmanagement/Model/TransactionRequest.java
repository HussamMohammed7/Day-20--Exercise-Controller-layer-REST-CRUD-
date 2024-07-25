package com.example.bankmanagement.Model;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class TransactionRequest {

    private String ID;
    private double amount;

}
