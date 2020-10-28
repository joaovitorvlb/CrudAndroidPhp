package com.example.crudandroidphp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editNome, editTelefone, editEmail, editId;
    Button btnNovo, btnSalvar, btnExcluir;
    ListView listViewContatos;
    ContatosAdapter contatosAdapter;
    ArrayList<Contato> lista;

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

        listaContatos();

        lista = new ArrayList<Contato>();
        contatosAdapter = new ContatosAdapter(MainActivity.this, lista);
        listViewContatos.setAdapter(contatosAdapter);

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
        

        listViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Contato contato = (Contato) adapterView.getAdapter().getItem(position);

                editId.setText(String.valueOf(contato.getId()));
                editNome.setText(contato.getNome());
                editTelefone.setText(contato.getTelefone());
                editEmail.setText(contato.getEmail());
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

    private void listaContatos(){
        String url = HOST + "/crud/read.php";
        Ion.with(getBaseContext())
                .load(url)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for(int i = 0; i < result.size(); i++){
                            JsonObject obj = result.get(i).getAsJsonObject();

                            Contato contato = new Contato();
                            contato.setId(obj.get("id").getAsInt());
                            contato.setNome(obj.get("nome").getAsString());
                            contato.setTelefone(obj.get("telefone").getAsString());
                            contato.setEmail(obj.get("email").getAsString());

                            lista.add(contato);
                        }
                        contatosAdapter.notifyDataSetChanged();
                    }
                });

    }
}