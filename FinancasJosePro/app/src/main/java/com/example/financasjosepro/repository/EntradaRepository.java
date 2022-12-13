package com.example.financasjosepro.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.financasjosepro.banco_dados.EsquemaBanco;
import com.example.financasjosepro.banco_dados.OperacoesBancoDados;
import com.example.financasjosepro.modelo.Entrada;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Vector;

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
    public Vector<Entrada> selectEventosPorDatas(long dataInicial, long dataFinal){

        Vector<Entrada> resultado = new Vector<>();

        //datai >= 1 AND ((dataf <= 31 AND repete = 0) OR (dataf = -1 AND repete = 1))

        try(OperacoesBancoDados conexaoBanco = new OperacoesBancoDados(this.contexto)){
            String sql_Busca = EsquemaBanco.Entrada.NOME_COLUNA_DATA_INICIAL + ">=? and ((" +
                    EsquemaBanco.Entrada.NOME_COLUNA_DATA_FINAL + "<=? " +
                    EsquemaBanco.Entrada.NOME_COLUNA_REPETE +"=0)OR (" +
                    EsquemaBanco.Entrada.NOME_COLUNA_DATA_FINAL + "=-1 AND " +
                    EsquemaBanco.Entrada.NOME_COLUNA_REPETE + "=1))";

            SQLiteDatabase tran = conexaoBanco.getReadableDatabase();

            //efetuamos a execucaçõ da tran. select aqui
            Cursor tuplas = tran.query(EsquemaBanco.Entrada.NOME_TABELA,
                    EsquemaBanco.Entrada.COLUNAS,
                    sql_Busca,
                    new String[]{dataInicial+"", dataFinal+""}, null,
                    null, null);

            //ler as tuplas retornadas pelo banco e inserir no vetor de resultado
            while(tuplas.moveToNext()){

                //long id, String nome, double valor, LocalDateTime dataInicial,
                // LocalDateTime dataFinal, boolean operacao, String descricao,
                // String classificacao, boolean repete
                Entrada entradaBd = new Entrada(
                      tuplas.getLong(0),
                        tuplas.getString(1),
                        tuplas.getDouble(2),
                        Instant.ofEpochMilli(tuplas.getLong(3))
                                .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        tuplas.getLong(4) == -1? null : Instant.ofEpochMilli(tuplas.getLong(4))
                                                                .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        tuplas.getInt(5) == 1,
                        tuplas.getString(6),
                        tuplas.getString(7),
                        tuplas.getInt(8) == 1
                );

                resultado.add(entradaBd);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

}
