package com.example.ft_hangout.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;


@Entity(tableName = "contacts_table")
public class Contacts {

    @PrimaryKey(autoGenerate = true)
    // @ColumnInfo(name = "ID_id")
    private int _id;
    //  @ColumnInfo(name = "AVATAR_id")
    private Boolean _checkAvatar;

    private String _avatar;
    //  @ColumnInfo(name = "LASTNAME_id")
    private String _lastname;
    //   @ColumnInfo(name = "FIRSTNAME_id")
    private String _firstname;
    //    @ColumnInfo(name = "NUMOBILE_id")
    @Nullable
    private String _numobile;
    //    @ColumnInfo(name = "NUMFIX_id")
    @Nullable
    private String _nufix;
    //   @ColumnInfo(name = "MAIL_id")
    private String _mail;
    //   @ColumnInfo(name = "ADDRESS_id")
    private String _address;

    private boolean expanded;
    private boolean updateContact;


    public Contacts(Boolean checkAvatar, String avatar, String lastname, String firstname, String numobile, String nufix, String mail, String address) {

        this._checkAvatar = checkAvatar;
        this._avatar = avatar;
        this._lastname = lastname;
        this._firstname = firstname;
        this._numobile = numobile;
        this._nufix = nufix;
        this._mail = mail;
        this._address = address;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }

    public Boolean checkAvatar() {
        return _checkAvatar;
    }

    public void setCheckAvatar(Boolean _checkAvatar) {
        this._checkAvatar = _checkAvatar;
    }

    public String getAvatar() {
        return _avatar;
    }

    public void setAvatar(String _avatar) {
        this._avatar = _avatar;
    }


    public String getLastname() {
        return _lastname;
    }

    public void setLastname(String _lastname) {
        this._lastname = _lastname;
    }

    public String getFirstname() {
        return _firstname;
    }

    public void setFirstname(String _firstname) {
        this._firstname = _firstname;
    }

    public String getNumobile() {
        return _numobile;
    }

    public void setNumobile(String _numobile) {
        this._numobile = _numobile;
    }

    public String getNufix() {
        return _nufix;
    }

    public void setNufix(String _nufix) {
        this._nufix = _nufix;
    }

    public String getMail() {
        return _mail;
    }

    public void setMail(String _mail) {
        this._mail = _mail;
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String _address) {
        this._address = _address;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "_avatar='" + _avatar + '\'' +
                "_lastname='" + _lastname + '\'' +
                "_firstname='" + _firstname + '\'' +
                ", _numobile=" + _numobile +
                ", _nufix=" + _nufix +
                ", _mail='" + _mail + '\'' +
                ", _address='" + _address + '\'' +
                '}';
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setUpdateContact(boolean updateContact) {
        this.updateContact = updateContact;
    }

    public boolean isUpdateContact() {
        return updateContact;
    }

}
