package com.example.marcio.logical;

/**
 * Created by marcio on 02/09/16.
 */
//Classe que modela o objeto do tipo jogadroes
public class Jogadores {
    private String nome, user_name, pass;
    private int valor, synced, id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Jogadores() {

    }

    public Jogadores (String username){
        this.user_name = username;

    }
}
