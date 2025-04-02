package com.pfe.config;

public class JwtResponse {
    public JwtResponse(String token,Long id,String role) {
        this.token = token;
        this.id=id;
        this.role=role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private Long id;
 private String role;

}