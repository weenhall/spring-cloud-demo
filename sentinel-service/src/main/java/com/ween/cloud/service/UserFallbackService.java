package com.ween.cloud.service;

import com.ween.cloud.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserFallbackService implements UserService {

    @Override
    public void add(User user) {
        User defaultUser=new User(-1L,"默认用户",99);
    }

    @Override
    public User getUser(Long id) {
        return new User(-1L,"默认用户",99);
    }
}
