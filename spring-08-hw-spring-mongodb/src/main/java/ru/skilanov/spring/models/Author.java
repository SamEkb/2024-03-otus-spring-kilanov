package ru.skilanov.spring.models;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    private String id;

    private String fullName;
}
