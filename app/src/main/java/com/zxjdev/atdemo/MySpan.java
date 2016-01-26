package com.zxjdev.atdemo;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public abstract class MySpan extends ClickableSpan {

    private User mUser;

    public MySpan(User user) {
        mUser = user;
    }

    public abstract void onClick(View widget);

    public User getUser() {
        return mUser;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
    }
}
