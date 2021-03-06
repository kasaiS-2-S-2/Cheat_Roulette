package com.kasai.cheatroulette.recyclerView;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kasai.cheatroulette.R;
import com.kasai.cheatroulette.activity.RouletteCreateActivity;
import com.kasai.cheatroulette.common.MyApplication;
import com.kasai.cheatroulette.ui.ColorButton;

public class RouletteItemListAdapter extends RecyclerView.Adapter<RouletteItemListAdapter.ViewHolder> {

    private RecyclerView rouletteItemList;
    private RouletteItemListInfo rouletteItemDataSet;

    public RouletteItemListAdapter(RecyclerView recyclerView, RouletteItemListInfo dataSet) {
        this.rouletteItemList = recyclerView;
        this.rouletteItemDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d("RouletteItemListAdapter", "onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rouletteitemlist_item, viewGroup, false);

        return new ViewHolder(view ,new EditTextListenerForItemName(),new EditTextListenerForRatio(),
                new Switch100OnCheckedChangeListener(), new Switch0OnCheckedChangeListener(), new EditTextFocusChangeListener());
    }

    // Replace the contents of ic_cheat_roulette_launcher2_foreground view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        //visibleFlag???false????????????switch100, switch0 ?????????????????????
        if (!RouletteCreateActivity.visibleFlag) {
            viewHolder.getLinearLayout2().setVisibility(View.GONE);
        } else {
            viewHolder.getLinearLayout2().setVisibility(View.VISIBLE);
        }
        ((GradientDrawable)viewHolder.getColorButton().getBackground()).setColor(rouletteItemDataSet.getColors().get(viewHolder.getAdapterPosition()));
        ((ColorButton) viewHolder.getColorButton()).setButtonColor(rouletteItemDataSet.getColors().get(viewHolder.getAdapterPosition()));

        //EditText???????????????????????????????????????????????????position?????????adapterPosition?????????
        viewHolder.editTextListenerForItemName.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getItemName().setText(rouletteItemDataSet.getItemNames().get(viewHolder.getAdapterPosition()));

        //EditText?????????????????????????????????????????????position?????????adapterPosition?????????
        viewHolder.editTextListenerForRatio.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getRatio().setText(String.valueOf(rouletteItemDataSet.getItemRatios().get(viewHolder.getAdapterPosition())));

        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?????????????????????????????????????????????????????????
                if (getItemCount() > 2) {
                    deleteItem(viewHolder.getAdapterPosition());
                }
            }
        });

        viewHolder.switch100OnCheckedChangeListener.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getSwitch100().setChecked(rouletteItemDataSet.getOnOffInfoOfSwitch100().get(viewHolder.getAdapterPosition()));

        viewHolder.switch0OnCheckedChangeListener.updatePosition(viewHolder.getAdapterPosition());
        viewHolder.getSwitch0().setChecked(rouletteItemDataSet.getOnOffInfoOfSwitch0().get(viewHolder.getAdapterPosition()));
    }

    protected void setFocusItemName(int position) {
        ViewHolder holder = (ViewHolder) rouletteItemList.findViewHolderForLayoutPosition(position);
        if (holder != null) {
            holder.getItemName().requestFocus();
        }
    }

    protected void setFocusRatio(int position) {
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
        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public void addItem(int generatedColor, String itemName, int itemRatio, Boolean OnOffInfoOfSwitch100, Boolean OnOffInfoOfSwitch0) {

        rouletteItemDataSet.getColors().add(generatedColor);
        rouletteItemDataSet.getItemNames().add(itemName);
        rouletteItemDataSet.getItemRatios().add(itemRatio);
        rouletteItemDataSet.getOnOffInfoOfSwitch100().add(OnOffInfoOfSwitch100);
        rouletteItemDataSet.getOnOffInfoOfSwitch0().add(OnOffInfoOfSwitch0);
        //????????????????????????????????????notify?????????getItemCount()-1 ???rouletteItemList?????????????????????????????????
        notifyItemInserted(getItemCount() - 1);
    }

    //?????????static?????????????????????????????????5/26 ??????????????????????????????????????????static????????????????????????????????????
    //??????https://stackoverflow.com/questions/31302341/what-difference-between-static-and-non-static-viewholder-in-recyclerview-adapter
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Button colorButton;
        private final EditText itemName;
        private final EditText ratio;
        private final ImageButton deleteButton;
        private final SwitchCompat switch100;
        private final SwitchCompat switch0;
        private final LinearLayout linearLayout1;
        private final LinearLayout linearLayout2;

        protected final EditTextListenerForItemName editTextListenerForItemName;
        protected final EditTextListenerForRatio editTextListenerForRatio;
        protected final Switch100OnCheckedChangeListener switch100OnCheckedChangeListener;
        protected final Switch0OnCheckedChangeListener switch0OnCheckedChangeListener;
        protected final EditTextFocusChangeListener editTextFocusChangeListener;

        protected void focusNextItemName() {
            int position = getAdapterPosition() + 1;
            if (position < getItemCount()) {
                setFocusItemName(position);
            }
        }

        protected void focusNextRatio() {
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
            //itemView = rouletteitemlist_item.xml?????????
            super(itemView);
            // Define click listener for the ViewHolder's View

            this.colorButton = itemView.findViewById(R.id.colorButton);

            this.itemName = itemView.findViewById(R.id.itemName);
            this.itemName.setMaxWidth(itemName.getWidth());
            //??????????????????????????????????????????cardview???itemName????????????????????????????????????????????????
            this.itemName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    boolean handled = false;
                    switch (actionId) {
                        //??????????????????NEXT??????????????????????????????????????????cardview???itemName????????????????????????????????????????????????
                        case EditorInfo.IME_ACTION_NEXT:
                            focusNextItemName();
                            handled = true;
                            break;

                        //??????????????????DONE??????????????????????????????????????????????????????
                        case EditorInfo.IME_ACTION_DONE:
                            InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            handled = true;
                            break;
                    }
                    return handled;
                }
            });

            this.ratio = itemView.findViewById(R.id.ratio);
            this.ratio.setMaxWidth(ratio.getWidth());
            //??????????????????????????????????????????cardview???Ratio????????????????????????????????????????????????
            this.ratio.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    boolean handled = false;
                    switch (actionId) {
                        //??????????????????NEXT??????????????????????????????????????????cardview???Ratio????????????????????????????????????????????????
                        case EditorInfo.IME_ACTION_NEXT:
                            focusNextRatio();
                            handled = true;
                            break;

                        //??????????????????DONE??????????????????????????????????????????????????????
                        case EditorInfo.IME_ACTION_DONE:
                            InputMethodManager inputMethodManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            handled = true;
                            break;
                    }
                    return handled;
                }
            });

            this.deleteButton = itemView.findViewById(R.id.deleteButton);

            this.switch100 = itemView.findViewById(R.id.switch100);

            this.switch0 = itemView.findViewById(R.id.switch0);

            this.linearLayout1 = itemView.findViewById(R.id.LinearLayout1);

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

        public LinearLayout getLinearLayout1() { return linearLayout1; }

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

    //itemName??????????????????????????????????????????
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

    //ratio??????????????????????????????????????????
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
                //??????????????????1?????????
                rouletteItemDataSet.setItemRatio(position, 1);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    //switch100???ONOFF???????????????????????????
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

    //switch0???ONOFF???????????????????????????
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

    //edittext?????????????????????????????????
    protected class EditTextFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean isFocused) {
            if (isFocused) {
                EditText editText = (EditText)view;
                //???????????????????????????????????????????????????????????????????????????????????????????????????
                editText.setSelection((editText.getText().length()));
            }
        }
    }
}
