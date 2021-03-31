package com.zh.ch.bigdata.base.util.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xzc
 * @description 获取本地hostname
 * @date 2020/11/04
 */
public class InetAddressUtil {

    public static String getLocalHostName() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName();
    }

    public static void main(String[] args) throws UnknownHostException{
        System.out.print(getLocalHostName());
    }
}
