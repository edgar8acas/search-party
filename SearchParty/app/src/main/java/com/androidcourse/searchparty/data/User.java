package com.androidcourse.searchparty.data;

import com.google.firebase.firestore.DocumentId;

//@Entity(tableName = "users")
public class User {

    @DocumentId
    public String id;

    public String email;

    public String name;

    public int color;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
