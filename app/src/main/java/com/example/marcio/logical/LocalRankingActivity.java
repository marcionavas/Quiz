package com.example.marcio.logical;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class LocalRankingActivity extends AppCompatActivity implements View.OnClickListener {
    ScrollView scroll;
    TextView resultado;
    Button jogar_novamente, rank_global;
    String name;
    int valor = 0, synced;
    DatabaseHelper myDb;
    private List<Jogadores> players;
    private List<Jogadores> players2;
    MediaPlayer player;
    UserSessionManager session;
    String wifi1;
    Boolean alarm = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_ranking_activity);
        session = new UserSessionManager(this);
        jogar_novamente = (Button)findViewById(R.id.btn_jogar_novamente);
        jogar_novamente.setOnClickListener(this);
        rank_global = (Button)findViewById(R.id.btn_Rank_Global);
        rank_global.setOnClickListener(this);
        name = session.getName();
        myDb = new DatabaseHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //name = extras.getString("name");
            valor = extras.getInt("value");
            sound_alarm();
            //The key argument here must match that used in the other activity
        }

        scroll = (ScrollView) findViewById(R.id.scroll1);
        resultado = (TextView) findViewById(R.id.txtResult);
        mostrar_result();
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()) {
            wifi1 = "on";
        } else wifi1 = "on";


        // myDb.updateSync_status(1);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    public void mostrar_result() { // Mostra resultado


        myDb.open();

        int id = myDb.getId(name);


        players = myDb.getRank(id);

        if (players.size() == 0) {
            int id1 = id;
            int pontos = valor;
            synced = 1;
            myDb.open();
            myDb.insertRank(id1, pontos);
            myDb.updateData(pontos, synced, id1);

        } else if (players.get(0).getValor() < valor) {
            synced = 1;
            myDb.open();
            myDb.updateData(valor, synced, id);

        } else if (players.get(0).getValor() >= valor) {
            synced = 0;
            myDb.open();
            myDb.updateData(valor, synced, id);
        }

        myDb.open();
        players = myDb.getAll();
        players2 = getJogadores(players);


        //List<Jogadores>players3 = getJogadores2(players);

        //myDb.order_Table(players3);


        send_result();
        myDb.close();
        getJogadores2(players);
    }

    private List<Jogadores> getJogadores(List<Jogadores> list) { // Recebe dados da tabela jogadroes para fazer o rank
        List<Jogadores> player = new ArrayList<>();
        myDb.open();


        for (int i = 0; i < list.size(); i++) {

            Jogadores jo = new Jogadores();
            int id2 = list.get(i).getId();
            List<Jogadores> list1 = myDb.getuser(id2);
            String username = list1.get(0).getUser_name();
            int sync = list1.get(0).getSynced();
            int id = list1.get(0).getId();
            int valor = list.get(i).getValor();


            jo.setId(id);
            jo.setValor(valor);
            jo.setSynced(sync);
            jo.setUser_name(username);
            player.add(jo);


        }


        return player;
    }

    private void getJogadores2(List<Jogadores> list) { // Recebe dados da tabela jogadroes para fazer o rank
        List<Jogadores> player = new ArrayList<>();
        myDb.open();


        for (int i = 0; i < list.size(); i++) {

            Jogadores jo = new Jogadores();
            int id2 = list.get(i).getId();
            List<Jogadores> list1 = myDb.getuser(id2);
            String username = list1.get(0).getUser_name();
            int sync = list1.get(0).getSynced();
            int id = list1.get(0).getId();
            int valor = list.get(i).getValor();

            if (sync != 0) {
                jo.setId(id);
                jo.setValor(valor);
                jo.setSynced(sync);
                jo.setUser_name(username);
                player.add(jo);

                String method = "sync";
                BackgroundTask backgroundTask = new BackgroundTask(this);
                backgroundTask.execute(method, wifi1, username, ((String.valueOf(valor))));
            }


        }

    }

    public void send_result() { // envia o resultado para serr mostrado
        int x = 1;
        resultado.setText("");
        //mLinearLayout.setBackgroundResource(R.drawable.fim);

        for (int i = 0; i < players2.size(); i++) {
            String nome = players2.get(i).getUser_name();
            int valor = players2.get(i).getValor();
            resultado.setText(resultado.getText() + "" + x + "  -  " + nome + "  " + valor + "\n \n");
            x++;
        }
    }

    public void sound_alarm() {
        alarm = true;
        player = MediaPlayer.create(this, R.raw.fail);
        player.setVolume(10, 10);
        player.start();
        //player.setLooping(true);


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LocalRanking Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.marcio.logical/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (alarm == true){
            player.stop();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LocalRanking Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.marcio.logical/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onClick(View v) {
        if (v == jogar_novamente){
            Intent i = new Intent(getApplicationContext(),QuizActivity.class);
            startActivity(i);
            finish();
        }
        else if (v == rank_global){
            String method = "recieve";
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(method);
        }

    }
}
