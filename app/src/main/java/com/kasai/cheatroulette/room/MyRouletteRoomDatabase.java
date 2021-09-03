package com.kasai.cheatroulette.room;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kasai.cheatroulette.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MyRoulette.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
abstract class MyRouletteRoomDatabase extends RoomDatabase {

    abstract MyRouletteDao myRouletteDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile MyRouletteRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MyRouletteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyRouletteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyRouletteRoomDatabase.class, context.getString(R.string.name_my_roulette_database))
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


                String rouletteName = "今日のディナー";
                String date = "xxxx/yy/zz";
                ArrayList<Integer> colorsInfo =
                        new ArrayList<Integer>(Arrays.asList(Color.parseColor("#FFEA5555"),
                                Color.parseColor("#FFF39C3C"), Color.parseColor("#FFECD03F"),
                                Color.parseColor("#FF6EB35E"), Color.parseColor("#FF4996C8"),
                                Color.parseColor("#FF774ED8")));

                ArrayList<String> itemNamesInfo = new ArrayList<String>(Arrays.asList("お寿司", "焼き肉", "ラーメン", "コンビニ", "串かつ", "中華"));

                ArrayList<Integer> itemRatiosInfo = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1));

                ArrayList<Integer> OnOffOfSwitch100Info = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

                ArrayList<Integer> OnOffOfSwitch0Info = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

                ArrayList<Float> itemProbabilitiesInfo = new ArrayList<Float>();

                MyRoulette defaultMyRoulette1 =
                        new MyRoulette(rouletteName, date, colorsInfo, itemNamesInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
                dao.insert(defaultMyRoulette1);



                String rouletteName2 = "罰ゲーム誰がやる？";
                String date2 = "xxxx/yy/zz";
                ArrayList<Integer> colorsInfo2 =
                        new ArrayList<Integer>(Arrays.asList(Color.parseColor("#FF631C48"),
                                Color.parseColor("#FF9F265C"), Color.parseColor("#FFCF2967"),
                                Color.parseColor("#FFF15B67"), Color.parseColor("#FFFDB25F"),
                                Color.parseColor("#FFFED883")));

                ArrayList<String> itemNamesInfo2 = new ArrayList<String>(Arrays.asList("田中", "佐藤", "フィリップ", "櫻井", "小池", "鈴木"));

                ArrayList<Integer> itemRatiosInfo2 = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1));

                ArrayList<Integer> OnOffOfSwitch100Info2 = new ArrayList<Integer>(Arrays.asList(0, 0, 1, 0, 0, 0));

                ArrayList<Integer> OnOffOfSwitch0Info2 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0, 0));

                ArrayList<Float> itemProbabilitiesInfo2 = new ArrayList<Float>(Arrays.asList(0f, 0f, 100f, 0f, 0f, 0f));

                MyRoulette defaultMyRoulette2 =
                        new MyRoulette(rouletteName2, date2, colorsInfo2, itemNamesInfo2, itemRatiosInfo2, OnOffOfSwitch100Info2, OnOffOfSwitch0Info2, itemProbabilitiesInfo2);
                dao.insert(defaultMyRoulette2);


            });
        }
    };
}
