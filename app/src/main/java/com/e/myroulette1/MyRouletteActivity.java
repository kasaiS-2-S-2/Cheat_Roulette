package com.e.myroulette1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.e.myroulette1.MainActivity.adapter;


public class MyRouletteActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    //private WordViewModel mWordViewModel;

    //private ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_roulette);

        recyclerView = findViewById(R.id.recyclerview);
        //MainActivity mainActivity = new MainActivity();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //スワイプ削除時にアラートダイアログを表示できるやつ
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //スワイプされたList<word>の項目をList<word>から削除
                                MainActivity.mWordViewModel.getAllWords().getValue().remove(viewHolder.getAdapterPosition());
                                //List<word>から削除されたことを通知（viewHolderにそのことを反映している？）
                                recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                                //スワイプされた箇所のデータベースのprimarykeyを取得
                                int primaryKey = ((WordViewHolder)viewHolder).getRouletteView().getId();
                                //取得したprimarykeyの所のデータを消去
                                MainActivity.mWordViewModel.delete(primaryKey);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog,
                                    // so we will refresh the adapter to prevent hiding the item from UI
                                    recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                                }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // ダイアログがキャンセルされた際の処理
                                recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                        .create()
                        .show();
            }
        }).attachToRecyclerView(recyclerView);





/*
        //スワイプ削除時にアラートダイアログ出せないバージョン
        ItemTouchHelper mIth  = new ItemTouchHelper(

                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN ,
                        ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        adapter.notifyItemMoved(fromPos, toPos);
                        return true;// true if moved, false otherwise
                    }
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        //スワイプされた箇所のデータベースのprimarykeyを取得
                        int primaryKey = ((WordViewHolder)viewHolder).getRouletteView().getId();
                        //取得したprimarykeyの所のデータを消去
                        MainActivity.mWordViewModel.delete(primaryKey);
                        //スワイプされたList<word>の項目をList<word>から削除
                        MainActivity.mWordViewModel.getAllWords().getValue().remove(viewHolder.getAdapterPosition());
                        //List<word>から削除されたことを通知（viewHolderにそのことを反映している？）
                        recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());


                    }
                });
        mIth.attachToRecyclerView(recyclerView);

 */

        returnButton = findViewById(R.id.returnFromMyRoulette_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
        // Get a new or existing ViewModel from the ViewModelProvider.
        mWordViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        // ここで、recyclerViewにDBのデータを表示する。無いと何も表示されない（recyclerViewがあり、DBも更新されているが、見た目のみ反映できない


         */

        //mWordViewModel.getAllWords().observe(this, words -> {

            /*
            ラムダ式でなければ、
           mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(List<Word> words) {
                    adapter.submitList(words);
                }
           });
            的な感じ？ 参考（onClickListenerの無名クラスの形での実装コード, https://qiita.com/sano1202/items/64593e8e981e8d6439d3#3-%E3%83%A9%E3%83%A0%E3%83%80%E5%BC%8F%E3%81%AE%E4%BD%BF%E3%81%84%E6%96%B9, 1-2.無名クラス)
            無名クラスは、インターフェースをimplementの記述なしで実装できる！みたいな書き方ができる。ラムダ式はその無名クラスをさらに簡略化して書ける！
            wordsはObserverインターフェースのonChange abstractメソッドの引数
            */

            // Update the cached copy of the words in the adapter.
          //  adapter.submitList(words);
        //});

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });
        */

    }

    public void onSelectedRoulette(View view) {
        //recyclerViewの中身（cardView）の中のconstrainLayout
        ViewGroup viewGroup = view.findViewById(R.id.constraintLayout);
        //constrainLayoutの中のRouletteView
        RouletteView selectedRouletteView = viewGroup.findViewById(R.id.myRoulette);

        //Uri uri = null;
        //Intent fromMyRouletteToMain = new Intent("SET_MYROULETTE", uri, getApplicationContext(), MainActivity.class);
        //Intent fromMyRouletteToMain = new Intent("SET_MYROULETTE");
        Intent fromMyRouletteToMain = new Intent();

        //Intent fromMyRouletteToMain = new Intent(getApplicationContext(), MainActivity.class);
        fromMyRouletteToMain.putExtra("rouletteName", selectedRouletteView.getRouletteName());
        fromMyRouletteToMain.putIntegerArrayListExtra("colors", selectedRouletteView.getColors());
        fromMyRouletteToMain.putStringArrayListExtra("textStrings", selectedRouletteView.getTextStrings());
        fromMyRouletteToMain.putIntegerArrayListExtra("itemRatios", selectedRouletteView.getItemRatios());
        fromMyRouletteToMain.putIntegerArrayListExtra("OnOffInfoOfSwitch100", selectedRouletteView.getOnOffInfoOfSwitch100());
        fromMyRouletteToMain.putIntegerArrayListExtra("OnOffInfoOfSwitch0", selectedRouletteView.getOnOffInfoOfSwitch0());
        ArrayList<Float> itemProbabilities = selectedRouletteView.getItemProbabilities();
        float itemProbabilityArray[] = new float[itemProbabilities.size()];
        for (int i=0; i<itemProbabilities.size(); i++) {
            itemProbabilityArray[i] = itemProbabilities.get(i);
        }
        fromMyRouletteToMain.putExtra("itemProbability", itemProbabilityArray);
        setResult(RESULT_OK, fromMyRouletteToMain);
        finish();
        /*
        startActivity(fromMyRouletteToMain);
        finish();

         */
    }

    public void onEditMyRoulette(View view) {
        //editボタンの親view(constrainLayout)
        ViewGroup viewGroup = (ViewGroup)view.getParent();
        //constrainLayoutの中のRouletteView
        RouletteView selectedRouletteView = viewGroup.findViewById(R.id.myRoulette);


        Intent fromMyRouletteToEditMyRoulette = new Intent(getApplicationContext(), EditMyRouletteActivity.class);
        fromMyRouletteToEditMyRoulette.putExtra("rouletteId", selectedRouletteView.getId());
        fromMyRouletteToEditMyRoulette.putExtra("rouletteName", selectedRouletteView.getRouletteName());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("colors", selectedRouletteView.getColors());
        fromMyRouletteToEditMyRoulette.putStringArrayListExtra("textStrings", selectedRouletteView.getTextStrings());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("itemRatios", selectedRouletteView.getItemRatios());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("OnOffInfoOfSwitch100", selectedRouletteView.getOnOffInfoOfSwitch100());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("OnOffInfoOfSwitch0", selectedRouletteView.getOnOffInfoOfSwitch0());

        //setResult(RESULT_OK, fromMyRouletteToEditMyRoulette);

        startActivity(fromMyRouletteToEditMyRoulette);
        //finish();
    }

