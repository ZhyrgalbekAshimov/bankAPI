package com.bank.bankAPI.repository;

import com.bank.bankAPI.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    Optional<User> findByUsername(String username);
  //  Optional<User> findByPhoneNumbers(String phoneNumber);
   // Optional<User> findByPhoneNumbersAndIsActiveTrue(String phoneNumber)
//    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
//    boolean existsByUsername(String username);
//    boolean existsByEmail(String email);
    //Optional<User> findByEmailsOrPhoneNumbersIn(String email, String phoneNumber);
    Page<User> findAllByIsActiveTrue(Pageable pageable);

    Page<User> findAll(Specification<User> filterBy, Pageable pageable);
//    List<User> findByDateOfBirthAfter(LocalDate dateOfBirth, Pageable pageable);
//    List<User> findByFullNameLike(String fullNamePattern, Pageable pageable);
}
