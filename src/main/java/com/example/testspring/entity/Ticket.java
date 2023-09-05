package com.example.testspring.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ticket")
public class Ticket {
    // để là Integer có thể check null được
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String clientName;

    private String clientPhone;

    // .....

    private String content;

    @CreatedDate //auto gen new date
    @Column(updatable = false) //khi update minh khong cap nhat minh se giu nguyen
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date processDate;

    private boolean status;

    @ManyToOne
    private Department department;


}
