package config;

import entity.Resource;
import entity.User;
import entity.enums.ResourceType;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.ResourceRepository;
import repository.UserRepository;

import java.time.LocalDateTime;

@Configuration
public class DataLoaderConfig {

    @Bean
    @Transactional
    public ApplicationRunner dataLoader(UserRepository userRepository , ResourceRepository resourceRepository){
        return args -> {
            System.out.println("Starting data loading ... ");
            if (userRepository.count()==0 && resourceRepository.count() == 0){
                User user = new User();
                user.setFullName("Narges");
                user.setEmail("narges.rahati004@gmail.com");
                user.setCreatedAt(LocalDateTime.now());
                user.setUpdatedAt(LocalDateTime.now());
                User savedUser = userRepository.save(user);
                System.out.println("User loaded: " +savedUser.getFullName());

                Resource resource = new Resource();
                resource.setName("Room A");
                resource.setType(ResourceType.ROOM);
                resource.setCapacity(12);
                resource.setActive(true);
                resource.setCreatedAt(LocalDateTime.now());
                resource.setUpdatedAt(LocalDateTime.now());
                Resource saveResource = resourceRepository.save(resource);
                System.out.println("Resource loaded: " + saveResource.getName());
                System.out.println("Initial data loaded successfully!");

            }else {
                System.out.println("Initial data already exists. Skipping loading.");
            }
        };
    }
}
