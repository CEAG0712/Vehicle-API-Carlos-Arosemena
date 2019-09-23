package com.udacity.pricing.testdata;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

//@Configuration
public class loadH2 {

    private static final Logger logger =  LogManager.getLogger(loadH2.class);

    @Bean
    CommandLineRunner loadSampleData(PriceRepository repository){
        return args -> {
          repository.save(new Price("CAD",new BigDecimal("12000"), 13L));
          repository.save(new Price("CAD",new BigDecimal("15000"), 15L));
          repository.save(new Price("CAD",new BigDecimal("17000"), 17L));

          logger.info("The loadH2 ran and inserted sample data on to the H2 db");
          logger.warn("WARNING! if this is running in PROD with MySQL, it will cause data duplicates ever time the server starts");
        };
    }
}
