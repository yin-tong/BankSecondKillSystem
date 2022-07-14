package com.bsks.service;

import com.bsks.entity.FirstFilterRule;

public interface RedisService {

    boolean hasFirstFilter();

    FirstFilterRule getFirstFilterRule();

    void saveUpdateFirstFilterRule(FirstFilterRule firstFilterRule);
}
