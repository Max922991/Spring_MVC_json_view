package org.example.spring_mvc_json_view.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spring_mvc_json_view.models.User;
import org.example.spring_mvc_json_view.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return Optional.ofNullable(userRepository.findUserByIdWithOrders(id))
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    public User createUser(@Valid User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }


    public void deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));

        userRepository.deleteById(user.getId());
    }
}
