package com.company;

/**
 * Created by Troy on 10/11/16.
 */
public class User {
    int id;
    String name;
    String password;


    public User(int id,String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
