package sh.now.npennie.sendtracker.sendservice;

import com.google.common.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sh.now.npennie.sendtracker.sendservice.providers.SendJsonProvider;
import sh.now.npennie.sendtracker.sendservice.providers.SendProvider;

import java.io.IOException;

@Configuration
public class AppConfig {
    private final String sendJsonResource = "sends.mock.json";

    /**
     * Constructs a SendProvider.
     * Currently defaults to the JSON SendProvider, which loads mock data from a JSON file.
     * @return The constructed SendProvider
     */
    @Bean
    SendProvider getSendProvider() throws IOException {
        var filePath = Resources.getResource(sendJsonResource);
        return new SendJsonProvider(filePath);
    }
}
