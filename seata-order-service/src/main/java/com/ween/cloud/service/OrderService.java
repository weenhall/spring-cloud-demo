package com.ween.cloud.service;


import com.ween.cloud.domain.Order;

public interface OrderService {

    /**
     * 创建订单
     */
    void create(Order order);
}
