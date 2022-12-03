package com.example.financasjosepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.financasjosepro.configuracao.SharedPreferencesAplicao;
import com.example.financasjosepro.fragmets.InformacoesFragment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Random;

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

        aplicaConfiguracoes();
        configuraDataInicial();
        registroEventos();
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
                x2 = event.getX();
                if(x1 > x2){
                    //direita
                    Toast.makeText(this,"direita",Toast.LENGTH_SHORT).show();
                    dataOperacao = dataOperacao.plusMonths(1);
                    configuraDataInicial();
                }else{
                    //esquerda
                    dataOperacao = dataOperacao.plusMonths(-1);
                    configuraDataInicial();
                }
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