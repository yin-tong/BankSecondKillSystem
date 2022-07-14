package com.bsks;

import com.bsks.api.entity.Account;
import com.bsks.mapper.AccountMapper;
import com.bsks.service.AccountPayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class AccountTest {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountPayService accountPayService;

    @Test
    public void testInsert(){
        Account account = new Account();
        account.setId(2L);
        account.setPhone("289");
        accountMapper.updateById(account);

    }

    @Test
    public void testUpdate(){
        Account account = new Account();
        account.setId(5785L);
        System.out.println(accountMapper.updateById(account));

    }
}
