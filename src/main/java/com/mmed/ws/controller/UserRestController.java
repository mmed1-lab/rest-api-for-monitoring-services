package com.mmed.ws.controller;

import com.mmed.ws.dto.RestApiResponse;
import com.mmed.ws.dto.UserDTO;
import com.mmed.ws.model.User;
import com.mmed.ws.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private final UserService service;

    public UserRestController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        User tmp = service.addUser(user); // saved in db
        UserDTO dto = new UserDTO(tmp.getId(), tmp.getEmail(), tmp.getCreatedAt());
        return new ResponseEntity<>(
                new RestApiResponse<>(true, "User created successfully", dto),
                HttpStatus.CREATED
        );
    }
}
