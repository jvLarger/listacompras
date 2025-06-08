package com.example.listacompras;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.listacompras.database.AppDatabase;
import com.example.listacompras.model.Item;
import com.example.listacompras.model.ListaComItens;
import com.example.listacompras.model.ListaDeCompras;

public class ListaDetalhesActivity extends AppCompatActivity {

    private TextView nomeLista, dataLista, totalLista;
    private LinearLayout containerItens;
    private AppDatabase db;
    private int listaId;
    private ListaDeCompras lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_detalhes);

        nomeLista = findViewById(R.id.textViewNomeLista);
        dataLista = findViewById(R.id.textViewDataLista);
        totalLista = findViewById(R.id.textViewTotalLista);
        containerItens = findViewById(R.id.containerItens);

        db = AppDatabase.getInstance(this);

        listaId = getIntent().getIntExtra("lista_id", -1);

        carregarDados();

        findViewById(R.id.buttonExcluir).setOnClickListener(v -> excluirLista());
        findViewById(R.id.buttonEditar).setOnClickListener(v -> {
            Intent intent = new Intent(ListaDetalhesActivity.this, NewListActivity.class);
            intent.putExtra("lista_id", listaId);
            startActivity(intent);
        });
    }

    private void carregarDados() {
        new Thread(() -> {
            ListaComItens listaComItens = db.listaDeComprasDao().getListaComItensPorId(listaId);
            lista = listaComItens.lista;

            runOnUiThread(() -> {
                nomeLista.setText(lista.nome);
                dataLista.setText(lista.data);

                double total = 0;
                for (Item item : listaComItens.itens) {
                    TextView itemView = new TextView(this);
                    itemView.setText("- " + item.nome + " (R$ " + String.format("%.2f", item.preco) + ")");
                    containerItens.addView(itemView);
                    total += item.preco;
                }

                totalLista.setText("Total: R$ " + String.format("%.2f", total));
            });
        }).start();
    }

    private void excluirLista() {
        new Thread(() -> {
            db.listaDeComprasDao().deletarRelacionamentos(listaId);
            db.listaDeComprasDao().deletarLista(lista);

            runOnUiThread(() -> {
                Toast.makeText(this, "Lista excluída", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
