package com.gwhn.elasticsearch.entity;

public class UserBean {
    public String id;
    public String name;
    public int age;
    public String addr;
    public String message;

    public UserBean(String id) {
        this.id = id;
    }

    public UserBean(String id, String name, int age, String addr, String message) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.addr = addr;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
