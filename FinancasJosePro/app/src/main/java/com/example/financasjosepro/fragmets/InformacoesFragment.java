package com.example.financasjosepro.fragmets;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.financasjosepro.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformacoesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformacoesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView saldoTotalTxt;
    private TextView saldoSaidaTxt;
    private TextView saldoEntradaTxt;

    public InformacoesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformacoesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformacoesFragment newInstance(String param1, String param2) {
        InformacoesFragment fragment = new InformacoesFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // fragment é a parte grafica inserida na Activity
        View fragment = inflater.inflate(R.layout.fragment_informacoes, container, false);

        /*
        fragment foi "inflado" dentro da Activity, então os componentes JÁ estão na memoria
        (foram instanciados) então a busca deve ser a partir da view inflada
        * */
        saldoTotalTxt = fragment.findViewById(R.id.saldoTxt);
        saldoSaidaTxt = fragment.findViewById(R.id.totalSaidaTxt);
        saldoEntradaTxt = fragment.findViewById(R.id.totalEntradasTxt);

        return fragment;
    }

    public void setSaldoTotal(double novoValor){
        saldoTotalTxt.setText(novoValor+"");
    }


}