package services;

import model.User;
import store.PsqlStore;
import store.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ValidateService implements Validate{


    private Map<Integer, User> store = Collections.emptyMap();
    private int ids = 0;

    public ValidateService() {
        PsqlStore.instOf().findAllUsers().stream().forEach(user -> this.store.put(user.getId(), user));
    }
    @Override
    public User add(User user) {
        user = PsqlStore.instOf().save(user);
        this.store.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<User>(this.store.values());
    }
}
