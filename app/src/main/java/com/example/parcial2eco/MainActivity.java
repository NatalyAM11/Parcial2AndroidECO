package com.example.parcial2eco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase db;

    private TextView pregunta;
    private EditText puntaje;
    private Button bListo;
    private  Pregunta p;
    private String key;
    private int total, totalP;
    private DataSnapshot d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=FirebaseDatabase.getInstance();

        pregunta=findViewById(R.id.pregunta);
        puntaje=findViewById(R.id.puntaje);
        bListo=findViewById(R.id.bListo);



        bListo.setOnClickListener(this);

        loadData();
        Log.e(">>>", String.valueOf(total));
    }


    public void loadData(){

        db.getReference().child("preguntas").child("actual").addValueEventListener(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot data) {
                        d=data;

                        //Busco si hay pregunta actual o no
                        for(DataSnapshot child: data.getChildren()){
                            Pregunta pr=child.getValue(Pregunta.class);
                            key=child.getKey();
                            p=pr;
                        }

                        //Si no hay pregunta actual deja un texto predeterminado, pero si si hay pregunta actual, pinta lo que dice esta
                        if(data.getValue()==null){
                            pregunta.setText(("Esperando la pregunta de la base de datos...."));
                        }else{
                            pregunta.setText(p.getPregunta());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                }
        );


    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bListo:

                String pun=puntaje.getText().toString();

                //Verifico de que exista una pregunta actual para que pueda votar, si no hay pues no puede
                if(d.getValue()==null){
                    runOnUiThread( ()-> Toast.makeText(this, "Aún no hay pregunta, no puede votar", Toast.LENGTH_LONG).show());
                    return;
                }

                //valido que no pueda calificar hasta que ingrese un numero
                if(pun.trim().isEmpty() ){
                    runOnUiThread( ()-> Toast.makeText(this, " Debe ingresar la calificación primero", Toast.LENGTH_LONG).show());
                    return;
                }

                //envio el valor de la calificacion que ingreso a la rama de calificaciones
                DatabaseReference reference=db.getReference().child("calificaciones").child(key).push();
                Calificacion cali=new Calificacion(pun);

                //envio los puntajes ingresados a una rama con el mismo id que la pregunta
                reference.setValue(cali);

                //El total del puntaje de esa pregunta que se encuentra en firebase
                int puntajeF=p.getPuntaje();
                int puntajeInt= Integer.parseInt(pun);

                //traigo el resultado de la suma de todas las votaciones, y lo sumo con lo que ingrese el usuario
                total= puntajeF+puntajeInt;

                //Hago un conteo del total de personas que han respondido la pregunta actual
                int Tpersonas=p.getTotalPersonas();
                totalP=Tpersonas+1;

                sumarPuntaje();

                break;

        }
    }


       private void sumarPuntaje(){

        //Envio los datos nuevos al firebase
           DatabaseReference ref= db.getReference().child("preguntas").child("actual").child(key);
           p.setPuntaje(total);
           p.setTotalPersonas(totalP);
           ref.setValue(p);
    }

}