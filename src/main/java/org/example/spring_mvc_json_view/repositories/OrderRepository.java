package org.example.spring_mvc_json_view.repositories;

import org.example.spring_mvc_json_view.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
