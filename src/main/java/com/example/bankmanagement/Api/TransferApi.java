package com.example.bankmanagement.Api;

import com.example.bankmanagement.Model.Customers;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class TransferApi {

    private ApiResponse apiResponse;
    private Customers sender;
    private Customers receiver;

}
