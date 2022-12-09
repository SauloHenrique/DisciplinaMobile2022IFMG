package com.example.financasjosepro.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.financasjosepro.banco_dados.EsquemaBanco;
import com.example.financasjosepro.banco_dados.OperacoesBancoDados;
import com.example.financasjosepro.modelo.Entrada;

import java.time.ZoneOffset;

public class EntradaRepository {

    private Context contexto;

    public EntradaRepository(Context contexto){
        this.contexto = contexto;
    }

    //cadastro
    public boolean insertEvento(Entrada novaEntrada){
        try(OperacoesBancoDados conexaoBanco =
                new OperacoesBancoDados(this.contexto)){

            SQLiteDatabase tran = conexaoBanco.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_NOME, novaEntrada.getNome());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_VALOR, novaEntrada.getValor());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_CLASSIFICACAO, novaEntrada.getClassificacao());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_DATA_INICIAL,
                    novaEntrada.getDataInicial().toInstant(ZoneOffset.UTC).toEpochMilli());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_DESCRICAO, novaEntrada.getDescricao());
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_OPERACAO, novaEntrada.isOperacao()? 1:0);
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_REPETE, novaEntrada.isRepete() ? 1:0);

            //valores null's não sao permitidos no SQLite portanto -1 será definido como data null
            values.put(EsquemaBanco.Entrada.NOME_COLUNA_DATA_FINAL,
                    novaEntrada.getDataFinal() == null? -1 :
                    novaEntrada.getDataFinal().toInstant(ZoneOffset.UTC).toEpochMilli());

            //executa o INSERT e RETORNA o id gerado pelo autoincrement
            long idGeradoBD = tran.insert(EsquemaBanco.Entrada.NOME_TABELA, null, values);

            if(idGeradoBD > 0){
                novaEntrada.setId(idGeradoBD);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    //recuperar as entradas/saida (data inicial e data final)


}
