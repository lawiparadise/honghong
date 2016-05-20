package com.law.hongcaphelp.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

/*
 * http 연결
 */
public class HttpConnect {

	//url로부터 정보를 받아 inputstream을 얻어냄
    public InputStream getInputstreamFromUrl(String url) {
        int TIMEOUT = 3000;

        try {
            System.out.println("connect : " + url);

            URL connectUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

            //타임아웃 설정
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);

            //요청 편집 허가
/*                conn.setDoInput(true);
                conn.setDoOutput(true);*/

            //캐쉬를 꺼두자
            conn.setUseCaches(false);

            //keepalive는 도움이 된다.
            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestMethod("GET");

            //압축해서 보내자
            //          conn.setRequestProperty("Accept-Encoding", "gzip");

            //서버에게 응답 요청
            conn.connect();

            int HttpResult = conn.getResponseCode();
            System.out.println("getResponseCode : " + HttpResult);
            System.out.println("getResponseMessage : " + conn.getResponseMessage());

            InputStream is = conn.getInputStream();

            return is;

        } catch (MalformedURLException e) { //잘못된 url
            e.printStackTrace();
        } catch (SocketTimeoutException e) { //타임아웃
            e.printStackTrace();
        } catch (IOException e) { //네트워크 문제
            e.printStackTrace();
        } catch (Exception e) { //기타 문제
            e.printStackTrace();
        }
        return null;

    }

  //url로부터 정보를 받아 inputstream을 얻어냄
    public InputStream getInputstreamFromUrl(String url, JsonObject jsonObject) {
        int TIMEOUT = 3000;

        try {
            System.out.println("connect : " + url);
            URL connectUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

            //타임아웃 설정
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);

            //요청 편집 허가
                conn.setDoInput(true);
                conn.setDoOutput(true);

            //캐쉬를 꺼두자
            conn.setUseCaches(false);

            //keepalive는 도움이 된다.
            conn.setRequestProperty("Connection", "Keep-Alive");

            //압축해서 보내자
//            conn.setRequestProperty("Accept-Encoding", "gzip");

            //연결 방식
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes());
            os.flush();

            int HttpResult = conn.getResponseCode();
            System.out.println("getResponseCode : " + HttpResult);
            System.out.println("getResponseMessage : " + conn.getResponseMessage());

            InputStream is = conn.getInputStream();

            os.close();

            return is;


        } catch (MalformedURLException e) { //잘못된 url
            e.printStackTrace();
        } catch (SocketTimeoutException e) { //타임아웃
            e.printStackTrace();
        } catch (IOException e) { //네트워크 문제
            e.printStackTrace();
        } catch (Exception e) { //기타 문제
            e.printStackTrace();
        }
        return null;

    }

  //url로부터 정보를 받아 inputstream을 얻어냄
    public InputStream getInputstreamFromUrl(String url, LinkedHashMap<String, String> keyVal, LinkedHashMap<String, String> fileMap) {
        int TIMEOUT = 3000;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "************WEDIDIT************";

        try {
            System.out.println("connect : " + url);
            URL connectUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

            //타임아웃 설정
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);

            //요청 편집 허가
/*                conn.setDoInput(true);
                conn.setDoOutput(true);*/

            //캐쉬를 꺼두자
            conn.setUseCaches(false);

            //keepalive는 도움이 된다.
            conn.setRequestProperty("Connection", "Keep-Alive");

            //압축해서 보내자
//            conn.setRequestProperty("Accept-Encoding", "gzip");

            //연결 방식
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            //post보내기. hashmap에서 key와 value 꺼내서 formfield로 보내기.
            Set<Map.Entry<String, String>> set = keyVal.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
                writeFormField(dos, e.getKey(), e.getValue());
//                Log.e(e.getKey(), e.getValue());
            }

            //파일 보내기
            if (fileMap != null) {
                Set<Map.Entry<String, String>> fileSet = fileMap.entrySet();
                Iterator<Map.Entry<String, String>> it2 = fileSet.iterator();
                while (it2.hasNext()) {
                    Map.Entry<String, String> e = (Map.Entry<String, String>) it2.next();
                    writeFileField(dos, e.getKey(), e.getValue());
//                    Log.e(e.getKey(), e.getValue());
                }
            }


            //마지막
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();

/*            Log.e("MY", Integer.toString(conn.getResponseCode()));
            Log.e("test", conn.getResponseMessage());*/
            conn.connect();

            int ch;
            InputStream is = conn.getInputStream();
