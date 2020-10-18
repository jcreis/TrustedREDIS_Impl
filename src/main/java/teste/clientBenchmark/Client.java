package teste.clientBenchmark;

import org.openjdk.jmh.annotations.*;

public class Client {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 2)
    @Warmup
    //Modes: Throughput, AverageTime, SampleTime, and SingleShotTime
    public void init() {
        // Do nothing
    }
}
