package com.kasai.cheatroulette;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyRouletteDao {
    // LiveData is ic_cheat_roulette_launcher2_foreground data holder class that can be observed within ic_cheat_roulette_launcher2_foreground given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * FROM myRoulette_table")
    LiveData<List<MyRoulette>> getAllMyRoulette();

    @Query("SELECT * FROM myRoulette_table WHERE ID = :id")
    MyRoulette getMyRoulette(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MyRoulette myRoulette);

    @Update
    void update(MyRoulette myRoulette);

    //指定primaryKey(id)のデータを削除する
    @Query("DELETE FROM myRoulette_table WHERE ID = :id")
    void delete(int id);

    @Query("DELETE FROM myRoulette_table")
    void deleteAll();
}
