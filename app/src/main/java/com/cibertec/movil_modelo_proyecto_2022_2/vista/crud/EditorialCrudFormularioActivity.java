package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.os.Bundle;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

public class EditorialCrudFormularioActivity extends NewAppCompatActivity {

    TextView txtTitulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_crud_formulario);

        txtTitulo = findViewById(R.id.txtCrudFrmEditorialTitulo);

        Bundle extras = getIntent().getExtras();
        String tipo = extras.getString("var_tipo");
        if (tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Editorial - REGISTRA");
        }else  if (tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Editorial - ACTUALIZA");
        }
    }


}
