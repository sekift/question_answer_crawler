/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.proxy;

import java.util.List;

/**
 * Created by Administrator on 2015/9/25.
 */
public interface ProxyPool {
    public void addProxy(List<String> httpProxyList);

    public Proxy getProxyHost();

    public void returnProxy(Proxy proxy);

}
