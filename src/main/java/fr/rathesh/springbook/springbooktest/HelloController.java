package fr.rathesh.springbook.springbooktest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    private OkHttpClient client = new OkHttpClient();
    private Map<String, Pokemon> cachePokemon = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public HelloController()  {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @RequestMapping("/")
    public String index() {
        return "Hello World !";
    }

    @RequestMapping("/espece/{pokemon}")
    public Pokemon getInfos(@PathVariable String pokemon) throws IOException {
        if ( cachePokemon.get(pokemon) != null)
        {
            return cachePokemon.get(pokemon);
        }
        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon/"+pokemon)
                .build();
        Response response = client.newCall(request).execute();

        // On sait pas pourquoi mais ça marche mieux avec ça
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Pokemon pokemonLu = objectMapper.readValue(response.body().charStream(), Pokemon.class);
        cachePokemon.put(pokemon, pokemonLu);
        return pokemonLu;
    }
}
