package com.bsks;

import com.bsks.utils.SM2Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class SM2Test {


    @Test
    public void test() throws IOException {
        System.out.println(SM2Utils.encrypt("8"));
        System.out.println(SM2Utils.decrypt("8"));
        System.out.println(SM2Utils.decrypt("MEUCIQDzvwaHD9qOM3H813OhRWRD8hXaOeKfcZ-m5kbcMDgWFgIgP9zUKM6Yp_OwEwC7vwSjNGH9QSuZD56jkv9wZSDQ448"));
    }

}
