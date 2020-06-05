package com.example.sannerqr;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecyclerAdapterView.ItemClickListener {
    ListView heures;
    Button scan_btn;
    RecyclerAdapterView  adapter;
    int from_Where_I_Am_Coming = 0;
    int id_To_Update = 0;
    private DBHelper mydb ;
    TextView texte;
    TextView heure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texte = (TextView) findViewById(R.id.list);
        heure = (TextView) findViewById(R.id.heure);
        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");

            if (Value > 0) {
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String text = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TEXTE));
                String hours = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_HEURE));
                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button) findViewById(R.id.scan_btn);
                b.setVisibility(View.INVISIBLE);
                texte.setText((CharSequence) text);
                texte.setFocusable(false);
                texte.setClickable(false);

                heure.setText((CharSequence) hours);
                heure.setFocusable(false);
                heure.setClickable(false);
            }
        }}

    Bundle extras = getIntent().getExtras();
    public void run (View view){
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateHistorique(id_To_Update, texte.getText().toString(),
                        heure.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertHistorique(texte.getText().toString(), heure.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "done",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }

        scan_btn = findViewById(R.id.scan_btn);
        // data to populate the RecyclerView with
        ArrayList<horaires> heures = new ArrayList<>();
        horaires recyclerRow =new horaires("Porte d'entrée  ouverte","08h00");
        horaires recyclerRow1=new horaires("Porte de sortie ouverte","20h00");
        heures.add(recyclerRow);
        heures.add(recyclerRow1);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterView (this, heures);
        recyclerView.setAdapter(adapter);
    }


    public void onClick(View view) {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "you cancelled the scanning",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + " on row number " + position, Toast.LENGTH_SHORT).show();

    }
}
