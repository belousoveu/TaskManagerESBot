package com.github.belousovea.taskmanageresbot.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "memo")
@Data
@Builder
@AllArgsConstructor
public class Memo {

    @Id
    private String id;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    @NotNull
    private LocalDateTime reminderTime;

    @Field(type = FieldType.Text)
    @NotNull
    private String reminderText;

    @Field(type = FieldType.Text)
    private String period;

    @Field(type = FieldType.Long)
    private Long userId;

    public boolean isRepetitive() {
        return !(Period.valueOf(this.period) == Period.ONE_TIME);
    }
}
