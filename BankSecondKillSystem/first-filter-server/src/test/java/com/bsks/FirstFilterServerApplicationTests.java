package com.bsks;


import com.bsks.mapper.RepayRecordMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@SpringBootTest
public class FirstFilterServerApplicationTests {

    @Autowired
    private RepayRecordMapper loanRecordMapper;

    @Test
    public void contextLoads() {
        BigDecimal bigDecimal = new BigDecimal(1);
        int cnt = loanRecordMapper.getLoanTimes("2",3,bigDecimal,2);
        System.out.println(cnt);
    }
}
