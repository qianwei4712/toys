package cn.shiva.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author shiva   2020/6/5 20:22
 */
public class HttpUtil {

    public static String get(String strUrl) throws Exception {
        StringBuilder sb = new StringBuilder();
        URL url = new URL(strUrl);
        //专为http特性做的URLconnection
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String temp = null;
        while ((temp = br.readLine()) != null) {
            sb.append(temp).append("\r\n");
        }
        return sb.toString();
    }


    public static void main(String[] args) throws Exception {
        String s = get(Consts.URL_PREFIX + 1 + Consts.URL_SUFFIX);
        String s1 = CommonUtil.subHtml(s);
        String[] items = s1.split("<tr>");
        //遍历每一期
        for (String item : items) {
            System.out.println(item);
            String[] split = item.split("<td");
            for (int i = 0; i < 10; i++) {
                System.out.println(1);
            }
        }
        System.out.println(s1);
    }

}
