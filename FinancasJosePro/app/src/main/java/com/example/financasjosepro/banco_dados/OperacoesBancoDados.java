package com.example.financasjosepro.banco_dados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OperacoesBancoDados extends SQLiteOpenHelper {

    public OperacoesBancoDados(Context contexto){
        super(contexto, EsquemaBanco.NOME_BANCO_DADOS, null, EsquemaBanco.VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EsquemaBanco.SQL_CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(EsquemaBanco.SQL_DROP_TABLES);
        onCreate(sqLiteDatabase);
    }
}
