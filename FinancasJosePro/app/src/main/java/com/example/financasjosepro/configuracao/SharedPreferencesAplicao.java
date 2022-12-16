package com.example.financasjosepro.configuracao;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class SharedPreferencesAplicao {

    private Context contextoAtual;

    public SharedPreferencesAplicao(Context cont){
        this.contextoAtual = cont;
    }

    public void registraData(LocalDateTime dataOperacao){

        //acessando o SharedPreferences
        SharedPreferences shared = this.contextoAtual.getSharedPreferences("FinancaJose",0);
        SharedPreferences.Editor ed = shared.edit();

        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        //gravando uma configuracao no sharedPreferences
        ed.putLong("data_atual",dataOperacao.toInstant(ZoneOffset.UTC).toEpochMilli());
        ed.commit();
    }

    public LocalDateTime leituraData(){
        SharedPreferences shared = this.contextoAtual.getSharedPreferences("FinancaJose",0);

        //buscando uma ocnfiguracao no shared (-1 CASO a chave não tenha sido gravada anteriormente)
        long tempoMil = shared.getLong("data_atual",-1);

        if(tempoMil == -1) {
            return LocalDateTime.now(ZoneOffset.UTC);
        }else{
            Instant temp = Instant.ofEpochMilli(tempoMil);
            return LocalDateTime.ofInstant(temp, ZoneOffset.UTC);
        }

    }


}
