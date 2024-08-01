package org.phamxuantruong.asm.restController;

import org.phamxuantruong.asm.entity.Account;
import org.phamxuantruong.asm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/accounts")
public class AccountRestController {
    @Autowired
    AccountService accountService;
    @GetMapping
    public List<Account> getAccounts(@RequestParam("admin")Optional<Boolean> admin) {
        if (admin.orElse(false)) {
            return accountService.getAdministrators();
        }
        return accountService.findAll();
    }
}
