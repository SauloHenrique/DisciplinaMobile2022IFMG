package com.example.financasjosepro.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Vector;

public class Entrada {
    private long id;
    private String nome;
    private double valor;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private boolean operacao; //true -> + | false -> -
    private String descricao;
    private String classificacao;
    private boolean repete;
    private Vector<Foto> imagens;

    public Entrada(String nome, double valor, LocalDateTime dataInicial, LocalDateTime dataFinal, boolean operacao, String descricao, String classificacao, boolean repete) {
        this.nome = nome;
        this.valor = valor;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.operacao = operacao;
        this.descricao = descricao;
        this.classificacao = classificacao;
        this.repete = repete;
        this.imagens = new Vector<>();
    }

    public Entrada(long id, String nome, double valor, LocalDateTime dataInicial, LocalDateTime dataFinal, boolean operacao, String descricao, String classificacao, boolean repete) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.operacao = operacao;
        this.descricao = descricao;
        this.classificacao = classificacao;
        this.repete = repete;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDateTime dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDateTime getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }

    public boolean isOperacao() {
        return operacao;
    }

    public void setOperacao(boolean operacao) {
        this.operacao = operacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public boolean isRepete() {
        return repete;
    }

    public void setRepete(boolean repete) {
        this.repete = repete;
    }

    public Vector<Foto> getImagens() {
        return imagens;
    }

    public void setImagens(Vector<Foto> imagens) {
        this.imagens = imagens;
    }

    public void addImagem(Foto novaFoto){
        this.imagens.add(novaFoto);
    }
}
