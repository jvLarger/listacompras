package com.example.listacompras.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.listacompras.model.Item;
import com.example.listacompras.model.ListaComItens;
import com.example.listacompras.model.ListaDeCompras;
import com.example.listacompras.model.ListaItemCrossRef;

import java.util.List;

@Dao
public interface ListaDeComprasDao {

    @Transaction
    @Query("SELECT * FROM ListaDeCompras")
    LiveData<List<ListaDeCompras>> getListasComItens();

    @Transaction
    @Query("SELECT * FROM ListaDeCompras WHERE id = :id")
    ListaComItens getListaComItensPorId(int id);

    @Query("SELECT * FROM ListaDeCompras ORDER BY id DESC")
    List<ListaDeCompras> getTodasListas();

    @Query("SELECT * FROM Item WHERE nome = :nome LIMIT 1")
    Item getItemPorNome(String nome);

    @Insert
    long inserirLista(ListaDeCompras lista);

    @Insert
    long inserirItem(Item item);

    @Insert
    void inserirRelacionamento(ListaItemCrossRef crossRef);

    @Delete
    void deletarLista(ListaDeCompras lista);

    @Query("DELETE FROM ListaItemCrossRef WHERE listaId = :listaId")
    void deletarRelacionamentos(int listaId);

    @Update
    void atualizarLista(ListaDeCompras lista);
}