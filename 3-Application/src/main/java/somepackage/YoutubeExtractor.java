package somepackage;


import java.util.List;

public interface YoutubeExtractor {
    List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws YoutubeException;
    FullVideoInfo getFullVideoInfo(String id) throws YoutubeException;
    void downloadAudio(String videoId, String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException;
}