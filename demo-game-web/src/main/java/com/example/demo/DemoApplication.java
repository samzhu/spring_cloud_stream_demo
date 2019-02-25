package com.example.demo;

import com.example.demo.config.MqChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * 參考資料
 * https://github.com/AnRic/WebSocket_demo
 * https://www.devglan.com/spring-boot/spring-boot-websocket-integration-example
 * https://www.jianshu.com/p/0f498adb3820
 * https://segmentfault.com/a/1190000009038991
 * https://blog.csdn.net/u014042066/article/details/76702120
 * https://hk.saowen.com/a/a4e4838a612fb494020fdb2695c36ab9367513e5ad2e1637fe3d52f15a275dc6
 * https://my.oschina.net/u/1590027/blog/879629
 */

@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding(MqChannel.class)
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
