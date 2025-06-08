package com.example.listacompras.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listacompras.ListaDetalhesActivity;
import com.example.listacompras.R;
import com.example.listacompras.model.ListaDeCompras;

import java.util.List;

public class ListaComprasAdapter extends RecyclerView.Adapter<ListaComprasAdapter.ViewHolder> {
    private List<ListaDeCompras> listas;

    public ListaComprasAdapter(List<ListaDeCompras> listas) {
        this.listas = listas;
    }

    public void setListas(List<ListaDeCompras> novasListas) {
        this.listas = novasListas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.example.listacompras.R.layout.item_lista_compras, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListaDeCompras lista = listas.get(position);
        holder.nome.setText(lista.nome);
        holder.data.setText(lista.data);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ListaDetalhesActivity.class);
            intent.putExtra("lista_id", lista.id);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listas != null ? listas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome, data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeListaTextView);
            data = itemView.findViewById(R.id.dataListaTextView);
        }
    }
}
