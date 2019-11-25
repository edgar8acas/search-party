package com.androidcourse.searchparty.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;

/*@Entity(tableName = "users_parties_join",
        primaryKeys = {"userId", "partyId"},
        foreignKeys = {
            @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId"),
            @ForeignKey(entity = Party.class,
                        parentColumns = "id",
                        childColumns = "partyId")
        })*/
public class UserPartyJoin {
    public int userId;
    public int partyId;
}
