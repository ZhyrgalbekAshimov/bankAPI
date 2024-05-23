package com.bank.bankAPI.repository.specifications;

import com.bank.bankAPI.entity.Email;
import com.bank.bankAPI.entity.PhoneNumber;
import com.bank.bankAPI.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecifications {

    public static Specification<User> dateOfBirthAfter(LocalDate dateOfBirth) {
        return (root, query, criteriaBuilder) ->
                dateOfBirth != null ? criteriaBuilder.greaterThan(root.get("dateOfBirth"), dateOfBirth) : null;
    }

    public static Specification<User> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            if (phoneNumber != null) {
                Join<User, PhoneNumber> phoneJoin = root.join("phoneNumbers", JoinType.INNER);
                return criteriaBuilder.equal(phoneJoin.get("number"), phoneNumber);
            }
            return null;
        };
    }

    public static Specification<User> hasFirstNameLike(String firstName) {
        return (root, query, criteriaBuilder) ->
                firstName != null ? criteriaBuilder.like(root.get("firstName"), firstName + "%") : null;
    }

    public static Specification<User> hasLastNameLike(String lastName) {
        return (root, query, criteriaBuilder) ->
                lastName != null ? criteriaBuilder.like(root.get("lastName"), lastName + "%") : null;
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email != null) {
                Join<User, Email> emailJoin = root.join("emails", JoinType.INNER);
                return criteriaBuilder.equal(emailJoin.get("email"), email);
            }
            return null;
        };
    }

    public static Specification<User> filterBy(LocalDate dateOfBirth, String phoneNumber, String firstName, String lastName, String email) {
        return Specification.where(dateOfBirthAfter(dateOfBirth))
                .and(hasPhoneNumber(phoneNumber))
                .and(hasFirstNameLike(firstName))
                .and(hasLastNameLike(lastName))
                .and(hasEmail(email));
    }
}
