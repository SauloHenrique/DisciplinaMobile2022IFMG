package com.example.financasjosepro.banco_dados;

public class EsquemaBanco {

    public static final String NOME_BANCO_DADOS = "financas_jose";

    //esta versao deve ser modificada SEMPRE que o esquema do banco é alterado
    //ativa onUpgrade()
    public static final int VERSAO = 1;

    public static class Entrada{
        public static final String NOME_TABELA = "entrada";

        public static final String NOME_COLUNA_ID = "id";

    }

}
