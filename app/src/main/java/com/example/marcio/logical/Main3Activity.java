package com.example.marcio.logical;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    ScrollView scroll;
    TextView resultado;
    String name;
    int valor = 0, synced;
    DatabaseHelper myDb;
    private List<Jogadores> players;
    private List<Jogadores> players2;
    MediaPlayer player;
    UserSessionManager session;
    String wifi1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        session = new UserSessionManager(this);
        name = session.getName();
        sound_alarm();
        myDb = new DatabaseHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //name = extras.getString("name");
            valor = extras.getInt("value");
            //The key argument here must match that used in the other activity
        }
        scroll = (ScrollView) findViewById(R.id.scroll1);
        resultado = (TextView) findViewById(R.id.txtResult);
        mostrar_result();
        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled()){
            wifi1 = "on";
        }
        else wifi1 = "off";


       // myDb.updateSync_status(1);


    }

    public void mostrar_result() { // Mostra resultado



        myDb.open();

        int id = myDb.getId(name);


        players = myDb.getRank(id);

        if (players.size()== 0){
            int id1 = id;
            int pontos = valor;
            synced = 1;
            myDb.open();
            myDb.insertRank(id1, pontos);
            myDb.updateData(pontos, synced, id1);

        }

        else if (players.get(0).getValor()< valor){
            synced = 1;
            myDb.open();
            myDb.updateData(valor, synced, id);

        }else if(players.get(0).getValor() >= valor){
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




        for (int i = 0; i < list.size(); i++){

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




        for (int i = 0; i < list.size(); i++){

            Jogadores jo = new Jogadores();
            int id2 = list.get(i).getId();
            List<Jogadores> list1 = myDb.getuser(id2);
            String username = list1.get(0).getUser_name();
            int sync = list1.get(0).getSynced();
            int id = list1.get(0).getId();
            int valor = list.get(i).getValor();

            if (sync != 0 ){
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

        player = MediaPlayer.create(this, R.raw.fail);
        player.setVolume(10, 10);
        player.start();
        //player.setLooping(true);


    }
}
