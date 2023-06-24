package com.example.contactmanager.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.contactmanager.entitie.Contact;
import com.example.contactmanager.service.ConnexionBd;

import java.util.ArrayList;

public class ContactManager {
    private static final String nomTableContact = "contact";

    public static ArrayList<Contact> getAll(Context context){
        SQLiteDatabase bd = ConnexionBd.getBd(context);
        String query = "SELECT * FROM contact";
        ArrayList<Contact> listContacts = null;
        Cursor cursor = bd.rawQuery(query, null);

        if (cursor.isBeforeFirst()) {
            listContacts = new ArrayList<>();
            while (cursor.moveToNext()) {
                listContacts.add(new Contact(cursor));
            }
        }
        return listContacts;
    }

    public static long addContact(Context context, Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", contact.getName());
        contentValues.put("phoneNumber1", contact.getPhoneNumber1());
        contentValues.put("phoneNumber2", contact.getPhoneNumber2());
        contentValues.put("email", contact.getEmail());
        contentValues.put("typeContact", contact.getTypeContact());
        contentValues.put("urlImage", contact.getUrlImage());
        SQLiteDatabase bd = ConnexionBd.getBd(context);
        return bd.insert(nomTableContact, null, contentValues);
    }

    public static ArrayList<Contact> getByType(Context context, String typeContact){
        SQLiteDatabase bd = ConnexionBd.getBd(context);
        String query = "SELECT * FROM contact WHERE typeContact = ?";
        ArrayList<Contact> contactAtType = null;
        Cursor cursor = bd.rawQuery(query, new String[]{typeContact});

        if (cursor.isBeforeFirst()) {
            contactAtType = new ArrayList<>();
            while (cursor.moveToNext()) {
                contactAtType.add(new Contact(cursor));

            }
        }

        return contactAtType;
    }

    public static void delete(Context context, int idContact){
        SQLiteDatabase bd = ConnexionBd.getBd(context);
        bd.delete(nomTableContact, "id = ?", new String[]{String.valueOf(idContact)});
    }

    public static void update(Context context, Contact contactToUpdate){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", contactToUpdate.getName());
        contentValues.put("phoneNumber1", contactToUpdate.getPhoneNumber1());
        contentValues.put("phoneNumber2", contactToUpdate.getPhoneNumber2());
        contentValues.put("email", contactToUpdate.getEmail());
        contentValues.put("typeContact", contactToUpdate.getTypeContact());
        contentValues.put("urlImage", contactToUpdate.getUrlImage());

        SQLiteDatabase bd = ConnexionBd.getBd(context);
        long nbRowChange = bd.update(nomTableContact, contentValues, "id=?", new String[]{String.valueOf(contactToUpdate.getId())});
    }
    public static ArrayList<Contact> searchContacts(Context context, String nomContactSearch){
        SQLiteDatabase bd = ConnexionBd.getBd(context);
        String query = "SELECT * FROM contact WHERE name like ?";
        ArrayList<Contact> contacts = null;
        Cursor cursor = bd.rawQuery(query, new String[]{"%" + nomContactSearch + "%"});

        if (cursor.isBeforeFirst()) {
            contacts = new ArrayList<>();
            while (cursor.moveToNext()) {
                contacts.add(new Contact(cursor));

            }
        }
        return contacts;
    }
}
