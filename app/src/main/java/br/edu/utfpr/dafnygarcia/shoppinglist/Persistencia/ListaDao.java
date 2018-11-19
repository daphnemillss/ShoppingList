package br.edu.utfpr.dafnygarcia.shoppinglist.Persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.edu.utfpr.dafnygarcia.shoppinglist.Modelo.Lista;

@Dao
public interface ListaDao {
    @Insert
    long insert(Lista lista);

    @Delete
    void delete(Lista lista);

    @Update
    void update(Lista lista);

    @Query("SELECT * FROM lista WHERE idLista = :id")
    Lista queryForId(long id);


    @Query("SELECT * FROM lista ORDER BY nome ASC")
    List<Lista> queryAll();
}