package com.example.bankmanagement.Controller;

import com.example.bankmanagement.Api.ApiResponse;
import com.example.bankmanagement.Api.CustomerApiResponse;
import com.example.bankmanagement.Api.TransferApi;
import com.example.bankmanagement.Model.Customers;
import com.example.bankmanagement.Model.TransactionRequest;
import com.example.bankmanagement.Model.TransferMoney;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/customers/")



public class ControllerCustomers {
    ArrayList<Customers> customers = new ArrayList<Customers>();





    @GetMapping("/get")
    public ArrayList<Customers> getTaskTrackers() {
        return customers;
    }


    @PostMapping("/add")
    public CustomerApiResponse addCustomer(@RequestBody Customers customer) {
        Random randa = new Random();
        String randomID;
        boolean idExists;

        do {
            randomID = Integer.toString(randa.nextInt(1000000));
            idExists = false;
            for (Customers i : customers) {
                if (i.getID().equals(randomID)) {
                    idExists = true;
                    break;
                }
            }
        } while (idExists);

        customer.setID(randomID);
        customers.add(customer);

        return new CustomerApiResponse( new ApiResponse("Customer added successfully", "202"), customer );
    }


    @PutMapping("/update/{index}")
    public CustomerApiResponse updateCustomers(@PathVariable int index ,@RequestBody Customers customer) {
        if (index < 0 || index >= customers.size()) {
            return new CustomerApiResponse(new ApiResponse("Index out of bounds", "400"),null);
        }
        Customers existingCustomer = customers.get(index);

        if (customer.getBalance() != 0) {
            existingCustomer.setBalance(customer.getBalance());
        }
        if (customer.getUsername() != null) {
            existingCustomer.setUsername(customer.getUsername());
        }

        customers.set(index, existingCustomer);
        return new CustomerApiResponse(new ApiResponse("customer updated", "202"),existingCustomer);
    }


    @PutMapping("/update-id/{id}")
    public CustomerApiResponse updateIdCustomer(@PathVariable String id, @RequestBody Customers customer) {
        Customers existingCustomer = null;
        int index = -1;

        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getID().equals(id)) {
                existingCustomer = customers.get(i);
                index = i;
                break;
            }
        }

        if (existingCustomer == null) {
            return new CustomerApiResponse(new ApiResponse("ID not found ", "400"),null);
        }

        if (customer.getBalance() != 0) {
            existingCustomer.setBalance(customer.getBalance());
        }
        if (customer.getUsername() != null) {
            existingCustomer.setUsername(customer.getUsername());
        }

        customers.set(index, existingCustomer);
        return new CustomerApiResponse(new ApiResponse("customer updated", "202"),existingCustomer);
    }





    @DeleteMapping ("/delete/{index}")
    public CustomerApiResponse deleteCustomers(@PathVariable int index) {

        if (index < 0 || index >= customers.size()) {
            return new CustomerApiResponse(new ApiResponse("Index out of bounds", "400"),null);
        }


        Customers deletedCustomer = customers.get(index);
        customers.remove(index);
        return new CustomerApiResponse(new ApiResponse("customer updated", "202"),deletedCustomer);
    }

    @DeleteMapping ("/delete-id/{id}")
    public CustomerApiResponse deleteCustomersID(@PathVariable String id) {

        for (Customers customer : customers) {
            if (id.equals(customer.getID())) {
                customers.remove(customer);
                return new CustomerApiResponse(new ApiResponse("customer deleted", "202"),customer);
            }
        }
        return new CustomerApiResponse(new ApiResponse("customer delete by ID not found", "404"),null);

    }

    @PutMapping("deposit")
    public CustomerApiResponse depositMoney(@RequestBody TransactionRequest trans) {
        int index = -1;

        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getID().equals(trans.getID())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return new CustomerApiResponse(new ApiResponse("customer not found", "404"),null);
        }
        Customers customer = customers.get(index);

        if (trans.getAmount() <= 0) {
            return new CustomerApiResponse(new ApiResponse("amount is not possible", "400"),null);
        }

        customers.get(index).setBalance(customer.getBalance()+trans.getAmount());
        return new CustomerApiResponse(new ApiResponse("customer deposit successfully", "202"),customer);

    }

    @PutMapping("withdraw")
    public CustomerApiResponse withdrawMoney(@RequestBody TransactionRequest trans) {
        int index = -1;

        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getID().equals(trans.getID())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return new CustomerApiResponse(new ApiResponse("customer not found", "404"),null);
        }
        Customers customer = customers.get(index);

        if (trans.getAmount() <= 0) {
            return new CustomerApiResponse(new ApiResponse("Amount must be positive number", "400"), customer);
        }
        if (customer.getBalance() < trans.getAmount()) {
            return new CustomerApiResponse(new ApiResponse("Insufficient balance", "400"), customer);
        }


        customer.setBalance(customer.getBalance() - trans.getAmount());
        return new CustomerApiResponse(new ApiResponse("Withdrawal successful", "202"), customer);



    }

    @PutMapping("/transfer")
    public TransferApi transferCustomers(@RequestBody TransferMoney transferMoney) {

        Customers sender = null;
        Customers receiver = null;
        for (Customers i : customers) {
            if (transferMoney.getIdSender().equals(i.getID())) {
                sender = i;
                break;
            }
        }
        for (Customers i : customers) {
            if (transferMoney.getIdReceiver().equals(i.getID())) {
                receiver = i;
                break;
            }
        }

        if (sender != null && receiver != null) {
            if (sender.getBalance() >= transferMoney.getMoneyTrans()) {
                sender.setBalance(sender.getBalance() - transferMoney.getMoneyTrans());
                receiver.setBalance(receiver.getBalance() +transferMoney.getMoneyTrans());
                return new TransferApi(new ApiResponse("customer transfer successfully", "202"), sender, receiver);
            }
        }else{
            return new TransferApi(new ApiResponse("Users not found ", "404"), sender, receiver);
        }

        return new TransferApi(new ApiResponse("Balance not enough", "404"), sender, receiver);
    }





}
