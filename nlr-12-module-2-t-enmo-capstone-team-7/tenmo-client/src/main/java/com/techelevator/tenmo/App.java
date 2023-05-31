package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL, currentUser);


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        System.out.println("--------------------------------------------");
        System.out.println("$-$-$-$-$-$-$ Current Balance $-$-$-$-$-$-$ ");
        System.out.println("--------------------------------------------");

        System.out.println("Account Balance: $" + transferService.getBalance(currentUser.getToken()));

        // TODO Auto-generated method stub

    }

    private void viewTransferHistory() {
        System.out.println("--------------------------------------------");
        System.out.println("$-$-$-$-$-$-$ Transfer History $-$-$-$-$-$-$ ");
        System.out.println("--------------------------------------------");


    }


    // TODO Auto-generated method stub


    private void viewPendingRequests() {
        System.out.println("--------------------------------------------");
        System.out.println("$-$-$-$-$-$-$ Pending Requests $-$-$-$-$-$-$");
        System.out.println("--------------------------------------------");
        // TODO Auto-generated method stub


    }

    private void sendBucks() {
        System.out.println("--------------------------------------------");
        System.out.println("$-$-$-$-$-$-$-$ Send Bucks$-$-$-$-$-$-$-$-$-$ ");
        System.out.println("--------------------------------------------");
        // TODO Auto-generated method stub
        //prints the list of accounts
        List<Account> userAccount = transferService.userAccounts(currentUser.getToken());
        consoleService.printGetAllAccounts(userAccount);

//prompts for the userID and amount to send
        System.out.println("--------------------------------------------");
        int selectedUserId = consoleService.promptForInt("Enter the Account Id of the user you want to send to: ");


        BigDecimal selectedTransactionAmount = consoleService.promptForBigDecimal("Enter the Amount to send to the User: ");
// Will have to add checks that not sending to self, >0.00, account balance - selectedTransactionAmount != 0.00
        System.out.println("--------------------------------------------");
///OK so have account sending-too and amount. NOW:
// A transfer includes the User IDs of the from and to
// users and the amount of TE Bucks.
// 4. The receiver's account balance is increased by the amount of the transfer.
// 5. The sender's account balance is decreased by the amount of the transfer.
// 8. A Sending Transfer has an initial status of *Approved*.

        // Activate the transfer/send


      //  System.out.println("$" + transferService.gettransferToAccount(currentUser.getToken()));



    }


    private void requestBucks() {
        System.out.println("--------------------------------------------");
        System.out.println("$-$-$-$-$-$-$-$ Request Bucks $-$-$-$-$-$-$-$");
        System.out.println("--------------------------------------------");
        // TODO Auto-generated method stub

    }

}
