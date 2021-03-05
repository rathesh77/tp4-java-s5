package fr.rathesh.springbook.springbooktest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class HelloController {
    private OkHttpClient client = new OkHttpClient();
    private HashMap<String, String> data = new HashMap<String, String>();

    @RequestMapping("/")
    public String index() {
        return "Hello World !";
    }
    @RequestMapping("/espece/{pokemon}")
    public String getInfos(@PathVariable String pokemon) throws IOException {
        if ( data.get("pokemon") != null)
        {
            return data.get("pokemon");
        }
        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon/"+pokemon)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
