package com.example.marcio.logical;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    String name;
    MediaPlayer player;
    int valor2;
    DatabaseHelper myDb;
    Button btn_view;
    Button btn_desistir;
    private List<Dados> dados;
    int controlador2 = 0;
    LinearLayout mLinearLayout;
    TextView pergunta, valor, tempo, valor_atual;
    boolean flag = false;
    boolean flag2 = false;
    Boolean alarm1 = false;
    Boolean alarm2 = false;
    ScrollView scroll;
    RadioGroup group;
    RadioButton res1;
    RadioButton res2;
    RadioButton res3;
    RadioButton res4;
    UserSessionManager session;
    CountDownTimer contador;
    long s1 = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {// metodo principal que e executado asim que o aplicatiov executa
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        session = new UserSessionManager(this);

       name = session.getName();




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //name = extras.getString("key");
            controlador2 = extras.getInt("key1");
            valor2 = extras.getInt("key2");
            flag = extras.getBoolean("key3");
            Intent i = getIntent();
            dados = (List<Dados>) i.getSerializableExtra("dados");
            //The key argument hee must match that used in the other activity
        }
        if (flag == false) {
            //myPlayer.setNome(name);
            group = (RadioGroup) findViewById(R.id.radio_group);
            myDb = new DatabaseHelper(this);
            btn_view = (Button)findViewById(R.id.btn_proximo);
            btn_view.setOnClickListener(this);
            btn_desistir = (Button)findViewById(R.id.btn_desistir);
            btn_desistir.setOnClickListener(this);
            //btn_desistir.setText(btn_desistir.getText() + " \n "+ desistir());
            mLinearLayout = (LinearLayout) findViewById(R.id.linear);
            scroll = (ScrollView) findViewById(R.id.scroll_question);
            dados = getDados();
            Collections.shuffle(dados);
            valor_atual = (TextView) findViewById(R.id.valor_atual);
            pergunta = (TextView) findViewById(R.id.txt_pergunta);
            valor = (TextView) findViewById(R.id.text_valor);
            tempo = (TextView) findViewById(R.id.text_tempo);
            shuffle();
            executa_quiz();
        } else {
            valor_atual = (TextView) findViewById(R.id.valor_atual);
            //myPlayer.setNome(name);
            valor_atual.setText("" + valor2);
            group = (RadioGroup) findViewById(R.id.radio_group);
            myDb = new DatabaseHelper(this);
            btn_view = (Button) findViewById(R.id.btn_proximo);
            btn_view.setOnClickListener(this);
            btn_desistir = (Button) findViewById(R.id.btn_desistir);
            btn_desistir.setOnClickListener(this);
            //btn_desistir.setText(btn_desistir.getText() + " \n "+ desistir());
            mLinearLayout = (LinearLayout) findViewById(R.id.linear);
            scroll = (ScrollView) findViewById(R.id.scroll_question);
            //dados = getDados();
            //Collections.shuffle(dados);
            pergunta = (TextView) findViewById(R.id.txt_pergunta);
            valor = (TextView) findViewById(R.id.text_valor);
            tempo = (TextView) findViewById(R.id.text_tempo);
            shuffle();
            executa_quiz();
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        if (alarm1 == true || alarm2 == true){
            player.stop();
        }

        try{
            contador.cancel();
        }catch (Exception e){

        }
        contador.cancel();
       // Toast.makeText(getApplicationContext(), "ON PAUSE" + s1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        contador.cancel();
       // Toast.makeText(getApplicationContext(), "ON START " + s1, Toast.LENGTH_SHORT).show();
        contador_tempo(s1);
        contador.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        contador.cancel();
    }

    public void contador_tempo(long time){
         contador = new CountDownTimer(time, 1000) { // Metodo contador do tempo de cada pergunta
            @Override
            public void onTick(long millisUntilFinished) {
                //tempo.setText(""+millisUntilFinished / 1000);
                s1 = millisUntilFinished;
                long milis = millisUntilFinished / 1000;
                if (milis <= 15) {
                    if (flag2 == false && milis == 8){
                        sound_alarm2();
                    }
                    tempo.setTextColor(Color.RED);
                    tempo.setText("" + millisUntilFinished / 1000);
                } else {
                    tempo.setTextColor(Color.WHITE);
                    tempo.setText("" + millisUntilFinished / 1000);
                }
            }

            public void onFinish() {
                tempo_esgotado();
            }
        };
    }



    private void executa_quiz() {//Metodo que executa o quiz setando os perguntas e os radio buttons
        btn_desistir.setText("Desistir\nR$" + desistir());
        contador_tempo(s1);
        contador.cancel();
        contador.start();
        shuffle();
        scroll.scrollTo(0, 0);

        if (controlador2 < 20) {

            String question = dados.get(controlador2).getPergunta();
            String resposta_certa = dados.get(controlador2).getResposta();
            String resposta_errada = dados.get(controlador2).getResposta_errada();
            String resposta_errada1 = dados.get(controlador2).getResposta_errada2();
            String resposta_errada2 = dados.get(controlador2).getResposta_errada3();

            valor.setText("Valendo: R$" + correct());
            pergunta.setText(question);
            res1.setText(resposta_certa);
            res2.setText(resposta_errada);
            res3.setText(resposta_errada1);
            res4.setText(resposta_errada2);
        } else {
            contador.cancel();
            Intent i = new Intent(getApplicationContext(), LocalRankingActivity.class);
            //i.putExtra("name", name);
            i.putExtra("value", valor2);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        }
    }

    private List<Dados> getDados() { // metodo que cria umal ista do tipo Dados e recebe aquela lista do metodo que esta na outra classe db helper.
        List<Dados> dados = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        try {
            db.create();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        if (db.open()) {
            dados = db.getDados();
        }
        db.close();

        return dados;
    }

    @Override
    public void onClick(View v) {// Metodo determina qual botao foi acionado

        if (v == btn_view) {
            clica();
        } else if (v == btn_desistir) {
            desist();
        }

    }

    public void desist() { // metodo do botao desistir
        if (controlador2 < 20) {
            valor2 = desistir();
            controlador2 = 20;
            executa_quiz();
            return;
        }


    }

    public void clica() { //Metodo do botoao clicar
        if (controlador2 < 20) {

            boolean flag = true;


            if (group.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Por favor selecione uma alternativa para proseguir.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // get selected radio button from radioGroup
                int selectedId = group.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                String text = selectedRadioButton.getText().toString();
                if (text == dados.get(controlador2).getResposta()) {
                    valor2 = correct();
                    Toast.makeText(getApplicationContext(), "Acertou! Você ganhou " + correct(), Toast.LENGTH_SHORT).show();
                } else {
                    valor2 = wrong();
                    Toast.makeText(getApplicationContext(), "Errou! Você ganhou " + wrong(), Toast.LENGTH_SHORT).show();
                    controlador2 = 20;
                    executa_quiz();
                    return;
                }
                controlador2++;
                contador.cancel();
                sound_alarm();
                // executa_quiz();
                //group.clearCheck();

                Intent i = new Intent(getApplicationContext(), QuizActivity.class);
                //Bundle bundle = new Bundle();
                // bundle.putParcelable("data", (Parcelable) dados);
                i.putExtra("dados", (Serializable) dados);
                //i.putExtra("key", name);
                i.putExtra("key1", controlador2);
                i.putExtra("key2", valor2);
                i.putExtra("key3", flag);
                startActivity(i);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                //setContentView(R.layout.quiz_activity);
                finish();
            }
        }
    }

    public void tempo_esgotado() { //  Metodo executado quando o tempo da pergunta esgota
        if (controlador2 < 20) {
            valor2 = wrong();
            controlador2 = 20;
            executa_quiz();
            return;
        }
    }

    public void shuffle() { // metodo que embaralha a as perguntas e alternativas

        res1 = (RadioButton) findViewById(R.id.radioButton);
        res2 = (RadioButton) findViewById(R.id.radioButton2);
        res3 = (RadioButton) findViewById(R.id.radioButton3);
        res4 = (RadioButton) findViewById(R.id.radioButton4);


        Integer[] a = {res1.getId(), res2.getId(), res3.getId(), res4.getId()};
        Collections.shuffle(Arrays.asList(a));

        res1 = (RadioButton) findViewById(a[0]);
        res2 = (RadioButton) findViewById(a[1]);
        res3 = (RadioButton) findViewById(a[2]);
        res4 = (RadioButton) findViewById(a[3]);
    }

    public int correct() { // Determina valor das respastas
        int acerto = 0;
        switch (controlador2) {
            case 0:
                acerto = 1000;
                break;

            case 1:
                acerto = 2000;
                break;

            case 2:
                acerto = 3000;
                break;

            case 3:
                acerto = 4000;
                break;

            case 4:
                acerto = 5000;
                break;

            case 5:
                acerto = 10000;
                break;

            case 6:
                acerto = 20000;
                break;

            case 7:
                acerto = 25000;
                break;

            case 8:
                acerto = 30000;

            case 9:
                acerto = 35000;
                break;

            case 10:
                acerto = 40000;
                break;

            case 11:
                acerto = 50000;
                break;

            case 12:
                acerto = 80000;
                break;

            case 13:
                acerto = 90000;
                break;

            case 14:
                acerto = 100000;
                break;

            case 15:
                acerto = 200000;
                break;

            case 16:
                acerto = 300000;
                break;

            case 17:
                acerto = 400000;
                break;

            case 18:
                acerto = 500000;

            case 19:
                acerto = 1000000;
                break;


        }
        return acerto;
    }

    public int wrong() {//Determina valor das respostas
        int erro = 0;
        switch (controlador2) {
            case 0:
                erro = 0;
                break;

            case 1:
                erro = 500;
                break;

            case 2:
                erro = 1000;
                break;

            case 3:
                erro = 1500;
                break;

            case 4:
                erro = 2000;
                break;

            case 5:
                erro = 2500;
                break;

            case 6:
                erro = 5000;
                break;

            case 7:
                erro = 10000;
                break;

            case 8:
                erro = 15000;

            case 9:
                erro = 20000;
                break;

            case 10:
                erro = 25000;
                break;

            case 11:
                erro = 50000;
                break;

            case 12:
                erro = 100000;
                break;

            case 13:
                erro = 200;
                break;

            case 14:
                erro = 20000;
                break;

            case 15:
                erro = 30000;
                break;

            case 16:
                erro = 40000;
                break;

            case 17:
                erro = 5000;
                break;

            case 18:
                erro = 500;

            case 19:
                erro = 200;
                break;


        }
        return erro;
    }

    public int desistir() { // determina valor das repostas
        int des = 0;
        switch (controlador2) {
            case 0:
                des = 500;
                break;

            case 1:
                des = 1000;
                break;

            case 2:
                des = 1500;
                break;

            case 3:
                des = 2000;
                break;

            case 4:
                des = 2500;
                break;

            case 5:
                des = 3000;
                break;

            case 6:
                des = 3500;
                break;

            case 7:
                des = 4000;
                break;

            case 8:
                des = 4500;

            case 9:
                des = 15000;
                break;

            case 10:
                des = 20000;
                break;

            case 11:
                des = 25000;
                break;

            case 12:
                des = 50000;
                break;

            case 13:
                des = 100;
                break;

            case 14:
                des = 10000;
                break;

            case 15:
                des = 15000;
                break;

            case 16:
                des = 20000;
                break;

            case 17:
                des = 2500;
                break;

            case 18:
                des = 150;

            case 19:
                des = 150;
                break;


        }
        return des;
    }

    public void sound_alarm() {
        alarm1 = true;
        player = MediaPlayer.create(this, R.raw.caixa);
        player.setVolume(10, 10);
        player.start();

    }

    public void sound_alarm2() {
        alarm2 = true;
        player = MediaPlayer.create(this, R.raw.tictoc);
        player.setVolume(10, 10);
        player.start();
        flag2 = true;

    }
}
