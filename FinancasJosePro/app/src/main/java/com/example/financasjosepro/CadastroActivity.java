package com.example.financasjosepro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financasjosepro.configUiDialogs.ConfigCalendarUi;
import com.example.financasjosepro.controller.EntradaController;
import com.example.financasjosepro.modelo.Foto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

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
    private Button addFotoBtn;
    //private ImageView fotoImageView;
    private LinearLayout fotosList;

    private Calendar dataSelecionada;
    private boolean operacaoAtual;
    private ActivityResultLauncher<Intent> callBackFoto;
    private Vector<Foto> fotosUsuario;

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
        addFotoBtn = findViewById(R.id.addFotoBtn);
        //fotoImageView = findViewById(R.id.imagemCadFoto);
        fotosList = findViewById(R.id.fotosCadList);

        fotosUsuario = new Vector<>();

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
        callBackFoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //será que o usuário "tirou" foto?
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Bundle retornoActivityFoto = result.getData().getExtras();

                            //aqui vamos receber a imagem!!
                            Bitmap fotoUsuario = (Bitmap) retornoActivityFoto.get("data");

                            //metodo "grava" a imagem capturada no dispositivo do usuario
                            String caminhoFoto = salvarImagemDevice(fotoUsuario);

                            if(caminhoFoto != null) {
                                //Criando um ImageView temporário para encapsular a foto do usuario
                                ImageView viewFoto = new ImageView(CadastroActivity.this);
                                viewFoto.setImageBitmap(fotoUsuario);

                                LinearLayout.LayoutParams parametrosView =
                                        new LinearLayout.LayoutParams(500, 500, 1);
                                viewFoto.setLayoutParams(parametrosView);

                                fotosList.addView(viewFoto);

                                //vamos add as fotos a entrada cadastrada
                                fotosUsuario.add(new Foto(caminhoFoto));

                                //fotoImageView.setImageBitmap(fotoUsuario);
                            }

                        }
                    }
                });

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

        addFotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });
    }

    //metodo salava a imagem e retorna o caminho onde a mesma esta arm.
    private String salvarImagemDevice(Bitmap fotoUsuario) {

        String raizDiretorio = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).
                toString();

        //vamos criar um diretório para o NOSSO app (armazenar as imagens)
        File meuDiretorio = new File(raizDiretorio+"/appJose");
        meuDiretorio.mkdir();

        //definindo o file para a imagem que será armazenada
        File arquivoImagem = new File(meuDiretorio,
                "foto"+new Date().getTime()+".jpg");

        try {
            //"caminho" para enviar os bytes da imagem para o arquivo.jpg
            FileOutputStream outputStream = new FileOutputStream(arquivoImagem);

            //aqui enviamos os bytes da imagem para o arquivo
            fotoUsuario.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            //"descarregando" os bytes não "transmitidos" para o arquivo da imagem
            outputStream.flush();
            outputStream.close();

            return arquivoImagem.getPath();

        } catch (FileNotFoundException e) {
            Toast.makeText(CadastroActivity.this,
                    "erro ao salvar imagem", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(CadastroActivity.this,
                    "Imagem Corrompida", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void tirarFoto(){
        Intent executaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //aqui estamos "linkando" a callback implementada à activitiy que trata a captura da foto
        callBackFoto.launch(executaCamera);
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

        //****para aprender mais (desafio)****
        //armazen. as fotos que o usuário tirou junto a entrada cadastrada
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