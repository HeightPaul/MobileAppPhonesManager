package com.example.sqlite_phones;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlite_phones.Helpers.DBListener;

public class MainActivity extends AppCompatActivity {

    public static final String DB_NAME = "kontakti.db";
    public static final String NAME_PATTERN = "^([A-Z][a-z]*((\\s)))+[A-Z][a-z]*$";
    public EditText editName, editPhone, editEgn;
    public Button btnInsert;
    public String dbPath;
    protected DBListener dbListener = new DBListener();

    public void switchToUpdateView(TableRow tr)
    {
        tr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TableRow tr = (TableRow) view;
                TextView idView = (TextView) tr.getChildAt(0);
                TextView nameView = (TextView) tr.getChildAt(1);
                TextView egnView = (TextView) tr.getChildAt(2);
                TextView phoneView = (TextView) tr.getChildAt(3);
                String ID = idView.getText().toString();
                String name = nameView.getText().toString();
                String egn = egnView.getText().toString();
                String phone = phoneView.getText().toString();

                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", ID);
                bundle.putString("name", name);
                bundle.putString("egn", egn);
                bundle.putString("phone", phone);
                intent.putExtras(bundle);
                startActivityForResult(intent, 200, bundle);
            }
        });
    }

    public void renderTable(){
        dbListener.select(this,dbPath);
    }

    public void render(Cursor c){
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
            String egn = c.getString(c.getColumnIndex("egn"));
            String phone = c.getString(c.getColumnIndex("phone"));
            String ID = c.getString(c.getColumnIndex("ID"));
            TextView tvId = new TextView(this);
            tvId.setText(ID);
            tvId.setLayoutParams(params);
            TextView tvName = new TextView(this);
            tvName.setText(name);
            tvName.setLayoutParams(params);
            TextView tvEgn = new TextView(this);
            tvEgn.setText(egn);
            tvEgn.setLayoutParams(params);
            TextView tvPhone = new TextView(this);
            tvPhone.setText(phone);
            tvPhone.setLayoutParams(params);
            TableRow tr = new TableRow(this);
            tr.setId(Integer.parseInt(ID));
            tr.addView(tvId);
            tr.addView(tvName);
            tr.addView(tvEgn);
            tr.addView(tvPhone);
            tr.setBackgroundColor(Color.WHITE);
            switchToUpdateView(tr);
            table.addView(tr);
        }
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            renderTable();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.editName);
        editEgn = findViewById(R.id.editEgn);
        editPhone = findViewById(R.id.editPhone);
        btnInsert = findViewById(R.id.btnInsert);
        dbPath = getFilesDir().getPath()+"/"+ DB_NAME;
        try{
            dbListener.init(dbPath);
            renderTable();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String egn = editEgn.getText().toString();
                if(name.matches(NAME_PATTERN))
                {
                    Object[] inputs = new Object[]{name,phone,egn};
                    dbListener.insert( dbPath, inputs, getApplicationContext() );
                    try{
                        renderTable();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
