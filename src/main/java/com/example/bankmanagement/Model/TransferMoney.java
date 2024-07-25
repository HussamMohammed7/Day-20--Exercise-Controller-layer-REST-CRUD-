package com.example.bankmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class TransferMoney

{

    private String idSender;
    private String idReceiver;
    private double moneyTrans;



}
