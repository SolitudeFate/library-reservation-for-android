package com.example.libraryreservationforandroid;

import java.io.*;
import java.net.*;
import java.util.*;

public class library_reservation {

    public static void main(String[] args) {
        String result = null;
        try {
            result = reserve("331", "186", "JSESSIONID=B6CA42F8F89A1B621F08F755A7125081");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);
    }

    public static String reserve(String room, String zwid, String cookie) throws Exception {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("201", "3fb64d94-0901-4683-be56-a9ada9804399");
        mapping.put("205", "3fb64d94-0901-4683-be56-a9ada9804399");
        mapping.put("232", "c710ea4b-91ae-4a9d-a3f2-ca7e31460b68");
        mapping.put("331", "bca546ce-1e6c-4b4d-9e45-a20aba076687");
        mapping.put("332", "601a3d02-4696-49e1-b9c6-120f0ea59465");
        mapping.put("302", "69eb5d3c-34a8-4b15-9a0d-01b3cc02f29c");
        mapping.put("306", "69eb5d3c-34a8-4b15-9a0d-01b3cc02f29c");
        mapping.put("431", "5388800a-4e31-4b23-92d7-f767c6f63c05");
        mapping.put("432", "d989e223-e0ce-4375-90ba-8d010f8b0735");

        String gsid = mapping.get(room);
        String starttime = "8:00";
        String endtime = "22:00";

        // 获取明天日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String url = "https://bgweixin.sspu.edu.cn/app/readroom/ylsyySave.do";

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x6309001c) XWEB/6500";

        String referer = "https://bgweixin.sspu.edu.cn/app/readroom/index.do";

        Map<String, String> data = new HashMap<>();
        data.put("gsid", gsid);
        data.put("zwid", zwid);
        data.put("day", Integer.toString(day));
        data.put("month", Integer.toString(month));
        data.put("starttime", starttime);
        data.put("endtime", endtime);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // 设置请求头
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", userAgent);
        con.setRequestProperty("Cookie", cookie);
        con.setRequestProperty("Referer", referer);

        // 设置请求体
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(getParamsString(data));
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        return response.toString();
    }

    // 将Map转换成POST请求的参数字符串
    private static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
    }

}

