package com.bsks.service;


import com.bsks.api.entity.Account;

public interface RedisService {

    boolean contains(Account account);

    public void saveAccount(Account account);

    Account findAccountById(long id);

    Account findAccountByPhone(String phone);

    public void deleteAccount(Account account);

    void saveCode(String phoneNumber,String code,int codeTimeLength);

    boolean hasCode(String phoneNumber);

    String getCode(String phoneNumber);
}
