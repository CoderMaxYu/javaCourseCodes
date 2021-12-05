package com.yuwq.service;

import com.yuwq.entity.AccountDO;
import com.yuwq.mapper.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    @Cacheable(cacheNames = "accounts",unless = "#result eq null")
    public List<AccountDO> findAll() {
        System.out.println("查询所有账户");
        return accountDao.findAll();

    }

    @CachePut(cacheNames = "accounts")
    public int update(Long id) {
        System.out.println("更新账户：" + id);
        return accountDao.update(id);
    }
    @CacheEvict(cacheNames = "accounts")
    public int delete(Long id){
        System.out.println("删除账户："+id);
        return accountDao.delete(id);
    }
}
