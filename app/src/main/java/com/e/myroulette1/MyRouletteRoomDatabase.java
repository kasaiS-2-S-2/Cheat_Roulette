package com.e.myroulette1;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.  In a real
 * app, consider exporting the schema to help you with migrations.
 */

@Database(entities = {MyRoulette.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
abstract class MyRouletteRoomDatabase extends RoomDatabase {

    abstract MyRouletteDao myRouletteDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile MyRouletteRoomDatabase INSTANCE;
    //なんで４？
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MyRouletteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRouletteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyRouletteRoomDatabase.class, "myRoulette_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                MyRouletteDao dao = INSTANCE.myRouletteDao();
                dao.deleteAll();


                String rouletteName = "１";
                String date = "2000";
                ArrayList<String> words = new ArrayList<String>();
                words.add("ああああ");
                words.add("qqqq");
                ArrayList<Integer> colorsInfo = new ArrayList<Integer>();
                colorsInfo.add(Color.parseColor("#FFFF0000"));
                colorsInfo.add(Color.parseColor("#FF80FF00"));
                colorsInfo.add(Color.parseColor("#FF0000FF"));
                ArrayList<String> itemNamesInfo = new ArrayList<String>();
                itemNamesInfo.add("寿司");
                itemNamesInfo.add("天丼");
                itemNamesInfo.add("焼き肉");
                ArrayList<Integer> itemRatiosInfo = new ArrayList<Integer>();
                itemRatiosInfo.add(1);
                itemRatiosInfo.add(1);
                itemRatiosInfo.add(1);
                ArrayList<Integer> OnOffOfSwitch100Info = new ArrayList<Integer>();
                OnOffOfSwitch100Info.add(0);
                OnOffOfSwitch100Info.add(0);
                OnOffOfSwitch100Info.add(0);
                ArrayList<Integer> OnOffOfSwitch0Info = new ArrayList<Integer>();
                OnOffOfSwitch0Info.add(0);
                OnOffOfSwitch0Info.add(0);
                OnOffOfSwitch0Info.add(0);
                ArrayList<Float> itemProbabilitiesInfo = new ArrayList<Float>();
                itemProbabilitiesInfo.add(33.33f);
                itemProbabilitiesInfo.add(33.33f);
                itemProbabilitiesInfo.add(33.33f);


                MyRoulette myRoulette = new MyRoulette(rouletteName, date, colorsInfo, itemNamesInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
                dao.insert(myRoulette);


            });
        }
    };
}
