package com.example.sannerqr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements RecyclerAdapterView.ItemClickListener {

    Button scan_btn;
    RecyclerAdapterView adapter;
    TextView textView;
    public String url = "https://reqres.in/api/users/2";
    private DBHelper mydb;
    ArrayList<horaires> heures = new ArrayList<>();
    horaires recyclerRow = new horaires("Porte d'entrée  ouverte", "08h00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan_btn = findViewById(R.id.scan_btn);

        // data to populate the RecyclerView with

        horaires recyclerRow1 = new horaires("Porte de sortie ouverte", "20h00");
        heures.add(recyclerRow);
        heures.add(recyclerRow1);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapterView(this, heures);
        recyclerView.setAdapter(adapter);

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("myReponse", "" + myResponse);
                        JSONObject json = null;
                        try {
                            json = new JSONObject(myResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            recyclerRow.setHeure(json.getJSONObject("data").getString("first_name"));
                            heures.add(recyclerRow);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });


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
                Toast.makeText(this, "you cancelled the scanning", Toast.LENGTH_LONG).show();
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
