package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TransferService<GetSendTransaction, SendTransaction> {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthenticatedUser currentUser;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.baseUrl = url;
        this.currentUser = currentUser;
    }

    // 1.Get current balance
    public BigDecimal getBalance(String userToken) {
        BigDecimal userBalance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "account/balance", HttpMethod.GET, makeAuthEntity(userToken), BigDecimal.class);
            userBalance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userBalance;
    }

    // step 2 view all transfers by id: Transfer History --> path = "/transfer",
    //    public Transfer[] AllTransfersByUserId(int userId) {
    //        AllTransfersByUserId()[] transfers = null;
    //        try {
    //            transfers = restTemplate.getForObject(API_BASE_URL + "transfer" , Transfer[].class);
    //        } catch (RestClientResponseException e) {
    //            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
    //        } catch (ResourceAccessException e) {
    //            BasicLogger.log(e.getMessage());
    //        }
    //        return transfers;
    //    }

//    public List<Transfer> listAllTransfersByUserId(String userToken) {
//        Transfer[] transfers = null;
//        try {
//            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.GET, makeAuthEntity(userToken), Transfer[].class);
//            transfers = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//        return Arrays.asList(transfers);


    // 3. Get pending transfers by user id : view pending requests   --> path = "/transfer/pending/{userId}",
//    public Review[] getReviewsByHotelId(int hotelId) {
//
//        return restTemplate.getForObject(API_BASE_URL + "/transfer", Review[].class);
//    }

    public Transfer[] listPendingTransfers(String userToken) {
        Transfer[] pendingTransfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/pending/{userId}", HttpMethod.GET, makeAuthEntity(userToken), Transfer[].class);
            pendingTransfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingTransfers;
    }

    //4. Send Bucks -->

    //got the list of accounts
    public List<Account> userAccounts(String userToken) {
        Account[] userAccounts = null;
        try {
            ResponseEntity<Account[]> response = restTemplate.exchange(API_BASE_URL + "account/listOfUsers", HttpMethod.GET, makeAuthEntity(userToken), Account[].class);
            userAccounts = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return Arrays.asList(userAccounts);}


//now have to choose an account id and the transfer amount
//if accountId = user id NO

//    //CREATE SEND
//    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
//    public void sendTransfer(@RequestBody Transfer transfer) {
//        transferDao.createSend(transfer);
//        accountDao.addAccountBalance(transfer.getAmount(), transfer.getAccountToId());
//        accountDao.subtractAccountBalance(transfer.getAmount(), transfer.getAccountFromId());
//    }
//then do .put for renaming the account table


    public transferToAccount addTransferToAccount(transferToAccount newTransferToAccount) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<transferToAccount> entity = new HttpEntity<>(newTransferToAccount, headers);

        transferToAccount returntransferToAccount = null;
        try {
            returntransferToAccount = restTemplate.postForObject(API_BASE_URL + "/transfer/send", entity, transferToAccount.class);
        } catch (RestClientResponseException ex){
            BasicLogger.log(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex){
            BasicLogger.log(ex.getMessage() );
        } catch (Exception ex){
            BasicLogger.log(ex.getMessage());
        }
        return returntransferToAccount;
    }








    //5 Request bucks(optional)


    private HttpEntity<Void> makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}




