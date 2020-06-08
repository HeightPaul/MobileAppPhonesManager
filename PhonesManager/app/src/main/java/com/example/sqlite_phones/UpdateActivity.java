package com.example.sqlite_phones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlite_phones.Helpers.DBListener;

public class UpdateActivity extends AppCompatActivity {

    protected String id;
    protected EditText editName;
    protected EditText editPhone;
    protected EditText editEmail;
    protected Button btnUpdate;
    protected Button btnDelete;
    protected DBListener dbListener = new DBListener();
    public String dbPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        Bundle bundle = getIntent().getExtras();
        dbPath = getFilesDir().getPath() + "/" + MainActivity.DB_NAME;
        if (bundle != null) {
            id = bundle.getString("ID");
            editName.setText(bundle.getString("name"));
            editPhone.setText(bundle.getString("phone"));
            editEmail.setText(bundle.getString("email"));
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editEmail.getText().toString();
                Object[] inputs = new Object[] { name, phone, email, id };
                dbListener.update(dbPath, inputs,getApplicationContext());
                closeThisActivity();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbListener.delete(dbPath,id,getApplicationContext());
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
