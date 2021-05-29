package extraction;


import model.metadata.Metadata;
import model.youtube.BasicVideoInfo;

import java.io.IOException;
import java.util.List;

public interface YoutubeExtractor {
    List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws ExtractionException;
    Metadata getFullVideoInfo(String id) throws ExtractionException, IOException;
    void downloadAudio(String videoId, String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws ExtractionException;
}
