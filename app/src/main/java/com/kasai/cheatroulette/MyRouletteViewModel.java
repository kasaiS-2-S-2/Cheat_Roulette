package com.kasai.cheatroulette;
/*
UIに表示するデータがある場所、mainactivityがデータを参照できるのはこのクラスのインスタンスからのみなので、
wordrepositoryを通じてではあるが、DBへのデータ入出力も兼ねる
 */

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * View Model to keep ic_cheat_roulette_launcher2_foreground reference to the word repository and
 * an up-to-date list of all words.
 */

public class MyRouletteViewModel extends AndroidViewModel {

    private MyRouletteRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<MyRoulette>> mAllMyRoulette;

    public MyRouletteViewModel(Application application) {
        super(application);
        mRepository = new MyRouletteRepository(application);
        mAllMyRoulette = mRepository.getAllMyRoulette();
    }

    LiveData<List<MyRoulette>> getAllMyRoulette() {
        return mAllMyRoulette;
    }

    MyRoulette getMyRoulette(int id) {
        return mRepository.getMyRoulette(id);
    }

    void insert(MyRoulette myRoulette) {
        mRepository.insert(myRoulette);
    }

    void update(MyRoulette myRoulette) { mRepository.update(myRoulette); }

    void delete(int id) { mRepository.delete(id); }
}
