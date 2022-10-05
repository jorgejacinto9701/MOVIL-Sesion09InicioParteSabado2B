package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Bundle;
import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cibertec.movil_modelo_proyecto_2022_2.entity.Editorial;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceEditorial;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorialRegistraActivity extends NewAppCompatActivity {

    Spinner spnPais;
    ArrayAdapter<String> adaptador;
    ArrayList<String> paises = new ArrayList<String>();

    //Servicio
    ServiceEditorial serviceEditorial;
    ServicePais servicePais;

    //Componentes
    EditText txtRaz, txtDir, txtRuc, txtFec;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_registra);

        txtRaz = findViewById(R.id.txtRegEdiRazonSocial);
        txtDir = findViewById(R.id.txtRegEdiDirecccion);
        txtRuc = findViewById(R.id.txtRegEdiRuc);
        txtFec = findViewById(R.id.txtRegEdiFechaCreacion);
        btnRegistrar = findViewById(R.id.btnRegEdiEnviar);

        //para conectar al servicio rest
        serviceEditorial = ConnectionRest.getConnection().create(ServiceEditorial.class);
        servicePais = ConnectionRest.getConnection().create(ServicePais.class);

        //Para el adapatador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnPais = findViewById(R.id.spnRegEdiPais);
        spnPais.setAdapter(adaptador);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String raz = txtRaz.getText().toString();
                String dir = txtDir.getText().toString();
                String ruc = txtRuc.getText().toString();
                String fec = txtFec.getText().toString();

                if (!raz.matches(ValidacionUtil.TEXTO)){
                    mensajeToastLong("La razón Social es de 2 a 20 caracteres");
                }else if (!dir.matches(ValidacionUtil.DIRECCION)){
                    mensajeToastLong("La dirección es de 3 a 20 caracteres");
                }else if (!ruc.matches(ValidacionUtil.RUC)){
                    mensajeToastLong("El ruc es de 11 dígitos");
                }else if (!fec.matches(ValidacionUtil.FECHA)){
                    mensajeToastLong("La fecha es de formato YYYY-MM-dd");
                }else{
                    String pais = spnPais.getSelectedItem().toString();
                    String idPais = pais.split(":")[0];

                    Pais objPais = new Pais();
                    objPais.setIdPais(Integer.parseInt(idPais));

                    Editorial objEditorial = new Editorial();
                    objEditorial.setRazonSocial(raz);
                    objEditorial.setDireccion(dir);
                    objEditorial.setRuc(ruc);
                    objEditorial.setFechaCreacion(fec);
                    objEditorial.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    objEditorial.setEstado(1);
                    objEditorial.setPais(objPais);

                    registra(objEditorial);
                }

            }
        });


        cargaPais();
    }

    public void registra(Editorial obj){
        Call<Editorial> call = serviceEditorial.insertaEditorial(obj);
        call.enqueue(new Callback<Editorial>() {
            @Override
            public void onResponse(Call<Editorial> call, Response<Editorial> response) {
                if (response.isSuccessful()){
                    Editorial objSalida =   response.body();
                    mensajeAlert("Se registro la Editorial " +
                            "\nID >> " + objSalida.getIdEditorial() +
                            "\nRazón Social >> " + objSalida.getRazonSocial() );
                }
            }
            @Override
            public void onFailure(Call<Editorial> call, Throwable t) {
                mensajeToastLong("Error al acceder al Servicio Rest >>> " + t.getMessage());
            }
        });
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