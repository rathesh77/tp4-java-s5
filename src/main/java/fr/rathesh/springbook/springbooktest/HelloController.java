package fr.rathesh.springbook.springbooktest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

        Pokemon mappedPokemon = objectMapper.readValue(response.body().string(), Pokemon.class);
        cachePokemon.put(pokemon,mappedPokemon);

        return mappedPokemon;
    }

    @RequestMapping("/pokemon")
    public Map<String,Pokemon> getListPokemons(){

        return cachePokemon;
    }

    @PostMapping(value= "/pokemon",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Map<String,Pokemon> createPokemon(@RequestBody Pokemon pokemon){
        System.out.println(pokemon);
        cachePokemon.put(pokemon.getName(),pokemon);
        return cachePokemon;
    }

}
