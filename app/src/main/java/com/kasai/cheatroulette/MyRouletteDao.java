package com.kasai.cheatroulette;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain ic_cheat_roulette_launcher2_foreground copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * The Room Magic is in this file, where you map ic_cheat_roulette_launcher2_foreground Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

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
