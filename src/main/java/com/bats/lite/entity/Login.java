package com.bats.lite.entity;

import com.bats.lite.enums.Roles;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@Builder
@Entity
@Table(name = "BAT_LOGIN")
@AllArgsConstructor
@NoArgsConstructor
public class Login implements UserDetails, Serializable {

    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String senha;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    protected String dataCadastro;
    @Enumerated(EnumType.STRING)
    private Roles roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Roles.values().toString()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @PrePersist
    public void setId() {
        var uuid = UUID.randomUUID();
        this.id = new String(uuid.toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        this.dataCadastro = dateTime.format(formatter);
    }
}
