package com.poype.prometheus;

import com.poype.prometheus.util.TestUtil;
import io.prometheus.client.Histogram;

import java.util.concurrent.TimeUnit;

/**
 * A summary can only provide the average latency, but what if you want a quantile?
 * 一定比例的event value低于某个给定的值，例如95%的request latency低于2s。
 * For example, the 0.95 quantile being 300 ms means that 95% of requests took less than 300 ms.
 */
public class HistogramMetricTask implements Runnable {
    @Override
    public void run() {
        Histogram histogram = Histogram.build()
                                       .name("histogram_metric")
                                       .help("study prometheus histogram metric")
                                       .buckets(1, 2, 5, 10, 30, 50, 100, 300, 500, 1000)  // 每个bucket都会对应一个label
                                       .register();

        int count = 0;
        int sum = 0;

        while(true) {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int number = TestUtil.randomNumber() % 1000;
            count++;
            sum = sum + number;

            // 程序中的一个Histogram Metric会对外暴露三个Metric。
            // 在本例中，对外暴露的三个Metric分别是 histogram_metric_bucket、histogram_metric_count 和 histogram_metric_sum
            // Histogram中的每个bucket在histogram_metric_bucket metric中都对应一个单独的label，还有一个额外的名字是 “+Inf” label。
            // histogram_metric_bucket所有的label如下所示：
            // histogram_metric_bucket{le="1.0",}
            // histogram_metric_bucket{le="2.0",}
            // histogram_metric_bucket{le="5.0",}
            // histogram_metric_bucket{le="10.0",}
            // histogram_metric_bucket{le="30.0",}
            // histogram_metric_bucket{le="50.0",}
            // histogram_metric_bucket{le="100.0",}
            // histogram_metric_bucket{le="300.0",}
            // histogram_metric_bucket{le="500.0",}
            // histogram_metric_bucket{le="1000.0",}
            // histogram_metric_bucket{le="+Inf",}

            // histogram_metric_count 和 histogram_metric_sum 这两个metric的作用可参考Summary类型的metric

            // 给observe方法传递100参数，则下面5个bucket都会被加1。
            // histogram_metric_bucket{le="100.0",}
            // histogram_metric_bucket{le="300.0",}
            // histogram_metric_bucket{le="500.0",}
            // histogram_metric_bucket{le="1000.0",}
            // histogram_metric_bucket{le="+Inf",}
            histogram.observe(100);

            // 给observe方法传递501参数，则只有下面2个bucket会被加1。
            // histogram_metric_bucket{le="1000.0",}
            // histogram_metric_bucket{le="+Inf",}
            // 1000比501大出499，对应的bucket被加1，但500只比501少1，对应的bucket却没被加1.
            // 虽然这看上去有点不合理，但这就是prometheus的设计
            histogram.observe(501);

            System.out.println("summary observe count: " + count);
            System.out.println("summary observe sum: " + sum);
        }
    }
}
