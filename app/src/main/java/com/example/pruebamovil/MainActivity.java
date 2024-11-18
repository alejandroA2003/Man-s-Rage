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

public class MainActivity extends AppCompatActivity {


    private EditText email, pass;
    private Button ingresar;
    private Button registrarse;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.etEmail);
        pass = findViewById(R.id.etPass);
        ingresar = findViewById(R.id.btnIngresar);
        registrarse = findViewById(R.id.btnRegistro);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Correcto", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(MainActivity.this, Registroprendas.class);  // Redirigir a MainActivity2
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Autenticación falló", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(MainActivity.this, Registro.class);  // Redirigir a Registro
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Registro.class);  // Cambia a ActivityMain3
                startActivity(intent);
            }
        });
    }
}
