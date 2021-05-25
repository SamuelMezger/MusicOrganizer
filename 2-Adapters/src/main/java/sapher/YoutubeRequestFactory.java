package sapher;

import model.youtube.FullVideoInfo;
import somepackage.YoutubeException;

public interface YoutubeRequestFactory {
    YoutubeRequest makeRequest(String id);
    YoutubeRequest makeRequest(String id, String destinationFolder);
    FullVideoInfo getFullVideoInfo(String id) throws YoutubeException;
}
