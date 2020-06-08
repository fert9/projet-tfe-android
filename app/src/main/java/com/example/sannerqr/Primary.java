package com.example.sannerqr;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Primary extends AppCompatActivity {
    DBHelper mydb;
    Button b1;
    EditText ed1, ed2;
    TextView tx1;
    User user = new User();
    //  String idUser;
    int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllHistorique();
        b1 = (Button) findViewById(R.id.bouton);
        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);

        tx1 = (TextView) findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);
        //recupérations des identifiants
//         ididUser.getText().toString();
//        ed2.getText().toString();

        //A l'appui du bouton, envoi vers une autre pagr
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().equals("admin") && ed2.getText().toString().equals("demo")) {
//                    // PrefManager.getInstance(new User()).setUsername("admin");
//                    UserLogin ul = new UserLogin(ed1.getText().toString(), ed1.getText().toString());
//                    ul.execute();
                    Intent intent = new Intent(Primary.this, Empreinte.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Insérer les identifiants correctes", Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent(Primary.this, SettingsActivity.class);
//                startActivity(intent);
//                finish();
            }

        });

    }
}

//class UserLogin extends AsyncTask<Void, Void, String> {
//    String username, password;
//
//    public UserLogin(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }
//
//    User user = new User(
//            "admin",
//            1
//    );
//
//    @Override
//    protected String doInBackground(Void... voids) {
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        PrefManager.getInstance(UserLogin.this).setUserLogin(user);
//        super.onPostExecute(s);
//    }
//
//
//}