package com.e.myroulette1;

//import android.annotation.SuppressLint;

//import android.annotation.SuppressLint;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import androidx.annotation.RequiresApi;
/*
今はゴリゴリレイアウト書いてるけど、ホントは,リストviewとか、adapterとか使ったほうが良さそう。
 */

public class EditRouletteActivity extends AppCompatActivity {

    //public int rouletteCount;
    private CheckBox checkBox;
    private EditText rouletteName;
    private Toolbar toolbar;
    //private EditText editText, editText2;
    //ルーレットの項目リストの情報を保持するもの
    private RouletteItemListInfo rouletteItemListInfo = new RouletteItemListInfo(
            new ArrayList<Integer>(), new ArrayList<String>(), new ArrayList<Integer>(), new ArrayList<Boolean>(), new ArrayList<Boolean>());
    private RecyclerView rouletteItemList;
    private EditRouletteAdapter rouletteItemListAdapter = new EditRouletteAdapter(rouletteItemListInfo);
    //private RouletteItemListInfo rouletteItemListInfo;

    public static boolean visibleFlag = false;

    private final ColorPalette colorPalette = new ColorPalette();

    //編集前のルーレット情報
    String rouletteNameFromMain;
    ArrayList<Integer> colorsFromMain;
    ArrayList<String>  itemNamesFromMain;
    ArrayList<Integer> itemRatiosFromMain;
    ArrayList<Integer> Switch100InfoFromMain;
    ArrayList<Boolean> Switch100InfoFromMainBoolean = new ArrayList<Boolean>();
    ArrayList<Integer> Switch0InfoFromMain;
    ArrayList<Boolean> Switch0InfoFromMainBoolean = new ArrayList<Boolean>();

    //private int rouletteCount = 0;


