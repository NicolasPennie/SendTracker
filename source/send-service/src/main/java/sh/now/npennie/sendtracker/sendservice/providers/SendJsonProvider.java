package sh.now.npennie.sendtracker.sendservice.providers;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sh.now.npennie.sendtracker.sendservice.models.Send;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SendJsonProvider implements SendProvider {
    private final List<Send> sends;

    public SendJsonProvider(URL filePath) throws IOException {
        this.sends = loadSends(filePath);
    }

    public List<Send> getAllSends() {
        return sends;
    }

    private List<Send> loadSends(URL filePath) throws IOException {
        var sendsJson = Resources.toString(filePath, Charsets.UTF_8);
        var gson = new Gson();
        var sendListType = new TypeToken<List<Send>>(){}.getType();
        return gson.fromJson(sendsJson, sendListType);
    }
}
