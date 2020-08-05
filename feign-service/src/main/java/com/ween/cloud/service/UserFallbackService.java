package com.ween.cloud.service;

import com.ween.cloud.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserFallbackService implements UserService {
    @Override
    public void create(User user) {
        User defaultUser=new User(-1L,"defaultUser",99);
    }

    @Override
    public User getUser(Long id) {
        return new User(-1L,"defaultUser",99);
    }

    @Override
    public void update(User user) {
        User defaultUser=new User(-1L,"defaultUser",99);
    }

    @Override
    public void delete(Long id) {
        User defaultUser=new User(-1L,"defaultUser",99);
    }
}
