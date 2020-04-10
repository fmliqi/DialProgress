package com.liqi.roulettedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.liqi.roulettedemo.widget.CoreControlView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CoreControlView view = findViewById(R.id.c_view0);
        final TextView tv = findViewById(R.id.tv_number0);
        view.setOnControlChangeListener(new CoreControlView.OnControlChangeListener() {
            @Override
            public void onChange(int currentValue) {
                tv.setText(String.valueOf(currentValue));
            }

            @Override
            public void onDown() {

            }

            @Override
            public void onUp() {

            }
        });

        CoreControlView view1 = findViewById(R.id.c_view1);
        view1.setMinValue(0);
        view1.setMaxValue(10);

        final TextView tv1 = findViewById(R.id.tv_number1);
        view1.setOnControlChangeListener(new CoreControlView.OnControlChangeListener() {
            @Override
            public void onChange(int currentValue) {
                tv1.setText(String.valueOf(currentValue));
            }

            @Override
            public void onDown() {

            }

            @Override
            public void onUp() {

            }
        });
    }
}
