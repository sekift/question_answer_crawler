/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.proxy;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.lookup.dynamic.Constants;

/**
 * Pooled Proxy Object
 *
 * @author yxssfxwzy@sina.com <br>
 * @see Proxy
 * @since 0.5.1
 */
@Qualifier
public class FileProxyPool implements ProxyPool {

    private Logger logger = LoggerFactory.getLogger("proxy");

    private final static BlockingQueue<Proxy> proxyQueue = new DelayQueue<Proxy>();
    private final static Map<String, Proxy> allProxy = new ConcurrentHashMap<String, Proxy>();

    //private final static String proxyFilePath = "/www/client/crawler/config";
    private final static String proxyFileName = "proxy.txt";

    private final static int reuseInterval = 1500;// ms
    private final static int reviveTime = 2 * 60 * 60 * 1000;// ms
    private final static int saveProxyInterval = 10 * 60 * 1000;// ms

    private static boolean isEnable = false;
    private static boolean validateWhenInit = false;

    static class CounterLine implements LineProcessor<List<String>> {
        private List<String> lines = new ArrayList<String>(1000);

        @Override
        public boolean processLine(String line) throws IOException {
            if (line != null) {
                String[] lineString = line.split("@");
                if (lineString != null) {
                    lines.add(lineString[0]);
                }
            }
            return true;
        }

        @Override
        public List<String> getResult() {
            return lines;
        }
    }

    public FileProxyPool() {
    	// 不启动
        //initProxy();
    }

    public void addProxy(Proxy proxy) {
        proxy.setFailedNum(0);
        proxy.setReuseTimeInterval(reuseInterval);
        proxyQueue.add(proxy);
        allProxy.put(proxy.getHttpHost(), proxy);
    }

    @SuppressWarnings("unused")
	private void addProxy(Map<String, Proxy> httpProxyMap) {
        isEnable = true;
        for (Entry<String, Proxy> entry : httpProxyMap.entrySet()) {
            try {
                if (allProxy.containsKey(entry.getKey())) {
                    continue;
                }
                Proxy proxy = entry.getValue();
                if (!validateWhenInit && ProxyUtils.validateProxy(proxy)) {
                    entry.getValue().setFailedNum(0);
                    entry.getValue().setReuseTimeInterval(reuseInterval);
                    proxyQueue.add(entry.getValue());
                    allProxy.put(entry.getKey(), entry.getValue());
                }
            } catch (NumberFormatException e) {
                logger.error("HttpHost init error:", e);
            }
        }
        // logger.info("proxy pool size>>>>" + allProxy.size());
    }

    public void initProxy() {
        //  List<String> proxyList = mysqlServiceMapper.getProxyList("");
        //  addProxy(proxyList);
        // 读取文件方式
        String testFilePath = Constants.proxyFilePath + File.separatorChar + proxyFileName;
        File testFile = new File(testFilePath);
        CounterLine counter = new CounterLine();
        try {
            List<String> proxyList = Files.readLines(testFile, Charsets.UTF_8, counter);
            addProxy(proxyList);
        } catch (IOException e) {
            logger.error("读取" + proxyFileName + " 文件失败", e);
        }


    }

    public void addProxy(List<String> httpProxyList) {
        isEnable = true;
        for (String proxyString : httpProxyList) {
            String[] proxy = proxyString.split(":");
            try {
                if (allProxy.containsKey(proxy[0] + "_" + proxy[1])) {
                    continue;
                }
                Proxy item = new Proxy(proxy[0], Integer.valueOf(proxy[1]));
                if (!validateWhenInit && ProxyUtils.validateProxy(item)) {
                    item.setReuseTimeInterval(reuseInterval);
                    proxyQueue.add(item);
                    allProxy.put(proxy[0] + "_" + proxy[1], item);
                }
            } catch (NumberFormatException e) {
                logger.error("HttpHost init error:", e);
            }

        }
        logger.info("proxy pool size>>>>" + allProxy.size());
    }

