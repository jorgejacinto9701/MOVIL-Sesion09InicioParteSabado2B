package com.cibertec.movil_modelo_proyecto_2022_2.vista.crud;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceEditorial;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorialCrudFormularioActivity extends NewAppCompatActivity {

    TextView txtTitulo;
    Button  btnEnviar;

    Spinner spnPais;
    ArrayAdapter<String> adaptador;
    ArrayList<String> paises = new ArrayList<String>();

    //Servicio
    ServiceEditorial serviceEditorial;
    ServicePais servicePais;

    //Componentes
    EditText txtRaz, txtDir, txtRuc, txtFec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_crud_formulario);

        txtRaz = findViewById(R.id.txtCrudFrmEdiRazonSocial);
        txtDir = findViewById(R.id.txtCrudFrmEdiDirecccion);
        txtRuc = findViewById(R.id.txtCrudFrmEdiRuc);
        txtFec = findViewById(R.id.txtCrudFrmFechaCreacion);

        txtTitulo = findViewById(R.id.txtCrudFrmEditorialTitulo);
        btnEnviar  = findViewById(R.id.btnCrudFrmEdiEnviar);

        //Para el adapatador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnCrudFrmRegEdiPais);
        spnPais.setAdapter(adaptador);

        //para conectar al servicio rest
        serviceEditorial = ConnectionRest.getConnection().create(ServiceEditorial.class);
        servicePais = ConnectionRest.getConnection().create(ServicePais.class);

        //Carga los paises
        cargaPais();

        Bundle extras = getIntent().getExtras();
        String tipo = extras.getString("var_tipo");
        if (tipo.equals("REGISTRAR")){
            txtTitulo.setText("Mantenimiento Editorial - REGISTRA");
            btnEnviar.setText("Registrar");
        }else  if (tipo.equals("ACTUALIZAR")){
            txtTitulo.setText("Mantenimiento Editorial - ACTUALIZA");
            btnEnviar.setText("Actualizar");

            Editorial objEditorial = (Editorial) extras.get("var_item");

            txtRaz.setText(objEditorial.getRazonSocial());
            txtDir.setText(objEditorial.getDireccion());
            txtRuc.setText(objEditorial.getRuc());
            txtFec.setText(objEditorial.getFechaCreacion());

            //int pos = posPais(objEditorial.getPais());

            //mensajeAlert("pos>> " + pos);
            //spnPais.setSelection(10);
        }
    }

    public int posPais(Pais obj){
        String opcion = obj.getIdPais()+":" + obj.getNombre();
        mensajeAlert("opcion>> " + opcion);
        for(int i=0; i< paises.size(); i++){
            if (paises.get(i).equalsIgnoreCase(opcion)){
                return i;
            }
        }
        return -1;
    }
    public void cargaPais(){
        Call<List<Pais>> call = servicePais.listaPais();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()){
                    List<Pais> lstPaises =  response.body();
                    for(Pais obj: lstPaises){
                        paises.add(obj.getIdPais() +":"+ obj.getNombre());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToastLong("Error al acceder al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
    }

}
