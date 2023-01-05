package com.example.financasjosepro;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financasjosepro.configUiDialogs.ConfigCalendarUi;
import com.example.financasjosepro.controller.EntradaController;
import com.example.financasjosepro.modelo.Entrada;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoricoActivity extends AppCompatActivity {

    private ListView listView;
    private TextView data1Txt;
    private TextView data2Txt;

    private ArrayList<Entrada> dataModels;
    private static ItemAdapter adapter;
    private LocalDateTime tempoInicial;
    private LocalDateTime tempoFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        listView = findViewById(R.id.entradasList);
        data1Txt = findViewById(R.id.dataInicialTxt);
        data2Txt = findViewById(R.id.dataFinalTxt);

        dataModels= new ArrayList<>();
        adapter= new ItemAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        configuraDatas();
        registraEventos();
        consultaEventos();
    }

    private void configuraDatas(){
        Bundle parametros = getIntent().getExtras();
        long t1 = parametros.getLong("tempo1",-1);
        long t2 = parametros.getLong("tempo2",-1);

        System.out.println("tempo " + t1 + " "+t2);

        if(t1 != -1 && t2 != -1){
            tempoInicial = Instant.ofEpochMilli(t1).atZone(ZoneOffset.UTC).toLocalDateTime();
            tempoFinal = Instant.ofEpochMilli(t2).atZone(ZoneOffset.UTC).toLocalDateTime();
        }else{
            tempoInicial = LocalDateTime.now().withDayOfMonth(1);
            tempoFinal = tempoInicial.withDayOfMonth(tempoInicial.getMonth().
                            length(tempoInicial.toLocalDate().isLeapYear()));
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        data1Txt.setText(format.format(tempoInicial));
        data2Txt.setText(format.format(tempoFinal));
    }

    private void registraEventos(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Entrada dataModel= dataModels.get(position);

                //temos o obj completo selecionado aqui
                System.out.println(dataModel.getNome());
            }
        });

        data1Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigCalendarUi.configuraCalendario(data1Txt, Calendar.getInstance(), HistoricoActivity.this);
            }
        });

        data1Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    tempoInicial = Instant.ofEpochMilli(formatter.parse(data1Txt.getText().toString())
                                    .getTime()).atZone(ZoneOffset.UTC).toLocalDateTime();

                    tempoFinal = tempoInicial.withDayOfMonth(tempoInicial.getMonth().
                            length(tempoInicial.toLocalDate().isLeapYear()));

                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    data2Txt.setText(format.format(tempoFinal));

                    consultaEventos();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        data2Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigCalendarUi.configuraCalendario(data2Txt, Calendar.getInstance(), HistoricoActivity.this);
            }
        });

        data2Txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    tempoFinal = Instant.ofEpochMilli(formatter.parse(data2Txt.getText().toString())
                            .getTime()).atZone(ZoneOffset.UTC).toLocalDateTime();

                    consultaEventos();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void consultaEventos(){
        dataModels.clear();
        EntradaController controller = new EntradaController(HistoricoActivity.this);

        try {
            dataModels.addAll(controller.buscaEntradasPorData(tempoInicial,tempoFinal));
        } catch (Exception e) {
            Toast.makeText(HistoricoActivity.this,"não foi possível recuperar eventos para este intervalo",
            Toast.LENGTH_SHORT).show();
        }
    }

}