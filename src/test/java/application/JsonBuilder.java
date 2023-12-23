package application;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class JsonBuilder {
    public static String toJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
    public static String addCredit(String credit) throws Exception {
        var map = new HashMap<String, String>();
        map.put("credit", credit);
        return toJsonString(map);
    }

    public static String rateCommodity(String username, String rate) throws Exception {
        var map = new HashMap<String, String>();
        map.put("username", username);
        map.put("rate", rate);
        return toJsonString(map);
    }

    public static String addComment(String username) throws Exception {
        var map = new HashMap<String, String>();
        map.put("username", username);
        map.put("comment", "this is a comment");
        return toJsonString(map);
    }

    public static String searchCommodities(String searchValue, String searchOption) throws Exception {
        var map = new HashMap<String, String>();
        map.put("searchOption", searchOption);
        map.put("searchValue", searchValue);
        return toJsonString(map);
    }
}

