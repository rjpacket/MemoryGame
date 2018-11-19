package com.rjp.memorygame.guessMovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.rjp.memorygame.R;
import com.rjp.memorygame.SPUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private Context mContext;

    public static void trendTo(Context mContext) {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        GuessCardView guessCardView = findViewById(R.id.guess_card_view);

        String fromAssets = getFromAssets("cards.json");
        if(!TextUtils.isEmpty(fromAssets)){
            List<Card> cards = JSONArray.parseArray(fromAssets, Card.class);
            guessCardView.initData(cards);
            int index = (int) SPUtil.getData(this, "index", 0);
            guessCardView.showCard(cards.get(index));
        }
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null)
                result += line;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
