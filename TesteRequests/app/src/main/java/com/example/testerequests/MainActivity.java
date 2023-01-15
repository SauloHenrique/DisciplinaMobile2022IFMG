package com.example.testerequests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText nomeTxt;
    private EditText emailTxt;
    private Spinner generoSpinner;
    private CheckBox ativoCheck;
    private Button salvarBtn;
    private Button clienteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomeTxt = findViewById(R.id.nomeTxt);
        emailTxt = findViewById(R.id.emailTxt);
        generoSpinner = findViewById(R.id.generoSpinner);
        ativoCheck = findViewById(R.id.ativoCheck);
        salvarBtn = findViewById(R.id.salvarBtn);
        clienteBtn = findViewById(R.id.clienteBrn);

        requisitaPermissoes();

        registraEventos();

    }

    private void requisitaPermissoes(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.INTERNET},0);
    }

    private void registraEventos(){
        salvarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviaUsarioServidor();
            }
        });

        clienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telaCliente = new Intent(MainActivity.this, ListaUsuariosActivity.class);

                startActivity(telaCliente);
            }
        });
    }


    public void enviaUsarioServidor(){
        JSONObject clienteJSON = new JSONObject();

        //montando o objeto JSON enviado no request
        try {
            clienteJSON.put("name", nomeTxt.getText().toString());
            clienteJSON.put("email",emailTxt.getText().toString());
            clienteJSON.put("gender",(String)generoSpinner.getSelectedItem());
            clienteJSON.put("status", ativoCheck.isSelected() ? "active" : "inactive");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String urlServico = "https://gorest.co.in/public/v2/users";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, urlServico,
                clienteJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respostaServidor) {
                        //esse metodo trabalha como uma "callback" TRATANDO o resultado
                        //enviado pelo servidor

                        if (respostaServidor.has("id")) {
                            Toast.makeText(MainActivity.this,
                                    "Cadastro com sucesso :)",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //o tratamento de erro da requisicao sera feito aqui
                        Toast.makeText(MainActivity.this,
                                "Deu ruim :x",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //configurando os parametros do cabecalho do header
                HashMap<String, String> valoresHeader = new HashMap<>();
                valoresHeader.put("Authorization", "Bearer b03a422c249cd2d6954d4650394329dbfde31edc48ec45b41e065da6f5ee1fa7");
                valoresHeader.put("Content-Type","application/JSON");
                return valoresHeader;
            }
        };

        //add a requisicao na fila de execucao do Volley
        RequestQueue filaRequ = Volley.newRequestQueue(MainActivity.this);
        filaRequ.add(req);

    }

}