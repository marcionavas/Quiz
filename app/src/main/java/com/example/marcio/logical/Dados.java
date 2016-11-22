package com.example.marcio.logical;

import java.io.Serializable;

/**
 * Created by marcio on 14/08/16.
 */
//Classe utilizada para modelar objeto que sera gravado e recuperado do banco
public class Dados implements Serializable {
    private String pergunta;
    private String resposta;
    private String resposta_errada;
    private String resposta_errada2;
    private String resposta_errada3;


    public String getResposta_errada() {
        return resposta_errada;
    }

    public void setResposta_errada(String resposta_errada) {
        this.resposta_errada = resposta_errada;
    }

    public String getResposta_errada2() {
        return resposta_errada2;
    }

    public void setResposta_errada2(String resposta_errada2) {
        this.resposta_errada2 = resposta_errada2;
    }

    public String getResposta_errada3() {
        return resposta_errada3;
    }

    public void setResposta_errada3(String resposta_errada3) {
        this.resposta_errada3 = resposta_errada3;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public Dados(String pergunta, String resposta, String resposta_errada, String resposta_errada2, String resposta_errada3) {
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.resposta_errada = resposta_errada;
        this.resposta_errada2 = resposta_errada2;
        this.resposta_errada3 = resposta_errada3;
    }
}
