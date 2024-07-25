package com.example.bankmanagement.Api;

import com.example.bankmanagement.Model.Customers;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class CustomerApiResponse {





    private ApiResponse apiResponse;
    private Customers customer;

}
