package org.example.spring_mvc_json_view.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserSummary.class)
    Long id;

    @JsonView(Views.UserSummary.class)
    @NotBlank(message = "name must be not null!")
    String name;

    @JsonView(Views.UserSummary.class)
    @Email
    @NotBlank(message = "email must be not null!")
    String email;

    @JsonView(Views.UserDetails.class)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Order> orders;


    public interface Views {
        interface UserSummary {}
        interface UserDetails extends UserSummary {}
    }
}