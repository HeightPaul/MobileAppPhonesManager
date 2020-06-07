package com.example.sqlite_phones;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String DB_NAME = "kontakti.db";
    protected EditText editName, editTel, editEmail;
    protected Button btnInsert;
    protected String dbPath;

    public void initDB() throws SQLException {
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

    protected void switchToUpdateView(TableRow tr)
    {
        tr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TableRow tr = (TableRow) view;
                TextView idView = (TextView) tr.getChildAt(0);
                TextView nameView = (TextView) tr.getChildAt(1);
                TextView emailView = (TextView) tr.getChildAt(2);
                TextView telView = (TextView) tr.getChildAt(3);
                String ID = idView.getText().toString();
                String name = nameView.getText().toString();
                String email = emailView.getText().toString();
                String tel = telView.getText().toString();

                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", ID);
                bundle.putString("name", name);
                bundle.putString("email", email);
                bundle.putString("tel", tel);
                intent.putExtras(bundle);
                startActivityForResult(intent, 200, bundle);
            }
        });
    }

    protected void selectDB() throws SQLException {
        SQLiteDatabase db = null;
        db = SQLiteDatabase.openOrCreateDatabase(dbPath,null);
        String q = "SELECT * FROM KONTAKTI;";
        Cursor c = db.rawQuery(q,null);
        TableLayout table = findViewById(R.id.table);
        View theadView = table.getChildAt(0);
        table.removeAllViewsInLayout();
        table.addView(theadView);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT, 1f);
        while(c.moveToNext())
        {
            String name = c.getString(c.getColumnIndex("name"));
            String email = c.getString(c.getColumnIndex("email"));
            String tel = c.getString(c.getColumnIndex("tel"));
            String ID = c.getString(c.getColumnIndex("ID"));
            TextView tvId = new TextView(this);
            tvId.setText(ID);
            tvId.setLayoutParams(params);
            TextView tvName = new TextView(this);
            tvName.setText(name);
            tvName.setLayoutParams(params);
            TextView tvEmail = new TextView(this);
            tvEmail.setText(email);
            tvEmail.setLayoutParams(params);
            TextView tvTel = new TextView(this);
            tvTel.setText(tel);
            tvTel.setLayoutParams(params);
            TableRow tr = new TableRow(this);
            tr.setId(Integer.parseInt(ID));
            tr.addView(tvId);
            tr.addView(tvName);
            tr.addView(tvEmail);
            tr.addView(tvTel);
            tr.setBackgroundColor(Color.WHITE);
            switchToUpdateView(tr);
            table.addView(tr);
        }
        db.rawQuery(q,null);
        db.close();
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            selectDB();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editTel = findViewById(R.id.editTel);
        btnInsert = findViewById(R.id.btnInsert);
        dbPath = getFilesDir().getPath()+"/"+ DB_NAME;

        try{
            initDB();
            selectDB();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
        BtnListener btnListener = new BtnListener();
        btnListener.insertListener(this);
    }
}
