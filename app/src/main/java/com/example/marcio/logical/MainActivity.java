package com.example.marcio.logical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nome_usuLogado;
    Button btn_iniciar, btn_logout, btn_top_rank, btn_local_rank;
    UserSessionManager session;
    ProgressDialog progressDialog;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        myDb = new DatabaseHelper(this);
        session = new UserSessionManager(this);
        nome_usuLogado = (TextView) findViewById(R.id.txt_nomeLogado);
        btn_iniciar = (Button) findViewById(R.id.btn_iniciar);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_top_rank = (Button) findViewById(R.id.btn_Top_Rank);
        btn_local_rank = (Button) findViewById(R.id.btn_Local_Rank);

        btn_logout.setOnClickListener(this);
        btn_iniciar.setOnClickListener(this);
        btn_top_rank.setOnClickListener(this);
        btn_local_rank.setOnClickListener(this);

        nome_usuLogado.setText(session.getName());

        getJogadores2();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_iniciar) {
            startActivity(new Intent(MainActivity.this, QuizActivity.class));
            finish();
        } else if (v == btn_logout) {
            logout();
        } else if (v == btn_top_rank) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Carregando...");
            progressDialog.show();
            String method = "recieve";
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(method);
        } else if (v == btn_local_rank) {
            startActivity(new Intent(MainActivity.this, LocalRankingActivity.class));
        }
    }

    private void logout() {
        session.setLoggedin(false, "");
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            progressDialog.dismiss();
        } catch (Exception e) {

        }
    }

    private void getJogadores2() { // Recebe dados da tabela jogadroes para fazer o rank

        String user ="";
        int valor = 0;
        List<Jogadores> player = new ArrayList<>();
        myDb.open();
        List<Jogadores> list1 = myDb.sync_oncreate();

        for(int i = 0; i < list1.size(); i++){
            if (list1.get(i).getSynced() != 0) {

                user = list1.get(i).getUser_name();
                valor = list1.get(i).getValor();

                String json = getjson1(user, valor);

                String method = "sync2";
                BackgroundTask backgroundTask = new BackgroundTask(this);
                backgroundTask.execute(method, json);

            }
        }


    }


    public String getjson1(String user, int valor) {
        JSONArray installedList = new JSONArray();


        try {

            JSONObject installedPackage = new JSONObject();


            installedPackage.put("username", user);
            installedPackage.put("valor", valor);


            installedList.put(installedPackage);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String dataToSend = installedList.toString();
        return dataToSend;
    }


}
