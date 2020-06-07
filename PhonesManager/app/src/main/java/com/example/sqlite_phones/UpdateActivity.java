package com.example.sqlite_phones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    protected String id;
    protected EditText editName;
    protected EditText editTel;
    protected EditText editEmail;
    protected Button btnUpdate;
    protected Button btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        editName = findViewById(R.id.editName);
        editTel = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("ID");
            editName.setText(bundle.getString("name"));
            editTel.setText(bundle.getString("tel"));
            editEmail.setText(bundle.getString("email"));
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "kontakti.db",
                            null
                    );
                    String name = editName.getText().toString();
                    String tel = editTel.getText().toString();
                    String email = editEmail.getText().toString();
                    String q = "UPDATE KONTAKTI SET name = ?, tel = ?, email = ? ";
                    q += "WHERE ID = ?;";
                    db.execSQL(q, new Object[] { name, tel, email, id });
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    if (db != null) {
                        db.close();
                        db = null;
                    }
                }
                closeThisActivity();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/" + "kontakti.db",
                            null
                    );
                    String q = "DELETE FROM KONTAKTI WHERE ";
                    q += "ID = ?;";
                    db.execSQL(q, new Object[] { id });
                    Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    if (db != null) {
                        db.close();
                        db = null;
                    }
                }
                closeThisActivity();
            }
        });
    }
    protected void closeThisActivity() {
        finishActivity(200);
        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
