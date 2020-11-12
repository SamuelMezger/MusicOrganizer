package anotherpackage;

import somepackage.BasicVideoInfo;
import somepackage.Playlist;
import somepackage.YoutubeException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SapherPlaylist implements Playlist {

    private final YoutubeRequestFactory youtubeRequestFactory;
    private final BasicVideoInfoParser basicVideoInfoParser;

    public SapherPlaylist(YoutubeRequestFactory youtubeRequestFactory, BasicVideoInfoParser basicVideoInfoParser) {
        this.youtubeRequestFactory = youtubeRequestFactory;
        this.basicVideoInfoParser = basicVideoInfoParser;
    }

    @Override
    public List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws YoutubeException {
        String flatPlayListRawOutput = this.getFlatPlayListRawOutput(playListId);

        return Arrays.stream(flatPlayListRawOutput.split("\n"))
                .map(basicVideoInfoParser::fromJson)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private String getFlatPlayListRawOutput(String playListId) throws YoutubeException {
        YoutubeRequest flatPlaylistInfoRequest = this.youtubeRequestFactory.makeRequest(playListId);
        flatPlaylistInfoRequest.setOption("skip-download");
        flatPlaylistInfoRequest.setOption("flat-playlist");
        flatPlaylistInfoRequest.setOption("dump-json");

        YoutubeResponse response = flatPlaylistInfoRequest.execute();

        return response.getOut();
    }
}
