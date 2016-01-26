package com.zxjdev.atdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    private TextView mTvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mTvMessage = (TextView) findViewById(R.id.tv_user_name);

        String message = getIntent().getStringExtra("message");
        mTvMessage.setText(message);
    }
}
