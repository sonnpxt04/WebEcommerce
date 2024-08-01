package org.phamxuantruong.asm.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.phamxuantruong.asm.entity.Order;
import org.phamxuantruong.asm.entity.OrderDetail;

import java.util.List;

public interface OrderService {
    Order create(JsonNode orderData);

    Order findById(Long id);

    List<Order> findByUsername(String username);

    List<Order> getAll();


    Order findByOrderNumber(String txnRef);

}
