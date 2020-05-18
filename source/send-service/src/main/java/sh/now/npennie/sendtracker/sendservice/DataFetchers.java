package sh.now.npennie.sendtracker.sendservice;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sh.now.npennie.sendtracker.sendservice.models.Send;
import sh.now.npennie.sendtracker.sendservice.providers.SendProvider;

import java.util.List;

@Component
public class DataFetchers {

    @Autowired
    private SendProvider sendProvider;

    public DataFetcher<List<Send>> listSends() {
        return env -> sendProvider.getAllSends();
    }
}
