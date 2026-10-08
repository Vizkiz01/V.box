package com.vbox.movies;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(Color.rgb(15, 15, 20));

        TextView title = new TextView(this);
        title.setText("V.BOX");
        title.setTextSize(36);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("Your movie universe starts here.");
        subtitle.setTextSize(16);
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 16, 0, 0);

        layout.addView(title);
        layout.addView(subtitle);

        setContentView(layout);
    }
          }
