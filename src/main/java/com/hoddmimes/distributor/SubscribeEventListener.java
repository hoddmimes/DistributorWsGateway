package com.hoddmimes.distributor;


import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class SubscribeEventListener implements ApplicationListener {


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("onApplicationEvent " + event.toString());
        System.out.println("        " + event.getSource().toString());
    }
}

