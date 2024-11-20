package org.example.spring_mvc_json_view.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(User.Views.UserDetails.class)
    Long id;

    @JsonView(User.Views.UserDetails.class)
    @NotBlank
    String product;

    @JsonView(User.Views.UserDetails.class)
    @NotNull
    Double amount;

    @JsonView(User.Views.UserDetails.class)
    @NotBlank
    String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}