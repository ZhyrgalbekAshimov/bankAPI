package com.bank.bankAPI.entity;

import com.bank.bankAPI.dto.responses.UserResponseDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // Данное поле нужно для того чтобы не удалять юзеров из БД (хранение истории о пользователей)
    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @OneToMany(cascade=CascadeType.ALL,fetch= FetchType.LAZY,mappedBy = "user")
    private List<Email> emails = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhoneNumber> phoneNumbers;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BankAccount bankAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    public UserResponseDto toUserResponseDto() {
        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(this.id);
        dto.setUsername(this.username);
        dto.setFirstName(this.firstName);
        dto.setLastName(this.lastName);
        dto.setDateOfBirth(this.dateOfBirth);

        if (!emails.isEmpty()) {
            dto.setEmails(new ArrayList<>());
            for (Email email: emails){
                dto.getEmails().add(email.getEmail());
            }

        }
        if (!phoneNumbers.isEmpty()) {
            dto.setPhoneNumbers(new ArrayList<>());
            for (PhoneNumber phoneNumber: phoneNumbers){
                dto.getPhoneNumbers().add(phoneNumber.getNumber());
            }
        }

        if (bankAccount != null) {
            dto.setBankAccountId(bankAccount.getId().toString());
            dto.setInitialDeposit(bankAccount.getInitialDeposit());
        }

        return dto;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}