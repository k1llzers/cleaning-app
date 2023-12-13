package com.ukma.cleaning.utils.security;

import com.ukma.cleaning.user.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityContextAccessor {
    public static UserEntity getAuthenticatedUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }

    public static List<String> getAuthorities() {
        return getAuthenticatedUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
