package com.github.belousovea.taskmanageresbot;

import org.springframework.boot.SpringApplication;

public class TestTaskManagerEsBotApplication {

    public static void main(String[] args) {
        SpringApplication.from(TaskManagerEsBotApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
