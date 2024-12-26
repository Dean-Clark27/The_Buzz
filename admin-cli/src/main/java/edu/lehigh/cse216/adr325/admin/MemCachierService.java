package edu.lehigh.cse216.adr325.admin;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.lang.InterruptedException;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MemCachierService {
    private static MemcachedClient client;

    static {
        try {
            // Get the servers and credentials from the environment
            List<InetSocketAddress> servers = AddrUtil.getAddresses(System.getenv("MEMCACHED_SERVERS").replace(",", " "));
            AuthInfo authInfo = AuthInfo.plain(System.getenv("MEMCACHED_USERNAME"),
                                               System.getenv("MEMCACHED_PASSWORD"));

            // Make builder
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);

            // Configure SASL auth for each server
            for(InetSocketAddress server : servers) {
                builder.addAuthInfo(server, authInfo);
            }
            
            // Use binary protocol
            builder.setCommandFactory(new BinaryCommandFactory());
            // Connection timeout in milliseconds (default: )
            builder.setConnectTimeout(1000);
            // Reconnect to servers (default: true)
            builder.setEnableHealSession(true);
            // Delay until reconnect attempt in milliseconds (default: 2000)
            builder.setHealSessionInterval(2000);

            // Create the client
            client = builder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void set(String key, int exp, Object value) {
        try {
            client.set(key, 0, value);
        }catch (TimeoutException te) {
            System.err.println("Timeout during set or get: " + te.getMessage());
        } catch (InterruptedException ie) {
            System.err.println("Interrupt during set or get: " + ie.getMessage());
        } catch (MemcachedException me) {
            System.err.println("Memcached error during get or set: " + me.getMessage());
        }
    }

    public static Object get(String key) {
        try {
            return client.get(key);
        } catch (TimeoutException te) {
            System.err.println("Timeout during get or set: " + te.getMessage());
        } catch (InterruptedException ie) {
            System.err.println("Interrupt during get or set: " + ie.getMessage());
        } catch (MemcachedException me) {
            System.err.println("Memcached error during get or set: " + me.getMessage());
        }
        return null;
    }
}