package org.phamxuantruong.asm.restController;

import com.fasterxml.jackson.databind.JsonNode;
import org.phamxuantruong.asm.entity.Order;
import org.phamxuantruong.asm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
    @Autowired
    OrderService orderService;
    @PostMapping()
    public Order create(@RequestBody JsonNode  orderData) {
        return orderService.create(orderData);
    }
    @GetMapping
    public List<Order> getAll() {
        return orderService.getAll();
    }
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.findById(id);
    }
}
