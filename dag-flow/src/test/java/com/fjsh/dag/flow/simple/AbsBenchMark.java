package com.fjsh.dag.flow.simple;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 *
 * @Author: <fujiansheng.com>
 * @Description：
 * 最后统计的结果为：
 * 统计5次 x 500ms运行时间下运算次数，测试结果为平均10W次调用耗时0.328ms
 * Benchmark          Mode  Cnt  Score   Error  Units
 * AbsBenchMark.test  avgt   25  0.328 ± 0.024  ms/op
 *参考性能指标：在本机Mac 2.2 GHz/16G 测试，执行10万次表达式耗时应在60ms以下,如果超过300ms则存在性能问题需要优化
 * @Date: Created in :2019-02-24 15:14  
 * @Modified by:
 */
@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)   // 预热，共5次， 每次500ms
@Measurement(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS, batchSize = 100000)  // 测试，共5次， 每次500ms, 以100000次调用时间作为测试指标
@BenchmarkMode(Mode.AverageTime)       // 测量值：平均运行时间
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 测量单位：毫秒
public class AbsBenchMark {
    private static Logger log = LoggerFactory.getLogger(AbsBenchMark.class);
  /*  @Benchmark
    public String stringConcat() {
        String a = "a";
        String b = "b";
        String c = "c";
        String s = a + b + c;
        log.debug(s);
        return s;
    }
    @Benchmark
    public int intSum() {

        return 1+2;
    }
    public static void main(String[] args) throws RunnerException {
        // 使用一个单独进程执行测试，执行5遍warmup，然后执行5遍测试
        Options opt = new OptionsBuilder().include(AbsBenchMark.class.getSimpleName()).forks(1).warmupIterations(5)
                .measurementIterations(5).build();
        new Runner(opt).run();
    }*/
  @Benchmark
  public Object test() {
      return 1+2;
  }

}
