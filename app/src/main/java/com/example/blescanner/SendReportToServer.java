package com.example.blescanner;

import android.os.Bundle;
import android.util.Log;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SendReportToServer implements Runnable {

    private static String url_post_log = "http://95.161.210.44/ble_scanner_report.php";
    private String report;
    private OnTaskCompleted listener;

    public SendReportToServer(String report, OnTaskCompleted listener) {
        Log.d("TAG", "Sending log to server");
        this.report = report;
        this.listener = listener;
    }

    @Override
    public void run() {
        Log.d("TAG", "Send report to server");
        URL u;
        Bundle result = new Bundle();
        result.putInt("resultCode", 100);
        int code = -1;
        try {
            u = new URL(url_post_log);
            HttpURLConnection httpsURLConnection = getConnection(u);
            httpsURLConnection.connect();
            int response = httpsURLConnection.getResponseCode();

            BufferedReader br;
            StringBuilder content;
            InputStreamReader reader = new InputStreamReader(httpsURLConnection.getInputStream());
            br = new BufferedReader(reader);
            content = new StringBuilder();
            String line;
            while (null != (line = br.readLine())) {
                content.append(line);
            }
            Log.d("TAG", "Send report response: " + response);
            if(response == 200){
                JSONObject jsonObject = new JSONObject(content.toString());
                code = Integer.parseInt(jsonObject.getString("code"));
            } else {
                code = 0;
            }
            result.putInt("code", code);
            listener.onTaskCompleted(result);
            reader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("POST");
        c.setConnectTimeout(12000);
        c.setReadTimeout(15000);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("report", report));
        OutputStream os = c.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
        c.connect();
        return c;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (NameValuePair pair : params) {
            if (first){
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
