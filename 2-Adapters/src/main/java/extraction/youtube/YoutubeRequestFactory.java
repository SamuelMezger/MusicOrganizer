package extraction.youtube;

import model.youtube.FullVideoInfo;
import extraction.ExtractionException;

public interface YoutubeRequestFactory {
    YoutubeRequest makeRequest(String id);
    YoutubeRequest makeRequest(String id, String destinationFolder);
    FullVideoInfo getFullVideoInfo(String id) throws ExtractionException;
}
