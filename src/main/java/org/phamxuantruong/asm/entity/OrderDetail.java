package org.phamxuantruong.asm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Orderdetails")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetail  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Double price;
    Integer quantity;
    @ManyToOne
    @JoinColumn(name = "Productid")
    Product product;
    @ManyToOne
    @JoinColumn(name = "Orderid")
    Order order;
}
