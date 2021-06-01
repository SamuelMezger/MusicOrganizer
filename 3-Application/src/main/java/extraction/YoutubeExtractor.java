package extraction;


import model.metadata.Metadata;
import model.youtube.BasicVideoInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface YoutubeExtractor {
    List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws ExtractionException;
    Metadata getFullVideoInfo(String id) throws ExtractionException, IOException;
    File downloadAudio(String videoId,
                       String destinationFolderPath,
                       ProgressCallback progressCallback)
            throws ExtractionException, FileNotFoundException;
}
