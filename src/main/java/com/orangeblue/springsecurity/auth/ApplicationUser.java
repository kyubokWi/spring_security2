package com.orangeblue.springsecurity.auth;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class ApplicationUser implements UserDetails {

    private final String username;
    private final String password;
    private final Set<? extends GrantedAuthority> grantedAuthority;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnable;


    public ApplicationUser(
        String username,
        String password,
        Set<? extends GrantedAuthority> grantedAuthority,
            boolean isAccountNonExpired,
            boolean isAccountNonLocked, 
            boolean isCredentialsNonExpired,
            boolean isEnable) {
        this.username = username;
        this.password = password;
        this.grantedAuthority = grantedAuthority;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnable = isEnable;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthority;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
    
}
