package org.dabs.back.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = IsTimeFromAndToIntervalValidator.class)
public @interface IsTimeFromAndToInterval {

    String message() default "Time From and To is not a valid interval";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
