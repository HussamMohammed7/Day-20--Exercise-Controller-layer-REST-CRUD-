package com.example.exerciseetasktracker.Controller;


import com.example.exerciseetasktracker.Api.ApiResponse;
import com.example.exerciseetasktracker.Model.TaskTracker;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/task-tracker/")





public class ControllerTaskTracker {

    ArrayList<TaskTracker> taskTrackers = new ArrayList<TaskTracker>();



    @GetMapping("/get")
    public ArrayList<TaskTracker> getTaskTrackers() {

        return taskTrackers;
    }

    @PostMapping("/create")
    public ApiResponse addTaskTracker(@RequestBody TaskTracker taskTracker) {
        Random randa = new Random();
        String randomID;
        boolean idExists;

        do {
            randomID = Integer.toString(randa.nextInt(1000000));
            idExists = false;
            for (TaskTracker existingTaskTracker : taskTrackers) {
                if (existingTaskTracker.getID().equals(randomID)) {
                    idExists = true;
                    break;
                }
            }
        } while (idExists);

        taskTracker.setID(randomID);
        taskTrackers.add(taskTracker);
        return new ApiResponse("TaskTracker added with ID: " + randomID, "202");
    }


    @PutMapping("/update-id/{id}")
    public ApiResponse updateIdTaskTracker(@PathVariable String id, @RequestBody TaskTracker taskTracker) {
        TaskTracker existingTaskTracker = null;
        int index = -1;

        for (int i = 0; i < taskTrackers.size(); i++) {
            if (taskTrackers.get(i).getID().equals(id)) {
                existingTaskTracker = taskTrackers.get(i);
                index = i;
                break;
            }
        }

        if (existingTaskTracker == null) {
            return new ApiResponse("Task tracker id not found", "404");
        }

        if (taskTracker.getTitle() != null) {
            existingTaskTracker.setTitle(taskTracker.getTitle());
        }
        if (taskTracker.getDescription() != null) {
            existingTaskTracker.setDescription(taskTracker.getDescription());
        }
        if (taskTracker.getStatus() != null) {
            if (taskTracker.getStatus().equalsIgnoreCase("done") || taskTracker.getStatus().equalsIgnoreCase("not done")) {
                existingTaskTracker.setStatus(taskTracker.getStatus());
            }else {
                return new ApiResponse("Task tracker status only except done or not done", "404");
            }
        }

        taskTrackers.set(index, existingTaskTracker);
        return new ApiResponse("Task tracker id updated", "202");
    }







    @DeleteMapping ("/delete-id/{id}")
    public ApiResponse deleteTaskTrackerID(@PathVariable String id) {

        for (TaskTracker taskTracker : taskTrackers) {
            if (id.equals(taskTracker.getID())) {
                taskTrackers.remove(taskTracker);
                return new ApiResponse("task tracker by id deleted", "202");
            }
        }
        return new ApiResponse("task tracker by id not found", "404");

    }



    @PutMapping("/update-id-status/{id}/{status}")
    public ApiResponse updateStatusIdTaskTracker(@PathVariable String id , @PathVariable String status) {

        String statusRaw = status;
        int index = -1;

        for (int i = 0; i < taskTrackers.size(); i++) {
            if (taskTrackers.get(i).getID().equals(id)) {
                index = i;
                break;
            }
        }

        if(index == -1){
            return new ApiResponse("Task tracker id not found", "404");
        }

        if (statusRaw.equalsIgnoreCase("done") || statusRaw.equalsIgnoreCase("not done")) {
            taskTrackers.get(index).setStatus(statusRaw);
            return new ApiResponse("Task tracker status updated", "202");
        }

        return new ApiResponse("task tracker not updated, check your status typing", "404");
    }



    @GetMapping("/search-title/{title}")
    public TaskTracker searchTitleTaskTracker(@PathVariable String title ) {

       for (TaskTracker taskTracker : taskTrackers) {
           if (taskTracker.getTitle().equals(title)) {

             return taskTracker ;
           }
       }

        return null ;

    }
}
