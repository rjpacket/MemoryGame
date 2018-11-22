package com.rjp.memorygame.carGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rjp.memorygame.R;

public class CarGameActivity extends Activity {

    private RoadPanelView roadPanelView;

    public static void trendTo(Context mContext) {
        Intent intent = new Intent(mContext, CarGameActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_game);

        roadPanelView = findViewById(R.id.car_game);
        roadPanelView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        roadPanelView.stop();
    }
}
