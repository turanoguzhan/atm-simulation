package com.ouz.atmsimulation.service;

import com.ouz.atmsimulation.controller.dto.ATMSuccessMessageDTO;
import com.ouz.atmsimulation.entity.ATM;
import com.ouz.atmsimulation.entity.Account;
import com.ouz.atmsimulation.entity.Transaction;
import com.ouz.atmsimulation.enums.BanknoteType;
import com.ouz.atmsimulation.enums.SourceType;
import com.ouz.atmsimulation.enums.StatusType;
import com.ouz.atmsimulation.enums.TransactionType;
import com.ouz.atmsimulation.exception.ExceptionMessage;
import com.ouz.atmsimulation.exception.account.*;
import com.ouz.atmsimulation.exception.atm.ATMException;
import com.ouz.atmsimulation.exception.atm.ATMNoMoneyException;
import com.ouz.atmsimulation.exception.atm.ATMNotEnoughMoneyException;
import com.ouz.atmsimulation.exception.atm.ATMNotFoundException;
import com.ouz.atmsimulation.repository.ATMRepository;
import com.ouz.atmsimulation.repository.AccountRepository;
import com.ouz.atmsimulation.repository.TransactionRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ATMServiceImpl implements ATMService{

    private static final Long DEFAULT_ATM_NO = 1L;

    private final ATMRepository atmRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MessageSource messageSource;
    private final HashMap<BanknoteType,Integer> atmMachineState;
    private final static Locale SYSTEM_LOCALESYSTEM_LOCALE = LocaleContextHolder.getLocale();

    public ATMServiceImpl(ATMRepository atmRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, MessageSource messageSource, HashMap<BanknoteType, Integer> atmMachineState) {
        this.atmRepository = atmRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.messageSource = messageSource;
        this.atmMachineState = atmMachineState;
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity exposeAccount(Long accountNumber, Long pin){

        Account account = null;

        try {
            account = getAccountByAccountNumber(accountNumber);

            pinValidation(account, pin);

            logTransaction(account, BigDecimal.ZERO, TransactionType.EXPOSE, SourceType.ATM);

            return ResponseEntity
                    .ok(new ATMSuccessMessageDTO(account.getAccountNumber(),null,
                            account.getBalance(),TransactionType.EXPOSE,LocalDateTime.now(),
                            messageSource.getMessage("account.expose.successful",
                                    null,
                                    SYSTEM_LOCALESYSTEM_LOCALE)));

        } catch(AccountException ae){
        return ResponseEntity.status(ae.getStatus()).body(
                new ExceptionMessage(messageSource.getMessage(ae.getMessage(),new Object[]{},"an error occured",SYSTEM_LOCALESYSTEM_LOCALE),
                        ae.getDesc(),
                        LocalDateTime.now(),
                        ae.getStatus().value()));
        }
    }

    @Override
    public ResponseEntity atmOperation(Long accountNumber, Long pin, BigDecimal amount,TransactionType transactionType) {

        Account account = null;
        HashMap<BanknoteType,Long> moneyValues=new HashMap<>();
        try {
            account =  getAccountByAccountNumber(accountNumber);

            pinValidation(account,pin);

            checkAmountIsValid(amount);

            if(TransactionType.WITHDRAW.equals(transactionType) && !account.checkTotalAmountIsEnough(amount)){
                throw new AccountHasNotEnoughBalance();
            }

            checkATMMoneyStatusIfWithdraw(amount,transactionType);

            moneyValues = getATMTransactionMoneyValues(amount,transactionType);
            updateATMMoney(moneyValues,transactionType);
            logTransaction(account,amount, transactionType, SourceType.ATM);

            modifyAccountBalance(account,amount,transactionType);
            logTransaction(account,amount, transactionType, SourceType.ACCOUNT);

            return ResponseEntity
                    .ok(new ATMSuccessMessageDTO(account.getAccountNumber(),moneyValues,
                            account.getBalance(),transactionType,LocalDateTime.now(),
                            messageSource.getMessage(
                                    TransactionType.WITHDRAW.equals(transactionType)
                                            ?"account.withdraw.successful"
                                            :"account.deposit.successful",
                                    null,
                                    SYSTEM_LOCALESYSTEM_LOCALE)));
        }catch(AccountException ae){
            return ResponseEntity.status(ae.getStatus()).body(
                    new ExceptionMessage(messageSource.getMessage(ae.getMessage(),new Object[]{},"an error occured",SYSTEM_LOCALESYSTEM_LOCALE),
                            ae.getDesc(),
                            LocalDateTime.now(),
                            ae.getStatus().value()));
        }catch(ATMException atme){
            return ResponseEntity.status(atme.getStatus()).body(
                    new ExceptionMessage(messageSource.getMessage(atme.getMessage(),new Object[]{},"an error occured",SYSTEM_LOCALESYSTEM_LOCALE),
                            atme.getDesc(),
                            LocalDateTime.now(),
                            atme.getStatus().value()));
        }
    }

    private Account getAccountByAccountNumber(Long accountNumber){
        if(Objects.isNull(accountNumber)){
            throw new AccountBadRequestException();
        }

        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(new AccountNotFoundException());
    }

    private void pinValidation(Account account, Long pin){
        if(Objects.isNull(pin)){
            throw new AccountBadRequestException();
        }
        if(!account.validatePin(pin)){
            throw new AccountPinIncorrectException();
        }
    }

    private void checkAmountIsValid(BigDecimal amount){
        if(Objects.isNull(amount)){
            throw new AccountAmountException();
        }
    }

    private void checkATMMoneyStatusIfWithdraw(BigDecimal amount,TransactionType type) {

        if(TransactionType.WITHDRAW.equals(type)){
            BigDecimal totalMoneyOfATM = totalOfATM();

            if(totalMoneyOfATM.compareTo(BigDecimal.ZERO) == 0){
                throw new ATMNoMoneyException();
            }

            if(totalMoneyOfATM.compareTo(amount)<0){
                throw new ATMNotEnoughMoneyException();
            }
        }
    }

    private BigDecimal totalOfATM() {
       return BigDecimal.valueOf(atmMachineState.entrySet().stream()
               .mapToLong(entry-> (long) entry.getKey().value * entry.getValue())
               .sum() );
    }

    private void modifyAccountBalance(Account account,BigDecimal amount,TransactionType type){
        BigDecimal newBalance = BigDecimal.ZERO;
        if(TransactionType.WITHDRAW.compareTo(type) == 0){
            newBalance = account.getBalance().subtract(amount);
        }else if(TransactionType.DEPOSIT.compareTo(type) == 0){
            newBalance = account.getBalance().add(amount);
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    private void logTransaction(Account account, BigDecimal amount, TransactionType type,SourceType sourceType) {
        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setStatus(StatusType.COMPLETED);
        transaction.setCreated_by(account.getAccountNumber().toString());
        transaction.setCreated_date(LocalDateTime.now());
        transaction.setSourceType(sourceType);

        transactionRepository.save(transaction);
    }

    private HashMap<BanknoteType,Long> getATMTransactionMoneyValues(BigDecimal amount, TransactionType type){

        List<ATM> atmMoneyList = atmRepository.findByAtmNo(DEFAULT_ATM_NO)
                .orElseThrow(new ATMNotFoundException());

        List<ATM> reverseOrderedATMList = atmMoneyList.stream()
                        .sorted(Comparator.comparing(ATM::getBanknoteType)
                        .reversed())
                        .collect(Collectors.toList());

        HashMap<BanknoteType,Long> atmTransactionMap= new HashMap<>();

        for (ATM element:reverseOrderedATMList) {
            if(checkAmountAndBanknoteAvailable(amount, element.getBanknoteType().value)){

                long countOfBanknote = amount.divide(BigDecimal.valueOf(element.getBanknoteType().value),
                        RoundingMode.HALF_DOWN).longValueExact();

                if(TransactionType.WITHDRAW.equals(type) ){
                    countOfBanknote = Math.min(countOfBanknote,element.getBanknoteCount());
                }

                amount = amount.subtract(BigDecimal.valueOf(element.getBanknoteType().value)
                        .multiply(BigDecimal.valueOf(countOfBanknote)));

                atmTransactionMap.put(element.getBanknoteType(),countOfBanknote);
            }
        }

        return atmTransactionMap;
    }

    private void updateATMMoney(Map<BanknoteType,Long> moneyValues, TransactionType transactionType){
        List<ATM> atmList = atmRepository.findByAtmNo(DEFAULT_ATM_NO)
                .orElseThrow(new ATMNotFoundException());

        atmList.forEach(element-> {
                    if (TransactionType.WITHDRAW.equals(transactionType))
                        element.setBanknoteCount(element.getBanknoteCount() - moneyValues.getOrDefault(element.getBanknoteType(),0L));
                    else if(TransactionType.DEPOSIT.equals(transactionType))
                        element.setBanknoteCount(element.getBanknoteCount() + moneyValues.getOrDefault(element.getBanknoteType(),0L));
                });
        atmRepository.saveAll(atmList);
    }

    private boolean checkAmountAndBanknoteAvailable(BigDecimal amount, Integer banknoteValue){
        return amount.compareTo(BigDecimal.ZERO)>0 &&
                amount.compareTo(BigDecimal.valueOf(banknoteValue))>=0;
    }
}
