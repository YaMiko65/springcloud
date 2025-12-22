package com.test.pojo;

public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String valid;

    // Getters, Setters, Constructor, toString...
    public UserDto() {}
    public UserDto(Integer id, String username, String password, String valid) {
        this.id = id; this.username = username; this.password = password; this.valid = valid;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getValid() { return valid; }
    public void setValid(String valid) { this.valid = valid; }
}