    public Proxy getProxyHost() {
        Proxy proxy = null;
        try {
            Long time = System.currentTimeMillis();
            proxy = proxyQueue.take();
            double costTime = (System.currentTimeMillis() - time) / 1000.0;
            if (costTime > reuseInterval) {
                logger.info("get proxy time >>>> " + costTime);
            }
            proxy.setLastBorrowTime(System.currentTimeMillis());
            proxy.borrowNumIncrement(1);
        } catch (InterruptedException e) {
            logger.error("get proxy error", e);
        }
        if (proxy == null) {
            throw new NoSuchElementException();
        }
        return proxy;
    }

    public void returnProxy(Proxy proxy) {
        if (proxy == null) {
            return;
        }
        int statusCode = proxy.getStateCode();
        String host = proxy.getHttpHost();
        switch (statusCode) {
            case Proxy.SUCCESS:
                proxy.setReuseTimeInterval(reuseInterval);
                proxy.setFailedNum(0);
                proxy.recordResponse();
                proxy.successNumIncrement(1);
                break;
            case Proxy.ERROR_403:
                // banned,try longer interval
                proxy.fail(Proxy.ERROR_403);
                proxy.setReuseTimeInterval(reuseInterval * proxy.getFailedNum());
                logger.warn(host + " >>>> reuseTimeInterval is >>>> " + proxy.getReuseTimeInterval() / 1000.0);
                break;
            case Proxy.ERROR_502:
                proxy.fail(Proxy.ERROR_502);
                proxy.setReuseTimeInterval(10 * 60 * 1000 * proxy.getFailedNum());
                logger.warn("this proxy is banned >>>> " + proxy.getHttpHost());
                logger.info(host + " this proxy sever is banned " + proxy.getReuseTimeInterval() / 1000.0);
                break;
            case Proxy.ERROR_500:
                proxy.fail(Proxy.ERROR_500);
                proxy.setReuseTimeInterval(10 * 60 * 1000 * proxy.getFailedNum());
                logger.warn(host + " this proxy sever is error >>>> " + proxy.getHttpHost());
                break;
            case Proxy.ERROR_404:
                proxy.fail(Proxy.ERROR_404);
                proxy.setReuseTimeInterval(reuseInterval * proxy.getFailedNum());
                break;
            default:
                proxy.fail(statusCode);
                break;
        }
        if (proxy.getFailedNum() > 50) {
            if (!ProxyUtils.validateProxy(proxy)) {
                proxy.setReuseTimeInterval(reviveTime);
                allProxy.remove(proxy.getHttpHost());////原来是注销的
                logger.error("remove proxy >>>> " + proxy.getHttpHost() + ">>>>" + proxy.getStateCode() + " >>>> remain proxy >>>> " + proxyQueue.size());
            }
        }

        try {
            proxyQueue.put(proxy);
        } catch (InterruptedException e) {
            logger.warn("proxyQueue return proxy error", e);
        }
    }

    public static String allProxyStatus() {
        String re = "all proxy info >>>> \n";
        for (Entry<String, Proxy> entry : allProxy.entrySet()) {
            re += entry.getValue().toString() + "\n";
        }
        return re;
    }

    public int getIdleNum() {
        return proxyQueue.size();
    }

    public int getReuseInterval() {
        return reuseInterval;
    }


    @SuppressWarnings("static-access")
	public void enable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public int getReviveTime() {
        return reviveTime;
    }

    public boolean isValidateWhenInit() {
        return validateWhenInit;
    }

    @SuppressWarnings("static-access")
	public void validateWhenInit(boolean validateWhenInit) {
        this.validateWhenInit = validateWhenInit;
    }

    public int getSaveProxyInterval() {
        return saveProxyInterval;
    }

    public static void main(String[] args) {

        PropertyConfigurator.configure("config/log4j.properties");
        FileProxyPool proxyPool = new FileProxyPool();
        proxyPool.initProxy();

//        for (int index = 0; index <= proxyPool.getIdleNum(); index++) {
//            Proxy proxy = proxyPool.getProxyHost();
//            System.out.println(proxy.getHttpHost() + ":" + proxy.getPort());
//            proxy.setStateCode(Proxy.SUCCESS);
//            proxyPool.returnProxy(proxy);
//        }
        System.out.println(FileProxyPool.allProxyStatus());
//        System.out.println(FileProxyPool.allProxy);
    }

}
