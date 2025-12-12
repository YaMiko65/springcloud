package com.test.pojo;

public class EBook {

    private Integer id;
    private String name;
    private String author;
    private String press;
    private String status;

    public EBook() {
    }

    public EBook(Integer id, String name, String author, String press, String status) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.press = press;
        this.status = status;
    }

    @Override
    public String toString() {
        return "EBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", press='" + press + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
