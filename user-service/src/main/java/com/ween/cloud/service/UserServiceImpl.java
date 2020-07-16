package com.ween.cloud.service;

import com.ween.cloud.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserServiceImpl implements UserService {

    private List<User> listOfUser;

    @PostConstruct
    public void init() {
        listOfUser = new ArrayList<>();
        listOfUser.add(new User(1L, "张三", 20));
        listOfUser.add(new User(2L, "李四", 21));
        listOfUser.add(new User(3L, "王五", 18));
    }

    @Override
    public void add(User user) {
        listOfUser.add(user);
    }

    @Override
    public User findById(Long id) {
        AtomicReference<User> result = new AtomicReference<>();
        listOfUser.forEach(user -> {
            if (user.getId().equals(id)) {
                result.set(user);
            }
        });
        return result.get();
    }

    @Override
    public void update(User user) {
        Long id = user.getId();
        listOfUser.forEach(user1 -> {
            if (user1.getId().equals(id)) {
                user1.setName(user.getName());
                user1.setAge(user.getAge());
            }
        });
    }

    @Override
    public void delete(Long id) {
        listOfUser.removeIf(user -> user.getId().equals(id));
    }
}
