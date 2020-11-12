package anotherpackage;

import somepackage.FullVideoInfo;
import somepackage.YoutubeException;

public interface YoutubeRequestFactory {
    public YoutubeRequest makeRequest(String id);
    public YoutubeRequest makeRequest(String id, String destinationFolder);

    FullVideoInfo getFullVideoInfo(String id) throws YoutubeException;
}
