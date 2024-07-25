package com.example.exerciseetasktracker.Api;

import com.example.exerciseetasktracker.Model.TaskTracker;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrackerRespons {




    private ApiResponse apiResponse;
    private TaskTracker taskTracker;


}
