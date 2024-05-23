package com.bank.bankAPI.entity;

import com.bank.bankAPI.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "phoneNumbers")

@Setter
@Getter
@NoArgsConstructor
public class PhoneNumber {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;


    @Column(nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PhoneNumber(String number, User user) {
        this.createdDate = LocalDateTime.now();
        this.number = number;
        this.user = user;
    }

}
