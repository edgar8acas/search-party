package com.androidcourse.searchparty.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//@Dao
public interface UserDao {
    //@Query("SELECT * FROM users")
    List<User> getAll();

    //@Query("SELECT * FROM users WHERE email LIKE :email LIMIT 1")
    User findByEmail(String email);

    //@Insert
    void insertAll(User... users);

    //@Delete
    void delete(User user);
}
