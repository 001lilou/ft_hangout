package com.example.ft_hangout.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.arch.persistence.room.Delete;


import com.example.ft_hangout.entity.Contacts;

import java.util.List;

@Dao
public interface ContactsDao {

    @Insert
    void insert(Contacts contact);

    @Update
    void update(Contacts contact);

    @Delete
    void delete(Contacts contact);

    @Query("DELETE FROM contacts_table")
    void deleteAllContacts();

    @Query("SELECT * FROM contacts_table ORDER BY _lastname ASC")
    LiveData<List<Contacts>> getAllContacts();

    @Query(value = "SELECT * FROM contacts_table WHERE _id = :id")
    Contacts selectedContact(int id);

}
