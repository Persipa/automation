package site.persipa.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author persipa
 */
@SpringBootApplication
public class AutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomationApplication.class, args);

        System.out.printf("\033[32m%s Started.\n\033[0m", Thread.currentThread().getStackTrace()[1].getClassName());

    }
}
