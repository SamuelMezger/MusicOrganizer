package extraction;


import model.youtube.BasicVideoInfo;
import model.Metadata;

import java.util.List;

public interface YoutubeExtractor {
    List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws ExtractionException;
    Metadata getFullVideoInfo(String id) throws ExtractionException;
    void downloadAudio(String videoId, String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws ExtractionException;
}
