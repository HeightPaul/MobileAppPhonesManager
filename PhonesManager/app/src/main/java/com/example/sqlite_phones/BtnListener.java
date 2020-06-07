package com.example.sqlite_phones;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BtnListener extends AppCompatActivity {

    public void insertListener(final MainActivity mainActivity)
    {
        final Context mainContext = mainActivity.getApplicationContext();
        mainActivity.btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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
                    mainActivity.selectDB();
                }catch (Exception e){
                    Toast.makeText(mainContext,e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
