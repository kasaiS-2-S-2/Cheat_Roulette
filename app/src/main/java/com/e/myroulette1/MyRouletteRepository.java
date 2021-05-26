package com.e.myroulette1;
/*
DAO(データベース操作の機能の集合)を使用するための機能の集合
 */

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

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<MyRoulette>> getAllMyRoulette() {
        return mAllMyRoulette;
    }

    MyRoulette getMyRoulette(int id) { return mMyRouletteDao.getMyRoulette(id); }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
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