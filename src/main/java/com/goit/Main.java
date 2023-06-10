package com.goit;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
//        String s = Base64.getEncoder().encodeToString("user12345".getBytes(StandardCharsets.UTF_8));
//        System.out.println(s);

        String encode = URLEncoder.encode("UTC+3", StandardCharsets.UTF_8);
        System.out.println(encode);
        String decode = URLDecoder.decode(encode);
        System.out.println(decode);
    }
}
