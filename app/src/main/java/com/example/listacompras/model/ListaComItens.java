package com.example.listacompras.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ListaComItens {
    @Embedded
    public ListaDeCompras lista;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(value = ListaItemCrossRef.class,
                    parentColumn = "listaId",
                    entityColumn = "itemId")
    )
    public List<Item> itens;
}