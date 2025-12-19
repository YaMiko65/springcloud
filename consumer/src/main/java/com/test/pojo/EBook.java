package com.test.pojo;

import java.math.BigDecimal;

public class EBook {

    private Integer id;
    private String name;
    private String author;
    private String press;
    private String status; // 0:可借阅, 1:借阅中, 2:归还中, 3:已售出
    private BigDecimal price; // 新增价格字段

    public EBook() {
    }

    public EBook(Integer id, String name, String author, String press, String status, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.press = press;
        this.status = status;
        this.price = price;
    }

    @Override
    public String toString() {
        return "EBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", press='" + press + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
