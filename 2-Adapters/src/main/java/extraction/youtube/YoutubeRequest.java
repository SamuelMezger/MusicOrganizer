package extraction.youtube;

import extraction.ProgressCallback;
import extraction.ExtractionException;

public interface YoutubeRequest {
     void setOption(String key);
     void setOption(String key, String value);
     YoutubeResponse execute() throws ExtractionException;
     YoutubeResponse execute(ProgressCallback progressCallback) throws ExtractionException;
}
