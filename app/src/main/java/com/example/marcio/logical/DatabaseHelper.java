package com.example.marcio.logical;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by marcio on 13/08/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DB_PATH = "";
    public static String DATABASE_NAME = "banco";
    public static String Table_Name = "Dados";
    public static String Table_Name2 = "Jogadores";
    public static String COL_2 = "nome";
    public static String COL_3 = "valor";

    private final Context context;
    private SQLiteDatabase db;


    public DatabaseHelper(Context context) { // Metodo que verifica a api do sipositivo rodando o aplicativo
        super(context, DATABASE_NAME, null, 1);

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.context = context;
    }


    public void create() throws IOException { //Metodo que cria o banco de dados utilizando o metodo copy que se encontra logo em baixo nesta classe
        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {
            // By calling this method and empty database will be created into the default system path
            // of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    // Check if the database exist to avoid re-copy the data
    private boolean checkDataBase() { //Metodo verifica se o banco ja existe para evitar copiar novamente sem necessidade
        SQLiteDatabase checkDB = null;
        try {
            String path = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database don't exist yet.
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    // copy your assets db
    private void copyDataBase() throws IOException { // metodo que copia o banco da pasta assets para a pasta que ira ser acessada pelo android
        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    //Open the database
    public boolean open() { // metodo que ira abrir o banco
        try {
            String myPath = DB_PATH + DATABASE_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            return true;
        } catch (SQLException sqle) {
            db = null;
            return false;
        }
    }

    @Override
    public synchronized void close() {//metodo que ira fechar o banco
        if (db != null)
            db.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
   * Get all Dados
   * */
    public List<Dados> getDados() {// Este metodo cria uma lista do tipo de dado Ddos, carega ela com dados do banco e retorn uma lista do messmo tipo
        List<Dados> perguntas = new ArrayList<>();

        try {
            //get all rows from Department table
            String query = "SELECT * FROM " + Table_Name;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                String pergunta = cursor.getString(1);
                String resposta = cursor.getString(2);
                String resposta_errada = cursor.getString(3);
                String resposta_errada2 = cursor.getString(4);
                String resposta_errada3 = cursor.getString(5);

                Dados dados = new Dados(pergunta, resposta, resposta_errada, resposta_errada2, resposta_errada3);

                perguntas.add(dados);
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        return perguntas;
    }

    public List<Jogadores> getJogadores() {// este metodo cria umalista do tipo Jogadore popula ela com dados dos jogadores present no banco e retorna um a lista do mesmo tipo.
        List<Jogadores> player = new ArrayList<>();

        try {
            //get all rows from Department table
            String query = "SELECT * FROM " + Table_Name2;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                String username = cursor.getString(2);

                Jogadores jogadores = new Jogadores(username);
                //Dados dados = new Dados(pergunta, resposta, resposta_errada, resposta_errada2, resposta_errada3);

                player.add(jogadores);
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        return player;


    }



    public List<Jogadores> insetData_msql() {// este metodo cria umalista do tipo Jogadore popula ela com dados dos jogadores present no banco e retorna um a lista do mesmo tipo.
        List<Jogadores> player = new ArrayList<>();

        try {
            //get all rows from Department table
            String query = "SELECT * FROM " + Table_Name2 + " ORDER BY " + COL_3 + " DESC";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()) {
                String email = cursor.getString(1);
                String username = cursor.getString(2);
                String pass = cursor.getString(3);
                int valor = cursor.getInt(4);

                Jogadores jogadores = new Jogadores();
                jogadores.setemail(email);
                jogadores.setUser_name(username);
                jogadores.setPass(pass);
                jogadores.setValor(valor);
                //Dados dados = new Dados(pergunta, resposta, resposta_errada, resposta_errada2, resposta_errada3);

                player.add(jogadores);
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        return player;


    }


    public void insertData(Jogadores jogadores) {// Metodo que recebe um objeto do do tipo jogadores e da get nos seus atributos e inseri no banco.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("email", jogadores.getemail());
        content.put("username", jogadores.getUser_name());
        content.put("password", jogadores.getPass());
        content.put("sync", jogadores.getSynced());
        try{
            db.insertOrThrow(Table_Name2, null, content);
        }
        catch(SQLiteConstraintException ex){

        }


        /*content.put(COL_2, nome );
       content.put(COL_3, valor );
       long result =  db.insert(Table_Name2, null, content);
        if (result == -1){
            return false;
        }else{
            return true;
        }*/

    }

    public void order_Table(List<Jogadores> list) {// Metodo que recebe um objeto do do tipo jogadores e da get nos seus atributos e inseri no banco.
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < list.size(); i++) {


            ContentValues content = new ContentValues();
            content.put("username", list.get(i).getUser_name());
            content.put("pontuacao", list.get(i).getValor());
            content.put("sync", list.get(i).getSynced());

            db.insert("Results", null, content);
        }
    }

    public List<Jogadores> getNewtable() {// este metodo cria umalista do tipo Jogadore popula ela com dados dos jogadores present no banco e retorna um a lista do mesmo tipo.
        List<Jogadores> pontos = new ArrayList<>();

        try {
            //get all rows from Department table
            String query = "SELECT * FROM " + "Results";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);



            while (cursor.moveToNext()) {
                Jogadores jogadores = new Jogadores();

                jogadores.setUser_name(cursor.getString(0));
                jogadores.setValor(cursor.getInt(1));

                pontos.add(jogadores);
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        return pontos;


    }

    public void insertRank(int id,  int pontuacao) {// Metodo que recebe um objeto do do tipo jogadores e da get nos seus atributos e inseri no banco.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();


            content.put("_id", id);
            content.put("pontuacao", pontuacao);

            db.insert("Rank", null, content);

    }

    public List<Jogadores> getRank(int id) {// este metodo cria umalista do tipo Jogadore popula ela com dados dos jogadores present no banco e retorna um a lista do mesmo tipo.
        List<Jogadores> pontos = new ArrayList<>();

        try {
            //get all rows from Department table
            String query = "SELECT * FROM Rank where _id =" + "\"" + id + "\"" + " ORDER BY pontuacao DESC";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            Jogadores jogadores = new Jogadores();

            while (cursor.moveToNext()) {
                jogadores.setValor(cursor.getInt(1));
                jogadores.setId(cursor.getInt(0));

                pontos.add(jogadores);
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        return pontos;
    }

    public List<Jogadores> getAll() {// este metodo cria umalista do tipo Jogadore popula ela com dados dos jogadores present no banco e retorna um a lista do mesmo tipo.
        List<Jogadores> pontos = new ArrayList<>();

        try {
            //get all rows from Department table
            String query = "select * from " + "Rank" + " ORDER BY pontuacao DESC";;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);



            while (cursor.moveToNext()) {
                Jogadores jogadores = new Jogadores();
                jogadores.setId(cursor.getInt(0));
                jogadores.setValor(cursor.getInt(1));

                pontos.add(jogadores);
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        return pontos;
    }

    public List<Jogadores> getuser(int id) {// este metodo cria umalista do tipo Jogadore popula ela com dados dos jogadores present no banco e retorna um a lista do mesmo tipo.
        List<Jogadores> pontos = new ArrayList<>();
        String username = "";
        int sync = 0;

        try {
            //get all rows from Department table
            String query = "select * from Jogadores where id =" + "\"" + id + "\"";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToNext();

            username = cursor.getString(2);
            sync = cursor.getInt(4);
            Jogadores jo = new Jogadores();

            jo.setUser_name(username);
            jo.setSynced(sync);

            pontos.add(jo);







               /* Jogadores jogadores = new Jogadores();

               jogadores.setUser_name(cursor.getString(2));

                pontos.add(jogadores);*/

        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        return pontos;


    }

    public int getId(String username){
        int valor = 0;

        try {
            //get all rows from Department table
            String query = "select * from Jogadores where username =" + "\"" + username + "\"";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            cursor.moveToNext();

          //  while (cursor.moveToNext()) {
                 valor = cursor.getInt(0);

                /*Jogadores jogadores = new Jogadores(nome, valor);
                //Dados dados = new Dados(pergunta, resposta, resposta_errada, resposta_errada2, resposta_errada3);

                player.add(jogadores);*/
          //  }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }

        return valor;

    }

    public void updateData(int valor, int synced, int id) {// Metodo que recebe um objeto do do tipo jogadores e da get nos seus atributos e inseri no banco.
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        ContentValues cv1 = new ContentValues();
        cv.put("pontuacao", valor);
        cv1.put("sync", synced);

        db.update("Rank", cv, "_id="+id, null);
        db.update("Jogadores", cv1, "id="+id, null);




       /* String rawQuery = "UPDATE Rank SET pontuacao = '"+valor+"' WHERE _id = '"+id+"'";
        String rawQuery1 = "UPDATE Jogadores SET sync = '"+synced+"' WHERE id = '"+id+"'";
        db.rawQuery(rawQuery, null);
        db.rawQuery(rawQuery1,null);*/


       /* ContentValues content = new ContentValues();
        ContentValues content1 = new ContentValues();



        content.put("valor", valor);
        content1.put("synced", synced);

        String[] args = new String[]{String.valueOf(id)};


        db.update("Rank", content, "_id=?", valor);
        db.update("Jogadores", content1, "id=?",args);*/

        /*content.put(COL_2, nome );
       content.put(COL_3, valor );
       long result =  db.insert(Table_Name2, null, content);
        if (result == -1){
            return false;
        }else{
            return true;
        }*/

    }

    public void updateSync_status(int synced) {// Metodo que recebe um objeto do do tipo jogadores e da get nos seus atributos e inseri no banco.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("synced", synced);

        String[] args = new String[]{String.valueOf(synced)};


        db.update(Table_Name2, content, "synced=?", args);

        /*content.put(COL_2, nome );
       content.put(COL_3, valor );
       long result =  db.insert(Table_Name2, null, content);
        if (result == -1){
            return false;
        }else{
            return true;
        }*/

    }

    public String logIn(String username, String pass) {
        String usuario = "";
        try {
            int i = 0;

            Cursor c = null;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            c = db.rawQuery("select * from Jogadores where username =" + "\"" + username.trim() + "\"" + " and password=" + "\"" + pass.trim() + "\"", null);
            c.moveToFirst();
            i = c.getCount();
            if (i > 0) {
                usuario = c.getString(2);
                c.close();

            } else {
                usuario = "Username ou senha incorreto!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;


    }
}

