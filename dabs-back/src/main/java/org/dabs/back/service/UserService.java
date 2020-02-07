package org.dabs.back.service;

import org.dabs.back.entity.User;
import org.dabs.back.model.bind.ChangePasswordModel;
import org.dabs.back.model.bind.UserRegistrationModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(UserRegistrationModel registrationModel);
    boolean updatePassword(ChangePasswordModel changePasswordModel);
}
