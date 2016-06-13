package com.zxjdev.atdemo;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MySpan extends ClickableSpan {

    private String userId;
    private String nickname;

    private OnSpanClickListener clickListener;

    public MySpan(String userId, String nickname) {
        this(userId, nickname, null);
    }

    public MySpan(String userId, String nickname, OnSpanClickListener listener) {
        this.userId = userId;
        this.nickname = nickname;
        this.clickListener = listener;
    }

    public void onClick(View widget) {
        if (clickListener != null) clickListener.onSpanClick(this.userId, this.nickname);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    interface OnSpanClickListener {
        void onSpanClick(String userId, String nickname);
    }
}
