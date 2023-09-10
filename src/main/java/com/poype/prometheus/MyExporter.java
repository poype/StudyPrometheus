package com.poype.prometheus;

import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExporter {
    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        executorService.execute(new HistogramMetricTask());

        // 对外暴露 JVM 相关的metric
        DefaultExports.initialize();

        // 使用HTTP Server暴露采集的数据
        HTTPServer server = new HTTPServer(8090);
    }
}
