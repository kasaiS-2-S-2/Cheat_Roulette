package com.e.myroulette1;

import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RouletteItemListAdapter extends RecyclerView.Adapter<RouletteItemListAdapter.ViewHolder> {

    //private ArrayList<RouletteItemListInfo> rouletteItemDataSet;
    private RecyclerView rouletteItemList;
    private RouletteItemListInfo rouletteItemDataSet;
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public RouletteItemListAdapter(RecyclerView recyclerView, RouletteItemListInfo dataSet) {
        this.rouletteItemList = recyclerView;
        this.rouletteItemDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d("ああああああああああああああああああ", "onCreateViewHolder");
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rouletteitemlist_item, viewGroup, false);

        return new ViewHolder(view ,new EditTextListenerForItemName(),new EditTextListenerForRatio(), new Switch100OnCheckedChangeListener(), new Switch0OnCheckedChangeListener(), new EditTextFocusChangeListener());
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Log.d("ああああああああああああああああああ", "onBindViewHolder");

        //visibleFlagがfalseの時は、switch100, switch0 を非表示にする
        if (!RouletteCreateActivity.visibleFlag) {
            viewHolder.getLinearLayout2().setVisibility(View.GONE);
        } else {
            viewHolder.getLinearLayout2().setVisibility(View.VISIBLE);
        }
        ((GradientDrawable)viewHolder.getColorButton().getBackground()).setColor(rouletteItemDataSet.getColors().get(viewHolder.getAdapterPosition()));
        ((ColorButton) viewHolder.getColorButton()).setButtonColor(rouletteItemDataSet.getColors().get(viewHolder.getAdapterPosition()));

        //EditText（ルーレット名）監視リスナーに使うpositionを今のadapterPositionに設定
        viewHolder.editTextListenerForItemName.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getItemName().setText(rouletteItemDataSet.getItemNames().get(viewHolder.getAdapterPosition()));

        //EditText（面積比率）監視リスナーに使うpositionを今のadapterPositionに設定
        viewHolder.editTextListenerForRatio.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getRatio().setText(String.valueOf(rouletteItemDataSet.getItemRatios().get(viewHolder.getAdapterPosition())));

        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ルーレット項目が２つ未満になるのを防ぐ
                if (getItemCount() > 2) {
                    deleteItem(viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.switch100OnCheckedChangeListener.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getSwitch100().setChecked(rouletteItemDataSet.getOnOffInfoOfSwitch100().get(viewHolder.getAdapterPosition()));
        /*
        viewHolder.getSwitch100().setOnCheckedChangeListener(null);
        viewHolder.getSwitch100().setChecked(rouletteItemDataSet.getOnOffInfoOfSwitch100().get(position));
        viewHolder.getSwitch100().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rouletteItemDataSet.setOnOffInfoOfSwitch100Partially(position, isChecked);
            }
        });

         */

        viewHolder.switch0OnCheckedChangeListener.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getSwitch0().setChecked(rouletteItemDataSet.getOnOffInfoOfSwitch0().get(viewHolder.getAdapterPosition()));
        /*
        viewHolder.getSwitch0().setOnCheckedChangeListener(null);
        viewHolder.getSwitch0().setChecked(rouletteItemDataSet.getOnOffInfoOfSwitch0().get(position));

        viewHolder.getSwitch0().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rouletteItemDataSet.setOnOffInfoOfSwitch0Partially(position, isChecked);
            }
        });

         */
    }

    public void setFocusItemName(int position) {
        ViewHolder holder = (ViewHolder) rouletteItemList.findViewHolderForLayoutPosition(position);
        if (holder != null) {
            holder.getItemName().requestFocus();
        }
    }

    public void setFocusRatio(int position) {
        ViewHolder holder = (ViewHolder) rouletteItemList.findViewHolderForLayoutPosition(position);
        if (holder != null) {
            holder.getRatio().requestFocus();
        }
    }

    protected void deleteItem(int position) {
        rouletteItemDataSet.getColors().remove(position);
        rouletteItemDataSet.getItemNames().remove(position);
        rouletteItemDataSet.getItemRatios().remove(position);
        rouletteItemDataSet.getOnOffInfoOfSwitch100().remove(position);
        rouletteItemDataSet.getOnOffInfoOfSwitch0().remove(position);

        notifyItemRemoved(position);
        //notifyDataSetChanged();
        //削除しただけではデータがリバインドされないので、以下のメソッドでリバインドさせる
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public void addItem(int generatedColor, String itemName, int itemRatio, Boolean OnOffInfoOfSwitch100, Boolean OnOffInfoOfSwitch0) {
        rouletteItemDataSet.getColors().add(generatedColor);
        rouletteItemDataSet.getItemNames().add(itemName);
        rouletteItemDataSet.getItemRatios().add(itemRatio);
        rouletteItemDataSet.getOnOffInfoOfSwitch100().add(OnOffInfoOfSwitch100);
        rouletteItemDataSet.getOnOffInfoOfSwitch0().add(OnOffInfoOfSwitch0);
        //挿入した位置を指定して、notifyする（getItemCount()-1 がrouletteItemListの末尾のインデックス）
        notifyItemInserted(getItemCount() - 1);
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    //なんかstaticになってた。理由不明　5/26
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Button colorButton;
        private final EditText itemName;
        private final EditText ratio;
        private final ImageButton deleteButton;
        private final SwitchCompat switch100;
        private final SwitchCompat switch0;
        private final LinearLayout linearLayout2;

        protected final EditTextListenerForItemName editTextListenerForItemName;
        protected final EditTextListenerForRatio editTextListenerForRatio;
        protected final Switch100OnCheckedChangeListener switch100OnCheckedChangeListener;
        protected final Switch0OnCheckedChangeListener switch0OnCheckedChangeListener;
        protected final EditTextFocusChangeListener editTextFocusChangeListener;

        int holderIndex;


        public void focusNextItemName() {
            int position = getAdapterPosition() + 1;
            if (position < getItemCount()) {
                setFocusItemName(position);
            }
        }

        public void focusNextRatio() {
            int position = getAdapterPosition() + 1;
            if (position < getItemCount()) {
                setFocusRatio(position);
            }
        }

        public ViewHolder(View itemView,
                          EditTextListenerForItemName editTextListenerForItemName,
                          EditTextListenerForRatio editTextListenerForRatio,
                          Switch100OnCheckedChangeListener switch100OnCheckedChangeListener,
                          Switch0OnCheckedChangeListener switch0OnCheckedChangeListener,
                          EditTextFocusChangeListener editTextFocusChangeListener) {
            //itemView = rouletteitemlist_item.xml一つ分
            super(itemView);
            // Define click listener for the ViewHolder's View

            this.colorButton = itemView.findViewById(R.id.colorButton);

            this.itemName = itemView.findViewById(R.id.itemName);
            this.itemName.setMaxWidth(itemName.getWidth());
            //フォーカス移動時、ひとつ下のcardviewのitemNameにフォーカスが移動するようにする
            this.itemName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    focusNextItemName();
                    return true;
                }
            });

            this.ratio = itemView.findViewById(R.id.ratio);
            this.ratio.setMaxWidth(ratio.getWidth());
            //フォーカス移動時、ひとつ下のcardviewのRatioにフォーカスが移動するようにする
            this.ratio.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    focusNextRatio();
                    return true;
                }
            });



            this.deleteButton = itemView.findViewById(R.id.deleteButton);

            this.switch100 = itemView.findViewById(R.id.switch100);

            this.switch0 = itemView.findViewById(R.id.switch0);

            this.linearLayout2 = itemView.findViewById(R.id.LinearLayout2);


            this.editTextListenerForItemName = editTextListenerForItemName;
            this.itemName.addTextChangedListener(editTextListenerForItemName);
            this.editTextFocusChangeListener = editTextFocusChangeListener;
            this.itemName.setOnFocusChangeListener(editTextFocusChangeListener);

            this.editTextListenerForRatio = editTextListenerForRatio;
            this.ratio.addTextChangedListener(editTextListenerForRatio);
            this.ratio.setOnFocusChangeListener(editTextFocusChangeListener);

            this.switch100OnCheckedChangeListener = switch100OnCheckedChangeListener;
            this.switch100.setOnCheckedChangeListener(switch100OnCheckedChangeListener);

            this.switch0OnCheckedChangeListener = switch0OnCheckedChangeListener;
            this.switch0.setOnCheckedChangeListener(switch0OnCheckedChangeListener);
        }

        public Button getColorButton() {
            return colorButton;
        }

        public EditText getItemName() {
            return itemName;
        }

        public EditText getRatio() {
            return ratio;
        }

        public ImageButton getDeleteButton() {
            return deleteButton;
        }

        public SwitchCompat getSwitch100() {
            return switch100;
        }

        public SwitchCompat getSwitch0() {
            return switch0;
        }

        public LinearLayout getLinearLayout2() {
            return linearLayout2;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rouletteItemDataSet.getColors().size();
    }

    public RouletteItemListInfo getRouletteItemDataSet() {
        return rouletteItemDataSet;
    }

    public void setRouletteItemDataSet(RouletteItemListInfo rouletteItemDataSet) {
        this.rouletteItemDataSet = rouletteItemDataSet;
    }

    //itemNameの更新状況を監視するリスナー
    protected class EditTextListenerForItemName implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            rouletteItemDataSet.setItemName(position, charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    //ratioの更新状況を監視するリスナー
    protected class EditTextListenerForRatio implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            String string = charSequence.toString();
            if (!string.equals("")) {
                Integer ratio = Integer.parseInt(string);
                rouletteItemDataSet.setItemRatio(position, ratio);
            } else {
                //空白の場合は1を設定
                rouletteItemDataSet.setItemRatio(position, 1);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    //switch100のONOFFを監視するリスナー
    protected class Switch100OnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            rouletteItemDataSet.setOnOffInfoOfSwitch100Partially(position, isChecked);
        }
    }

    //switch0のONOFFを監視するリスナー
    protected class Switch0OnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            rouletteItemDataSet.setOnOffInfoOfSwitch0Partially(position, isChecked);
        }
    }

    //edittextのフォーカスのリスナー
    protected class EditTextFocusChangeListener implements View.OnFocusChangeListener {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onFocusChange(View view, boolean isFocused) {
            if (isFocused) {
                EditText editText = (EditText)view;
                //フォーカスがあたった時、文字列の最後にフォーカスが当たるようにする
                editText.setSelection((editText.getText().length()));
                //editText.setNextFocusDownId(R.id.itemName);
            }
        }
    }
}
