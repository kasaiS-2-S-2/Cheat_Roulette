package com.kasai.cheatroulette;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
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
import androidx.preference.PreferenceManager;
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

    public RecyclerView myRouletteList;
    private ConstraintLayout myRouletteConstrainLayout;
    private Button returnButton;
    private Toolbar toolbar;

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

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int myRouletteItemCount = myRouletteList.getAdapter().getItemCount();
                if (myRouletteItemCount >= 3) {
                    SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(MyRouletteActivity.this);
                    if (defaultPref.getBoolean(getString(R.string.saved_appear_alert_delete_myRoulette_key), true)) {
                        new AlertDialog.Builder(viewHolder.itemView.getContext())
                                .setMessage("削除してもよろしいですか？")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteMyRoulette(viewHolder);
                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

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
                    } else {
                        deleteMyRoulette(viewHolder);
                    }
                } else {
                    myRouletteList.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }
        }).attachToRecyclerView(myRouletteList);
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        SharedPreferences sharedPref = MyRouletteActivity.this.getPreferences(Context.MODE_PRIVATE);
        boolean isFirstTutorialDone = sharedPref.getBoolean(getString(R.string.saved_myRoulette_first_tutorial_done_key), false);
        if (!isFirstTutorialDone) {
            tutorial();
            //最初のチュートリアルが終わったら、そのことを保存しておく
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.saved_myRoulette_first_tutorial_done_key), true);
            editor.apply();
        }
    }

    private void deleteMyRoulette(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        //スワイプされたList<word>の項目をList<word>から削除
        MainActivity.mMyRouletteViewModel.getAllMyRoulette().getValue().remove(position);
        //List<word>から削除されたことを通知（viewHolderにそのことを反映している？）
        myRouletteList.getAdapter().notifyItemRemoved(position);
        //削除しただけではデータがリバインドされないので、以下のメソッドでリバインドさせる
        myRouletteList.getAdapter().notifyItemRangeChanged(position, myRouletteList.getAdapter().getItemCount() - position);
        //スワイプされた箇所のデータベースのprimarykeyを取得
        int primaryKey = ((MyRouletteViewHolder) viewHolder).getRouletteView().getId();
        //取得したprimarykeyの所のデータを消去
        MainActivity.mMyRouletteViewModel.delete(primaryKey);
    }



    private void tutorial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100);
        config.setRenderOverNavigationBar(true);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, getString(R.string.myRoulette_tutorial_id));

        sequence.setConfig(config);

        sequence.singleUse(getString(R.string.myRoulette_tutorial_id));

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(myRouletteConstrainLayout)
                        .setContentText("ここでは保存したルーレットの一覧を見ることができます。\n\n作成したルーレットを保存するには、ルーレット作成完了時の「Myルーレットに保存しますか？」にて「YES」をタップしてください。")
                        .setContentTextColor(getResources().getColor(R.color.showcase_text_color))
                        .setGravity(16)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
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
                        .setTarget(myRouletteList.getChildAt(1).findViewById(R.id.roulette_name))
                        .setToolTip(myRouletteNameToolTip)
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
                        .setTarget(myRouletteList.getChildAt(1).findViewById(R.id.edit_myRoulette))
                        .setToolTip(editMyRouletteToolTip)
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
                        .setTarget(myRouletteList.getChildAt(1).findViewById(R.id.delete_myRoulette))
                        .setToolTip(deleteMyRouletteToolTip)
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
                        .setTarget(myRouletteList.getChildAt(1))
                        .setToolTip(myRouletteItemToolTip)
                        .setDismissOnTouch(true)
                        .setMaskColour(getResources().getColor(R.color.tutorial_overlay_color))
                        .withRectangleShape()
                        .build()
        );
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MyRouletteActivity.this);
                builder.setTitle("チュートリアルを開始しますか？")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MaterialShowcaseView.resetSingleUse(MyRouletteActivity.this, getString(R.string.myRoulette_tutorial_id));
                                tutorial();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // ダイアログがキャンセルされた際の処理
                            }
                        })
                        .create()
                        .show();

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

        Intent fromMyRouletteToMain = new Intent();

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
    }

    public void onEditMyRoulette(View view) {
        //editボタンの親view(constrainLayout)
        ViewGroup viewGroup = (ViewGroup)view.getParent().getParent();
        //constrainLayoutの中のRouletteView
        RouletteView selectedRouletteView = viewGroup.findViewById(R.id.myRoulette);

        Intent fromMyRouletteToEditMyRoulette = new Intent(getApplicationContext(), EditMyRouletteActivity.class);
        fromMyRouletteToEditMyRoulette.putExtra("rouletteId", selectedRouletteView.getId());
        fromMyRouletteToEditMyRoulette.putExtra("rouletteName", selectedRouletteView.getRouletteName());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("colors", selectedRouletteView.getColors());
        fromMyRouletteToEditMyRoulette.putStringArrayListExtra("itemNames", selectedRouletteView.getItemNames());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("itemRatios", selectedRouletteView.getItemRatios());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("OnOffInfoOfSwitch100", selectedRouletteView.getOnOffInfoOfSwitch100());
        fromMyRouletteToEditMyRoulette.putIntegerArrayListExtra("OnOffInfoOfSwitch0", selectedRouletteView.getOnOffInfoOfSwitch0());

        startActivity(fromMyRouletteToEditMyRoulette);
    }
}