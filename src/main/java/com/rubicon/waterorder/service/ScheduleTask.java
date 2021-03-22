package com.rubicon.waterorder.service;

import com.rubicon.waterorder.event.TaskCompletePublisher;

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
        taskCompletePublisher.notifyStartTaskDone();
        System.out.println("task called!");
        return taskCompletePublisher;
    }

}