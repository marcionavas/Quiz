package com.example.marcio.logical;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcio on 09/10/16.
 */
public class BackgroundTask extends AsyncTask<String, Void, String> {


    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADD = "address";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    String Resultado;


    AlertDialog alertDialog;

    UserSessionManager session;

    Context ctx;


    String wifi = "";
    String method;

    DatabaseHelper myDb;


    private List<Jogadores> players;
    String objeto_json = "";

    BackgroundTask(Context context) {
        ctx = context;


    }


    @Override
    protected void onPreExecute() {
        myDb = new DatabaseHelper(ctx);
        personList = new ArrayList<HashMap<String, String>>();
        players = getNewTable();
        session = new UserSessionManager(ctx);
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information...");


    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://logical.pe.hu/webapp/register.php";
        String login_url = "http://logical.pe.hu/webapp/login.php";
        String insert_data = "http://logical.pe.hu/webapp/insertValores.php";
        method = params[0];

        if (method.equals("register")) {
            String email = params[1];
            String login_name = params[2];
            String login_pass = params[3];
            wifi = params[4];


            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                        URLEncoder.encode("login_pass", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (method.equals("login")) {
            String login_name = params[1];
            String login_pass = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                        URLEncoder.encode("login_pass", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (method.equals("sync")) {

            String username = params[2];
            int valor = Integer.parseInt(params[3]);

            objeto_json = getjson1(username, valor);


            // String name = params[1];
            //String user_name = players.get(i).getUser_name();
            // int valor = players.get(i).getValor();

            try {
                URL url = new URL(insert_data);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("json", "UTF-8") + "=" + URLEncoder.encode(objeto_json, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                //IS.close();

                // InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;

                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return response;

                // return "Cadastrado com sucesso";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (method.equals("recieve")) {

            String json = download_data();

            return json;


        }

        return null;

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(String result) {
        if (method.equals("register")) {
            if (result.equals("1")) {
                Toast.makeText(ctx, "Cadastrado com sucesso", Toast.LENGTH_LONG).show();
                ((Cadastro) ctx).finish();

            } else {
                Toast.makeText(ctx, "Usuario já cadastrado, tente novamente!", Toast.LENGTH_LONG).show();
            }
        } else if (method.equals("login")) {
            if (result.equals("0")) {
                Toast.makeText(ctx, "Usuário ou senha incorreto. Tente Novamente ou Cadastre-se é gratis!", Toast.LENGTH_LONG).show();
            } else {
                download_data2(result);
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                method = "copy_data";
                //Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();


            }
        } else if (method == "recieve") {


            Intent intent = new Intent(ctx, WorldRankingActivity.class);
            intent.putExtra("key1", result);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.getApplicationContext().startActivity(intent);
        }

        //Toast.makeText(ctx, "Para ter todos os resultados ligue o WIFI", Toast.LENGTH_LONG).show();
         if (method.equals("copy_data")) {
            Resultado = result;
            copiar_jogador();
             session.setLoggedin(true, result);
             ctx.startActivity(new Intent(ctx, QuizActivity.class));

        }
    }


    public String getjson() {
        JSONArray installedList = new JSONArray();

        for (int i = 0; i < players.size(); i++) {
            try {

                // if (players.get(i).getSynced() == 0 ){
                String email = players.get(i).getemail();
                String username = players.get(i).getUser_name();
                String pass = players.get(i).getPass();

                JSONObject installedPackage = new JSONObject();

                installedPackage.put("email", email);
                installedPackage.put("username", username);
                installedPackage.put("password", pass);


                installedList.put(installedPackage);
                //}


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        String dataToSend = installedList.toString();
        return dataToSend;
    }

    public String getjson1(String user, int valor) {
        JSONArray installedList = new JSONArray();


        try {

            // if (players.get(i).getSynced() == 0 ){

            // String username = players.get(i).getUser_name();
            //String valor = String.valueOf(players.get(i).getValor());

            JSONObject installedPackage = new JSONObject();


            installedPackage.put("username", user);
            installedPackage.put("valor", valor);


            installedList.put(installedPackage);
            //}


        } catch (JSONException e) {
            e.printStackTrace();
        }


        String dataToSend = installedList.toString();
        return dataToSend;
    }

    private String download_data() {
        InputStream is = null;
        String line = null;

        try {
            URL url = new URL("http://logical.pe.hu/webapp/getData.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuffer sb = new StringBuffer();

            if (br != null) {
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } else {
                return null;
            }

            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private String download_data2(String login_name) {
        InputStream is = null;
        String line = null;
        String response = "";

        try {
            URL url = new URL("http://logical.pe.hu/webapp/copyuser.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            String line1 = "";
            while ((line1 = bufferedReader.readLine()) != null) {
                response += line1;

            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();


        } catch (MalformedURLException |NetworkOnMainThreadException  e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Teste DownloadData2: "+response);
        return response;

    }

    String json = Resultado;
    List<Jogadores> list = new ArrayList<>();

    public void copiar_jogador() {
        list = getjason(json);

        String email = list.get(0).getemail();
        String username = list.get(0).getUser_name();
        String senha = list.get(0).getPass();

        Toast.makeText(ctx, email + "+" + username + "+" + senha, Toast.LENGTH_LONG).show();
    }

    public List<Jogadores> getjason(String json) {
        List<Jogadores> players = new ArrayList<>();


        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(json);
            for (int i = 0; i < jsonarray.length(); i++) {
                Jogadores jo = new Jogadores();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String name = jsonobject.getString("username");
                int valores = Integer.parseInt(jsonobject.getString("valores"));

                jo.setUser_name(name);
                jo.setValor(valores);
                players.add(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return players;
    }


    private List<Jogadores> getNewTable() { // metodo que cria umal ista do tipo Dados e recebe aquela lista do metodo que esta na outra classe db helper.
        List<Jogadores> player = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(ctx);

        try {
            db.create();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        if (db.open()) {
            player = db.getNewtable();
        }
        db.close();

        return player;
    }


}
