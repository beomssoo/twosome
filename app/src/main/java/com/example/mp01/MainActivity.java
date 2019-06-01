package com.example.mp01;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


//import com.example.mp01.service.JobSchedulerStart;

import com.example.mp01.Fragment.Menu2Fragment;
import com.example.mp01.Fragment.Menu3Fragment;
import com.example.mp01.service.JobSchedulerStart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    //XML UI 선언
    EditText input;
    Button button;
    TextView output;


    TextView webPasingSubList1;
    TextView webPasingSubList2;

    String HTMLPageURL = "https://search.naver.com/search.naver?ie=utf8&query=";
    String HTMLContentInStringFormat = "";
    String subItemList1 = "";
    String subItemList2 = "";
    PendingIntent intent;

    NotificationManager notificationManager;
    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;
    int price = -1;
    int lowPrice = 99999999;
    Menu2Fragment menu2Fragment;
    Menu3Fragment menu3Fragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        //XML Inflation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("tag", "onCreate()");
        final LinearLayout linear2 = findViewById(R.id.linear2);
        final LinearLayout linear1 = findViewById(R.id.linear1);
        //menu2Fragment = (Menu2Fragment) getSupportFragmentManager().findFragmentById(R.id.jaesin);
        final ViewPager mainViewPager = findViewById(R.id.mainViewPager);
        mainViewPager.setAdapter(new BottomNavigationAdapter(getSupportFragmentManager()));


        final BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        mainViewPager.setCurrentItem(0);
                        return true;
                    case R.id.side:
                        mainViewPager.setCurrentItem(1);
                        return true;
                    case R.id.notification:
                        mainViewPager.setCurrentItem(2);
                        return true;
                    default:
                        return false;

                }
            }
        });


        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);
            }


            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


        //UI 끌당
        input = findViewById(R.id.input);
        button = findViewById(R.id.button);
        output = findViewById(R.id.output);
        webPasingSubList1 = findViewById(R.id.webPasingSubItemList1);
        webPasingSubList2 = findViewById(R.id.webPasingSubItemList2);
        output.setMovementMethod(new ScrollingMovementMethod());
        intent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "웹크롤링 중입니다...", Toast.LENGTH_LONG).show();
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }
        });
        //혹시 예전에 썻던 쿼리가 있으면 써야지
        applySharedPreference();


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.notification_channel_id);
            CharSequence channelName = getString(R.string.notification_channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        JobSchedulerStart.start(this); // 푸시설정 버튼 누른 이후에 이것을 부를것*/
    } // 여기까지가 onCreate?

    public void applySharedPreference() {
        sh_Pref = getSharedPreferences("Previous query", MODE_PRIVATE);
        if (sh_Pref != null && sh_Pref.contains("storedQuery")) {
            String storedQuery = sh_Pref.getString("storedQuery", "이주형 교수님 짱");
            input.setText(storedQuery);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // create menu
        menu.add(0, 0, 50, "Push notification").setIcon(R.drawable.push).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 1, 50, "Info").setIcon(R.drawable.info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 2, 50, "Guideline").setIcon(R.drawable.info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("tag", "onPreExecute()");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("tag", "doInBackground()");

                String sample = "";
                String sub = "";
                String userQuery = input.getText().toString();
                String modifiedKeyword;

                HTMLPageURL += userQuery;
                Document doc = Jsoup.connect(HTMLPageURL).get();
                Log.d("tag", "\nHTMLPageURL(modified) : " + HTMLPageURL);


                //여기부터 리스트뷰, 여기에 걸리면 갤러리뷰엔 걸리지 않습니다.
                //최저가를 따고
                Elements titles = doc.select("div.info_price em.num");
                //Elements a = doc.select("img.https://shopping-phinf.pstatic.net/main_1791792/17917923062.20190308142555.jpg?type=f190");
                //함꼐 ~한 상품을 땁니다.
                Elements subItems = doc.select("ul.product_list li.product_item div.info");
                //파싱한 데이타들을 전처리해주면서, 최저가를 찾습니다.
                for (Element e : titles) {
                    sample = e.text();
                    sample = sample.replaceAll("\\,", "");
                    price = Integer.parseInt(sample);
                    lowPrice = price;

                }
                //함께 찾아본 상품, 함께 살만한 상품 둘다 각각 5개씩 찾습니다.
                int subCount = 0;
                //리스트뷰에서는 함께 찾아본 상품, 함께 살만한 상품을 지원합니다. 갤러리뷰에서는 그렇지 않지요
                for (Element e : subItems) {
                    //함께 찾아본 상품
                    if (subCount < 5) {
                        sub = e.text();
                        subItemList1 += sub + "\n";
                        subCount++;
                    }
                    //함께 살만한 상품
                    else {
                        sub = e.text();
                        subItemList2 += sub + "\n";
                    }
                }


                //여기가 갤러리뷰, 리스트뷰에 걸렸다면 이곳에 걸리지 않습니다.
                //최저가를 땁니다.
                titles = doc.select("em.price_num");
                //전처리를 하면서, 최저가를 찾습니다.
                for (Element e : titles) {
                    sample = e.text();
                    int index = sample.indexOf("저");
                    sample = sample.substring(index + 1, sample.length());
                    index = sample.indexOf("원");
                    sample = sample.substring(0, index);
                    sample = sample.replaceAll("\\,", "");
                    Log.d("tag", sample);
                    price = Integer.parseInt(sample);
                    if (price < lowPrice) {
                        lowPrice = price;
                    }
                }
                //혹시라도 검색어를 잘못 입력했다면, 잘못 입력된 검색어 말고 보통 어떤 올바른 검색어로 검색하는지 찾습니다.
                titles = doc.select("div.sp_keyword dd em");
                modifiedKeyword = titles.text();
                //검색어를 제대로 입력한 경우
                if (modifiedKeyword == "") {
                    Log.d("tag", "ModifiedKeyword(\"\") : " + modifiedKeyword);
                    HTMLContentInStringFormat = userQuery + "로 검색하니 " + lowPrice + "라는 값이 최저가로 나왔습니다.";
                }
                //검색어를 제대로 입력하지 않았지만, 검색하고 싶은 것을 어림 잡을 수 있는 경우
                if (modifiedKeyword != "") {
                    Log.d("tag", "ModifiedKeyword(not \"\") : " + modifiedKeyword);
                    HTMLContentInStringFormat = "검색어가 잘못 된 것 같네요, 하지만 " + modifiedKeyword + "로 검색하니 " + lowPrice + "라는 값이 최저가로 나왔습니다.";
                    if (lowPrice == 99999999) {
                        HTMLContentInStringFormat = "검색어가 잘못 된 것 같네요, " + modifiedKeyword + "로 검색해 보세요!";
                    }
                }
                //검색어를 제대로 입력하지도 않았고, 무엇을 검색하려는지 알 수 없을 경우.
                if (modifiedKeyword == "" & lowPrice == 99999999) {
                    HTMLContentInStringFormat = "검색어가 잘못 된 것 같네요.";
                }
                //셰어드프리퍼런스에 최근 유저쿼리(방금 쓴 거) 저-장
                sharedPreference(userQuery);
            }
            //Log메시지가 주석을 대신합니다.
            catch (NumberFormatException e001) {
                Log.d("tag", "이 에러는 190525에 처리 되었지만, 혹시나 해서 익셉션을 지우지는 않았습니다.");
                HTMLContentInStringFormat = "좀 더 구체적인 제품명으로 검색해 주십시오";
                subItemList1 = "그 키워드로는";
                subItemList2 = "기준이 명확하지가 않습니다";
            } catch (IOException e002) {
                Log.d("tag", "이 오류는, 인터넷 연결이 원활하지 않을때 일어납니다.");
                HTMLContentInStringFormat = "인터넷 연결이 원활하지 않으면 웹파싱이 어렵습니다.";
                subItemList1 = "LTE를 쓰던가!!!!!!";
                subItemList2 = "WIFI를 잡으라 이 말이야!";
            } catch (Exception e) {
                Log.d("tag", "무슨 에러일까? :" + e);
                HTMLContentInStringFormat = "이 에러는";
                subItemList1 = "테스트와 디버그를";
                subItemList2 = "충분히 하지못한 개발자 탓 입니다.";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("tag", "onPostExecute()");
            HTMLContentInStringFormat = "이 제품의 최저가는 " + lowPrice + "원입니다.";
            output.setText(HTMLContentInStringFormat);
            //Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            Bundle myBundle = new Bundle();
            myBundle.putString("content1", subItemList1);
            myBundle.putString("content2", subItemList2);
            //intent.putExtras(myBundle);
            // startActivity(intent);
            HTMLPageURL = "https://search.naver.com/search.naver?ie=utf8&query=";
            HTMLContentInStringFormat = "";
            subItemList1 = "";
            subItemList2 = "";

        }
    }

    public void sharedPreference(String query) {
        sh_Pref = getSharedPreferences("Previous query", MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        toEdit.putString("storedQuery", query);
        toEdit.commit();
    }

}