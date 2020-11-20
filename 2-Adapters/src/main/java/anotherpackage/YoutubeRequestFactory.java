package anotherpackage;

import somepackage.BasicVideoInfo;
import somepackage.FullVideoInfo;
import somepackage.MyDownloadProgressCallback;
import somepackage.YoutubeException;

import java.util.List;

public interface YoutubeRequestFactory {
//    YoutubeRequest makeRequest(String id);
//    YoutubeRequest makeRequest(String id, String destinationFolder);
    FullVideoInfo getFullVideoInfo(String id) throws YoutubeException;
    void downloadAudio(String videoId, String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException;
    List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws YoutubeException;
}
