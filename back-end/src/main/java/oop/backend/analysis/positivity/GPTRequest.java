package oop.backend.analysis.positivity;

import com.google.gson.Gson;
import lombok.Getter;
import oop.backend.App;
import oop.backend.analysis.Analyzer;
import oop.backend.analysis.dtos.PostData;
import oop.backend.analysis.dtos.Positivity;
import oop.backend.analysis.dtos.ResponseDTO;
import oop.backend.analysis.utils.JsonReader;
import oop.backend.analysis.utils.ContentFilter;
import oop.backend.config.Openai;
import oop.backend.config.PathFile;
import oop.backend.dtos.post.TwitterDTO;
import oop.backend.utils.fix.PathFixUtil;
import oop.backend.utils.json.JsonUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


/** Gửi request lên chatgpt để đánh giá độ tích cực của bài viết **/
@Component
@RestController
@RequestMapping("${api.v1}/analysis")
@Getter
public class GPTRequest extends Analyzer{

    private static final String REQUEST_BODY = """
            { 
                "model": "gpt-3.5-turbo", 
                "max_tokens": 50, 
                "messages" : [{"role":"user", "content":"Label sentiment(index+only positive|neutral|negative): %s"}]
            }
            """;

    /* List<DataElement> loadData() ... */

    /* handleData() */
    @Override
    public List<Positivity> handleData(String selection) throws Exception {
        System.out.println("GPTRequest");
        List<PostData> loadDataset = loadData(selection);
        List<Positivity> res = new ArrayList<>();
        for (PostData element : loadDataset) {
            String marketplace = element.getMarketplace();
            String collection = element.getCollection();
            System.out.println("Processing " + collection + " from " + marketplace);
            String LOAD_PATH = PathFixUtil.fix(App.class.getResource("/json/post/").getPath() + "top/" + marketplace + "/" + collection + ".json");
            System.out.println("Loading post data of " + collection + " in " + LOAD_PATH);
            List<TwitterDTO> postList = JsonReader.jsonToObjectList(LOAD_PATH, TwitterDTO.class);
            List<String> contentList = new ArrayList<>();
            for (TwitterDTO t : postList) {
                contentList.add(ContentFilter.removeSpecialCharacters(t.getContent()));
            }
            List<String> response = getGPTResponse(contentList);
            for (String r : response) {
                Positivity positivity = ContentFilter.countRating(collection, r);
                res.add(positivity);
            }
            String PATH = PathFixUtil.fix(App.class.getResource(PathFile.PATH_POSITIVITY).getPath()) + marketplace + ".json";
            JsonUtil<Positivity> jsonHandler = new JsonUtil<>(PATH);
            jsonHandler.saveDataToJson(res);
        }
        return res;
    }



    // Gửi resonse
    @GetMapping("/positivity/{selection}/AllTime")
    public ResponseEntity<?> response(@PathVariable String selection) {
        try {
            return ResponseEntity.ok(handleData(selection));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error");
        }
    }




    public List<String> getGPTResponse (List<String> contentList) throws Exception {
        List<String> res = new ArrayList<>();

        StringBuilder inputBuilder = new StringBuilder();
        int i = 0;
        int size = contentList.size();
        System.out.println(size);
        int toggle = 0;
        for (String s : contentList) {
            i++;
            inputBuilder.append("(").append(i).append(".)").append(s);
            System.out.println(inputBuilder.toString());
            if (i == 10 || i == size) {
                try {
                    String input = String.format(REQUEST_BODY, inputBuilder.toString());
                    System.out.println(input);
                    Gson gson = new Gson();
                    String key = (toggle == 0) ? Openai.API_KEY_1 : Openai.API_KEY_2;
                    System.out.println("toggle "+toggle);
                    toggle = 1-toggle;
                    String restring = sendRequest(Openai.API, input, key);

                    System.out.println(restring);
                    ResponseDTO response = gson.fromJson(restring , ResponseDTO.class);
                    System.out.println(response.getChoices().get(0).getMessage().getContent());
                    res.add(response.getChoices().get(0).getMessage().getContent());

                    i=0;size-=10;
                    inputBuilder.setLength(0);
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }



    private static String sendRequest(String url, String data, String key) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
