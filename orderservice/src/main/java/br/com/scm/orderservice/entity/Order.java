package br.com.scm.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tbl_order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "product_id")
    private long productId;
    @Column(name = "quantity")
    private long quantity;
    @Column(name = "order_date")
    private Instant orderDate;
    @Column(name = "status")
    private String orderStatus;
    @Column(name = "total_amount")
    private long amount;
}
