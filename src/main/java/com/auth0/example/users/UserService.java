package com.auth0.example.users;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRequest userRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setRole(Role.ROLE_USER);

        return userRepository.save(user);
    }

    public Optional<User> findUserByMail(String email) {
        return userRepository.findByEmail(email);
    }
}
