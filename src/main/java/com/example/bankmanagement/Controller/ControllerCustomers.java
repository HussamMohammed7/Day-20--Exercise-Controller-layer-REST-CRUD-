package com.example.bankmanagement.Controller;

import com.example.bankmanagement.Api.ApiResponse;
import com.example.bankmanagement.Model.Customers;
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
    public ApiResponse addCustomer(@RequestBody Customers customer) {
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

        return new ApiResponse("Customer added successfully", "202");
    }




    @PutMapping("/update-id/{id}")
    public ApiResponse updateIdCustomer(@PathVariable String id, @RequestBody Customers customer) {
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
            return new ApiResponse("ID not found ", "400");
        }

        if (customer.getBalance() != 0) {
            existingCustomer.setBalance(customer.getBalance());
        }
        if (customer.getUsername() != null) {
            existingCustomer.setUsername(customer.getUsername());
        }

        customers.set(index, existingCustomer);
        return new ApiResponse("customer updated", "202");
    }






    @DeleteMapping ("/delete-id/{id}")
    public ApiResponse deleteCustomersID(@PathVariable String id) {

        for (Customers customer : customers) {
            if (id.equals(customer.getID())) {
                customers.remove(customer);
                return  new ApiResponse("customer deleted", "202");
            }
        }
        return new ApiResponse("customer delete by ID not found", "404");

    }

    @PutMapping("deposit/{id}/{amount}")
    public ApiResponse depositMoney(@PathVariable String id ,@PathVariable double amount  ) {
        int index = -1;

        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getID().equals(id)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return new ApiResponse("customer not found", "404");
        }
        Customers customer = customers.get(index);

        if (amount <= 0) {
            return new ApiResponse("amount is not possible", "400");
        }

        customers.get(index).setBalance(customer.getBalance()+amount);
        return new ApiResponse("customer deposit successfully", "202");

    }

    @PutMapping("withdraw/{id}/{amount}")
    public ApiResponse withdrawMoney(@PathVariable String id ,@PathVariable double amount  ) {
        int index = -1;

        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getID().equals(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return   new ApiResponse("customer not found", "404");
        }
        Customers customer = customers.get(index);

        if (amount <= 0) {
            return new ApiResponse("Amount must be positive number", "400");
        }
        if (customer.getBalance() < amount) {
            return new ApiResponse("Insufficient balance", "400");
        }


        customer.setBalance(customer.getBalance() - amount);
        return new ApiResponse("Withdrawal successful", "202");


    }

    @PutMapping("/transfer/{idSender}/{idReciver}/{amount}")
    public ApiResponse transferCustomers(@PathVariable String idSender, @PathVariable String idReciver,@PathVariable double amount) {

        Customers sender = null;
        Customers receiver = null;
        for (Customers i : customers) {
            if (idSender.equals(i.getID())) {
                sender = i;
                break;
            }
        }
        for (Customers i : customers) {
            if (idReciver.equals(i.getID())) {
                receiver = i;
                break;
            }
        }

        if (sender != null && receiver != null) {
            if (sender.getBalance() >= amount) {
                sender.setBalance(sender.getBalance() - amount);
                receiver.setBalance(receiver.getBalance() +amount);
                return new ApiResponse("customer transfer successfully", "202");
            }
        }else{
            return  new ApiResponse("Users not found ", "404");
        }

        return  new ApiResponse("Balance not enough", "404");
    }





}
