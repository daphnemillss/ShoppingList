package br.edu.utfpr.dafnygarcia.shoppinglist.Modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "listaHasProduto",
        foreignKeys = {@ForeignKey(entity = Produto.class,
                                    parentColumns = "idProduto",
                                    childColumns  = "idProduto"),
                      @ForeignKey(entity = Lista.class,
                                    parentColumns = "idLista",
                                    childColumns  = "idLista")})

public class ListaHasProduto {
    @ColumnInfo(index = true)
    private int idProduto;

    @ColumnInfo(index = true)
    private int idLista;


    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }


}
