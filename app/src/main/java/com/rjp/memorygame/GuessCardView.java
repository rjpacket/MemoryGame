package com.rjp.memorygame;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GuessCardView extends LinearLayout {

    private Context mContext;
    private TextView tvTitle;
    private ImageView ivImage;
    private LinearLayout llAnswerContainer;
    private List<Card> cards = new ArrayList<>();
    private List<Word> words = new ArrayList<>();
    private List<Word> selectedWords = new ArrayList<>();
    private NoScrollGridView gridView;
    private BaseAdapter adapter;
    private LayoutInflater layoutInflater;
    private String answer;

    public GuessCardView(Context context) {
        this(context, null);
    }

    public GuessCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        layoutInflater.inflate(R.layout.view_guess_card, this);

        tvTitle = findViewById(R.id.tv_title);
        ivImage = findViewById(R.id.iv_image);
        llAnswerContainer = findViewById(R.id.ll_card_container);
        gridView = findViewById(R.id.grid_view);
        gridView.setAdapter(adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 24;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_view, null);
                }
                TextView tvCard = convertView.findViewById(R.id.tv_card);

                Word word = words.get(position);
                tvCard.setText(word.getName());
                tvCard.setVisibility(word.isSelected() ? INVISIBLE : VISIBLE);

                tvCard.setTag(word);
                tvCard.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedWords.size() < answer.length()) {
                            Word word = (Word) v.getTag();
                            selectedWords.add(word);
                            word.setSelected(true);
                            notifyDataSetChanged();
                            refreshSelectedCards();
                        }
                    }
                });

                return convertView;
            }
        });
    }

    private void refreshSelectedCards() {
        int size = answer.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            View view = llAnswerContainer.getChildAt(i);
            TextView tvCard = view.findViewById(R.id.tv_card);
            if(i < selectedWords.size()){
                Word word = selectedWords.get(i);
                tvCard.setText(word.getName());
                sb.append(word.getName());
            }else{
                tvCard.setText("");
            }
        }

        if(size == answer.length() && sb.toString().equals(answer)){
            Toast.makeText(mContext, "恭喜通过这一关", Toast.LENGTH_SHORT).show();
            int index = (int) SPUtil.getData(mContext, "index", 0);
            SPUtil.saveData(mContext, "index", ++index);
            if(index >= cards.size()){
                mContext.startActivity(new Intent(mContext, LastActivity.class));
            }else{
                showCard(cards.get(index));
            }
        }
    }

    private void initSelectedCards() {
        llAnswerContainer.removeAllViews();

        int length = answer.length();
        for (int i = 0; i < length; i++) {
            final View view = layoutInflater.inflate(R.layout.item_grid_view, null);
            LinearLayout.LayoutParams params = new LayoutParams(dp2px(mContext, 40), dp2px(mContext, 50));
            params.leftMargin = dp2px(mContext, 5);
            view.setLayoutParams(params);
            llAnswerContainer.addView(view);
            view.setTag(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (Integer) view.getTag();
                    try{
                        Word word = selectedWords.get(index);
                        word.setSelected(false);
                        selectedWords.remove(word);
                        adapter.notifyDataSetChanged();
                        refreshSelectedCards();
                    }catch (Exception e){

                    }
                }
            });
        }
    }

    public int dp2px(Context context,float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void initData(List<Card> cards) {
        this.cards = cards;
    }

    public void showCard(Card card) {
        tvTitle.setText("第" + card.getId() + "关");

        Glide.with(mContext).load(card.getImage()).into(ivImage);

        answer = card.getAnswer();
        initSelectedCards();

        String choose = card.getCards();
        String[] splits = choose.split("");
        words.clear();
        selectedWords.clear();
        for (String split : splits) {
            words.add(new Word(split));
        }
        words.remove(0);
        adapter.notifyDataSetChanged();
    }
}
