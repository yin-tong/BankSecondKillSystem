package com.bsks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsks.api.entity.Account;
import com.bsks.api.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 账户信息表 服务类
 * </p>
 *
 * @author Li
 * @since 2022-02-10
 */
public interface AccountService extends IService<Account> {

    Account findByPhone(String phone);

    Result register(Account account);

    Result updateAccount(Account account);

    List<Account> fuzzyQueryByName(String name, long page, long size);

    List<Account> fuzzyQueryByIdentityId(String identityId,long page, long size);

    Account findById(Long id);

    int fuzzyQueryByIdentityIdCount(String identityId);

    int fuzzyQueryByNameCount(String name);

    int getAccountCount();

    List<Account> getAccount(long page, long size);

    int fuzzyQueryByPhoneCount(String phone);

    List<Account> fuzzyQueryByPhone(String phone, long currentPage, long pageSize);
}
