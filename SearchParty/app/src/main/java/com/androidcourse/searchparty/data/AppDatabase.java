package com.androidcourse.searchparty.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.androidcourse.searchparty.utils.DateConverter;

//@Database(entities = {User.class, Party.class, UserPartyJoin.class}, version = 1)
//@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
