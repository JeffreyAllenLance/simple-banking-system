package banking;

import java.util.Random;

public class CustomerNumber {

    public static String generateCardNumber() {
        Random random = new Random();
        StringBuilder digitString = new StringBuilder("400000");
        int digit;
        int[] digits = new int[15];
        digits[0] = 4;
        for (int i = 6; i < 15; i++) {
            digit = random.nextInt(10);
            digits[i] = digit;
            digitString.append(digit);
        }
        int checkSum = CustomerNumber.generateCheckSum(digits);
        digitString.append(checkSum);
        return digitString.toString();
    }

    public static String generatePin() {
        Random random = new Random();
        StringBuilder digitString = new StringBuilder();
        int digit;
        for (int i = 0; i < 4; i++) {
            digit = random.nextInt(10);
            digitString.append(digit);
        }
        return digitString.toString();
    }

    public static int generateCheckSum(int[] digits) {
        for (int i = 0; i < 15; i++) {
            if (i % 2 == 0) {
                digits[i] *= 2;
            }
        }
        for (int i = 0; i < 15; i++) {
            if (digits[i] > 9) {
                digits[i] -= 9;
            }
        }
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            sum += digits[i];
        }
        return (sum * 9) % 10;
    }
}