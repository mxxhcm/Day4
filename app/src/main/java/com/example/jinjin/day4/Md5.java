package com.example.jinjin.day4;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by MFY-Here-Courage on 2017/3/18.
 */
public class Md5 {
    public String toMD5(String unencode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md=MessageDigest.getInstance("MD5");
        md.update(unencode.getBytes("utf-8"));
        //将MD5的加密值返回并且转换为16进制结果
        return new BigInteger(md.digest()).toString(16);
    }
}
