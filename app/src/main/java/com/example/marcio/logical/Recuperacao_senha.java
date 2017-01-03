package com.example.marcio.logical;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;
import java.net.URLConnection;

public class Recuperacao_senha extends AppCompatActivity implements View.OnClickListener {

    Button btn_enviar;
    EditText txt_email;
    boolean isConected;
    String url = "http://logical.pe.hu/webapp/login.php";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacao_senha);

        btn_enviar = (Button)findViewById(R.id.btn_enviar);
        btn_enviar.setOnClickListener(this);

        txt_email = (EditText)findViewById(R.id.txt_email);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            send_email();
            progressDialog.dismiss();
        }
    };


    @Override
    public void onClick(View v) {

        progressDialog = ProgressDialog.show(Recuperacao_senha.this, "Login", "Por favor, aguarde...");

        //Thread responsável por verificar se há conexão com o servidor
        new Thread(new Runnable() {
            @Override
            public void run() {

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
                //handler finaliza a progressDialog e executa o método userLogin();
                handler.sendEmptyMessage(0);
            }
        }).start();



    }


    public void check_connection(){
        new Thread(new Runnable() {
            @Override
            public void run() {

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
                //handler finaliza a progressDialog e executa o método userLogin();
                //handler.sendEmptyMessage(0);
            }
        }).start();
    }

    public void send_email(){
        if (isConected == true) {
            String email = txt_email.getText().toString();
            String method = "pass_recovery";
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(method, email);
        } else if (isConected == false) {
            Toast.makeText(this, "Sem Conexçao Com a Internet", Toast.LENGTH_LONG).show();
        }

    }





}
