package com.rjp.memorygame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class GuessCardView extends LinearLayout {

    private Context mContext;
    private TextView tvTitle;
    private ImageView ivImage;
    private LinearLayout llAnswerContainer;
    private List<Card> cards;

    public GuessCardView(Context context) {
        this(context, null);
    }

    public GuessCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.view_guess_card, this);

        tvTitle = findViewById(R.id.tv_title);
        ivImage = findViewById(R.id.iv_image);
        llAnswerContainer = findViewById(R.id.ll_card_container);

        NoScrollGridView gridView = findViewById(R.id.grid_view);
        gridView.setAdapter(new BaseAdapter() {
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
                return convertView;
            }
        });
    }

    public void initData(List<Card> cards) {
        this.cards = cards;
        showCard(cards.get(0));


    }

    private void showCard(Card card) {

    }
}
