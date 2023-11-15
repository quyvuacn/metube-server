package org.aptech.metube.personal.entity;

import lombok.*;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int unit;
    private int amount;
    private String status;
    private String payer;
    private String payee;
    private Long accountTypeId;
    private Long userId;
    private String address;
    private LocalDateTime createDate;
}
