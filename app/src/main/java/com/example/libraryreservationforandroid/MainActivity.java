package com.example.libraryreservationforandroid;

import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.Python;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_room;
    private TextView tv_seat;
    private TextView tv_cookie;
    private TextView tv_card_title;
    private TextView tv_secondary;
    private TextView tv_response;
    private Python py;

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

        findViewById(R.id.btn_reserve).setOnClickListener(this);

        initPython();
        py = Python.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reserve:
                String room = tv_room.getText().toString();
                String seat = tv_seat.getText().toString();
                String jsessionid = tv_cookie.getText().toString();

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

                    if (responseBody == ""){
                        tv_response.setText(R.string.success);
                    } else {
                        tv_response.setText(responseBody);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    tv_response.setText(R.string.retry);
                }
                break;
        }
    }

    void initPython() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
    }

}