package com.demo.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class OtpStore {

    // global memory store
    public static Map<String,String> store = new ConcurrentHashMap<>();

}

