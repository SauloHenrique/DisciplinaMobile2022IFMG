package com.example.financasjosepro.controller;

import android.content.Context;

import com.example.financasjosepro.modelo.Entrada;
import com.example.financasjosepro.repository.EntradaRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Vector;

public class EntradaController {

    private Context contexto;

    public EntradaController(Context contexto){
        this.contexto = contexto;
    }

    public Vector<Entrada> buscaEntradasPorData(LocalDateTime data1, LocalDateTime data2) throws Exception {

        if(data1 != null && data2 != null){
            long dataLong1 = data1.toInstant(ZoneOffset.UTC).toEpochMilli();
            long dataLong2 = data2.toInstant(ZoneOffset.UTC).toEpochMilli();

            if(dataLong1 < dataLong2){

                EntradaRepository repos = new EntradaRepository(contexto);

                return repos.selectEventosPorDatas(dataLong1, dataLong2);

            }else{
                throw new Exception("A data 1 precisa ser menor que a data 2");
            }
        }else{
            throw  new Exception("As datas não podem ser nulas");
        }

    }

    public boolean insertEntrada(Entrada novaEntrada){
        if(novaEntrada != null){
            EntradaRepository repository = new EntradaRepository(contexto);
            return repository.insertEvento(novaEntrada);
        }

        return false;
    }

    public boolean insertEntrada(String nome, double valor, LocalDateTime dataI,
                                 LocalDateTime dataF, String descricao,
                                 String classe, boolean operacao, boolean repete){

        //verificando os parametros passados no cadastro
        if(nome != null && !nome.isEmpty() && valor > 0 && dataI != null &&
        descricao != null && !descricao.isEmpty() && classe != null && !classe.isEmpty()){

            Entrada novaEntrada = new Entrada(nome, valor, dataI, dataF, operacao,
                    descricao, classe, repete);

            //executa o INSERT
            return insertEntrada(novaEntrada);
        }else{
            return false;
        }
    }

}
