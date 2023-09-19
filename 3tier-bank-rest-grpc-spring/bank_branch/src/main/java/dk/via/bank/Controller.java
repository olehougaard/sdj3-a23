package dk.via.bank;

import dk.via.bank.service.BranchAccountService;
import dk.via.bank.service.BranchCustomerService;
import dk.via.bank.service.BranchExchangeService;
import dk.via.bank.service.BranchTransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Controller {
    private final BranchCustomerService customerService;
    private final BranchAccountService accountService;
    private final BranchTransactionService transactionService;
    private final BranchExchangeService exchangeService;


    public Controller(BranchCustomerService customerService, BranchAccountService accountService, BranchTransactionService transactionService, BranchExchangeService exchangeService) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.exchangeService = exchangeService;
    }

    @GetMapping
    public String get() {
        return "<html><body>Hello, World!</body></html>";
    }
}
