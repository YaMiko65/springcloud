package com.test.pojo;

public class Privileda {

    private Integer id;
    private String authority;

    public Privileda() {
    }

    public Privileda(Integer id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Privileda{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
