package com.cibertec.movil_modelo_proyecto_2022_2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class EditorialAdapter extends ArrayAdapter<Editorial>  {

    private Context context;
    private List<Editorial> lista;

    public EditorialAdapter(@NonNull Context context, int resource, @NonNull List<Editorial> lista) {
        super(context, resource, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.activity_edtorial_consulta_item, parent, false);

        Editorial obj = lista.get(position);

        TextView txtID = row.findViewById(R.id.idEditorialItemId);
        txtID.setText(String.valueOf(obj.getIdEditorial()));

        TextView  txtRazSoc = row.findViewById(R.id.idEditorialItemRazSoc);
        txtRazSoc.setText(String.valueOf(obj.getRazonSocial()));

        TextView  txtPais = row.findViewById(R.id.idEditorialItemPais);
        txtPais.setText(String.valueOf(obj.getPais().getNombre()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ruta ;
                    if (obj.getIdEditorial() == 1){
                        ruta = "https://i.postimg.cc/bv5nPxj9/etiquetanegra.jpg";
                    }else if (obj.getIdEditorial() == 2){
                        ruta = "https://i.postimg.cc/50FC2X4d/planeta.jpg";
                    }else{
                        ruta = "https://i.postimg.cc/gjF6XGjw/no-disponible.png";
                    }
                    URL rutaImagen  = new URL(ruta);
                    InputStream is = new BufferedInputStream(rutaImagen.openStream());
                    Bitmap b = BitmapFactory.decodeStream(is);
                    ImageView vista = row.findViewById(R.id.idEditorialItemImagen);
                    vista.setImageBitmap(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


        return row;
    }

}
