package digit.matrimony.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidUserRoleValidator.class)
@Target({ ElementType.TYPE })   // ✅ applies to class-level (UserDTO)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserRole {
    String message() default "Invalid role and linked user combination";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
