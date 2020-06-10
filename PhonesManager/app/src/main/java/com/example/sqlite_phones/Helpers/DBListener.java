package com.example.sqlite_phones.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.sqlite_phones.MainActivity;

public class DBListener {
    public void init(String dbPath) throws SQLException {
        SQLiteDatabase db = null;
        db = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
        String q = "CREATE TABLE if not exists PHONE_CONTACTS(";
        q += "ID integer primary key AUTOINCREMENT,";
        q += "name text not null,";
        q += "phone text not null,";
        q += "egn text not null,";
        q += "unique(name,phone));";
        db.execSQL(q);
        db.close();
    }

    public void select(MainActivity mainActivity,String dbPath){
        SQLiteDatabase db = null;
        db = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
        String q = "SELECT * FROM PHONE_CONTACTS ORDER BY ID DESC;";
        Cursor c = db.rawQuery(q,null);
        mainActivity.render(c);
        db.close();
    }

    public void insert(String dbPath, Object[] inputs, Context mainContext) throws SQLException {
        SQLiteDatabase db = null;
        try{
            db = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
            String q = "INSERT INTO PHONE_CONTACTS (name,phone,egn) ";
            q+="VALUES(?,?,?)";
            db.execSQL(q,inputs);
            Toast.makeText(mainContext,"Insert is successful!",
                    Toast.LENGTH_LONG).show();
        }catch(SQLiteConstraintException e) {
            Toast.makeText(mainContext,
                    "Already exists with same name and telephone number!",
                    Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(mainContext,e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }finally{
            if(db!=null){
                db.close();
            }
        }
    }

    public void update(String dbPath, Object[] inputs,Context mainContext){
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
            String q = "UPDATE PHONE_CONTACTS SET name = ?, phone = ?, egn = ? ";
            q += "WHERE ID = ?;";
            db.execSQL(q, inputs);
            Toast.makeText(mainContext, "Update is successful!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(mainContext, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }

    public void delete(String dbPath, String id,Context mainContext){
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
            String q = "DELETE FROM PHONE_CONTACTS WHERE ";
            q += "ID = ?;";
            db.execSQL(q, new Object[] { id });
            Toast.makeText(mainContext, "Delete is successful!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(mainContext, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db != null) {
                db.close();
                db = null;
            }
        }
    }
}
