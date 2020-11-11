package sapher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import somepackage.FlatVideoInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonFlatVideoInfo implements FlatVideoInfo {
    private String id;
    private String title;

    public JsonFlatVideoInfo() { }

    public JsonFlatVideoInfo(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }
}