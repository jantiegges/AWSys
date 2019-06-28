// TODO: remove for final turn in
// this is only for adding a test channel upon server start up!

package de.tub.ise.anwsys;

import de.tub.ise.anwsys.repositories.ChannelRepository;
import de.tub.ise.anwsys.model.Channel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class PreloadDatabase {

    @Bean
    CommandLineRunner initDatabase(ChannelRepository channelRepository) {
        return args -> {
            System.out.println("Preloading " + channelRepository.save(new Channel("Preloaded Test Channel 1", "Test 1")));
            System.out.println("Preloading " + channelRepository.save(new Channel("Preloaded Test Channel 2", "Test 2")));
        };
    }
}