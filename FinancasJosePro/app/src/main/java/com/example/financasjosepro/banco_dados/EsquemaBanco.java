package com.example.financasjosepro.banco_dados;

public class EsquemaBanco {

    public static final String NOME_BANCO_DADOS = "financas_jose";

    //esta versao deve ser modificada SEMPRE que o esquema do banco é alterado
    //ativa onUpgrade()
    public static final int VERSAO = 1;

    public static final String SQL_CREATE_TABLES =
            Entrada.SQL_CREATE; //+ Foto.SQL_CREATE;

    public static final String SQL_DROP_TABLES = Entrada.SQL_DROP;
            //+ Foto.SQL_DROP;

    public static class Entrada{
        public static final String NOME_TABELA = "entrada";

        //id, nome, descricao, dataInicial, dataFinal,
        // valor, repete, operacao, classificacao

        public static final String NOME_COLUNA_ID = "id";
        public static final String NOME_COLUNA_NOME = "nome";
        public static final String NOME_COLUNA_DESCRICAO = "descricao";
        public static final String NOME_COLUNA_DATA_INICIAL = "datai";
        public static final String NOME_COLUNA_DATA_FINAL = "dataf";
        public static final String NOME_COLUNA_VALOR = "valor";
        public static final String NOME_COLUNA_REPETE = "repete";
        public static final String NOME_COLUNA_OPERACAO = "operacao";
        public static final String NOME_COLUNA_CLASSIFICACAO = "classificao";

        /*
        * long id, String nome, double valor, LocalDateTime dataInicial,
        * LocalDateTime dataFinal, boolean operacao, String descricao,
        * String classificacao, boolean repete
        * */

        public static final String COLUNAS[] = {NOME_COLUNA_ID, NOME_COLUNA_NOME, NOME_COLUNA_VALOR,
                NOME_COLUNA_DATA_INICIAL, NOME_COLUNA_DATA_FINAL, NOME_COLUNA_OPERACAO,
                NOME_COLUNA_DESCRICAO, NOME_COLUNA_CLASSIFICACAO, NOME_COLUNA_REPETE};

        public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " +
                "entrada(" +
                NOME_COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOME_COLUNA_NOME + " TEXT," +
                NOME_COLUNA_DESCRICAO + " TEXT,"+
                NOME_COLUNA_DATA_INICIAL + " INTEGER,"+
                NOME_COLUNA_DATA_FINAL + " INTEGER,"+
                NOME_COLUNA_VALOR + " REAL,"+
                NOME_COLUNA_REPETE + " INTEGER,"+
                NOME_COLUNA_OPERACAO + " INTEGER,"+
                NOME_COLUNA_CLASSIFICACAO + " TEXT"+
                ");";

        public static final String SQL_DROP = "DROP IF EXISTS entrada;";
    }

    public static class Foto{
        //add fotos (valor 5 pontos)
    }

}
