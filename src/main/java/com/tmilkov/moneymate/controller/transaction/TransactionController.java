package com.tmilkov.moneymate.controller.transaction;

import com.tmilkov.moneymate.service.transaction.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionsService service;

    @GetMapping
    public String getTransactions(Model model) {
        model.addAttribute("transactions", service.getAllTransactions());


        return "transactions";
    }

}
