package com.github.belousovea.taskmanageresbot.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.DateFormat;

import java.time.LocalDateTime;

@Document(indexName = "memo")
@Data
public class Memo {

    @Id
    private String id;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    @NotNull
    private LocalDateTime reminderTime;

    @Field(type = FieldType.Text)
    @NotNull
    private String reminderText;

    @Field(type = FieldType.Integer)
    @NotNull
    private int periodicityMinutes;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private LocalDateTime nextEventTime;

    @Field(type = FieldType.Long)
    private Long userId;
}
