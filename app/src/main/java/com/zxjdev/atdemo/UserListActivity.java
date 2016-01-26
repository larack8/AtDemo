package com.zxjdev.atdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private ListView mLvUsers;
    private ArrayAdapter<User> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mLvUsers = (ListView) findViewById(R.id.lv_users);
        mAdapter = new ArrayAdapter<>(UserListActivity.this, android.R.layout.simple_list_item_1);
        mLvUsers.setAdapter(mAdapter);
        mLvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                Intent data = new Intent();
                data.putExtra(User.TAG, user);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        mAdapter.addAll(mockUpData());
    }

    private List<User> mockUpData() {
        List<User> users = new ArrayList<>();
        users.add(new User("#001", "Bulbasaur"));
        users.add(new User("#002", "Ivysaur"));
        users.add(new User("#003", "Venusaur"));
        users.add(new User("#004", "Charmander"));
        users.add(new User("#005", "Charmeleon"));
        users.add(new User("#006", "Charizard"));
        users.add(new User("#007", "Squirtle"));
        users.add(new User("#008", "Wartortle"));
        return users;
    }
}
