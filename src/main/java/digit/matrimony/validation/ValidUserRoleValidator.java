package digit.matrimony.validation;

import digit.matrimony.dto.UserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUserRoleValidator implements ConstraintValidator<ValidUserRole, UserDTO> {

    @Override
    public boolean isValid(UserDTO user, ConstraintValidatorContext context) {
        if (user == null || user.getRoleId() == null) {
            return false; // role must always be set
        }

        Short role = user.getRoleId();

        switch (role) {
            case 1: // Admin
                return true;

            case 2: // Manager
                // Managers can exist without linked users
                return true;

            case 3: // User
                // Users should NOT have linkedUserId
                if (user.getLinkedUserId() != null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("User cannot be linked to another user")
                            .addPropertyNode("linkedUserId")
                            .addConstraintViolation();
                    return false;
                }
                return true;

            case 4: // Family Member
                // Family members MUST have a linked user
                if (user.getLinkedUserId() == null) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Family member must be linked to a user")
                            .addPropertyNode("linkedUserId")
                            .addConstraintViolation();
                    return false;
                }
                return true;

            default:
                return false;
        }
    }
}
