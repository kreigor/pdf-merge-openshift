package io.kreiger.pdfmerge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import io.kreiger.pdfmerge.controller.HomeController;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationTest {

    @Autowired
    private HomeController homeController;

    @Test
    public void contextLoads() {
        assertThat(homeController).isNotNull();
    }
}
