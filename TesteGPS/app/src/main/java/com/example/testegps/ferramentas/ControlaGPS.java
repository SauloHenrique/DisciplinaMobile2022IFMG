package com.example.testegps.ferramentas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class ControlaGPS implements LocationListener {

    //ultima posicao do usuario
    private double lat;
    private double lng;

    private Context contexto;

    private static ControlaGPS instancia;

    private LocationManager gerenciadorEventosGPS;

    public ControlaGPS(Context contexto) {
        this.contexto = contexto;
    }

    public ControlaGPS getInstance(Context contexto) {
        //sera que eu não criei um outro controla GPS antes?
        if (instancia == null) {
            instancia = new ControlaGPS(contexto);
        }

        return instancia;
    }

    public void atualizaGPS() {
        configuraGPS();
    }

    public void configuraGPS() {
        gerenciadorEventosGPS = (LocationManager) contexto.getSystemService(Context.LOCATION_SERVICE);

        Criteria confLocal = new Criteria();
        confLocal.setAccuracy(Criteria.ACCURACY_COARSE);

        //sera que o usuario perm. o acesso a local.
        if (ActivityCompat.checkSelfPermission(contexto,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(contexto,"Sem permissão do acesso a gps",Toast.LENGTH_SHORT).show();
            return;
        }

        Location ultimaLocal = gerenciadorEventosGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        onLocationChanged(ultimaLocal);

        gerenciadorEventosGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,0,this);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

}
