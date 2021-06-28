package com.kasai.cheatroulette.room;
/*
DAO(データベース操作の機能の集合)を使用するための機能の集合
 */

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

class MyRouletteRepository {

    private MyRouletteDao mMyRouletteDao;
    private LiveData<List<MyRoulette>> mAllMyRoulette;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    MyRouletteRepository(Application application) {
        MyRouletteRoomDatabase db = MyRouletteRoomDatabase.getDatabase(application);
        mMyRouletteDao = db.myRouletteDao();
        mAllMyRoulette = mMyRouletteDao.getAllMyRoulette();
    }

    // Room executes all queries on ic_cheat_roulette_launcher2_foreground separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<MyRoulette>> getAllMyRoulette() {
        return mAllMyRoulette;
    }

    MyRoulette getMyRoulette(int id) { return mMyRouletteDao.getMyRoulette(id); }

    // You must call this on ic_cheat_roulette_launcher2_foreground non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(MyRoulette myRoulette) {
        MyRouletteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMyRouletteDao.insert(myRoulette);
        });
    }

    void update(MyRoulette myRoulette) {
        MyRouletteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMyRouletteDao.update(myRoulette);
        });
    }

    void delete(int id) {
        MyRouletteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMyRouletteDao.delete(id);
        });
    }
}
