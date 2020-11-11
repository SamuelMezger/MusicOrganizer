package anotherpackage;

import somepackage.FlatVideoInfo;
import somepackage.YouTube;
import somepackage.YoutubeException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SapherYouTube implements YouTube {

    private final YoutubeRequestFactory youtubeRequestFactory;
    private final FlatVideoInfoParser flatVideoInfoParser;

    public SapherYouTube(YoutubeRequestFactory youtubeRequestFactory, FlatVideoInfoParser flatVideoInfoParser) {
        this.youtubeRequestFactory = youtubeRequestFactory;
        this.flatVideoInfoParser = flatVideoInfoParser;
    }

    @Override
    public List<FlatVideoInfo> flatPlaylistInfo(String playListId) throws YoutubeException {
        String flatPlayListRawOutput = this.getFlatPlayListRawOutput(playListId);

        return Arrays.stream(flatPlayListRawOutput.split("\n"))
                .map(flatVideoInfoParser::fromJson)
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
