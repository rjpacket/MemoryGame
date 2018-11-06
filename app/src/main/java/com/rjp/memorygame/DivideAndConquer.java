package com.rjp.memorygame;

public class DivideAndConquer {
    public static void main(String[] args) {
        System.out.println(dc(new int[]{1,2,3,4,5,6,7,8,9}));
    }

    public static int dc(int[] arr){
        if(arr.length == 1){
            return arr[0];
        }
        int length = arr.length;
        int[] copy = new int[length - 1];
        for (int i = 0; i < length - 1; i++) {
            copy[i] = arr[i + 1];
        }
        return arr[0] + dc(copy);
    }
}
