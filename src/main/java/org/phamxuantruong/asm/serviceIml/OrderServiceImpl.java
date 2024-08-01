package org.phamxuantruong.asm.serviceIml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.phamxuantruong.asm.dao.OrderDAO;
import org.phamxuantruong.asm.dao.OrderDetailDAO;
import org.phamxuantruong.asm.dao.ProductDAO;
import org.phamxuantruong.asm.entity.Account;
import org.phamxuantruong.asm.entity.Order;
import org.phamxuantruong.asm.entity.OrderDetail;
import org.phamxuantruong.asm.entity.Product;
import org.phamxuantruong.asm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    ProductDAO productDAO;


    @Override
    public Order create(JsonNode orderData) {
        ObjectMapper mapper = new ObjectMapper();

        // Chuyển đổi orderData thành đối tượng Order
        Order order = mapper.convertValue(orderData, Order.class);

        // Kiểm tra dữ liệu nhận được
        JsonNode orderDetailsNode = orderData.get("orderDetails");
        System.out.println("Order details from request: " + orderDetailsNode);

        List<OrderDetail> details = new ArrayList<>();
        if (orderDetailsNode != null && orderDetailsNode.isArray()) {
            for (JsonNode detailNode : orderDetailsNode) {
                Integer productId = detailNode.get("productId").asInt();  // Sử dụng asInt() nếu productId là Integer
                Double price = detailNode.get("price").asDouble();
                Integer quantity = detailNode.get("quantity").asInt();

                // Tìm kiếm sản phẩm từ cơ sở dữ liệu
                Product product = productDAO.findById(productId).orElse(null);
                if (product != null) {
                    OrderDetail detail = new OrderDetail();
                    detail.setProduct(product);
                    detail.setPrice(price);
                    detail.setQuantity(quantity);
                    detail.setOrder(order);

                    details.add(detail);
                } else {
                    System.err.println("Product with ID " + productId + " not found.");
                }
            }
        } else {
            System.err.println("Order details are missing or not in the correct format.");
        }

        // Gán danh sách OrderDetail vào đối tượng Order
        order.setOrderDetails(details);

        // Lưu đối tượng Order, bao gồm cả các OrderDetail liên quan
        return orderDAO.save(order);
    }


    @Override
    public Order findById(Long id) {
        return orderDAO.findById(id).orElse(null);
    }

    @Override
    public List<Order> findByUsername(String username) {
        return orderDAO.findByUsername(username);
    }

    @Override
    public List<Order> getAll() {
        return orderDAO.findAll();
    }

    @Override
    public Order findByOrderNumber(String txnRef) {
        return orderDAO.findByOrderNumber(txnRef);
    }

}

