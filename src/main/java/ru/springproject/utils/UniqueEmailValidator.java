package ru.springproject.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }
        try {
            Long count = (Long) entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
            return count == 0;
        } catch (NullPointerException e) {
            return true;
        }
    }
}