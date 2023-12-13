package com.ukma.cleaning.utils.security;

import com.ukma.cleaning.user.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityContextAccessor {
    public static UserEntity getAuthenticatedUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    public static Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }
}
