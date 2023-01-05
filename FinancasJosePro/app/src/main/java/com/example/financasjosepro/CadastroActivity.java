package com.example.financasjosepro;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financasjosepro.configUiDialogs.ConfigCalendarUi;
import com.example.financasjosepro.controller.EntradaController;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

public class CadastroActivity extends AppCompatActivity {

    private TextView dataTxt;
    private EditText nomeTxt;
    private EditText valorTxt;
    private Spinner classeSpin;
    private CheckBox repeteBtn;
    private Spinner mesesRepeteSpin;
    private TextView mesesRepeteTxt;
    private EditText descricaoTxt;
    private Button salvarBtn;

    private Calendar dataSelecionada;
    private boolean operacaoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dataTxt = findViewById(R.id.dataCadTxt);
        nomeTxt = findViewById(R.id.nomeCadTxt);
        valorTxt = findViewById(R.id.valorCadTxt);
        classeSpin = findViewById(R.id.classeCadSpinner);
        repeteBtn = findViewById(R.id.repeteCadCheck);
        mesesRepeteSpin = findViewById(R.id.mesesCadSpinner);
        mesesRepeteTxt = findViewById(R.id.mesesRepeteTxt);
        descricaoTxt = findViewById(R.id.descricaoCadTxt);
        salvarBtn = findViewById(R.id.salvarBtn);

        //acessando os parametros passados pela Activity anterior
        Bundle parametros = getIntent().getExtras();
        operacaoAtual = parametros.getBoolean("operacao");

        configuraDataInicial();
        registrarEventos();

        carregaSpinners();
    }

    private void carregaSpinners(){
        String valores[] = new String[421];
        valores[0] = "indefinido";

        for(int i = 1; i <= 420;i++){
            valores[i] = i + "";
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(CadastroActivity.this,
                android.R.layout.simple_spinner_dropdown_item,valores);

        mesesRepeteSpin.setAdapter(adapter);

    }

    //trocar para LocalDateFormat
    private void configuraDataInicial(){
        dataSelecionada = Calendar.getInstance();

        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

        dataTxt.setText(sf.format(dataSelecionada.getTime()));
    }

    private void registrarEventos(){
        dataTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigCalendarUi.configuraCalendario(dataTxt, dataSelecionada, CadastroActivity.this);
            }
        });

        repeteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mesesRepeteSpin.setVisibility(repeteBtn.isChecked() ? View.VISIBLE : View.INVISIBLE);
                mesesRepeteTxt.setVisibility(repeteBtn.isChecked() ? View.VISIBLE : View.INVISIBLE);
            }
        });

        salvarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastraEntrada();
            }
        });
    }



    private void cadastraEntrada(){
        String nome = nomeTxt.getText().toString();
        double valor = Double.parseDouble(valorTxt.getText().toString());

        LocalDateTime dataCadastro = Instant.ofEpochMilli(dataSelecionada.getTime()
                .getTime()).atZone(ZoneOffset.UTC).toLocalDateTime();

        LocalDateTime dataFinal = null;
        //calculando a data final dado a seleção de tempo de repeticao
        if(repeteBtn.isChecked() && mesesRepeteSpin.getSelectedItemPosition() != 0){
            dataFinal = dataCadastro.plusMonths(mesesRepeteSpin.getSelectedItemPosition());
            //setando a data da ultima parcela para o ultimo dia do mes da data final
            dataFinal = dataFinal.withDayOfMonth(
                    dataFinal.getMonth().length(dataFinal.toLocalDate().isLeapYear()));
        }else{
            if(!repeteBtn.isChecked()){
                dataFinal = dataCadastro.withDayOfMonth(
                        dataCadastro.getMonth().length(dataCadastro.toLocalDate().isLeapYear()));
            }
        }

        String descricao = descricaoTxt.getText().toString();

        String classe = (String)classeSpin.getSelectedItem();

        EntradaController controller = new EntradaController(CadastroActivity.this);

        //se ocorreu erro vamos indicar para o user
        if(!controller.insertEntrada(nome, valor,dataCadastro,dataFinal,descricao,
                classe,operacaoAtual,repeteBtn.isChecked())){
            Toast.makeText(CadastroActivity.this,"problema!!!",
                    Toast.LENGTH_SHORT).show();
        }

        //NOVO
        setResult(RESULT_OK);

        //encerra a execucao da activity atual e resume a execucao da activity anterior
        finish();

    }

}