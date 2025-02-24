package com.example.SpringBootMicroservice.validation;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component
public class UniqueColumnValidator implements ConstraintValidator<UniqueColumn, Object> {

    @Autowired
    private EntityManager entityManager;

    private String field;
    private Class<?> entity;
    @Autowired
    private MessageSource messageSource;


    @Override
    public void initialize(UniqueColumn constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.entity = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || value.toString().isEmpty()) {
            return true;
        }

        // consulta dinamica
        String query = "SELECT COUNT(e) FROM " + entity.getName() + " e WHERE e." + field + " = :value";
        Long count = (Long) entityManager.createQuery(query)
                .setParameter("value", value)
                .getSingleResult();

        boolean isValid = count == 0;

        if (!isValid) {
            // personaliza el mensaje dinamico
            context.disableDefaultConstraintViolation();
            // Acceder al mensaje dinámico desde el archivo messages.properties
            String format = messageSource.getMessage("validation.column.unique", new Object[]{value}, Locale.getDefault());
            String errorMessage = MessageFormat.format(format, value);
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        }

        return isValid;
    }
}
