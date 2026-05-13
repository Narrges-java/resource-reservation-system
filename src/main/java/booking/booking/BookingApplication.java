package booking.booking;

import config.DataLoaderConfig;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "booking.booking",
        "config",
        "controller",
        "service",
        "service.impl",
        "repository",
        "entity",
        "exception"
}
)
@EntityScan("entity")
@EnableJpaRepositories("repository")
public class BookingApplication {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }



    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

}
