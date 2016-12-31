package com.example.marcio.logical;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorldRankingActivity extends AppCompatActivity {
String json = "";
List<Jogadores> list = new ArrayList<>();
    TextView resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.world_ranking_activity);

        resultado = (TextView)findViewById(R.id.txtResult);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            json = extras.getString("key1");
        }

       list = getjason(json);
        send_result();
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
        }catch (RuntimeException e){
            Toast.makeText(this, "Não foi possivel carregar o World Ranking, tente novamente " +
                    "mais tarde", Toast.LENGTH_LONG).show();
            finish();
        }
        return players;
    }

    public void send_result() { // envia o resultado para serr mostrado
        int x = 1;
        resultado.setText("");
       // mLinearLayout.setBackgroundResource(R.drawable.fim);

        for (int i = 0; i < list.size(); i++) {
            String nome = list.get(i).getUser_name();
            int valor = list.get(i).getValor();
            resultado.setText(resultado.getText() + "" + x + "  -  " + nome + "  " + valor + "\n \n");
            x++;
        }
    }
}
