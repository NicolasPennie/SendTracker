package sh.now.npennie.sendtracker.sendservice;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {
    private GraphQL graphQL;

    @Bean
    public GraphQL GraphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        var url = Resources.getResource("schema.graphql");
        var sdl = Resources.toString(url, Charsets.UTF_8);
        var schema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        var typeRegistry = new SchemaParser().parse(sdl);
        var wiring = buildWiring();
        var schemaGen = new SchemaGenerator();
        return schemaGen.makeExecutableSchema(typeRegistry, wiring);
    }

    private DataFetcher sendsDataFetcher() {
        List<Map<String, String>> sends = Arrays.asList(
                ImmutableMap.of("name", "November"),
                ImmutableMap.of("name", "Sahara"),
                ImmutableMap.of("name", "Feature Press")
        );

        return dataFetchingEnvironment -> sends;
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("sends", sendsDataFetcher())
                )
                .build();
    }
}
