package br.edu.utfpr.dafnygarcia.shoppinglist.Persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.edu.utfpr.dafnygarcia.shoppinglist.Modelo.Produto;

@Dao
public interface ProdutoDao {
    @Insert
    long insert(Produto produto);

    @Delete
    void delete(Produto produto);

    @Update
    void update(Produto produto);

    @Query("SELECT * FROM produtos WHERE idProduto = :id")
    Produto queryForId(long id);


    @Query("SELECT * FROM produtos ORDER BY nome ASC")
    List<Produto> queryAll();
}

