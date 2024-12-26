package org.example;

import java.util.concurrent.Executors;

class Main {
    public static void main(String[] args) {
        int[][] matrixA = {
                { 1, 3, 5, 7 },
                { 2, 4, 6, 8 },
                { 3, 5, 7, 9 },
                { 1, 9, 7, 5 }
        };

        int[][] matrixB = {
                { 4, 2, 0, 6 },
                { 2, 4, 1, 5 },
                { 9, 2, 1, 7 },
                { 1, 3, 5, 9 }
        };

        System.out.println("\nMatrix A:");
        MatrixOperations.printMatrix(matrixA, 4, 4);

        System.out.println("\nMatrix B:");
        MatrixOperations.printMatrix(matrixB, 4, 4);

        int[][] matrixC = MatrixOperations.multiplyMatrices(matrixA, matrixB, 4, 4, 4,
                Executors.newVirtualThreadPerTaskExecutor());

        System.out.println("\nMatrix C (multiplication result):");
        MatrixOperations.printMatrix(matrixC, 4, 4);
    }
}
