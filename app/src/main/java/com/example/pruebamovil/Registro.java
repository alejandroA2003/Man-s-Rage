package com.example.pruebamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends AppCompatActivity {

    EditText emailR, passR;
    Button registrar;
    FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Inicializar las vistas
        emailR = findViewById(R.id.etEmailRegistro);
        passR = findViewById(R.id.etPassRegistro);
        registrar = findViewById(R.id.btnRegistrar);


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailR.getText().toString().trim();
                String password = passR.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese todos los campos", Toast.LENGTH_LONG).show();
                    return;
                }

                // Intentar registrar al usuario con los datos proporcionados
                firebaseauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Registro.this, MainActivity.class);
                                    startActivity(i);
                                    finish(); // Finaliza la actividad de registro
                                } else {

                                    Toast.makeText(getApplicationContext(), "Falló el registro", Toast.LENGTH_LONG).show();
                                    emailR.setText(""); // Limpiar los campos
                                    passR.setText("");
                                }
                            }
                        });
            }
        });
    }
}
