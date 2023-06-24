package com.example.contactmanager.entitie;

import android.database.Cursor;

public class Contact {
int id;
String name;
String phoneNumber1;
String phoneNumber2;
String email;
String typeContact;
String urlImage;

    public Contact() {
    }

    public Contact(String name, String phoneNumber1, String phoneNumber2, String email, String typeContact, String urlImage) {
        this.name = name;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.email = email;
        this.typeContact = typeContact;
        this.urlImage = urlImage;
    }

    public Contact(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        this.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        this.phoneNumber1 = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber1"));
        this.phoneNumber2 = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber2"));
        this.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        this.typeContact = cursor.getString(cursor.getColumnIndexOrThrow("typeContact"));
        this.urlImage = cursor.getString(cursor.getColumnIndexOrThrow("urlImage"));;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTypeContact() {
        return typeContact;
    }

    public void setTypeContact(String typeContact) {
        this.typeContact = typeContact;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
