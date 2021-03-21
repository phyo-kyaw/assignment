package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.TaskCompletePublisher;
import com.rubicon.waterorder.model.Status;
import com.rubicon.waterorder.model.WaterOrder;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class ScheduleTask implements Callable<TaskCompletePublisher>
{

    TaskCompletePublisher taskCompletePublisher;

    public ScheduleTask(TaskCompletePublisher taskCompletePublisher) {
        this.taskCompletePublisher = taskCompletePublisher;
    }

    @Override
    public TaskCompletePublisher call() throws Exception {
        System.out.println("task called!");
        taskCompletePublisher.notifyTaskDone();
        System.out.println("task called!");
        return taskCompletePublisher;
    }

}