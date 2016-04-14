package com.zxjdev.atdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_USER_LIST = 1;

    private Context mContext;
    private EditText mEtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;

        mEtInput = (EditText) findViewById(R.id.et_input);

        findViewById(R.id.btn_at).setOnClickListener(this);
        findViewById(R.id.btn_preview_display).setOnClickListener(this);
        findViewById(R.id.btn_preview_data).setOnClickListener(this);

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
    }

    /**
     * 在输入框光标处插入"@somebody"
     *
     * @param user 要插入的用户
     */
    public void insertAt(final User user) {
        int start = mEtInput.getSelectionStart();
        mEtInput.getText().insert(start, "@" + user.getNickname());
        MySpan span = new MySpan(user) {
            @Override
            public void onClick(View view) {
                Log.d(TAG, user.getNickname() + " click");
                Toast.makeText(mContext, user.getNickname() + " is clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        mEtInput.getText()
                .setSpan(span, start, start + user.getNickname().length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 把输入的文字转换成发送给后台的数据,这里假定“@”相关的格式为<编号,名字>
     *
     * @param ss 输入框中的内容
     * @return 生成的数据
     */
    private String parseData(Spannable ss) {
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
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击“@”
            case R.id.btn_at:
                Intent userList = new Intent(mContext, UserListActivity.class);
                startActivityForResult(userList, REQUEST_USER_LIST);
                break;
            // 预览显示效果
            case R.id.btn_preview_display:
                showPreviewDisplay();
                break;
            // 预览数据
            case R.id.btn_preview_data:
                showPreviewData();
                break;
            default:
                break;
        }
    }

    private void showPreviewDisplay() {
        View view = getLayoutInflater().inflate(R.layout.dialog_preview, null);
        TextView tvDisplay = (TextView) view.findViewById(R.id.tv_display);
        TextView tvDescription = (TextView) view.findViewById(R.id.tv_description);
        tvDisplay.setText(mEtInput.getText());
        tvDisplay.setMovementMethod(LinkMovementMethod.getInstance());
        tvDescription.setText("预览在TextView中的显示效果,每个“@”对象都可点击");
        AlertDialog dialog = new AlertDialog.Builder(mContext).setView(view)
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void showPreviewData() {
        View view = getLayoutInflater().inflate(R.layout.dialog_preview, null);
        TextView tvDisplay = (TextView) view.findViewById(R.id.tv_display);
        TextView tvDescription = (TextView) view.findViewById(R.id.tv_description);
        tvDisplay.setText(parseData(mEtInput.getText()));
        tvDescription.setText("发送给后台的数据,假定“@”对象的格式为<编号,名称>");
        AlertDialog dialog = new AlertDialog.Builder(mContext).setView(view)
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