/*            StringBuffer b = new StringBuffer();

            while((ch = is.read()) != -1){
                b.append((char) ch);
            }
            String s = b.toString();
            Log.e("Test", "result = " + s);*/
            dos.close();

            return is;


        } catch (MalformedURLException e) { //잘못된 url
            e.printStackTrace();
        } catch (SocketTimeoutException e) { //타임아웃
            e.printStackTrace();
        } catch (IOException e) { //네트워크 문제
            e.printStackTrace();
        } catch (Exception e) { //기타 문제
            e.printStackTrace();
        }


        return null;

    }

  //inputstream으로 정보를 받아 string을 얻어냄
    public String getStringFromInputstream(InputStream is) {
        StringBuilder sb = null;

        if(is == null){
//            Log.e("my getStringFromInputstream", "fail");
            return "fail";
        }
        try {
            sb = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();

            System.out.println("" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

/*    public Bitmap connectBitmap(String urlStr) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);

            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
*/

/////////////////////////////////////////////////////////---form field ---///////////////////////////////////////////////////////////////////////////////////////

    //문자와 사진 둘 다 보내는 필드
    public void writeFormField(DataOutputStream dos, String key, String value) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "************WEDIDIT************";
        try {
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
//            dos.writeBytes(URLEncoder.encode(value, "utf-8")); //안됨
//            dos.write(value.getBytes("EUC_KR")); //안됨
            dos.write(value.getBytes("UTF-8"));
            dos.writeBytes(lineEnd);
//            Log.e(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
  //문자와 사진 둘 다 보내는 필드
    public void writeFormField2(DataOutputStream dos, String key, String value) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "************WEDIDIT************";
        try {
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(value); //굳이 write로 안바꿔도 잘 됨.
            dos.writeBytes(lineEnd);
//            Log.e(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
  //문자와 사진 둘 다 보내는 필드
    public void writeFormField(DataOutputStream dos, String key, int value) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "************WEDIDIT************";
        try {
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(value));
            dos.writeBytes(lineEnd);
//            Log.e(key, String.valueOf(value));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void writeFileField(DataOutputStream dos, String key, String value) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "************WEDIDIT************";

        try {
            FileInputStream mFileInputStream = new FileInputStream(value);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\";filename=\"" + value + "\"" + lineEnd); //filename을 경로로 설정하지 않으면 에러 뜸.
            dos.writeBytes("Content-Type: " + "image/jpeg" + lineEnd);
            dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //////////////////////////////////////////////////////////--- GET방식일 때 url 덧붙이기 ---////////////////////////////////////////////////////////////////////////////////////////
    public String getUrlForGET2(String url, LinkedHashMap<String, String> map) {
        StringBuffer sb = new StringBuffer();
        String book = null;
        //북마크가 들어올 일 없지만 일단 제거
        if (url.indexOf("#") > -1) {
            String[] urlmark = url.split("#");
            url = urlmark[0];
            book = urlmark[1];
        }

        String str = null;
        sb.append(url).append("?");
        Set<Map.Entry<String, String>> set = map.entrySet();
        Iterator<Map.Entry<String, String>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
            try{
//                str = new String(e.getValue().getBytes("euc-kr"), "utf-8");
                str = URLEncoder.encode(e.getValue());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            Log.e("my insert ", str);
            sb.append(e.getKey()).append("=").append(str);
            if (it.hasNext()) sb.append("&");
        }

        if (book != null) sb.append("#").append(book);

        //    Log.e("MY", sb.toString());

        return sb.toString();
    }

    
    public String getUrlForGET(String url, LinkedHashMap<String, String> map) {
        StringBuffer sb = new StringBuffer();
        String book = null;
        //북마크가 들어올 일 없지만 일단 제거
        if (url.indexOf("#") > -1) {
            String[] urlmark = url.split("#");
            url = urlmark[0];
            book = urlmark[1];
        }

        sb.append(url).append("?");
        Set<Map.Entry<String, String>> set = map.entrySet();
        Iterator<Map.Entry<String, String>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
            sb.append(e.getKey()).append("=").append(e.getValue());
            if (it.hasNext()) sb.append("&");
        }

        if (book != null) sb.append("#").append(book);

        //    Log.e("MY", sb.toString());

        return sb.toString();
    }

    //북마크용
    public String getUrlForGET(String url) {
        StringBuffer sb = new StringBuffer();
        String book = null;

        //북마크가 들어올 일 없지만 일단 제거
        if (url.indexOf("#") > -1) {
            String[] urlmark = url.split("#");
            url = urlmark[0];
            book = urlmark[1];
        }

        if (book != null) sb.append("#").append(book);

//        Log.e("MY", sb.toString());

        return sb.toString();
    }

    @Deprecated
    public String getUrlForGET(String url, String quest1, String ans1) {
        StringBuffer sb = new StringBuffer();
        String book = null;

        //북마크가 들어올 일 없지만 일단 제거
        if (url.indexOf("#") > -1) {
            String[] urlmark = url.split("#");
            url = urlmark[0];
            book = urlmark[1];
        }

        sb.append(url).append("?").append(quest1).append("=").append(ans1);

        if (book != null) sb.append("#").append(book);

//        Log.e("MY", sb.toString());

        return sb.toString();
    }


    @Deprecated
    public String getUrlForGET(String url, String quest1, String ans1, String quest2, String ans2) {
        StringBuffer sb = new StringBuffer();
        String book = null;

        //북마크가 들어올 일 없지만 일단 제거
        if (url.indexOf("#") > -1) {
            String[] urlmark = url.split("#");
            url = urlmark[0];
            book = urlmark[1];
        }

        sb.append(url).append("?").append(quest1).append("=").append(ans1).append("&").append(quest2).append("=").append(ans2);

        if (book != null) sb.append("#").append(book);

//        Log.e("MY", sb.toString());

        return sb.toString();
    }

    @Deprecated
    public String getUrlForGET(String url, String quest1, String ans1, String quest2, String ans2, String quest3, String ans3) {
        StringBuffer sb = new StringBuffer();
        String book = null;

        //북마크가 들어올 일 없지만 일단 제거
        if (url.indexOf("#") > -1) {
            String[] urlmark = url.split("#");
            url = urlmark[0];
            book = urlmark[1];
        }

        sb.append(url).append("?")
                .append(quest1).append("=").append(ans1).append("&")
                .append(quest2).append("=").append(ans2).append("&")
                .append(quest3).append("=").append(ans3);

        if (book != null) sb.append("#").append(book);

//        Log.e("MY", sb.toString());

        return sb.toString();
    }
}