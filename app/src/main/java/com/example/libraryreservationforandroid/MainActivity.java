package com.example.libraryreservationforandroid;

import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.Python;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

//                try {
//                    PyObject obj = py.getModule("library_reservation").callAttr("reserve",
//                            new Kwarg("room", room), new Kwarg("zwid", seat), new Kwarg("cookie", jsessionid));
//                    String result = obj.toJava(String.class);
//                    if (result == "") {
//                        tv_response.setText(R.string.success);
//                    } else {
//                        tv_response.setText(result);
//                    }
//                } catch (Exception e) {
//                    tv_response.setText(R.string.retry);
//                }

                PyObject obj = py.getModule("library_reservation").callAttr("reserve",
                        new Kwarg("room", room), new Kwarg("zwid", seat), new Kwarg("cookie", jsessionid));
                String result = obj.toJava(String.class);
                if (result == "") {
                    tv_response.setText(R.string.success);
                } else {
                    tv_response.setText(result);
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