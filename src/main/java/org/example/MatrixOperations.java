package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixOperations {

    public static void printMatrix(int[][] matrix, int rowCount, int colCount) {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                System.out.print(matrix[row][col] + "\t");
            }
            System.out.println();
        }
    }

    public static int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB,
                                           int rowCountA, int commonDim, int colCountB,
                                           ExecutorService threadPool) {

        int[][] resultMatrix = new int[rowCountA][colCountB];
        AtomicInteger current_RowIndex = new AtomicInteger(0);
        AtomicInteger current_ColIndex = new AtomicInteger(0);

        int totalElements = rowCountA * colCountB;
        AtomicInteger completedElements = new AtomicInteger(0);

        for (int taskIndex = 0; taskIndex < totalElements; taskIndex++) {
            threadPool.submit(() -> {
                int rowIndex, colIndex;

                synchronized (current_RowIndex) {
                    rowIndex = current_RowIndex.get();
                    colIndex = current_ColIndex.getAndIncrement();

                    if (colIndex == colCountB) {
                        current_ColIndex.set(0);
                        colIndex = current_ColIndex.getAndIncrement();
                        rowIndex = current_RowIndex.incrementAndGet();
                    }
                }

                if (rowIndex < rowCountA && colIndex < colCountB) {
                    int cellValue = 0;
                    for (int i = 0; i < commonDim; i++) {
                        cellValue += matrixA[rowIndex][i] * matrixB[i][colIndex];
                    }
                    resultMatrix[rowIndex][colIndex] = cellValue;

                    int completed = completedElements.incrementAndGet();
                    if (completed == totalElements) {
                        threadPool.shutdown();
                    }
                }
            });
        }
        while (!threadPool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return resultMatrix;
    }
}

