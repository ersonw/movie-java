package com.telebott.moviejava.util;

import com.telebott.moviejava.entity.ShowPay;
import com.telebott.moviejava.entity.ToPayData;
import com.telebott.moviejava.entity.ToPayNotify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ShowPayUtil {
    public static String request(String httpUrl, String body) {
        BufferedReader reader = null;
        String result = null;
        StringBuilder sbf = new StringBuilder();
//        httpUrl = httpUrl + "?" + httpArg;
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty( "Content-Length", Integer.toString( data.length ));
            connection.setUseCaches( false );
            connection.connect();
            try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
                wr.write( data );
            }
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String strRead = reader.readLine();
            if (strRead != null) {
                sbf.append(strRead);
                while ((strRead = reader.readLine()) != null) {
                    sbf.append("\n");
                    sbf.append(strRead);
                }
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
    public static String getSign(ShowPay showPay, ToPayData toPayData){
        return null;
    }
    public static boolean verifySign(ToPayNotify toPayNotify){
        return false;
    }

}