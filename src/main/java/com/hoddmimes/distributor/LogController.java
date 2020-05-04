package com.hoddmimes.distributor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;


@Controller
public class LogController {
    private Logger mLog;

    public LogController() {
        Configuration tConfig = Configuration.getInstance();
        mLog = LogManager.getLogger(tConfig.getDistributorApplicationName());
    }

    public void info(String pMessage) {
        mLog.info(pMessage);
    }

    public void info(Object pObject) {
        mLog.info(pObject);
    }

    public void warn(String pMessage) {
        mLog.warn(pMessage);
    }

    public void warn(Object pObject) {
        mLog.warn(pObject);
    }

    public void warn(String pMessage, Throwable pThrowable) {
        mLog.warn(pMessage, pThrowable);
    }

    public void warn(Object pObject, Throwable pThrowable) {
        mLog.warn(pObject, pThrowable);
    }

    public void error(String pMessage) {
        mLog.error(pMessage);
    }

    public void error(Object pObject) {
        mLog.error(pObject);
    }

    public void error(String pMessage, Throwable pThrowable) {
        mLog.error(pMessage, pThrowable);
    }

    public void error(Object pObject, Throwable pThrowable) {
        mLog.error(pObject, pThrowable);
    }


    public void fatal(Object pObject, Throwable pThrowable) {
        mLog.fatal(pObject, pThrowable);
    }

    public void fatal(String pMessage) {
        mLog.fatal(pMessage);
    }

    public void fatal(Object pObject) {
        mLog.fatal(pObject);
    }

    public void fatal(String pMessage, Throwable pThrowable) {
        mLog.error(pMessage, pThrowable);
    }

    public void debug(String pMessage) {
        mLog.debug(pMessage);
    }

    public void debug(String pMessage, Object pObject) {
        mLog.debug(pMessage, pObject);
    }

}
