package com.example.crudandroidphp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    EditText editNome, editTelefone, editEmail, editId;
    Button btnNovo, btnSalvar, btnExcluir;
    ListView listViewContatos;

    // private String HOST = "http://192.168.0.16";
    private String HOST = "http://env-9429261.jelastic.saveincloud.net";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editId = findViewById(R.id.editId);
        editNome = findViewById(R.id.editNome);
        editTelefone = findViewById(R.id.editTelefone);
        editEmail = findViewById(R.id.editEmail);

        btnNovo = findViewById(R.id.btnNovo);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnExcluir = findViewById(R.id.btnExcluir);

        listViewContatos= findViewById(R.id.listVewContato);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = editId.getText().toString();
                String nome = editNome.getText().toString();
                String telefone = editTelefone.getText().toString();
                String email = editEmail.getText().toString();

                String url = HOST + "/crud/create.php";

                if (nome.isEmpty()){
                    editNome.setError("O nome é obrigatório");
                } else if (id.isEmpty()){
                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("nome", nome)
                            .setBodyParameter("telefone", telefone)
                            .setBodyParameter("email", email)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (e == null && result != null) {
                                        if (result.get("create").getAsString().equals("ok")) {
                                            limparCampos();
                                            int idRetornado = Integer.parseInt(result.get("id").getAsString());
                                            Toast.makeText(MainActivity.this, "Salvo com sucesso, id: " + idRetornado, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Ocorreu erro na hora de gravar", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    public void limparCampos(){
        editId.setText(" ");
        editNome.setText(" ");
        editTelefone.setText(" ");
        editEmail.setText(" ");
        editNome.requestFocus();
    }
}