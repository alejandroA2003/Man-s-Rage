package com.example.pruebamovil;
import com.example.pruebamovil.Pojo.Modelo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ModeloAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Modelo> listModelos;

    public ModeloAdapter(Context context, ArrayList<Modelo> listModelos) {
        this.context = context;
        this.listModelos = listModelos;
    }

    @Override
    public int getCount() {
        return listModelos.size();
    }

    @Override
    public Object getItem(int position) {
        return listModelos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_modelo, parent, false);
        }

        Modelo modelo = listModelos.get(position);

        TextView tvId = convertView.findViewById(R.id.tvId);
        TextView tvNombre = convertView.findViewById(R.id.tvNombre);

        tvId.setText(String.valueOf(modelo.getId()));
        tvNombre.setText(modelo.getNombre());

        return convertView;
    }
}
