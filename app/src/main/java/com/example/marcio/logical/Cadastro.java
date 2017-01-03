package com.example.marcio.logical;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Cadastro extends AppCompatActivity implements View.OnClickListener {

    EditText et_email, et_user, et_pass;
    String email, user_name, pass;
    int synced;
    Button btn_cadastrar;
    DatabaseHelper myDb;
    boolean isConected; //Variável que verifica conexão

    String url = "http://logical.pe.hu/webapp/register.php";

    ProgressDialog progressDialog;

    Jogadores jo = new Jogadores();


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            userReg();
            progressDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        myDb = new DatabaseHelper(this);

        btn_cadastrar = (Button) findViewById(R.id.button);
        btn_cadastrar.setOnClickListener(this);

        et_email = (EditText) findViewById(R.id.txt_email);
        et_user = (EditText) findViewById(R.id.usuario);
        et_pass = (EditText) findViewById(R.id.senha1);
    }

    public void userReg() {

        if (isConected == true) {


            String wifi1 = "";
            // WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            // if (wifi.isWifiEnabled()){
            // wifi1 = "on";

            email = et_email.getText().toString();
            user_name = et_user.getText().toString();
            pass = et_pass.getText().toString();
            if (email.equals("") || user_name.equals("") || pass.equals("")) {
                Toast.makeText(getApplicationContext(), "Todos os campos devem ser preenchidos.", Toast.LENGTH_SHORT).show();
            } else {
                synced = 0;
                String method = "register";

                jo.setemail(email);
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
                backgroundTask.execute(method, email, user_name, pass, wifi1);
            }

            // finish();
            //  }
            // else Toast.makeText(this, "Ative o wifi para cadastrar um novo usuario!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder adlg = new AlertDialog.Builder(Cadastro.this);
            adlg.setCancelable(true);
            adlg.setPositiveButton("teste", null);
            adlg.setMessage("Não foi possível se conectar. Verifique a conexão e tente novamente");
            adlg.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    btn_cadastrar.performClick();
                }
            });
            adlg.setNegativeButton("Cancelar", null);
            adlg.show();
        }

    }

    @Override
    public void onClick(View v) {

        isConected = false;

        progressDialog = ProgressDialog.show(Cadastro.this, "Cadastro", "Por favor, aguarde...");

//        //Thread responsável por verificar se há conexão com o servidor
        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                try {
                    URL myUrl = new URL(url);
                    URLConnection connection = myUrl.openConnection();
                    connection.setConnectTimeout(2000);
                    connection.connect();
                    isConected = true;
                } catch (Exception e) {
                    // Handle your exceptions
                    isConected = false;
                }
//                //handler finaliza a progressDialog e executa o método userReg();
                handler.sendEmptyMessage(0);
            }
        }).start();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();

    }
}
