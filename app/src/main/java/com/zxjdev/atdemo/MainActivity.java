package com.zxjdev.atdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*public void updateOutput(Spannable ss) {
        PlatoSpan[] spans1 = ss.getSpans(0, mEtSpannable.length(), PlatoSpan.class);

        // 对span对象进行排序，在字符串靠前的排在前面
        int position;
        for (int i = 0; i < spans1.length; i++) {
            int j = i + 1;
            position = i;
            PlatoSpan temp = spans1[i];
            for (; j < spans1.length; j++) {
                if (ss.getSpanStart(spans1[j]) < ss.getSpanStart(temp)) {
                    temp = spans1[j];
                    position = j;
                }
            }
            spans1[position] = spans1[i];
            spans1[i] = temp;
        }

        StringBuffer sb = new StringBuffer(ss);

        for (int i = spans1.length; i > 0; i--) {
            int spanStart = ss.getSpanStart(spans1[i - 1]);
            int spanEnd = ss.getSpanEnd(spans1[i - 1]);
            sb.replace(spanStart, spanEnd, "<" + spans1[i - 1].userid + ">");
        }
        mTvSpannable.setText(sb.toString());
    }

    public void insert(String text) {
        int start = mEtSpannable.getSelectionStart();
        mEtSpannable.getText().insert(start, text);
        PlatoSpan span = new PlatoSpan() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "柏拉图 click");
            }
        };
        span.userid = "789";
        mEtSpannable.getText().setSpan(span, start, start + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }*/
}
