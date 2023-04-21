package com.example.libraryreservationforandroid;

import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.Python;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView tv_room;
    private TextView tv_seat;
    private TextView tv_cookie;
    private TextView tv_card_title;
    private TextView tv_secondary;
    private TextView tv_response;
    private Python py;
    private SharedPreferences preferences;
    int count;
    private ImageView img_wx;
    private View btn_mar;
    private int btn_dp_top1;
    private int btn_dp_top2;
    private int img_dp_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_room = findViewById(R.id.tv_room);
        tv_seat = findViewById(R.id.tv_seat);
        tv_cookie = findViewById(R.id.tv_cookie);
        tv_card_title = findViewById(R.id.tv_card_title);
        tv_secondary = findViewById(R.id.tv_secondary);
        tv_response = findViewById(R.id.tv_response);
        img_wx = findViewById(R.id.weixin);
        btn_mar = findViewById(R.id.btn_mar);
        btn_dp_top1 = dip2px(this, 60);
        btn_dp_top2 = dip2px(this, -8);
        img_dp_top = dip2px(this, -5);
        count = 0;

        findViewById(R.id.card).setOnClickListener(this);
        findViewById(R.id.card).setOnLongClickListener(this);
        findViewById(R.id.btn_reserve).setOnClickListener(this);

        initPython();
        py = Python.getInstance();

        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        reload();
    }

    private void reload() {
        String room = preferences.getString("room", null);
        if (room != null) {
            tv_room.setText(room);
        }

        String seat = preferences.getString("seat", null);
        if (seat != null) {
            tv_seat.setText(seat);
        }

        String jsessionid = preferences.getString("jsessionid", null);
        if (jsessionid != null) {
            tv_cookie.setText(jsessionid);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reserve:
                String room = tv_room.getText().toString();
                String seat = tv_seat.getText().toString();
                String jsessionid = tv_cookie.getText().toString();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("room", room);
                editor.putString("seat", seat);
                editor.putString("jsessionid", jsessionid);
                editor.commit();

                PyObject obj1 = py.getModule("reflection")
                        .callAttr("room_to_gsid",new Kwarg("room", room));
                PyObject obj2 = py.getModule("reflection")
                        .callAttr("get_month");
                PyObject obj3 = py.getModule("reflection")
                        .callAttr("get_day");

                String gsid = obj1.toJava(String.class);
                String month = obj2.toJava(String.class);
                String day = obj3.toJava(String.class);

                try {
                    // 创建 OkHttpClient 对象
                    OkHttpClient client = new OkHttpClient();

                    // 创建 FormBody 对象，用于存储请求参数
                    FormBody.Builder formBuilder = new FormBody.Builder()
                            .add("gsid", gsid)
                            .add("zwid", seat)
                            .add("day", day)
                            .add("month", month)
                            .add("starttime", "8:00")
                            .add("endtime", "22:00");
                    RequestBody requestBody = formBuilder.build();

                    // 创建 Request 对象
                    Request request = new Request.Builder()
                            .url("https://bgweixin.sspu.edu.cn/app/readroom/ylsyySave.do")
                            .post(requestBody)
                            .addHeader("Cookie", jsessionid)
                            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6309001c) XWEB/6500")
                            .addHeader("Referer", "https://bgweixin.sspu.edu.cn/app/readroom/index.do")
                            .build();

                    // 发送请求
                    Response response = client.newCall(request).execute();

                    // 获取响应体
                    String responseBody = response.body().string();

                    if (responseBody.equals("\"\"")){
                        tv_response.setText(R.string.success);
                    } else {
                        tv_response.setText(responseBody);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    tv_response.setText(R.string.retry);
                }
                break;
            case R.id.card:
                if (count < 10) {
                    count++;
                }
                if (count == 0) {
                    tv_response.setText(R.string.initialization);
                } else if (count == 1) {
                    tv_response.setText(R.string.one);
                } else if (count == 2) {
                    tv_response.setText(R.string.two);
                } else if (count == 3) {
                    tv_response.setText(R.string.three);
                } else if (count == 4) {
                    tv_response.setText(R.string.four);
                } else if (count == 5) {
                    tv_response.setText(R.string.five);
                } else if (count == 6) {
                    tv_response.setText(R.string.six);
                } else if (count == 7){
                    tv_response.setText(R.string.seven);
                } else if (count == 8){
                    tv_response.setText(R.string.eight);
                    img_wx.setImageResource(R.drawable.weixin);

                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(img_wx.getLayoutParams());
                    lp1.setMargins(0, img_dp_top, 0, 0);
                    img_wx.setLayoutParams(lp1);

                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(btn_mar.getLayoutParams());
                    lp2.setMargins(0, btn_dp_top2, 0, 0);
                    btn_mar.setLayoutParams(lp2);
                } else {

                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(btn_mar.getLayoutParams());
                    lp2.setMargins(0, btn_dp_top1, 0, 0);
                    btn_mar.setLayoutParams(lp2);

                    img_wx.setImageDrawable(null);
                    tv_response.setText(R.string.others);
                }
                break;
        }
    }

    void initPython() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        tv_response.setText(R.string.initialization);
        count = -1;
        img_wx.setImageDrawable(null);
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 转成为 px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}