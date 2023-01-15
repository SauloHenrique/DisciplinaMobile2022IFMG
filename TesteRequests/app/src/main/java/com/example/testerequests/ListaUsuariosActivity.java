package com.example.testerequests;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaUsuariosActivity extends AppCompatActivity {

    private ArrayList<String> nomesUsuarios;

    private ListView listausuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        listausuarios = findViewById(R.id.usuariosList);

        nomesUsuarios = new ArrayList<>();

        consultaUsuarios();
    }

    private void consultaUsuarios(){
        String urlServico = "https://gorest.co.in/public/v2/users";

        JsonArrayRequest requ = new JsonArrayRequest(Request.Method.GET,
                urlServico, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray respostaServidor) {
                //será que cliente para inserir na lista?
                if (respostaServidor.length() > 0) {
                    for (int i = 0; i < respostaServidor.length(); i++) {
                        try {
                            JSONObject clienteI = respostaServidor.getJSONObject(i);

                            nomesUsuarios.add(clienteI.getString("name"));

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<String>(ListaUsuariosActivity.this,
                                            android.R.layout.simple_list_item_1, nomesUsuarios);

                            listausuarios.setAdapter(adapter);

                        } catch (JSONException e) {
                            Toast.makeText(ListaUsuariosActivity.this,
                                    "erro no servidor tente novamente",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListaUsuariosActivity.this,"Erro na requisição", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> cabecalho = new HashMap<>();

                cabecalho.put("Authorization","Bearer b03a422c249cd2d6954d4650394329dbfde31edc48ec45b41e065da6f5ee1fa7");
                return cabecalho;
            }
        };

        RequestQueue filaResquest = Volley.newRequestQueue(ListaUsuariosActivity.this);
        filaResquest.add(requ);
    }
}