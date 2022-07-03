package com.ouz.atmsimulation.config;

import com.ouz.atmsimulation.enums.BanknoteType;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.HashMap;


@Configuration
public class AppDataConfiguration {

    @Bean
    public HashMap<BanknoteType,Integer> atmMachineState(){
        HashMap<BanknoteType,Integer> atmMachineMoney = new HashMap<>();
        atmMachineMoney.put(BanknoteType.FIFTY_BANKNOTE,10);
        atmMachineMoney.put(BanknoteType.TWENTY_BANKNOTE,30);
        atmMachineMoney.put(BanknoteType.TEN_BANKNOTE,30);
        atmMachineMoney.put(BanknoteType.FIVE_BANKNOTE,20);

        return atmMachineMoney;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/message/api-messages"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
