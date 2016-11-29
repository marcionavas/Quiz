package com.example.marcio.logical;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    QuizActivity main = new QuizActivity();
    Button btn_iniciar, btn_cadastrar, btn_rank;

    boolean flag = false;
    int controlador = 0;
    int valor2 = 0;

    DatabaseHelper myDb;

    UserSessionManager session;

    EditText ET_name, ET_pass;
    String login_name, login_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        myDb = new DatabaseHelper(this);

        try {
            myDb.create();
        } catch (IOException e) {
            e.printStackTrace();
        }

        session = new UserSessionManager(this);





        ET_name = (EditText)findViewById(R.id.usuario);
        ET_pass = (EditText)findViewById(R.id.senha);

        btn_rank = (Button)findViewById(R.id.btn_Rank);
        btn_rank.setOnClickListener(this);

        btn_iniciar = (Button) findViewById(R.id.btn_iniciar);
        btn_iniciar.setOnClickListener(this);

        btn_cadastrar = (Button)findViewById(R.id.button_cadastrar);
        btn_cadastrar.setOnClickListener(this);

        if (session.loggedin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View v) { // clica de botao para gravar o nome do usuario e ir para a proxima activity

       /* String name = jogador.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Insira Seu Nome para Proseguir.", Toast.LENGTH_LONG).show();


        } else {
            Intent i = new Intent(getApplicationContext(), QuizActivity.class);
            i.putExtra("key", name);
            i.putExtra("key1", controlador);
            i.putExtra("key2", valor2);
            i.putExtra("key3", flag);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);

            //setContentView(R.layout.quiz_activity);
            finish();
        }*/


        if (v == btn_cadastrar){
            userReg();
        }
        else if (v == btn_iniciar){
            userLogin();
        }
        else if (v == btn_rank){


            String method = "recieve";
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(method);





        }
    }

    public void userReg(){
        startActivity(new Intent (this,Cadastro.class));
    }

    public void userLogin(){
        login_name = ET_name.getText().toString();
        login_pass = ET_pass.getText().toString();
        String usuario = myDb.logIn(login_name, login_pass);


        if(usuario.equals("Username ou senha incorreto!")){
            Toast.makeText(this, usuario, Toast.LENGTH_LONG).show();
        }
        else{
            session.setLoggedin(true, usuario);
            Intent i = new Intent(getApplicationContext(), QuizActivity.class);
            i.putExtra("key1", controlador);
            i.putExtra("key2", valor2);
            i.putExtra("key3", flag);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);

            //setContentView(R.layout.quiz_activity);
            finish();

        }

       /* String method = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,login_name,login_pass);*/
       // finish();

    }

}
