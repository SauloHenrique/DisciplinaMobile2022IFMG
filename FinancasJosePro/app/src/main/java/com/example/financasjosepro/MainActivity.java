package com.example.financasjosepro;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.financasjosepro.configuracao.SharedPreferencesAplicao;
import com.example.financasjosepro.controller.EntradaController;
import com.example.financasjosepro.fragmets.InformacoesFragment;
import com.example.financasjosepro.modelo.Entrada;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private TextView mesAnoTxt;
    private Button cadastraEntradaBtn;
    private Button cadastraSaidaBtn;
    private InformacoesFragment informacoesMesFragment;
    private Button historicoBtn;

    private float x1, x2; //y1, y2 - quando altera a orientacao
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private String nomeMes[] = {"Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"};
    private LocalDateTime dataOperacao;

    private ActivityResultLauncher<Intent> callBackCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesAnoTxt = findViewById(R.id.titulomesanotxt);
        cadastraEntradaBtn = findViewById(R.id.entradaBtn);
        cadastraSaidaBtn = findViewById(R.id.saidaBtn);
        historicoBtn = findViewById(R.id.historicoBtn);

        //referenciando o nosso fragment para um atributo
        informacoesMesFragment = (InformacoesFragment) getFragmentManager().findFragmentById((R.id.fragmentovalores));

        requisitaPermissoes();

        aplicaConfiguracoes();
        configuraDataInicial();
        registroEventos();

        atualizaDadosMes();
    }

    private void atualizaDadosMes(){
        EntradaController controller = new EntradaController(MainActivity.this);
        try {
            Vector<Entrada> entradas = controller.buscaEntradasPorData(
                    dataOperacao.withDayOfMonth(1),
                    dataOperacao.withDayOfMonth(dataOperacao.getMonth().
                            length(dataOperacao.toLocalDate().isLeapYear())));

            //NOVO
            double somaMes = 0.0;
            double somaSaida = 0.0;
            double somaEntrada = 0.0;

            //somando os valores registrados no mes
            for(Entrada et : entradas){
                somaMes += et.isOperacao() ? et.getValor() : (et.getValor()*-1);

                if(et.isOperacao()){
                    somaSaida += et.getValor();
                }else{
                    somaEntrada += et.getValor();
                }

            }

            informacoesMesFragment.setSaldoTotal(somaMes, somaSaida, somaEntrada);
            System.out.println(somaMes + " " + somaSaida + " " + somaEntrada);

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

        //---NOVO
        callBackCadastro = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //nesta funcao vamos tratar a busca quando o cadastro encerra
                        atualizaDadosMes();

                    }
                });

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

        //NOVO
        historicoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configuraDataInicial();
                Intent activityHistorico = new Intent(MainActivity.this, HistoricoActivity.class);

                activityHistorico.putExtra("tempo1",dataInicial.toInstant(ZoneOffset.UTC).toEpochMilli());
                activityHistorico.putExtra("tempo2",dataFinal.toInstant(ZoneOffset.UTC).toEpochMilli());

                startActivity(activityHistorico);
            }
        });

    }

    //redireciona true - Cadastra ENTRADA
    //redireciona false - Cadastra SAIDA
    private void redirecionaCadastro(boolean operacao){
        Intent activityEntrada = new Intent(MainActivity.this,
                CadastroActivity.class);

        activityEntrada.putExtra("operacao",operacao);

        //NOVO
        callBackCadastro.launch(activityEntrada);
    }

    //mantendo a data inicial e final das entradas sumarizadas para o usuario
    private void configuraDataInicial(){
        //primeiro dia do mes, hora, minuto e segundo 1/mm/yyyy 00:00:00
        dataInicial = dataOperacao.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        dataFinal = dataOperacao.withDayOfMonth(dataOperacao.getMonth().
                length(dataOperacao.toLocalDate().isLeapYear())).withHour(23).
                withMinute(59).withSecond(59);

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