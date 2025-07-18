package com.i2i.intern.kotam.aom.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(
            Set.of(
                    Permission.READ,
                    Permission.WRITE,
                    Permission.DELETE,
                    Permission.UPDATE
            )
    ),

    USER(
            Set.of(
                    Permission.READ,
                    Permission.WRITE,
                    Permission.UPDATE
            )
    ),

    CUSTOMER(
            Set.of(
                    Permission.READ,
                    Permission.UPDATE
            )
    );

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
