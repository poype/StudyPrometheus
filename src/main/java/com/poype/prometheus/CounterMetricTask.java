package com.poype.prometheus;

import com.poype.prometheus.util.TestUtil;
import io.prometheus.client.Counter;

import java.util.concurrent.TimeUnit;

public class CounterMetricTask implements Runnable {

    @Override
    public void run() {
        // 定义一个counter类型的metric
        Counter requestCounter = Counter.build()
                                        .name("counter_metric")
                                        .labelNames("path", "method", "code")
                                        .help("study prometheus counter metric")
                                        .register();

        long total = 0;

        while(true) {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 模拟请求

            System.out.println(++total);

            if (TestUtil.randomNumber() % 4 == 0) {
                requestCounter.labels("/aaa", "GET", "200").inc();
            } else if (TestUtil.randomNumber() % 4 == 1) {
                requestCounter.labels("/bbb", "POST", "200").inc();
            } else if (TestUtil.randomNumber() % 4 == 2) {
                requestCounter.labels("/bbb", "POST", "504").inc();
            } else {
                requestCounter.labels("/ccc", "GET", "200").inc();
            }
        }
    }
}
