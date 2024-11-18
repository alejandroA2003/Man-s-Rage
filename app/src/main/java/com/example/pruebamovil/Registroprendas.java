package com.example.pruebamovil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pruebamovil.Pojo.Modelo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import java.util.ArrayList;

public class Registroprendas extends AppCompatActivity {

    private EditText txtId, txtNombre;
    private Button btnBuscar, btnAgregar, btnEliminar, btnModificar;
    private ListView lvListModelos;
    private ArrayList<Modelo> listModelos;
    private ModeloAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_prendas);

        txtId = findViewById(R.id.etIdModelo);
        txtNombre = findViewById(R.id.etNombreModelo);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);
        lvListModelos = findViewById(R.id.lvListModelos);

        listModelos = new ArrayList<>();
        adaptador = new ModeloAdapter(this, listModelos);
        lvListModelos.setAdapter(adaptador);

        Buscar();
        Modificar();
        Eliminar();
        Agregar();
        ListarModelos();
    }

    private void Agregar() {
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtId.getText().toString().trim().isEmpty() || txtNombre.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registroprendas.this, "Complete los campos faltantes", Toast.LENGTH_LONG).show();
                } else {
                    int id = Integer.parseInt(txtId.getText().toString());
                    String nombre = txtNombre.getText().toString();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Modelo.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            boolean idExiste = false;
                            boolean nombreExiste = false;

                            for (DataSnapshot x : snapshot.getChildren()) {
                                if (x.child("id").getValue().toString().equalsIgnoreCase(String.valueOf(id))) {
                                    idExiste = true;
                                    break;
                                }
                                if (x.child("nombre").getValue().toString().equalsIgnoreCase(nombre)) {
                                    nombreExiste = true;
                                    break;
                                }
                            }

                            if (idExiste) {
                                Toast.makeText(Registroprendas.this, "Este ID ya existe", Toast.LENGTH_LONG).show();
                            } else if (nombreExiste) {
                                Toast.makeText(Registroprendas.this, "Este Nombre ya existe", Toast.LENGTH_LONG).show();
                            } else {
                                Modelo modelo = new Modelo(id, nombre);
                                dbref.push().setValue(modelo);
                                Toast.makeText(Registroprendas.this, "Modelo agregado!!", Toast.LENGTH_LONG).show();
                                txtId.setText("");
                                txtNombre.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(Registroprendas.this, "Error al conectar con Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void ListarModelos() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Modelo.class.getSimpleName());

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Modelo modelo = snapshot.getValue(Modelo.class);
                listModelos.add(modelo);
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        lvListModelos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Modelo modelo = listModelos.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(Registroprendas.this);
                builder.setCancelable(true)
                        .setTitle("Modelo seleccionado")
                        .setMessage("Id: " + modelo.getId() + "\nNombre: " + modelo.getNombre())
                        .show();
            }
        });
    }

    private void Buscar() {
        btnBuscar.setOnClickListener(v -> {
            String idText = txtId.getText().toString();
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference dbref = db.getReference(Modelo.class.getSimpleName());

            dbref.orderByChild("id").equalTo(Integer.parseInt(idText)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Modelo modelo = child.getValue(Modelo.class);
                            txtId.setText(String.valueOf(modelo.getId()));
                            txtNombre.setText(modelo.getNombre());
                        }
                    } else {
                        Toast.makeText(Registroprendas.this, "Modelo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            });
        });
    }

    private void Modificar() {
        btnModificar.setOnClickListener(v -> {
            String idText = txtId.getText().toString();
            String nombre = txtNombre.getText().toString();

            if (!idText.isEmpty() && !nombre.isEmpty()) {
                int id = Integer.parseInt(idText);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference(Modelo.class.getSimpleName());

                dbref.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().child("nombre").setValue(nombre);
                        }
                        Toast.makeText(Registroprendas.this, "Modelo modificado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
            }
        });
    }

    private void Eliminar() {
        btnEliminar.setOnClickListener(v -> {
            String idText = txtId.getText().toString();

            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference dbref = db.getReference(Modelo.class.getSimpleName());

                dbref.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            child.getRef().removeValue();
                        }
                        Toast.makeText(Registroprendas.this, "Modelo eliminado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
            }
        });
    }
}
