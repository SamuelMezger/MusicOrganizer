package anotherpackage;

import somepackage.BasicVideoInfo;
import somepackage.Playlist;
import somepackage.YoutubeException;

import java.util.List;

public class SapherPlaylist implements Playlist {

    private final String playListId;
    private final YoutubeRequestFactory youtubeRequestFactory;

    public SapherPlaylist(String playListId, YoutubeRequestFactory youtubeRequestFactory) {
        this.playListId = playListId;
        this.youtubeRequestFactory = youtubeRequestFactory;
    }

    @Override
    public List<BasicVideoInfo> getBasicVideoInfos() throws YoutubeException {
        return this.youtubeRequestFactory.getBasicVideoInfos(this.playListId);
    }
    
}
