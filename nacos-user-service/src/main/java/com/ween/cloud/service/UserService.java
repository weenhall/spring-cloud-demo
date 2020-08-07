package com.ween.cloud.service;

import com.ween.cloud.pojo.User;

public interface UserService {

    void add(User user);

    User findById(Long id);

    void update(User user);

    void delete(Long id);
}
