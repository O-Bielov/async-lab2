package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 2, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 15, time = 2, timeUnit = TimeUnit.MILLISECONDS)
@Fork(value = 1)
public class AppBenchmark {
    int[][] matrixA, matrixB;
    final int ROWS_COLUMNS = 2000;

    @Setup
    public void setup() {
        matrixA = new int[ROWS_COLUMNS][ROWS_COLUMNS];
        matrixB = new int[ROWS_COLUMNS][ROWS_COLUMNS];
        RandomGenerator rng = RandomGenerator.getDefault();
        for (int i = 0; i < ROWS_COLUMNS; i++) {
            for (int j = 0; j < ROWS_COLUMNS; j++) {
                matrixA[i][j] = rng.nextInt(100);
            }
        }
        for (int i = 0; i < ROWS_COLUMNS; i++) {
            for (int j = 0; j < ROWS_COLUMNS; j++) {
                matrixB[i][j] = rng.nextInt(100);
            }
        }
    }

    @Benchmark
    public int[][] benchmarkThreadPool() {
        ExecutorService threadPool = Executors.newFixedThreadPool(8);
        return MatrixOperations.multiplyMatrices(matrixA, matrixB, ROWS_COLUMNS, ROWS_COLUMNS, ROWS_COLUMNS,
                threadPool);
    }

    @Benchmark
    public int[][] benchmarkVirtualThreads() {
        ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor();
        return MatrixOperations.multiplyMatrices(matrixA, matrixB, ROWS_COLUMNS, ROWS_COLUMNS, ROWS_COLUMNS,
                virtualThreads);
    }
}