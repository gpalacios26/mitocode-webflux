package com.mitocode.webflux.security;

import com.mitocode.webflux.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Clase S2
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String username;

    private String password;

    private List<Role> roles;
}
