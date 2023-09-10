package com.poype.prometheus;

import com.poype.prometheus.util.TestUtil;
import io.prometheus.client.Summary;

import java.util.concurrent.TimeUnit;

public class SummaryMetricTask implements Runnable {
    @Override
    public void run() {
        Summary summaryMetric = Summary.build()
                                       .name("summary_metric")
                                       .help("study prometheus summary metric")
                                       .register();

        int count = 0;
        int sum = 0;
        while(true) {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 模拟请求

            int number = TestUtil.randomNumber() % 1000;
            count++;
            sum = sum + number;

            // 程序中的一个Summary Metric会对外暴露两个Metric。
            // 在本例中，对外暴露的两个Metric分别是 summary_metric_count 和 summary_metric_sum
            // summary_metric_sum 的值就是所有传给observe方法参数值的总和，对应程序变量sum的值
            // summary_metric_count 的值是observe方法被调用的总次数，对应程序变量count的值
            summaryMetric.observe(number);

            System.out.println("summary observe count: " + count);
            System.out.println("summary observe sum: " + sum);
        }
    }
}
