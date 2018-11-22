package com.rjp.memorygame.memoryGame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rjp.memorygame.R;

public class ResultActivity extends Activity {

    public static void start(Context mContext, String level){
        Intent intent = new Intent(mContext, ResultActivity.class);
        intent.putExtra("level", level);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageView ivScore = findViewById(R.id.iv_score);
        ImageView ivResult = findViewById(R.id.iv_result);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("level")){
            String level = intent.getStringExtra("level");
            switch (level){
                case "sss":
                    ivScore.setImageResource(R.drawable.sss);
                    ivResult.setImageResource(R.drawable.sss_result);
                    break;
                case "s":
                    ivScore.setImageResource(R.drawable.s);
                    ivResult.setImageResource(R.drawable.s_result);
                    break;
                case "a":
                    ivScore.setImageResource(R.drawable.a);
                    ivResult.setImageResource(R.drawable.a_result);
                    break;
                case "b":
                    ivScore.setImageResource(R.drawable.b);
                    ivResult.setImageResource(R.drawable.b_result);
                    break;
                case "c":
                    ivScore.setImageResource(R.drawable.c);
                    ivResult.setImageResource(R.drawable.c_result);
                    break;
                case "d":
                    ivScore.setImageResource(R.drawable.d);
                    ivResult.setImageResource(R.drawable.f_result);
                    break;
                case "e":
                    ivScore.setImageResource(R.drawable.e);
                    ivResult.setImageResource(R.drawable.f_result);
                    break;
                case "f":
                    ivScore.setImageResource(R.drawable.f);
                    ivResult.setImageResource(R.drawable.f_result);
                    break;
                case "error":
                    ivScore.setImageResource(R.drawable.failed);
                    ivResult.setImageResource(R.drawable.failed_result);
                    break;
            }
        }

        findViewById(R.id.iv_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
