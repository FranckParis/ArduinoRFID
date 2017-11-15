package Server.src.Utils;

import Server.src.Ressources.Key;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonAdapter {

    private Gson gson;

    public JsonAdapter() {
        this.gson = new Gson();
    }

    public Key createKeyFromJson(JsonObject json){
        Key keyObject = this.gson.fromJson(json, Key.class);
        return keyObject;
    }
}