    /*
        //ルーレットの色のリスト
        private static ArrayList<Integer> colors = new ArrayList<Integer>();
        //ルーレットの文字列のリスト
        private static ArrayList<String> textString = new ArrayList<String>();
        //ルーレットの項目比率のリスト
        private static ArrayList<String> itemRatio = new ArrayList<String>();


     */
    @SuppressLint({"WrongConstant", "SetTextI18n"})
    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roulette);
        visibleFlag = false;

        toolbar = findViewById(R.id.toolbar_edit_roulette);
        toolbar.setTitle(R.string.edit_roulette);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rouletteItemList = findViewById(R.id.roulette_item_list);
        rouletteItemList.setAdapter(rouletteItemListAdapter);
        rouletteItemList.setLayoutManager(new LinearLayoutManager(this));

        //スクリーンサイズの取得
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point realSize = new Point();
        disp.getRealSize(realSize);
        int ScreenWidth = realSize.x;
        int ScreenHeight = realSize.y;

        //recyclerViewの真下に少しの空白を空ける（スクリーンサイズの1/8分空ける）
        rouletteItemList.setPadding(0, 0, 0, ScreenHeight/8);

        //スワイプ削除時にアラートダイアログを表示できるやつ
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                // Here is where you'll implement swipe to delete
                rouletteItemListAdapter.getRouletteItemDataSet().getColors().remove(position);
                rouletteItemListAdapter.getRouletteItemDataSet().getItemNames().remove(position);
                rouletteItemListAdapter.getRouletteItemDataSet().getItemRatios().remove(position);
                rouletteItemListAdapter.getRouletteItemDataSet().getOnOffInfoOfSwitch100().remove(position);
                rouletteItemListAdapter.getRouletteItemDataSet().getOnOffInfoOfSwitch0().remove(position);

                rouletteItemListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                //削除しただけではデータがリバインドされないので、以下のメソッドでリバインドさせる
                rouletteItemListAdapter.notifyItemRangeChanged(position, rouletteItemListAdapter.getItemCount() - position);
            }
        }).attachToRecyclerView(rouletteItemList);

        //RouletteItemListInfo rouletteItemListInfo;
        //RouletteItemListAdapter rouletteItemListAdapter;

        checkBox = findViewById(R.id.checkBox);
        rouletteName = findViewById(R.id.rouletteName);

        //checkBox.setChecked(false);

        Intent fromMainIntent = getIntent(); //Mainでedit_Buttonを押した時の遷移
        //セットしてあるルーレットを編集する場合
        rouletteNameFromMain = fromMainIntent.getStringExtra("editInfoOfRouletteName");
        colorsFromMain = fromMainIntent.getIntegerArrayListExtra("editInfoOfColors");
        itemNamesFromMain = fromMainIntent.getStringArrayListExtra("editInfoOfTextStrings");
        itemRatiosFromMain = fromMainIntent.getIntegerArrayListExtra("editInfoOfItemRatio");
        Switch100InfoFromMain = fromMainIntent.getIntegerArrayListExtra("editInfoOfSwitch100");
        //ArrayList<Boolean> Switch100InfoFromMainBoolean = new ArrayList<Boolean>();
        Switch0InfoFromMain = fromMainIntent.getIntegerArrayListExtra("editInfoOfSwitch0");
        //ArrayList<Boolean> Switch0InfoFromMainBoolean = new ArrayList<Boolean>();


        for (int i=0; i<Switch100InfoFromMain.size(); i++) {
            if (Switch100InfoFromMain.get(i) == 1) {
                Switch100InfoFromMainBoolean.add(true);
            } else {
                Switch100InfoFromMainBoolean.add(false);
            }

            if (Switch0InfoFromMain.get(i) == 1) {
                Switch0InfoFromMainBoolean.add(true);
            } else {
                Switch0InfoFromMainBoolean.add(false);
            }
        }

        rouletteName.setText(rouletteNameFromMain);//ルーレット名の項目に設定

        rouletteItemListInfo.setColors(colorsFromMain);
        rouletteItemListInfo.setItemNames(itemNamesFromMain);
        rouletteItemListInfo.setItemRatios(itemRatiosFromMain);
        rouletteItemListInfo.setOnOffInfoOfSwitch100(Switch100InfoFromMainBoolean);
        rouletteItemListInfo.setOnOffInfoOfSwitch0(Switch0InfoFromMainBoolean);

        rouletteItemListAdapter.notifyDataSetChanged();


        //************* ボタンの設定 *************/
        Button button = findViewById(R.id.button);

        // リスナーをボタンに登録, lambda
        button.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                //addEditView(layout, scale, margins);
                //rouletteItemListAdapter.addItem(generateColor(), "", 1, 0, 0);

                //RouletteItemListAdapterのaddItemをルーレット項目追加に使用する
                ((RouletteItemListAdapter)rouletteItemList.getAdapter()).addItem(generateColor(), "", 1, false ,false);
                //新しいルーレット項目が追加された時、recyclerViewを一番下に自動スクロールする
                rouletteItemList.smoothScrollToPosition(rouletteItemList.getAdapter().getItemCount() - 1);
                /*
                hueLevel++;
                if (hueLevel >= 12) { hueLevel = 0; }

                 */
            }
        });

        Button returnButton = findViewById(R.id.return_button);
        //textView = findViewById(R.id.text_view);
        //final EditText editText= findViewById(R.id.edit_text);
        //Button button = findViewById(R.id.button);
        // lambda式
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { EditRouletteActivity.this.finish(); }
        });


        Button cheatButton = findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visibleFlag) {
                    //trueの場合は、イカサマスイッチを隠す
                    for (int i = 0; i < rouletteItemList.getChildCount(); i++) {
                        rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).setVisibility(View.GONE);
                        //((ViewGroup) layout.getChildAt(i)).getChildAt(1).setVisibility(View.GONE);
                        //rouletteItemListAdapter.notifyItemChanged(i);
                    }
                    rouletteItemListAdapter.notifyDataSetChanged();
                    //rouletteItemListAdapter.notifyItemRangeChanged(0, rouletteItemList.getChildCount());

                    visibleFlag = false;
                    //cheatButton.setVisibility(View.INVISIBLE);
                    cheatButton.setText("");
                    cheatButton.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    // falseの場合は、イカサマスイッチを表示
                    for (int i = 0; i < rouletteItemList.getChildCount(); i++) {
                        rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).setVisibility(View.VISIBLE);
                        //((ViewGroup) layout.getChildAt(i)).getChildAt(1).setVisibility(View.VISIBLE);
                        //rouletteItemListAdapter.notifyItemChanged(i);
                    }
                    rouletteItemListAdapter.notifyDataSetChanged();
                    //rouletteItemListAdapter.notifyItemRangeChanged(0, rouletteItemList.getChildCount());

                    visibleFlag = true;
                    //cheatButton.setVisibility(View.VISIBLE);
                    cheatButton.setText("HIDE");
                    cheatButton.setBackgroundColor(Color.RED);
                }
            }
        });

        Button countButton = findViewById(R.id.count_button);
        countButton.setOnClickListener(new View.OnClickListener() {
            // @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                int switch100PositiveCount = 0;
                int switch0PositiveCount = 0;
                RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                int rouletteItemCount = rouletteItemDataSet.getColors().size();
                //ルーレットの項目別の当選確率のリスト
                ArrayList<Float> itemProbabilities = new ArrayList<Float>();
                //必中スイッチのONOFF情報（データベースに保存するためboolean から Integerに変換）
                ArrayList<Integer> OnOffOfSwitch100 = new ArrayList<Integer>();
                //絶対ハズレスイッチのONOFF情報（データベースに保存するためboolean から Integerに変換）
                ArrayList<Integer> OnOffOfSwitch0 = new ArrayList<Integer>();

                for (int i=0; i < rouletteItemCount; i++) {
                    //switch100, switch0 それぞれのONOFF情報を追加
                    if (rouletteItemDataSet.getOnOffInfoOfSwitch100().get(i)) {
                        switch100PositiveCount++;
                        OnOffOfSwitch100.add(1);// 1 = ON
                        OnOffOfSwitch0.add(0);// 0 = OFF
                    } else if (rouletteItemDataSet.getOnOffInfoOfSwitch0().get(i)) {
                        switch0PositiveCount++;
                        OnOffOfSwitch0.add(1);// 1 = ON
                        OnOffOfSwitch100.add(0);// 0 = OFF
                    } else {
                        OnOffOfSwitch100.add(0); // 0 = OFF
                        OnOffOfSwitch0.add(0); // 0 = OFF
                    }
                }


                if (switch100PositiveCount >= 1) {
                    for (int i = 0; i < rouletteItemCount; i++) {
                        //switch100がONだったら
                        if (rouletteItemDataSet.getOnOffInfoOfSwitch100().get(i)) {
                            //そこの項目に当たる確率を付与する
                            //itemProbabilityArray[i] = 1f / switch100PositiveCount;
                            itemProbabilities.add(1f/switch100PositiveCount);
                        } else {
                            //ONになっていない項目には０％を付与
                            //itemProbabilityArray[i] = 0f;
                            itemProbabilities.add(0f);
                        }
                    }
                }

                if (switch0PositiveCount >= 1) {
                    //switch100, switch0 のONOFF情報を元に、項目別当選確率を決定、追加する
                    for (int i = 0; i < rouletteItemCount; i++) {
                        //switch0がONだったら
                        if (rouletteItemDataSet.getOnOffInfoOfSwitch0().get(i)) {
                            //そこの項目に０％を付与する
                            //itemProbabilityArray[i] = 0f;
                            itemProbabilities.add(0f);
                        } else {
                            //ONになっていない項目は均等に確率を付与する
                            //itemProbabilityArray[i] = 1f / (rouletteItemCount - switch0PositiveCount);
                            itemProbabilities.add(1f / (rouletteItemCount - switch0PositiveCount));
                        }
                    }
                }

                //ArrayList<Float>はintentで送れないので、送信可能な配列の形に変換
                float itemProbabilityArray[] = new float[itemProbabilities.size()];
                for (int i=0; i<itemProbabilities.size(); i++) {
                    itemProbabilityArray[i] = itemProbabilities.get(i);
                }

                //保存するかどうかをチェックボックスで確認する場合
                /*
                if (checkBox.isChecked()) {
                    Word word = new Word(rouletteName.getText().toString(), getNowDate(),
                            rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                            rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                    //データベースにinsertされて初めて、primaryKeyがautoGenerateされる
                    MainActivity.mWordViewModel.insert(word);

                    //Log.d("うううううううううううううう", String.valueOf(mainActivity.mWordViewModel.getAllWords().getValue().get(mainActivity.mWordViewModel.getAllWords().getValue().size() - 1).getId()));
                }

                Intent fromRouletteCreateIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？
                fromRouletteCreateIntent.putExtra("rouletteName", rouletteName.getText().toString());
                fromRouletteCreateIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                fromRouletteCreateIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                fromRouletteCreateIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                fromRouletteCreateIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                //rouletteCreateIntent.putExtra("itemProbabilitySize", itemProbability.size());
                fromRouletteCreateIntent.putExtra("itemProbability", itemProbabilityArray);

                setResult(RESULT_OK, fromRouletteCreateIntent);

                finish();

                 */

                // 保存するかどうかをアラートダイアログで確認する場合
                AlertDialog.Builder builder = new AlertDialog.Builder(EditRouletteActivity.this);
                builder.setMessage("Myルーレットに保存しますか？")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Word word = new Word(rouletteName.getText().toString(), getNowDate(),
                                        rouletteItemDataSet.getColors(), rouletteItemDataSet.getItemNames(),
                                        rouletteItemDataSet.getItemRatios(), OnOffOfSwitch100, OnOffOfSwitch0, itemProbabilities);
                                //データベースにinsertされて初めて、primaryKeyがautoGenerateされる
                                MainActivity.mWordViewModel.insert(word);

                                Intent fromEditRouletteIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？
                                fromEditRouletteIntent.putExtra("rouletteName", rouletteName.getText().toString());
                                fromEditRouletteIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                                fromEditRouletteIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                                //fromEditRouletteIntent.putStringArrayListExtra("textStrings", itemNamesFromMain);
                                fromEditRouletteIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                                fromEditRouletteIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                                fromEditRouletteIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                                //rouletteCreateIntent.putExtra("itemProbabilitySize", itemProbability.size());
                                fromEditRouletteIntent.putExtra("itemProbability", itemProbabilityArray);

                                setResult(RESULT_OK, fromEditRouletteIntent);

                                EditRouletteActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent fromEditRouletteIntent = new Intent();//引数いれるなら、遷移先のアクティビティクラスを入れる？？
                                fromEditRouletteIntent.putExtra("rouletteName", rouletteName.getText().toString());
                                fromEditRouletteIntent.putIntegerArrayListExtra("colors", rouletteItemDataSet.getColors());
                                fromEditRouletteIntent.putStringArrayListExtra("textStrings", rouletteItemDataSet.getItemNames());
                                fromEditRouletteIntent.putIntegerArrayListExtra("itemRatios", rouletteItemDataSet.getItemRatios());
                                fromEditRouletteIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch100", OnOffOfSwitch100);
                                fromEditRouletteIntent.putIntegerArrayListExtra("OnOffInfoOfSwitch0", OnOffOfSwitch0);
                                //rouletteCreateIntent.putExtra("itemProbabilitySize", itemProbability.size());
                                fromEditRouletteIntent.putExtra("itemProbability", itemProbabilityArray);

                                setResult(RESULT_OK, fromEditRouletteIntent);

                                EditRouletteActivity.this.finish();
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

//                startActivity(rouletteCreateIntent);

                //finish();

            }
        });

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

    //バックキーを押すと、内容に変更があった場合にアラートダイアログを出す処理
    //うまくいかない
    /*
    @Override
    public void onBackPressed() {
        Log.d("あああああああああああああああああああ3", String.valueOf(getIntent().getStringArrayListExtra("editInfoOfTextStrings")));

        RouletteItemListInfo rouletteItemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();

        Log.d("あああああああああああああああああああ1", String.valueOf((rouletteName.getText().toString()).equals(rouletteNameFromMain)));
        Log.d("あああああああああああああああああああ2", String.valueOf((rouletteItemDataSet.getColors()).equals(colorsFromMain)));
        Log.d("あああああああああああああああああああ3", String.valueOf((rouletteItemDataSet.getItemNames()).equals(itemNamesFromMain)));
        Log.d("あああああああああああああああああああ3", String.valueOf((rouletteItemDataSet.getItemNames())));
        Log.d("あああああああああああああああああああ3", String.valueOf(itemNamesFromMain));

        Log.d("あああああああああああああああああああ4", String.valueOf((rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMain)));
        Log.d("あああああああああああああああああああ5", String.valueOf((rouletteItemDataSet.getOnOffInfoOfSwitch100()).equals(Switch100InfoFromMainBoolean)));
        Log.d("あああああああああああああああああああ6", String.valueOf((rouletteItemDataSet.getOnOffInfoOfSwitch0().equals(Switch0InfoFromMainBoolean))));

        Log.d("あああああああああああああああああああ7", String.valueOf(! (rouletteName.getText().toString()).equals(rouletteNameFromMain) &&
                (rouletteItemDataSet.getColors()).equals(colorsFromMain) &&
                (rouletteItemDataSet.getItemNames()).equals(itemNamesFromMain) &&
                (rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMain) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch100()).equals(Switch100InfoFromMainBoolean) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch0().equals(Switch0InfoFromMainBoolean))));

        Log.d("あああああああああああああああああああ8", String.valueOf(!( (rouletteName.getText().toString()).equals(rouletteNameFromMain) &&
                (rouletteItemDataSet.getColors()).equals(colorsFromMain) &&
                (rouletteItemDataSet.getItemNames()).equals(itemNamesFromMain) &&
                (rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMain) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch100()).equals(Switch100InfoFromMainBoolean) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch0().equals(Switch0InfoFromMainBoolean)))));



        //~FromMainが勝手に更新されるから、全部trueになって絶対if文通らない
        if (! (rouletteName.getText().toString()).equals(rouletteNameFromMain) &&
                (rouletteItemDataSet.getColors()).equals(colorsFromMain) &&
                (rouletteItemDataSet.getItemNames()).equals(itemNamesFromMain) &&
                (rouletteItemDataSet.getItemRatios()).equals(itemRatiosFromMain) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch100()).equals(Switch100InfoFromMainBoolean) &&
                (rouletteItemDataSet.getOnOffInfoOfSwitch0().equals(Switch0InfoFromMainBoolean)) ) {


            // 内容更新が合った場合にそれを破棄するかどうかをアラートダイアログで確認
            AlertDialog.Builder builder = new AlertDialog.Builder(EditRouletteActivity.this);
            builder.setMessage("編集内容を破棄しますか？")
                    .setPositiveButton("破棄する", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditRouletteActivity.this.finish();
                        }
                    })
                    .setNegativeButton("編集を続行する", new DialogInterface.OnClickListener() {
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
        } else {
            EditRouletteActivity.this.finish();
        }

    }

     */

    public void onClickColorButton(View colorButton) {
        //ColorPickDialogを開始する
        AlertDialog.Builder colorPickAlert = new AlertDialog.Builder(EditRouletteActivity.this);

        colorPickAlert.setTitle("色の選択");

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.color_pick_dialog, null);
        //アラートダイアログの中にある色ボタン（選択色確認のview）
        ColorButton dialogColorButton = dialoglayout.findViewById(R.id.color_preview);
        colorPickAlert.setView(dialoglayout);

        ColorPickerView colorPickerView = (ColorPickerView) dialoglayout.findViewById(R.id.color_picker_view);
        colorPickerView.setInitialColor(((ColorButton)colorButton).getButtonColor(), false);
        colorPickerView.setShowBorder(true);
        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                ((GradientDrawable) dialogColorButton.getBackground()).setColor(selectedColor);
            }
        });

        colorPickAlert
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //色情報の設定
                        ((ColorButton)colorButton).setButtonColor(colorPickerView.getSelectedColor());
                        //色の変更
                        ((GradientDrawable)colorButton.getBackground()).setColor(colorPickerView.getSelectedColor());
                        //色の変更があった部分のadapterPositionを取得
                        int position = rouletteItemList.getLayoutManager().getPosition((View)colorButton.getParent().getParent().getParent());
                        //色の変更を適応
                        ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().setColor(position, colorPickerView.getSelectedColor());
                        //colorButton.setBackgroundColor(picker.getColor());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
    }

    public void onSwitch100Clicked(View view ) {

        SwitchCompat clickedSwitch = (SwitchCompat)view;

        if (clickedSwitch.isChecked()) {
            // The toggle is enabled
            //int itemCount = rouletteItemList.getChildCount();
            int itemCount = ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().getOnOffInfoOfSwitch100().size();
            for (int i = 0; i < itemCount; i++) {
                //ViewGroup layout2 = rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2);
                //SwitchCompat switch0 = rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2).findViewById(R.id.switch0);
                //((SwitchCompat) layout2.findViewById(R.id.switch0)).setChecked(false);
                //switch0.setChecked(false);
                //((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().setOnOffInfoOfSwitch0Partially(i, false);
                RouletteItemListInfo itemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                itemDataSet.setOnOffInfoOfSwitch0Partially(i, false);
            }
            rouletteItemListAdapter.notifyDataSetChanged();
        } else {
            // The toggle is disabled
        }
    }


    public void onSwitch0Clicked(View view) {

        SwitchCompat clickedSwitch = (SwitchCompat) view;

        if (clickedSwitch.isChecked()) {
            // The toggle is enabled
            Boolean containSwitch0False = false;
            //int itemCount = rouletteItemList.getChildCount();
            int itemCount = ((RouletteItemListAdapter)rouletteItemList.getAdapter()).getRouletteItemDataSet().getOnOffInfoOfSwitch100().size();

            for (int i = 0; i < itemCount; i++) {
                //ViewGroup layout2 = rouletteItemList.getChildAt(i).findViewById(R.id.LinearLayout2);
                //((SwitchCompat) layout2.findViewById(R.id.switch100)).setChecked(false);
                RouletteItemListInfo itemDataSet = rouletteItemListAdapter.getRouletteItemDataSet();
                itemDataSet.setOnOffInfoOfSwitch100Partially(i, false);
                //他のswitch0が全てONになっているか確認　→　ひとつでもOFFがあったらflag = 1
                if (!itemDataSet.getOnOffInfoOfSwitch0().get(i)) {
                    containSwitch0False = true;
                }
            }
            //flag = 0 →　全てのswitch0がONになっているため、今ONになったswitch0をOFFにする（switch0を全てONにすることを禁止している）
            if (!containSwitch0False) {
                clickedSwitch.setChecked(false);
            }
            rouletteItemListAdapter.notifyDataSetChanged();
        } else {
            // The toggle is disabled
        }
    }

    public int generateColor() {
        int red = ((int) (Math.random() * 255));
        int green = ((int) (Math.random() * 255));
        int blue = ((int) (Math.random() * 255));
        return Color.rgb(red, green, blue);
    }

    public static String getNowDate(){
        @SuppressLint("SimpleDateFormat") final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    //public static int getRouletteCount() { return rouletteCount; }
}