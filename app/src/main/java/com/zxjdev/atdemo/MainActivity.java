package com.zxjdev.atdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_USER_LIST = 1;

    private Context mContext;
    private EditText mEtInput;
    private TextView mTvDisplay, mTvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        mEtInput = (EditText) findViewById(R.id.et_input);
        mTvDisplay = (TextView) findViewById(R.id.tv_display);
        mTvData = (TextView) findViewById(R.id.tv_data);

        findViewById(R.id.btn_at).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userList = new Intent(mContext, UserListActivity.class);
                startActivityForResult(userList, REQUEST_USER_LIST);
            }
        });
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateDisplay(mEtInput.getText());
                updateData(mEtInput.getText());
            }
        });
        mEtInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        // 处理删除事件，在选中范围内的span都需要被删除
                        int selectionStart = mEtInput.getSelectionStart();
                        int selectionEnd = mEtInput.getSelectionEnd();
                        MySpan[] spans = mEtInput.getText().getSpans(0, mEtInput.length(), MySpan.class);
                        for (MySpan span : spans) {
                            int spanStart = mEtInput.getText().getSpanStart(span);
                            int spanEnd = mEtInput.getText().getSpanEnd(span);

                            if (selectionStart > spanStart && selectionStart <= spanEnd) {
                                mEtInput.setSelection(spanStart, selectionEnd);
                                selectionStart = spanStart;
                            }

                            if (selectionEnd >= spanStart && selectionEnd < spanEnd) {
                                mEtInput.setSelection(selectionStart, spanEnd);
                                selectionEnd = spanEnd;
                            }
                        }
                    }
                }
                return false;
            }
        });
        mTvDisplay.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_USER_LIST && resultCode == RESULT_OK) {
            insertAt((User) data.getParcelableExtra(User.TAG));
            updateDisplay(mEtInput.getText());
            updateData(mEtInput.getText());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 在输入框光标处插入"@somebody"
     * @param user 要插入的用户
     */
    public void insertAt(final User user) {
        int start = mEtInput.getSelectionStart();
        mEtInput.getText().insert(start, "@" + user.getNickname());
        MySpan span = new MySpan(user) {
            @Override
            public void onClick(View view) {
                Log.d(TAG, user.getNickname() + " click");
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("message", user.getNickname());
                startActivity(intent);
            }
        };
        mEtInput.getText()
                .setSpan(span, start, start + user.getNickname().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void updateDisplay(CharSequence content) {
        mTvDisplay.setText(content);
    }

    private void updateData(Spannable ss) {
        MySpan[] spans = ss.getSpans(0, ss.length(), MySpan.class);
        // 对span对象进行排序，在字符串靠前的排在前面
        int position;
        for (int i = 0; i < spans.length; i++) {
            int j = i + 1;
            position = i;
            MySpan temp = spans[i];
            for (; j < spans.length; j++) {
                if (ss.getSpanStart(spans[j]) < ss.getSpanStart(temp)) {
                    temp = spans[j];
                    position = j;
                }
            }
            spans[position] = spans[i];
            spans[i] = temp;
        }

        String pattern = "<%s,%s>";
        StringBuilder sb = new StringBuilder(ss);
        for (int i = spans.length; i > 0; i--) {
            int spanStart = ss.getSpanStart(spans[i - 1]);
            int spanEnd = ss.getSpanEnd(spans[i - 1]);
            sb.replace(spanStart, spanEnd,
                    String.format(pattern, spans[i - 1].getUser().getUserId(), spans[i - 1].getUser().getNickname()));
        }
        mTvData.setText(sb.toString());
    }
}
