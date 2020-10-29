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
    private int itemClicado;

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

                final String id = editId.getText().toString();
                final String nome = editNome.getText().toString();
                final String telefone = editTelefone.getText().toString();
                final String email = editEmail.getText().toString();

                if (nome.isEmpty()){
                    editNome.setError("O nome é obrigatório");
                } else if (id.isEmpty()){   // Se sim então é un novo contato

                    String url = HOST + "/crud/create.php";

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

                                            Contato contato  = new Contato();
                                            contato.setId(idRetornado);
                                            contato.setNome(nome);
                                            contato.setTelefone(telefone);
                                            contato.setEmail(email);

                                            lista.add(contato);

                                            contatosAdapter.notifyDataSetChanged();

                                            limparCampos();

                                            Toast.makeText(MainActivity.this, "Salvo com sucesso, id: " + idRetornado, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Ocorreu erro na hora de gravar", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                } else {    // Se não é a edicao de um contato

                    String url = HOST + "/crud/update.php";

                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("nome", nome)
                            .setBodyParameter("telefone", telefone)
                            .setBodyParameter("email", email)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (e == null && result != null && id == null) {
                                        if (result.get("update").getAsString().equals("ok")) {
                                            limparCampos();

                                            Contato contato  = new Contato();
                                            contato.setId(Integer.parseInt(id));
                                            contato.setNome(nome);
                                            contato.setTelefone(telefone);
                                            contato.setEmail(email);

                                            lista.set(itemClicado, contato);

                                            contatosAdapter.notifyDataSetChanged();

                                            limparCampos();

                                            Toast.makeText(MainActivity.this, "Editado com sucesso", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Ocorreu erro na hora de gravar", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });

        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id = editId.getText().toString();

                if (id.isEmpty()){
                    Toast.makeText(MainActivity.this, "Nenhum contato está selecionado", Toast.LENGTH_LONG).show();
                } else {    // Tentar apagar o contato

                    String url = HOST + "/crud/delete.php";

                    Ion.with(MainActivity.this)
                            .load(url)
                            .setBodyParameter("id", id)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (e == null && result != null) {
                                        if (result.get("delete").getAsString().equals("ok")) {
                                            limparCampos();

                                            lista.remove(itemClicado);

                                            contatosAdapter.notifyDataSetChanged();

                                            limparCampos();

                                            Toast.makeText(MainActivity.this, "Deletado com sucesso", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Ocorreu erro na hora de deletar", Toast.LENGTH_LONG).show();
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

                itemClicado = position;
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