package sapher;

import somepackage.BasicVideoInfo;
import somepackage.FullVideoInfo;
import somepackage.YoutubeException;
import somepackage.MyDownloadProgressCallback;
import anotherpackage.YoutubeResponse;
import anotherpackage.SapherFullVideoInfo;
import anotherpackage.YoutubeRequest;
import anotherpackage.YoutubeRequestFactory;
import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.mapper.VideoInfo;
import jackson.JacksonVideoInfoParser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SapherYoutubeRequestFactory implements YoutubeRequestFactory {
    public YoutubeRequest makeRequest(String id) {
        return new SapherYoutubeRequest(id);
    }

    public YoutubeRequest makeRequest(String id, String destinationFolder) {
        return new SapherYoutubeRequest(id, destinationFolder);
    }

    @Override
    public FullVideoInfo getFullVideoInfo(String id) throws YoutubeException {
        try {
            VideoInfo videoInfo = YoutubeDL.getVideoInfo(id);
            return new SapherFullVideoInfo(
                    videoInfo.id,
                    videoInfo.title,
                    videoInfo.thumbnail,
                    videoInfo.description,
                    videoInfo.track == null ? Optional.empty() : Optional.of(videoInfo.track),
                    videoInfo.artist == null ? Optional.empty() : Optional.of(videoInfo.artist),
                    videoInfo.album == null ? Optional.empty() : Optional.of(videoInfo.album),
                    videoInfo.releaseDate == null ? Optional.empty() : Optional.of(videoInfo.releaseDate)
            );
        } catch (YoutubeDLException e) {
            e.printStackTrace();
            throw new YoutubeException(e);
        }
    }

//    @Override
//    public void download(String videoId, String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException {
//        YoutubeRequest downloadRequest = this.makeRequest(videoId, destinationFolder);
//        downloadRequest.setOption("format", "m4a");
//        downloadRequest.execute(myDownloadProgressCallback);
//    }
//
//    @Override
//    public List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws YoutubeException {
//        String flatPlayListRawOutput = this.getFlatPlayListRawOutput(playListId);
//
//        JacksonVideoInfoParser basicVideoInfoParser = new JacksonVideoInfoParser();
//        return Arrays.stream(flatPlayListRawOutput.split("\n"))
//                .map(basicVideoInfoParser::fromJson)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toList());
//    }
//
//    public String getFlatPlayListRawOutput(String playListId) throws YoutubeException {
//        YoutubeRequest flatPlaylistInfoRequest = this.makeRequest(playListId);
//        flatPlaylistInfoRequest.setOption("skip-download");
//        flatPlaylistInfoRequest.setOption("flat-playlist");
//        flatPlaylistInfoRequest.setOption("dump-json");
//
//        YoutubeResponse response = flatPlaylistInfoRequest.execute();
//
//        return response.getOut();
//    }
}
