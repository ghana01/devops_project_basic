package com.myapp.monitor;

import com.codahale.metrics.*;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class App {
    private static final MetricRegistry metrics = new MetricRegistry();
    private static final Meter requestMeter = metrics.meter("requests");
    private static final Histogram cpuUsageHistogram = metrics.histogram("cpu.usage");
    private static final Histogram memoryUsageHistogram = metrics.histogram("memory.usage");
    
    public static void main(String[] args) {
        System.out.println("Health Monitor App is Running!");
        
        // Configure Graphite reporting
        final Graphite graphite = new Graphite(new InetSocketAddress("graphite", 2003));
        final GraphiteReporter reporter = GraphiteReporter.forRegistry(metrics)
            .prefixedWith("health.monitor")
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build(graphite);
        
        // Start reporting metrics every second
        reporter.start(1, TimeUnit.SECONDS);
        
        // Start monitoring
        startMonitoring();
    }
    
    private static void startMonitoring() {
        new Thread(() -> {
            try {
                while (true) {
                    // Record a request
                    requestMeter.mark();
                    
                    // Generate and record CPU usage (0-100%)
                    double cpuUsage = Math.random() * 100.0;
                    cpuUsageHistogram.update((long) cpuUsage);
                    
                    // Generate and record memory usage (0-1024MB)
                    double memoryUsage = Math.random() * 1024.0;
                    memoryUsageHistogram.update((long) memoryUsage);
                    
                    // Log current metrics to console
                    System.out.printf("Current metrics - CPU: %.2f%%, Memory: %.2fMB%n", 
                        cpuUsage, memoryUsage);
                    
                    // Wait for 5 seconds before next update
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                System.err.println("Monitoring thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}