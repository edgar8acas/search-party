package com.androidcourse.searchparty.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//@Dao
public interface UserPartyDaoJoin {
    //@Insert
    void insert(UserPartyJoin userPartyJoin);

    //@Query("SELECT * FROM users " +
           //"INNER JOIN users_parties_join " +
           //"ON users.id = users_parties_join.userId " +
           //"WHERE users_parties_join.partyId=:partyId")
    List<User> getUsersForParty(final int partyId);

    //@Query("SELECT * FROM parties " +
            //"INNER JOIN users_parties_join " +
            //"ON parties.id = users_parties_join.partyId " +
            //"WHERE users_parties_join.partyId=:userId")
    List<Party> getPartiesForUser(final int userId);

}
