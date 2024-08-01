package org.phamxuantruong.asm.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OrderDetailDTO {
    private Long productId;
    private Double price;
    private Integer quantity;
}