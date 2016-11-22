package com.example.marcio.logical;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcio on 19/10/16.
 */
public class BackgroundTask2 extends AsyncTask<String, Void, String> {

    Context ctx;
    String objeto_json = "";
    private List<Jogadores> players;


    public BackgroundTask2(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        players = getJogadores();

    }

    @Override
    protected String doInBackground(String... params) {
        String valores_url = "http://192.168.1.160/webapp/insertValores.php";
        objeto_json = getjson1();

        for (int i = 0; i < players.size(); i++) {


            // String name = params[1];
            String user_name = players.get(i).getUser_name();
            int valor = players.get(i).getValor();

            try {
                URL url = new URL(valores_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("json", "UTF-8") + "=" + URLEncoder.encode(objeto_json, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();

                 return "Cadastrado com sucesso";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
       if (result.equals("Cadastrado com sucesso")){

       }
    }

    private List<Jogadores> getJogadores() { // Recebe dados da tabela jogadroes para fazer o rank
        List<Jogadores> player = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(ctx);

        try {
            db.create();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        if (db.open()) {
            player = db.insetData_msql();
        }
        db.close();
        return player;
    }

    public String getjson1(){
        JSONArray installedList = new JSONArray();

        for (int i = 0; i < players.size(); i++)
        {
            try {

                // if (players.get(i).getSynced() == 0 ){

                String username = players.get(i).getUser_name();
                String valor = String.valueOf(players.get(i).getValor());

                JSONObject installedPackage = new JSONObject();


                installedPackage.put("username", username);
                installedPackage.put("valor", valor);


                installedList.put(installedPackage);
                //}




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        String dataToSend = installedList.toString();
        return dataToSend;
    }
}
