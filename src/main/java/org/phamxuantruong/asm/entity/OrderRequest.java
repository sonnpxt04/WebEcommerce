package org.phamxuantruong.asm.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.phamxuantruong.asm.DTO.OrderDetailDTO;

import java.util.List;

@Data
public class OrderRequest {
    private String address;
    private Account account;
    private List<OrderDetailDTO> orderDetails;

    @Data
    public static class OrderDetailDTO {
        private Long productId;
        private Double price;
        private Integer quantity;
    }
}