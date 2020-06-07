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
        String q = "CREATE TABLE if not exists KONTAKTI(";
        q += "ID integer primary key AUTOINCREMENT,";
        q += "name text not null,";
        q += "tel text not null,";
        q += "email text not null,";
        q += "unique(name,tel));";
        db.execSQL(q);
        db.close();
    }

    public void select(MainActivity mainActivity,String dbPath){
        SQLiteDatabase db = null;
        db = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
        String q = "SELECT * FROM KONTAKTI;";
        Cursor c = db.rawQuery(q,null);
        mainActivity.render(c);
        db.close();
    }

    public void insert(final MainActivity mainActivity) throws SQLException {
        Context mainContext = mainActivity.getApplicationContext();
        SQLiteDatabase db = null;
        try{
            db = SQLiteDatabase.openOrCreateDatabase(mainActivity.dbPath,null);
            String name = mainActivity.editName.getText().toString();
            String tel = mainActivity.editTel.getText().toString();
            String email = mainActivity.editEmail.getText().toString();
            String q = "INSERT INTO KONTAKTI (name,tel,email) ";
            q+="VALUES(?,?,?)";
            db.execSQL(q,new Object[]{name,tel,email});
            Toast.makeText(mainContext,"Insert successfully!",
                    Toast.LENGTH_LONG).show();
        }catch(SQLiteConstraintException e) {
            Toast.makeText(mainContext,
                    "Already exists with same name and telephone number!",
                    Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(mainContext,e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }finally {
            if(db!=null){
                db.close();
            }
        }

        try{
            mainActivity.renderTable();
        }catch (Exception e){
            Toast.makeText(mainContext,e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
