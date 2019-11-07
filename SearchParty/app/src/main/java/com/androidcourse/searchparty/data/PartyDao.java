package com.androidcourse.searchparty.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PartyDao {
    @Query("SELECT * FROM parties")
    List<Party> getAll();

    @Insert
    void insertAll(Party... parties);

    @Delete
    void delete(Party party);
}
