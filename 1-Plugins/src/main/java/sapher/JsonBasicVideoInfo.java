package sapher;

import com.fasterxml.jackson.annotation.*;
import somepackage.BasicVideoInfo;


public class JsonBasicVideoInfo implements BasicVideoInfo {

    private final String videoTitle;
    private final String videoId;
    
    @JsonAnySetter
    public void methodToCatchUnneededValuesFromJson(String propertyKey, Object value) {
        System.out.println(propertyKey + value);
    }
    
    @JsonCreator
    public JsonBasicVideoInfo(
            @JsonProperty(value = "id", required = true) String videoId,
            @JsonProperty(value = "title", required = true) String videoTitle) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
    }

    @Override
    public String getVideoId() {
        return videoId;
    }

    @Override
    public String getVideoTitle() {
        return videoTitle;
    }

    @Override
    public String toString() {
        return "\"" + videoId + "\", \"" + videoTitle + "\"";
    }
}