package com.example.marcio.logical;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener {

    TextView nome_usuLogado;
    Button btn_iniciar, btn_logout;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        session = new UserSessionManager(this);
        nome_usuLogado = (TextView)findViewById(R.id.txt_nomeLogado);
        btn_iniciar = (Button)findViewById(R.id.btn_iniciar);
        btn_logout = (Button)findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(this);
        btn_iniciar.setOnClickListener(this);

        nome_usuLogado.setText(session.getName());
    }

    @Override
    public void onClick(View v) {
        if (v == btn_iniciar){
            startActivity(new Intent(Main4Activity.this, Main2Activity.class));
            finish();
        }
        else{
            logout();
        }
    }
    private void logout(){
        session.setLoggedin(false, "");
        finish();
        startActivity(new Intent(Main4Activity.this, MainActivity.class));
    }
}
