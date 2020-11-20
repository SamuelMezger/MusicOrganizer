package sapher;

import somepackage.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SapherYoutubeExtractor implements YoutubeExtractor {

    private final YoutubeRequestFactory youtubeRequestFactory;
    private final BasicVideoInfoParser basicVideoInfoParser;

    public SapherYoutubeExtractor(YoutubeRequestFactory youtubeRequestFactory, BasicVideoInfoParser basicVideoInfoParser) {
        this.youtubeRequestFactory = youtubeRequestFactory;
        this.basicVideoInfoParser = basicVideoInfoParser;
    }

    @Override
    public void downloadAudio(String videoId, String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException {
        YoutubeRequest downloadRequest = this.youtubeRequestFactory.makeRequest(videoId, destinationFolder);
        downloadRequest.setOption("format", "m4a");
        downloadRequest.execute(myDownloadProgressCallback);
    }

    @Override
    public List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws YoutubeException {
        String flatPlayListRawOutput = this.getFlatPlayListRawOutput(playListId);

        return Arrays.stream(flatPlayListRawOutput.split("\n"))
                .map(this.basicVideoInfoParser::fromJson)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public String getFlatPlayListRawOutput(String playListId) throws YoutubeException {
        YoutubeRequest flatPlaylistInfoRequest = this.youtubeRequestFactory.makeRequest(playListId);
        flatPlaylistInfoRequest.setOption("skip-download");
        flatPlaylistInfoRequest.setOption("flat-playlist");
        flatPlaylistInfoRequest.setOption("dump-json");

        YoutubeResponse response = flatPlaylistInfoRequest.execute();

        return response.getOut();
    }
    
    @Override
    public FullVideoInfo getFullVideoInfo(String id) throws YoutubeException {
        return this.youtubeRequestFactory.getFullVideoInfo(id);
    }
}
