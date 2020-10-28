package com.example.crudandroidphp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContatosAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Contato> lista;

    public ContatosAdapter(Context context1, ArrayList<Contato> lista1){
        this.context = context1;
        this.lista = lista1;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contato contato = lista.get(position);
        View layout;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.item_lista, null);
        }
        else{
            layout = convertView;
        }


        TextView itemNome = (TextView) layout.findViewById(R.id.eeditNome);
        itemNome.setText(contato.getNome());

        TextView itemTelefone = (TextView) layout.findViewById(R.id.eeditTelefone);
        itemTelefone.setText(contato.getTelefone());

        TextView itemEmail = (TextView) layout.findViewById(R.id.eeditEmail);
        itemEmail.setText(contato.getEmail());

        return layout;
    }
}
