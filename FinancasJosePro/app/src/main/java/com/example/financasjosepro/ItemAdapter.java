package com.example.financasjosepro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.financasjosepro.modelo.Entrada;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Entrada> implements View.OnClickListener{

    //dados armazenados na lista (model)
    private ArrayList<Entrada> dataSet;
    //contexto original
    Context mContext;
    //pattern de data
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // dados do item (tipo UI)
    private static class ViewHolder {
        TextView nomeTxt;
        TextView tipoTxt;
        TextView valorTxt;
        TextView dataTxt;
    }

    public ItemAdapter(ArrayList<Entrada> data, Context context) {
        super(context, R.layout.item_layout, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Entrada dataModel=(Entrada)object;

        //dado disponivel
        //faca o que quiser com dataModel
    }

    private int ultimaPosicao = -1;

    @Override
    public View getView(int position, View novaView, ViewGroup parent) {
        // acessando a entrada da posicao a ser renderizada
        Entrada dataModel = getItem(position);
        // verificando a possibilidade de reutilizar uma view presente na lista
        ViewHolder viewHolder;

        final View resultado;

        if (novaView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            novaView = inflater.inflate(R.layout.item_layout_relative, parent, false);
            viewHolder.nomeTxt = novaView.findViewById(R.id.nomeItemTxt2);
            viewHolder.tipoTxt = novaView.findViewById(R.id.operacaoItemTxt2);
            viewHolder.valorTxt = novaView.findViewById(R.id.valorItemTxt2);
            viewHolder.dataTxt = novaView.findViewById(R.id.dataItemTxt2);

            novaView.setTag(viewHolder);
        } else {
            //caso em que o item exista
            viewHolder = (ViewHolder) novaView.getTag();
        }

        ultimaPosicao = position;



        viewHolder.nomeTxt.setText(dataModel.getNome());
        viewHolder.valorTxt.setText(dataModel.getValor()+"");
        viewHolder.tipoTxt.setText(dataModel.isOperacao()?"+":"-");
        viewHolder.dataTxt.setText(format.format(dataModel.getDataInicial()));
        // retorna a view preenchida
        return novaView;
    }
}
