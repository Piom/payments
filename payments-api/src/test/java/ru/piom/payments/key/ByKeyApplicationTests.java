package ru.piom.payments.key;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ByKeyApplicationTests {
    @Autowired
    private MockMvc mvc;


    @Test
    public void givenPayment_whenCreatePayment_thenStatus200()
            throws Exception {
    }

}
