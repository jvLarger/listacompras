package com.example.listacompras;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listacompras.database.AppDatabase;
import com.example.listacompras.model.Item;
import com.example.listacompras.model.ListaComItens;
import com.example.listacompras.model.ListaDeCompras;
import com.example.listacompras.model.ListaItemCrossRef;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class NewListActivity extends AppCompatActivity {

    private EditText editTextListName;
    private LinearLayout itemsContainer;
    private TextView textViewTotal;
    private AppDatabase db;

    private final Item[] produtosFixos = new Item[]{
            new Item("Arroz 1 Kg", 2.69),
            new Item("Leite longa vida", 2.70),
            new Item("Carne Friboi", 16.70),
            new Item("Feijão carioquinha 1 Kg", 3.38),
            new Item("Refrigerante Coca-Cola 2L", 3.00)
    };

    private final Set<Item> itensSelecionados = new HashSet<>();
    private boolean modoEdicao = false;
    private int listaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_list);

        editTextListName = findViewById(R.id.editTextListName);
        itemsContainer = findViewById(R.id.itemsContainer);
        textViewTotal = findViewById(R.id.textViewTotal);
        Button buttonSave = findViewById(R.id.buttonSave);

        db = AppDatabase.getInstance(this);

        if (getIntent().hasExtra("lista_id")) {
            modoEdicao = true;
            listaId = getIntent().getIntExtra("lista_id", -1);
            carregarDadosParaEdicao();
        } else {
            montarListaProdutos();
        }

        buttonSave.setOnClickListener(v -> salvarLista());
    }

    private void montarListaProdutos() {
        itemsContainer.removeAllViews();
        for (Item item : produtosFixos) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item.nome + " (R$ " + String.format("%.2f", item.preco) + ")");
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    itensSelecionados.add(item);
                } else {
                    itensSelecionados.remove(item);
                }
                atualizarTotal();
            });
            itemsContainer.addView(checkBox);
        }
    }

    private void carregarDadosParaEdicao() {
        new Thread(() -> {
            ListaComItens listaComItens = db.listaDeComprasDao().getListaComItensPorId(listaId);
            runOnUiThread(() -> {
                editTextListName.setText(listaComItens.lista.nome);
                montarListaProdutos();

                for (int i = 0; i < produtosFixos.length; i++) {
                    Item itemFixado = produtosFixos[i];
                    for (Item itemSalvo : listaComItens.itens) {
                        if (itemFixado.nome.equals(itemSalvo.nome)) {
                            itensSelecionados.add(itemFixado);
                            CheckBox cb = (CheckBox) itemsContainer.getChildAt(i);
                            cb.setChecked(true);
                        }
                    }
                }

                atualizarTotal();
            });
        }).start();
    }

    private void atualizarTotal() {
        double total = 0.0;
        for (Item item : itensSelecionados) {
            total += item.preco;
        }
        textViewTotal.setText("Total: R$ " + String.format("%.2f", total));
    }

    private void salvarLista() {
        String nomeLista = editTextListName.getText().toString().trim();
        if (nomeLista.isEmpty()) {
            Toast.makeText(this, "Digite o nome da lista", Toast.LENGTH_SHORT).show();
            return;
        }

        String dataAtual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        new Thread(() -> {
            ListaDeCompras lista = new ListaDeCompras();
            lista.nome = nomeLista;
            lista.data = dataAtual;

            int idFinal;

            if (modoEdicao) {
                lista.id = listaId;
                db.listaDeComprasDao().deletarRelacionamentos(listaId);
                db.listaDeComprasDao().atualizarLista(lista);
                idFinal = listaId;
            } else {
                idFinal = (int) db.listaDeComprasDao().inserirLista(lista);
            }

            for (Item item : itensSelecionados) {
                Item existente = db.listaDeComprasDao().getItemPorNome(item.nome);
                long itemId;

                if (existente != null) {
                    itemId = existente.id;
                } else {
                    itemId = db.listaDeComprasDao().inserirItem(item);
                }

                ListaItemCrossRef ref = new ListaItemCrossRef();
                ref.listaId = idFinal;
                ref.itemId = (int) itemId;
                db.listaDeComprasDao().inserirRelacionamento(ref);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, modoEdicao ? "Lista editada com sucesso" : "Lista salva com sucesso", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NewListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}
