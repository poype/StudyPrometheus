package com.poype.prometheus;

import com.poype.prometheus.util.TestUtil;
import io.prometheus.client.Gauge;

import java.util.concurrent.TimeUnit;

public class GaugeMetricTask implements Runnable {
    @Override
    public void run() {
        // 注册到default registry，也可以通过参数指定注册到其它的registry
        Gauge inProgress = Gauge.build()
                                .name("gauge_metric")
                                .help("study prometheus gauge metric")
                                .labelNames("app", "path", "latency")
                                .register();

        int metricValue1 = 0;
        int metricValue2 = 0;
        while(true) {
            try {
                // 延迟3秒
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 生成随机数，给不同标签的metric增加或减少相应的值
            int number = TestUtil.randomNumber() % 100;
            if (number % 5 == 0) {
                metricValue1 -= number;
                inProgress.labels("order", "/get", "88").dec(number);   // 减少
            } else if (number % 5 == 1) {
                metricValue1 += number;
                inProgress.labels("order", "/get", "88").inc(number);   // 增加
            } else if (number % 5 == 2) {
                metricValue2 -= number;
                inProgress.labels("pay", "/get", "99").dec(number);     // 减少
            } else if (number % 5 == 3) {
                metricValue2 += number;
                inProgress.labels("pay", "/get", "99").inc(number);     // 增加
            } else {
                metricValue2 += number;
                inProgress.labels("pay", "/get", "99").inc(number);     // 增加
            }
            System.out.println("Gauge metric1 value is " + metricValue1);
            System.out.println("Gauge metric2 value is " + metricValue2);
        }
    }
}