/*
    public void onDeleteMyRoulette(View view) {
        //deleteボタンの親view(constrainLayout)
        ViewGroup viewGroup = (ViewGroup)view.getParent().get;
        //constrainLayoutの中のRouletteView
        RouletteView selectedRouletteView = viewGroup.findViewById(R.id.myRoulette);


        class DeleteMyRouletteFragmentDialog extends DialogFragment {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("削除してもよろしいですか？")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //スワイプされたList<word>の項目をList<word>から削除
                                MainActivity.mWordViewModel.getAllWords().getValue().remove(getAdapterPosition());
                                //List<word>から削除されたことを通知（viewHolderにそのことを反映している？）
                                recyclerView.getAdapter().notifyItemRemoved(recyclerView.ViewHolder.getAdapterPosition());
                                //スワイプされた箇所のデータベースのprimarykeyを取得
                                int primaryKey = ((WordViewHolder)viewHolder).getRouletteView().getId();
                                //取得したprimarykeyの所のデータを消去
                                MainActivity.mWordViewModel.delete(id);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                // Create the AlertDialog object and return it
                return builder.create();
            }
        }
    }


 */

    /*

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String string = data.getStringExtra("word");
            ArrayList<String> words = data.getStringArrayListExtra(NewWordActivity.EXTRA_REPLY);
            ArrayList<Integer> colorsInfo = data.getIntegerArrayListExtra("colors");
            ArrayList<String> textStringsInfo = data.getStringArrayListExtra("textStrings");
            ArrayList<Integer> itemRatiosInfo = data.getIntegerArrayListExtra("itemRatio");
            ArrayList<Integer> OnOffOfSwitch100Info = data.getIntegerArrayListExtra("OnOffInfoOfSwitch100");
            ArrayList<Integer> OnOffOfSwitch0Info = data.getIntegerArrayListExtra("OnOffInfoOfSwitch0");
            //rouletteView.itemProbabilities.clear();
            ArrayList<Float> itemProbabilitiesInfo = new ArrayList<Float>();
            //int itemProbabilitySize = intent.getIntExtra("itemProbabilitySize", 0);
            float itemProbabilityArray[] = data.getFloatArrayExtra("itemProbability");
            for (int i = 0; i < itemProbabilityArray.length; i++) {
                itemProbabilitiesInfo.add(itemProbabilityArray[i]);
            }

            //rouletteView.setRouletteContents(colorsInfo, textStringsInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);

            Word word = new Word(string, colorsInfo, textStringsInfo, itemRatiosInfo, OnOffOfSwitch100Info, OnOffOfSwitch0Info, itemProbabilitiesInfo);
            mWordViewModel.insert(word);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

     */

}