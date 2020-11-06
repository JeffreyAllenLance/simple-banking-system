package banking;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File(args[1]);
        try {
            file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        Database db = new Database(file.getName());
        db.createTable();

        loggedOutMenu(db);
    }

    public static void loggedOutMenu(Database db) {
        int choice;
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("1. Create an account\n2. Log into account\n0. Exit");
            choice = scanner.nextInt();
            if (choice != 0 && choice != 1 && choice != 2) {
                System.out.println("Invalid choice. Please try again.");
            } else if (choice == 1) {
                String number = CustomerNumber.generateCardNumber();
                String pin = CustomerNumber.generatePin();
                db.addToTable(number, pin, 0);
                System.out.println("Your card has been created\n" +
                        "Your card number:\n" +
                        number +
                        "\nYour card PIN:\n" +
                        pin);
            } else if (choice == 2) {
                String login = logIn(db);
                if (!login.equals("failed")) {
                    loggedInMenu(login, db);
                }
            } else {
                System.out.println("Bye!");
                System.exit(0);
            }
        }
    }

    public static void loggedInMenu(String customerNumber, Database db) {
        int choice;
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("1. Balance\n2. Add income\n3. Do Transfer\n4. Close account\n5. Log out\n0. Exit");
            choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Balance: " + db.getAvailableBalance(customerNumber));
            } else if (choice == 2) {
                addIncome(customerNumber, db);
            } else if (choice == 3) {
                transfer(customerNumber, db);
            } else if (choice == 4) {
                deleteAccount(customerNumber, db);
            } else if (choice == 5){
                System.out.println("You have successfully logged out!");
                break;
            } else {
                System.out.println("Bye!");
                System.exit(0);
            }
        }
    }

    public static String logIn(Database db) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your card PIN:");
        String pin = scanner.nextLine();
        if (db.checkForNumber(number) || !pin.equals(db.getPin(number))) {
            System.out.println("Wrong card number or PIN!");
            return "failed";
        } else {
            System.out.println("You have successfully logged in!");
            return number;
        }
    }

    public static void addIncome(String customerNumber, Database db) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        int balance = db.getAvailableBalance(customerNumber);
        db.updateTable(customerNumber, balance + income);
        System.out.println("Income was added!");
    }

    public static void transfer(String customerNumber, Database db) {
        Scanner scanner = new Scanner(System.in);
        String transferNumber;
        System.out.println("Enter card number:");
        transferNumber = scanner.nextLine();
        String firstFifteen = transferNumber.substring(0, 15);
        int[] digits = new int[15];

        for (int i = 0; i < 15; i++) {
            digits[i] = Character.getNumericValue(firstFifteen.charAt(i));
        }

        int checkSum = CustomerNumber.generateCheckSum(digits);

        if (checkSum != Character.getNumericValue(transferNumber.charAt(15))) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
        } else if (db.checkForNumber(transferNumber)) {
            System.out.println("Such a card does not exist");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            int transferAmt = scanner.nextInt();
            int available = db.getAvailableBalance(customerNumber);
            if (transferAmt > available) {
                System.out.println("Not enough money!");
            } else {
                System.out.println("Success!");
                db.updateTable(transferNumber, db.getAvailableBalance(transferNumber) + transferAmt);
                db.updateTable(customerNumber, available - transferAmt);
            }
        }
    }

    public static void deleteAccount(String customerNumber, Database db) {
        db.deleteFromTable(customerNumber);
        System.out.println("The account has been closed!");
    }
}
