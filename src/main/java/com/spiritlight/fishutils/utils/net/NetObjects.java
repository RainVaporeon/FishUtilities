package com.spiritlight.fishutils.utils.net;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class NetObjects {

    /**
     * Creates the proxy with given parameters
     * @param type the proxy type
     * @param addr the address
     * @return the proxy
     */
    public static Proxy createProxy(Proxy.Type type, SocketAddress addr) {
        return new Proxy(type, addr);
    }

    /**
     * Creates the proxy with given parameters
     * @param type the proxy type
     * @param ip the ip of the proxy
     * @param port the port of the proxy
     * @return the proxy
     */
    public static Proxy createProxy(Proxy.Type type, String ip, int port) {
        return new Proxy(type, createAddress(ip, port));
    }

    /**
     * Creates the proxy with given parameters, with the port defaulted to 80
     * @param type the proxy type
     * @param ip the ip of the proxy
     * @return the proxy
     */
    public static Proxy createProxy(Proxy.Type type, String ip) {
        return new Proxy(type, createAddress(ip));
    }

    /**
     * Creates a new InetSocketAddress with the ip and default port 80,
     * or if the input format includes ports, use that port instead.
     * @param ip the ip
     * @return the created address
     */
    public static SocketAddress createAddress(String ip) {
        if(ip.contains(":")) {
            String[] s = ip.split(":");
            int port = Integer.parseInt(s[1]);
            return createAddress(s[0], port);
        }
        return createAddress(ip, 80);
    }

    /**
     * Creates a new InetSocketAddress with the ip and port
     * @param ip the ip
     * @param port the port
     * @return the created address
     */
    public static SocketAddress createAddress(String ip, int port) {
        return new InetSocketAddress(ip, port);
    }
}
