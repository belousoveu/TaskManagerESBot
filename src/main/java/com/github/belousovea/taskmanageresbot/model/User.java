package com.github.belousovea.taskmanageresbot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Document(indexName = "users")
@Builder
@AllArgsConstructor
@ToString
public class User {

    @Id
    private long userId;

    @Field(type = FieldType.Text)
    private String userName;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Long)
    private long timeOffset;


}
