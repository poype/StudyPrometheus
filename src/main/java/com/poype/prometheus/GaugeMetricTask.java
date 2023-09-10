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
                                .register();

        int metricValue = 0;
        while(true) {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 模拟请求

            int number = TestUtil.randomNumber();
            if (number % 5 == 0) {
                metricValue -= number;
                inProgress.dec(number);   // 减少
            } else {
                metricValue += number;
                inProgress.inc(number);   // 增加
            }
            System.out.println("Gauge metric value is " + metricValue);
        }
    }
}
