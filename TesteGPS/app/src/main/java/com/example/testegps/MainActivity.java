package com.example.testegps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;

import com.example.testegps.ferramentas.ControlaGPS;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {

    private TextView posicaoTexto;
    private MapView mapa;
    private IMapController controllerMapa;

    private ControlaGPS gpsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posicaoTexto = findViewById(R.id.posicaoTxt);
        mapa = findViewById(R.id.mapa);

        requisitaPermissoes();

        //acessamos a instancia do controla GPS
        gpsController = ControlaGPS.getInstance(MainActivity.this);
        //a partir de agora o texto será atualizado a cada nova posicao do usuario
        gpsController.atualizarCampoTexto(posicaoTexto);
        gpsController.atualizaGPS();

        configuraMapaOSM();

    }

    private void configuraMapaOSM(){
        Configuration.getInstance().setUserAgentValue("saulocabral");

        //uso de zoom com mult. dedos
        mapa.setMultiTouchControls(true);

        controllerMapa = mapa.getController();
        controllerMapa.animateTo(new GeoPoint(gpsController.getLat(),gpsController.getLng()));
        controllerMapa.zoomTo(18,1L);
        gpsController.addControleMapa(controllerMapa);
    }

    public void requisitaPermissoes(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE},0);
    }


}