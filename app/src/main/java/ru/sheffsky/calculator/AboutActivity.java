package ru.sheffsky.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView textView = (TextView) findViewById(R.id.donateLink);
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

    }

}