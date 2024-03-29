package com.example.mp01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    TextView low_price;
    TextView pro_name;
    TextView pro_price;
    Button prev;

    private RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        /*
        prev = findViewById(R.id.previous);
        low_price = findViewById(R.id.output);
        pro_name = findViewById(R.id.prod_name);
        pro_price = findViewById(R.id.prod_price);
        Intent intent = getIntent();
        Bundle myBundle = intent.getExtras();
        String val2 = myBundle.getString("content1");
        String val3 = myBundle.getString("content2");

        pro_name.setText(val2);
        pro_price.setText(val3);




        prev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }
        });
        */





        ///////////////
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        ////////////


        init();

        getData();
    }

    ///////////////////////////
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립",
                "국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립");
        List<String> listContent = Arrays.asList(
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다.",
                "이 동물은 펭귄입니다.",
                "이 꽃은 튤립입니다.",
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다.",
                "이 동물은 펭귄입니다.",
                "이 꽃은 튤립입니다."
        );
        List<Integer> listResId = Arrays.asList(
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app,
                R.drawable.ic_app
        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }


}
