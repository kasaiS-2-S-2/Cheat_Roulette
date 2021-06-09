package com.e.myroulette1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.ShowcaseTooltip;


public class MyRouletteActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    //private WordViewModel mWordViewModel;

    //private ConstraintLayout constraintLayout;
    public RecyclerView myRouletteList;
    private ConstraintLayout myRouletteConstrainLayout;
    private Button returnButton;
    private Toolbar toolbar;

    //private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_roulette);

        //広告を付ける
        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = findViewById(R.id.toolbar_myRoulette);
        toolbar.setTitle(R.string.myRoulette);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myRouletteList = findViewById(R.id.myRoulette_list);
        //MainActivity mainActivity = new MainActivity();
        myRouletteList.setAdapter(MainActivity.adapter);
        myRouletteList.setLayoutManager(new LinearLayoutManager(this));

        myRouletteConstrainLayout = findViewById(R.id.MyRouletteConstraintLayout);

        //スクリーンサイズの取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point realSize = new Point();
        disp.getRealSize(realSize);
        int ScreenWidth = realSize.x;
        int ScreenHeight = realSize.y;

        //recyclerViewの真下に少しの空白を空ける（スクリーンサイズの1/8分空ける）
        myRouletteList.setPadding(0, 0, 0, ScreenHeight/8);

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
                        .setMessage("削除してもよろしいですか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int position = viewHolder.getAdapterPosition();
                                //スワイプされたList<word>の項目をList<word>から削除
                                MainActivity.mMyRouletteViewModel.getAllMyRoulette().getValue().remove(position);
                                //List<word>から削除されたことを通知（viewHolderにそのことを反映している？）
                                myRouletteList.getAdapter().notifyItemRemoved(position);
                                //削除しただけではデータがリバインドされないので、以下のメソッドでリバインドさせる
                                myRouletteList.getAdapter().notifyItemRangeChanged(position, myRouletteList.getAdapter().getItemCount() - position);
                                //スワイプされた箇所のデータベースのprimarykeyを取得
                                int primaryKey = ((MyRouletteViewHolder)viewHolder).getRouletteView().getId();
                                //取得したprimarykeyの所のデータを消去
                                MainActivity.mMyRouletteViewModel.delete(primaryKey);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog,
                                    // so we will refresh the adapter to prevent hiding the item from UI
                                    myRouletteList.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                                }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // ダイアログがキャンセルされた際の処理
                                myRouletteList.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                        .create()
                        .show();
            }
        }).attachToRecyclerView(myRouletteList);





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

        Log.d("あああああああああああXXXXXXXXX", String.valueOf(myRouletteList.getChildCount()));
        Log.d("あああああああああああXXXXXXXXX", String.valueOf(myRouletteList.getChildAt(0)));
        Log.d("あああああああああああXXXXXXXXX", String.valueOf(myRouletteList.getChildAt(1)));

        /*
        SharedPreferences sharedPref = MyRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_myRoulette_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            //isTutorialState = true;
            tutorial();
            //最初のチュートリアルが終わったら、そのことを保存しておく
            //SharedPreferences sharedPref = EditRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.saved_myRoulette_first_tutorial_done_key), true);
            editor.apply();
            //MaterialShowcaseView.resetSingleUse(this, getString(R.string.roulette_create_first_tutorial_id));//////////////////////////////////////////////////////
        }

         */

    }

    //@RequiresApi(api = Build.VERSION_CODES.Q)
    public void onWindowFocusChanged(boolean hasFocus) {
        //public void onResume() {
        //super.onRestart();
        //super.onStart();
        //super.onResume();

        super.onWindowFocusChanged(hasFocus);

        SharedPreferences sharedPref = MyRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_myRoulette_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            //isTutorialState = true;
            tutorial();
            //最初のチュートリアルが終わったら、そのことを保存しておく
            //SharedPreferences sharedPref = EditRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.saved_myRoulette_first_tutorial_done_key), true);
            editor.apply();
            //MaterialShowcaseView.resetSingleUse(this, getString(R.string.roulette_create_first_tutorial_id));//////////////////////////////////////////////////////
        }
    }

    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.myRoulette_tutorial_id));

        /*
        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });
        */

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.myRoulette_tutorial_id));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(myRouletteConstrainLayout)
                        .setContentText("ここでは保存したルーレットの一覧を見ることができます。\n\n作成したルーレットを保存するには、ルーレット作成完了時の「Myルーレットに保存しますか？」にて「YES」をタップしてください。")
                        .setContentTextColor(getResources().getColor(R.color.showcase_text_color))
                        .setGravity(16)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        //.setToolTip(itemNameToolTip)
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .withoutShape()
                        .build()
        );

        ShowcaseTooltip myRouletteNameToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("Myルーレットの名前が表示されます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        //.setTarget(((MyRouletteViewHolder)myRouletteList.findViewHolderForLayoutPosition(1)).getCardView().findViewById(R.id.roulette_name))
                        .setTarget(myRouletteList.getChildAt(1).findViewById(R.id.roulette_name))
                        .setToolTip(myRouletteNameToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );

        ShowcaseTooltip editMyRouletteToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("タップするとMyルーレットを編集をすることができます。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        //.setTarget(((MyRouletteViewHolder)myRouletteList.findViewHolderForLayoutPosition(1)).getEditMyRouletteButton())
                        .setTarget(myRouletteList.getChildAt(1).findViewById(R.id.edit_myRoulette))
                        .setToolTip(editMyRouletteToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );

        ShowcaseTooltip deleteMyRouletteToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("タップするとMyルーレットを削除することができます。スワイプでも削除が可能です。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        //.setTarget(((MyRouletteViewHolder)myRouletteList.findViewHolderForLayoutPosition(1)).getDeleteMyRouletteButton())
                        .setTarget(myRouletteList.getChildAt(1).findViewById(R.id.delete_myRoulette))
                        .setToolTip(deleteMyRouletteToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );

        ShowcaseTooltip myRouletteItemToolTip = ShowcaseTooltip.build(this)
                .corner(30)
                .textColor(getResources().getColor(R.color.tooltip_text_color))
                .color(getResources().getColor(R.color.appPrimaryColor))
                .text("タップすることで選択されたMyルーレットがセットされます。<br><br>以上でこの画面のチュートリアルを終了します。");

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        //.setTarget(((MyRouletteViewHolder)myRouletteList.findViewHolderForLayoutPosition(1)).getCardView())
                        .setTarget(myRouletteList.getChildAt(1))
                        .setToolTip(myRouletteItemToolTip)
                        //.setContentText("ここでルーレットの作成を完了します。")
                        //.setTargetTouchable(true)
                        //.setDismissOnTargetTouch(true)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );

        /*
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(((RouletteItemListAdapter.ViewHolder)rouletteItemList.findViewHolderForAdapterPosition(1)).getLinearLayout2())
                        .setContentText("This is button three")
                        .withRectangleShape()
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(cheatButton)
                        .setContentText("This is button two")
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(createFinishFab)
                        .setContentText("This is button two")
                        .setTargetTouchable(true)
                        .setDismissOnTargetTouch(true)
                        .build()
        );

         */

        Log.d("あああああああああああああ", "firstTutorial()");
        sequence.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_roulette_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.page_explain:
                // ボタンをタップした際の処理を記述
                return true;
            case R.id.tutorial:
                // ボタンをタップした際の処理を記述
                MaterialShowcaseView.resetSingleUse(MyRouletteActivity.this, getString(R.string.myRoulette_tutorial_id));
                tutorial();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ツールバーの戻るボタンを押した時の処理
    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return super.onSupportNavigateUp();
    }

    public void onSelectedRoulette(View view) {
        //recyclerViewの中身（cardView）の中のconstrainLayout
        ViewGroup viewGroup = view.findViewById(R.id.myRoulette_constraintLayout);
        //constrainLayoutの中のRouletteView
        RouletteView selectedRouletteView = viewGroup.findViewById(R.id.myRoulette);

        //Uri uri = null;
        //Intent fromMyRouletteToMain = new Intent("SET_MYROULETTE", uri, getApplicationContext(), MainActivity.class);
        //Intent fromMyRouletteToMain = new Intent("SET_MYROULETTE");
        Intent fromMyRouletteToMain = new Intent();

        //Intent fromMyRouletteToMain = new Intent(getApplicationContext(), MainActivity.class);
        fromMyRouletteToMain.putExtra("rouletteName", selectedRouletteView.getRouletteName());
        fromMyRouletteToMain.putIntegerArrayListExtra("colors", selectedRouletteView.getColors());
        fromMyRouletteToMain.putStringArrayListExtra("textStrings", selectedRouletteView.getItemNames());
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
        ViewGroup viewGroup = (ViewGroup)view.getParent().getParent();
        //constrainLayoutの中のRouletteView
        RouletteView selectedRouletteView = viewGroup.findViewById(R.id.myRoulette);
        //RouletteView selectedRouletteView = myRouletteConstrainLayout.findViewById(R.id.myRoulette);

        Intent fromMyRouletteToEditMyRoulette = new Intent(getApplicationContext(), EditMyRouletteActivity.class);
        //fromMyRouletteToEditMyRoulette.putExtra("editItemPosition", recyclerView.getChildAdapterPosition((View)viewGroup.getParent()));
        fromMyRouletteToEditMyRoulette.putExtra("rouletteId", selectedRouletteView.getId());
        fromMyRouletteToEditMyRoulette.putExtra("rouletteName", selectedRouletteView.getRouletteName());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("colors", selectedRouletteView.getColors());
        fromMyRouletteToEditMyRoulette.putStringArrayListExtra("itemNames", selectedRouletteView.getItemNames());
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