package com.example.contactmanager.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactmanager.helper.ContactBdHelper;

public class ConnexionBd {
    private static int version = 1;
    private static String bnName = "contactDb.db";
    private static SQLiteDatabase bd = null;
    private static ContactBdHelper helper;

    public static SQLiteDatabase getBd(Context context){
        if(helper == null){
            helper = new ContactBdHelper(context, bnName, null, version);
        }
        bd = helper.getWritableDatabase();
        return bd;
    }
    public static void close(){
        if(bd!= null &&  !bd.isOpen()){
            bd.close();
        }
    }
}
