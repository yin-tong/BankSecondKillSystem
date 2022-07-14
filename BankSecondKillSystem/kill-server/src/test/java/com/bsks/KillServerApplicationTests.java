package com.bsks;

import com.bsks.utils.SM2Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class KillServerApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        System.out.println(SM2Utils.decrypt("16565"));
    }

}
