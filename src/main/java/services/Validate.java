package services;

import model.User;

import java.util.List;

public interface Validate {
    User add(User user);
    List<User> getAll();
}
