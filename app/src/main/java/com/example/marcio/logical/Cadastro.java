package com.example.marcio.logical;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class Cadastro extends AppCompatActivity implements View.OnClickListener {

    EditText et_name, et_user, et_pass;
    String name, user_name, pass;
    int synced;
    Button btn_cadastrar;
    DatabaseHelper myDb;

    Jogadores jo = new Jogadores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        myDb = new DatabaseHelper(this);

        btn_cadastrar = (Button)findViewById(R.id.button);
        btn_cadastrar.setOnClickListener(this);

        et_name = (EditText)findViewById(R.id.nome);
        et_user = (EditText)findViewById(R.id.usuario);
        et_pass = (EditText)findViewById(R.id.senha1);
    }

    public void userReg(){

        String wifi1 = "";
       // WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
       // if (wifi.isWifiEnabled()){
           // wifi1 = "on";

            name = et_name.getText().toString();
            user_name = et_user.getText().toString();
            pass = et_pass.getText().toString();
        if (name.equals("") || user_name.equals("") || pass.equals("")){
            Toast.makeText(getApplicationContext(), "Todos os campos devem ser preenchidos.", Toast.LENGTH_SHORT).show();
        }else {
            synced = 0;
            String method = "register";

            jo.setNome(name);
            jo.setUser_name(user_name);
            jo.setPass(pass);
            jo.setSynced(synced);
            try {
                myDb.create();
            } catch (IOException e) {
                e.printStackTrace();
            }
            myDb.open();
            myDb.insertData(jo);

            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(method, name, user_name, pass, wifi1);
        }

           // finish();
      //  }
       // else Toast.makeText(this, "Ative o wifi para cadastrar um novo usuario!", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onClick(View v) {
        userReg();
    }
}
