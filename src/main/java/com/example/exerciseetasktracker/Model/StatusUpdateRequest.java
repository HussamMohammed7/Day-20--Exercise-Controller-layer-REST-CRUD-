package com.example.exerciseetasktracker.Model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class StatusUpdateRequest {

    private String status,id;
}
