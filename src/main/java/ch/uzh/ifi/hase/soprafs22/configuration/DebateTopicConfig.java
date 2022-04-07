package ch.uzh.ifi.hase.soprafs22.configuration;


import ch.uzh.ifi.hase.soprafs22.entity.DebateTopic;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.DebateTopicRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Configuration
public class DebateTopicConfig {


    //create one default topic for user just for testing
    @Bean
    CommandLineRunner commandLineRunner(DebateTopicRepository debateTopicRepository, UserRepository userRepository){

        return args -> {

            User user1 = new User();
            user1.setUsername("u1");
            user1.setName("n1");
            user1.setPassword("p1");
            user1.setCreationDate(LocalDate.now());
            user1.setToken(UUID.randomUUID().toString());

            User user2 = new User();
            user2.setUsername("u2");
            user2.setName("n2");
            user2.setPassword("p2");
            user2.setCreationDate(LocalDate.now());
            user2.setToken(UUID.randomUUID().toString());

            DebateTopic defaultDebateTopic1 =  new DebateTopic();
            defaultDebateTopic1.setCreatorUserId(1L);
            defaultDebateTopic1.setTopic("Test default Topic1 belongs user1");
            defaultDebateTopic1.setTopicDescription("Test default Topic1 description");

            DebateTopic defaultDebateTopic2 =  new DebateTopic();
            defaultDebateTopic2.setCreatorUserId(2L);
            defaultDebateTopic2.setTopic("Test default Topic2 belongs user2 ");
            defaultDebateTopic2.setTopicDescription("Test default Topic2 description");




            userRepository.saveAll(List.of(user1,user2));
            debateTopicRepository.saveAll(List.of(defaultDebateTopic1,defaultDebateTopic2));
        };
    }

}
