package com.example.financasjosepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financasjosepro.configuracao.SharedPreferencesAplicao;
import com.example.financasjosepro.controller.EntradaController;
import com.example.financasjosepro.fragmets.InformacoesFragment;
import com.example.financasjosepro.modelo.Entrada;
import com.example.financasjosepro.repository.EntradaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private TextView mesAnoTxt;
    private Button cadastraEntradaBtn;
    private Button cadastraSaidaBtn;
    private InformacoesFragment informacoesMesFragment;

    private float x1, x2; //y1, y2 - quando altera a orientacao
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private String nomeMes[] = {"Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"};
    private LocalDateTime dataOperacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesAnoTxt = findViewById(R.id.titulomesanotxt);
        cadastraEntradaBtn = findViewById(R.id.entradaBtn);
        cadastraSaidaBtn = findViewById(R.id.saidaBtn);

        //referenciando o nosso fragment para um atributo
        informacoesMesFragment = (InformacoesFragment) getFragmentManager().findFragmentById((R.id.fragmentovalores));

        requisitaPermissoes();

        aplicaConfiguracoes();
        configuraDataInicial();
        registroEventos();

        cadastraEvento();

        atualizaDadosMes();
    }

    private void cadastraEvento(){

        //String nome, double valor, LocalDateTime dataInicial, LocalDateTime dataFinal,
        // boolean operacao, String descricao, String classificacao, boolean repete

        EntradaController controller = new EntradaController(MainActivity.this);

        Entrada novaEntrada = new Entrada("Salario",500,LocalDateTime.now(ZoneOffset.UTC),
                LocalDateTime.now(ZoneOffset.UTC).withDayOfMonth(30), false, "Prolabore da startup", "Salario", false);

        controller.insertEntrada(novaEntrada);
    }

    private void atualizaDadosMes(){
        EntradaController controller = new EntradaController(MainActivity.this);
        try {
            Vector<Entrada> entradas = controller.buscaEntradasPorData(
                    dataOperacao.withDayOfMonth(1),
                    dataOperacao.withDayOfMonth(dataOperacao.getMonth().
                            length(dataOperacao.toLocalDate().isLeapYear())));

            double somaMes = 0.0;

            //somando os valores registrados no mes
            for(Entrada et : entradas){
                somaMes += et.isOperacao() ? et.getValor() : (et.getValor()*-1);
            }

            informacoesMesFragment.setSaldoTotal(somaMes);

        }catch (Exception e){
            //caso aconte. um erro mostramos a mens.
            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void requisitaPermissoes(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
    }

    private void aplicaConfiguracoes(){
        SharedPreferencesAplicao minhasConfiguracoes =
                new SharedPreferencesAplicao(MainActivity.this);
        this.dataOperacao = minhasConfiguracoes.leituraData();
    }

    private void registroEventos(){
        cadastraEntradaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecionaCadastro(true);
            }
        });

        cadastraSaidaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecionaCadastro(false);
            }
        });
    }

    //redireciona true - Cadastra ENTRADA
    //redireciona false - Cadastra SAIDA
    private void redirecionaCadastro(boolean operacao){
        Intent activityEntrada = new Intent(MainActivity.this,
                CadastroActivity.class);

        activityEntrada.putExtra("operacao",operacao);

        startActivity(activityEntrada);
    }

    //mantendo a data inicial e final das entradas sumarizadas para o usuario
    private void configuraDataInicial(){
        //primeiro dia do mes, hora, minuto e segundo 1/mm/yyyy 00:00:00
        dataInicial = dataOperacao.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        dataFinal = dataOperacao.withDayOfMonth(dataOperacao.getDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        atualizaMesAno();
    }

    private void atualizaMesAno(){
        mesAnoTxt.setText(nomeMes[dataInicial.getMonth().getValue()-1]+"/"+dataInicial.getYear());
    }


    //evento que trata a acao do usuario em "arrastar" para esquerda e direita
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX(); break;
            case MotionEvent.ACTION_UP:
                //movimento de parada...
                x2 = event.getX();
                if(x1 > x2){
                    //direita
                    dataOperacao = dataOperacao.plusMonths(1);
                }else{
                    //esquerda
                    dataOperacao = dataOperacao.plusMonths(-1);
                }
                configuraDataInicial();
                atualizaDadosMes();
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferencesAplicao minhasConfiguracoes =
                new SharedPreferencesAplicao(MainActivity.this);
        minhasConfiguracoes.registraData(this.dataOperacao);

    }
}