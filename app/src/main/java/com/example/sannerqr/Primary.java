package com.example.sannerqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Primary extends AppCompatActivity {
    DBHelper mydb;
    Button b1;
    EditText ed1,ed2;
    TextView tx1;
    int counter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllHistorique();
        b1 = (Button)findViewById(R.id.bouton);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);

        tx1 = (TextView)findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Primary.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
