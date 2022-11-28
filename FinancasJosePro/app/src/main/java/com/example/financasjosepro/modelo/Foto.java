package com.example.financasjosepro.modelo;

public class Foto {
    private long id;
    private String caminhoImagem;

    public Foto(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }
}
