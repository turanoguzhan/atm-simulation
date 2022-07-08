package com.ouz.atmsimulation.unittest.service;

import com.ouz.atmsimulation.entity.ATM;
import com.ouz.atmsimulation.entity.Account;
import com.ouz.atmsimulation.entity.Bank;
import com.ouz.atmsimulation.entity.Transaction;
import com.ouz.atmsimulation.enums.BanknoteType;
import com.ouz.atmsimulation.enums.TransactionType;
import com.ouz.atmsimulation.repository.ATMRepository;
import com.ouz.atmsimulation.repository.AccountRepository;
import com.ouz.atmsimulation.repository.TransactionRepository;
import com.ouz.atmsimulation.service.ATMServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class TestATMService {

    @InjectMocks
    private ATMServiceImpl atmService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ATMRepository atmRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private HashMap<BanknoteType,Integer> atmMachineState;

    private Account account;

    private Bank bank;

    private ATM atm;

    private List<ATM> atmList;

    private ResponseEntity response;


    private final static Locale SYSTEM_LOCALESYSTEM_LOCALE = LocaleContextHolder.getLocale();

    @BeforeEach
    void beforeEach(){

        account = new Account();
        account.setBalance(new BigDecimal("800"));
        account.setOverdraft(new BigDecimal("200"));
        account.setAccountNumber(123456789L);
        account.setPin(1234L);

        bank = new Bank();
        bank.setID(1L);
        bank.setAccounts(Set.of(account));
        bank.setName("Default Bank");

        atmList = new ArrayList<>();
        atm = new ATM();
        atm.setAtmNo(1L);
        atm.setBank(bank);
        atm.setBanknoteType(BanknoteType.FIFTY_BANKNOTE);
        atm.setBanknoteCount(10L);
        atmList.add(atm);

        atm = new ATM();
        atm.setAtmNo(1L);
        atm.setBank(bank);
        atm.setBanknoteType(BanknoteType.TWENTY_BANKNOTE);
        atm.setBanknoteCount(30L);
        atmList.add(atm);

        atm = new ATM();
        atm.setAtmNo(1L);
        atm.setBank(bank);
        atm.setBanknoteType(BanknoteType.TEN_BANKNOTE);
        atm.setBanknoteCount(30L);
        atmList.add(atm);

        atm = new ATM();
        atm.setAtmNo(1L);
        atm.setBank(bank);
        atm.setBanknoteType(BanknoteType.FIVE_BANKNOTE);
        atm.setBanknoteCount(20L);
        atmList.add(atm);

        bank.setAtms(new HashSet<>(atmList));

        response = ResponseEntity.ok().build();
    }

    @Test
    void testExposeAccount(){

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.ofNullable(account));

        ResponseEntity responseService = atmService.exposeAccount(account.getAccountNumber(),account.getPin());


        assertEquals(HttpStatus.OK, responseService.getStatusCode());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
        verify(transactionRepository).save(any(Transaction.class));
        verify(messageSource).getMessage("account.expose.successful",null,LocaleContextHolder.getLocale());

        verifyNoMoreInteractions(accountRepository,transactionRepository,messageSource);

    }

    @Test
    public void testAtmOperationWithdraw_AccountNotFoundException() {
        ResponseEntity response = atmService.atmOperation(1597534568L,account.getPin(),account.getBalance(), TransactionType.WITHDRAW);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testAtmOperationWithdraw_AccountPinNotValidException() {
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.ofNullable(account));

        ResponseEntity response = atmService.atmOperation(account.getAccountNumber(),159753L,account.getBalance(), TransactionType.WITHDRAW);

        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    public void testAtmOperationWithdraw_AccountHasNotEnoughMoneyException()  {
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.ofNullable(account));

        ResponseEntity response = atmService.atmOperation(account.getAccountNumber(),account.getPin(),BigDecimal.valueOf(5000L), TransactionType.WITHDRAW);

        assertEquals(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE,response.getStatusCode());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    public void testAtmOperationWithdraw_AccountBadRequestException()  {

        ResponseEntity response = atmService.atmOperation(null,account.getPin(),BigDecimal.valueOf(5000L), TransactionType.WITHDRAW);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void testAtmOperationWithdraw_AccountAmountException()  {

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.ofNullable(account));

        ResponseEntity response = atmService.atmOperation(account.getAccountNumber(),account.getPin(),null, TransactionType.WITHDRAW);

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    public void testAtmOperationWithdraw_ATMNoMoneyException()  {
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.ofNullable(account));

        ResponseEntity response = atmService.atmOperation(account.getAccountNumber(),account.getPin(),account.getBalance(), TransactionType.WITHDRAW);

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    void testAtmOperationWithdraw(){

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.ofNullable(account));

        Set<Map.Entry<BanknoteType, Integer>> entrySet = new HashSet<>(10);
        entrySet.add(new AbstractMap.SimpleEntry<>(BanknoteType.FIFTY_BANKNOTE, 10));
        entrySet.add(new AbstractMap.SimpleEntry<>(BanknoteType.TWENTY_BANKNOTE, 30));
        entrySet.add(new AbstractMap.SimpleEntry<>(BanknoteType.TEN_BANKNOTE, 30));
        entrySet.add(new AbstractMap.SimpleEntry<>(BanknoteType.FIVE_BANKNOTE, 20));

        when(atmMachineState.entrySet()).thenReturn(entrySet);

        when(atmRepository.findByAtmNo(1L)).thenReturn(Optional.of(atmList));

        ResponseEntity responseService = atmService.atmOperation(account.getAccountNumber(),account.getPin(),BigDecimal.valueOf(50L), TransactionType.WITHDRAW);

        assertEquals(responseService.getStatusCode(),response.getStatusCode());
        assertEquals(account.getBalance(),BigDecimal.valueOf(750L));
        assertEquals(atmList.stream().mapToLong(element->element.getBanknoteType().value*element.getBanknoteCount()).sum(),1450L);

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
        verify(atmMachineState).entrySet();
        verify(atmRepository,times(2)).findByAtmNo(1L);
        verify(atmRepository).saveAll(atmList);
        verify(transactionRepository,times(2)).save(any(Transaction.class));
        verify(messageSource).getMessage("account.withdraw.successful",null,LocaleContextHolder.getLocale());

        verifyNoMoreInteractions(accountRepository,atmMachineState,atmRepository,transactionRepository,messageSource);
    }

    @Test
    void testAtmOperationDeposit(){
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.ofNullable(account));
        when(atmRepository.findByAtmNo(1L)).thenReturn(Optional.of(atmList));

        ResponseEntity responseService = atmService.atmOperation(account.getAccountNumber(),account.getPin(),BigDecimal.valueOf(50L), TransactionType.DEPOSIT);

        assertEquals(responseService.getStatusCode(),response.getStatusCode());
        assertEquals(account.getBalance(),BigDecimal.valueOf(850L));
        assertEquals(atmList.stream().mapToLong(element->element.getBanknoteType().value*element.getBanknoteCount()).sum(),1550L);

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
        verify(atmRepository,times(2)).findByAtmNo(1L);
        verify(atmRepository).saveAll(atmList);
        verify(transactionRepository,times(2)).save(any(Transaction.class));
        verify(messageSource).getMessage("account.deposit.successful",null,LocaleContextHolder.getLocale());

        verifyNoMoreInteractions(accountRepository,atmRepository,transactionRepository,messageSource);
    }

}
