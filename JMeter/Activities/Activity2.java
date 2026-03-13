import java.util.*;

public class Activity2 {
    public static void main(String[] args) {
        // Initialize the array
        int[] numArr = {10, 77, 10, 54, -11, 10};
        System.out.println("Original Array: " + Arrays.toString(numArr));

        // Set constants
        int searchNum = 10;
        int fixedSum = 30;

        // Print result
        System.out.println("Result: " + result(numArr, searchNum, fixedSum));
    }

    public static boolean result(int[] numbers, int searchNum, int fixedSum) {
        int temp_sum = 0;

        // Loop through array
        for (int number : numbers) {
            // If the current number matches searchNum, add it; otherwise, skip
            if (number == searchNum) {
                temp_sum += searchNum;

                // If sum exceeds fixedSum, break out
                if (temp_sum > fixedSum) {
                    // Exceeded the allowed sum; stop processing further
                    break;
                }
            } else {
                // Number is not the searchNum; do nothing and continue
                // (Explicit else for clarity per your request)
            }
        }

        // Return true if the total equals fixedSum
        return temp_sum == fixedSum;
    }
}