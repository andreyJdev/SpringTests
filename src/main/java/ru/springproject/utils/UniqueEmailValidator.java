package ru.springproject.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.springproject.models.User;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, User> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {

        if (user == null || user.getEmail() == null) {
            return true;
        }
        // для нового пользователя всегда id == null
        if (user.getId() == null) {
            return isEmailUnique(user.getEmail());
        // апдейт пользователя
        } else {
            return isEmailUniqueForExistingUser(user);
        }
    }

    private boolean isEmailUnique(String email) {
        try {
            // имеется ли такой email уже в БД
            Long count = (Long) entityManager
                    .createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();

            return count == 0;
        // отключаем вторую проверку на уровне БД
        } catch (NullPointerException e) {
            return true;
        }
    }

    private boolean isEmailUniqueForExistingUser(User user) {
        try {
            // проверка, есть ли пользователь с указанным id
            User existingUser = entityManager
                    .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", user.getId())
                    .getSingleResult();
            // если email не изменился
            if (existingUser.getEmail().equals(user.getEmail())) {
                return true;
            }
            // иначе проверка на уникальность
            return isEmailUnique(user.getEmail());
        // отключаем вторую проверку на уровне БД
        } catch (NullPointerException e) {
            return true;
        }
    }
}