package com.law.hongcaphelp.common;

import java.util.LinkedHashMap;

import com.google.gson.JsonObject;


/*
 * web연결로 학생에게 보여줄 문제와 보기를 서버로 전달함
 */
public class Common extends HttpConnect {

    public static String BASE_URL = "http://192.168.2.100:8080";
    public static String BASE_URL2 = "http://192.168.43.6:8080";

    
    public static final String POST_SERVER_URL = BASE_URL2 + "/HelpCap/QA";
    public static final String  LEAK_SERVER_URL = BASE_URL2 + "/HelpCap/QA?kind=PHONE";

    //그냥 GET으로 요청 및 응답
    public String connect(String url) {
        return getStringFromInputstream(getInputstreamFromUrl(url));
    }

    //key와 value 있는 GET , GET은 HashMap으로 한다.
    public String connect(String url, LinkedHashMap<String, String> keyVal) {
        return getStringFromInputstream(getInputstreamFromUrl(getUrlForGET2(url, keyVal)));
    }

    //POST , POST는 json으로 한다.
    public String connect(String url, JsonObject keyVal) {
        return getStringFromInputstream(getInputstreamFromUrl(url, keyVal));
    }

    //MULTI, Mulit의 Post는 HashMap으로 한다.
    public String connect(String url, LinkedHashMap<String, String> keyVal, LinkedHashMap<String, String> fileMap) {
        return getStringFromInputstream(getInputstreamFromUrl(url, keyVal, fileMap));
    }


    private Common() {
    }

    //singleton방식으로 클래스 제작
    private static class Singleton {
        private static final Common common = new Common();
    }

    public static Common getInstance() {
        return Singleton.common;
    }
    
}
