package org.phamxuantruong.asm.dao;

import org.phamxuantruong.asm.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailDAO extends JpaRepository<OrderDetail, Long> {
}
