package dk.via.bank;

import dk.via.bank.data.DatabaseHelper;
import dk.via.bank.model.Account;
import dk.via.bank.model.Customer;
import dk.via.bank.model.ExchangeRate;
import dk.via.bank.model.transaction.AbstractTransaction;
import dk.via.bank.model.transaction.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;

@Configuration
@ComponentScan
public class Config implements WebMvcConfigurer {
    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=bank";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "password";

    @Bean
    @Scope("singleton")
    public DatabaseHelper<Account> getAccountHelper() {
        return new DatabaseHelper<>(JDBC_URL, USERNAME, PASSWORD);
    }

    @Bean
    @Scope("singleton")
    public DatabaseHelper<Customer> getCustomerHelper() {
        return new DatabaseHelper<>(JDBC_URL, USERNAME, PASSWORD);
    }

    @Bean
    @Scope("singleton")
    public DatabaseHelper<ExchangeRate> getExchangeRateHelper() {
        return new DatabaseHelper<>(JDBC_URL, USERNAME, PASSWORD);
    }

    @Bean
    @Scope("singleton")
    public DatabaseHelper<Transaction> getTransactionHelper() {
        return new DatabaseHelper<>(JDBC_URL, USERNAME, PASSWORD);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns("*");
    }
}
