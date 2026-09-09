package com.mmed.ws.service;

import com.mmed.ws.model.User;

public interface UserService {

    User addUser(User user);
    String login(String email, String password);

}
