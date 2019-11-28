package com.androidcourse.searchparty.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//@Entity(tableName = "parties")
public class Party {
    //@Ignore
    public Party() {
    }
    public String getName() {
        return name;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<String> getMemberUsers() {
        return memberUsers;
    }

    public void setMemberUsers(List<String> memberUsers) {
        this.memberUsers = memberUsers;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    //@Ignore
    public Party(String name, String invitationCode, Date createdAt) {
        this.name = name;
        this.invitationCode = invitationCode;
        this.createdAt = createdAt;
    }

    @DocumentId
    private String id;

    //@ColumnInfo(name = "name")
    private String name;

    //@ColumnInfo(name = "invitation_code")
    private String invitationCode;

    @ServerTimestamp
    //@ColumnInfo(name = "created_at")
    private Date createdAt;

    private List<String> memberUsers;

    public Party(String id, String name, String invitationCode, Date createdAt) {
        this.id = id;
        this.name = name;
        this.invitationCode = invitationCode;
        this.createdAt = createdAt;
    }

    public Party(String name, String invitationCode, String currentUser) {
        this.name = name;
        this.invitationCode = invitationCode;
        this.createdAt = new Date();
        this.memberUsers = new ArrayList<>();
        memberUsers.add(currentUser);
    }

    public static final ArrayList<Party> parties = new ArrayList<>(
            Arrays.asList(
                    new Party("1", "Blue party", "ABC123", new Date()),
                    new Party("2", "Another party", "ABC123", new Date()),
                    new Party("3", "Another one", "ABC123", new Date())
            )
    );

}
