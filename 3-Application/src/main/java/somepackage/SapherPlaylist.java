package somepackage;


import java.util.List;

public class SapherPlaylist implements Playlist {

    private final String playListId;
    private final YoutubeExtractor youtubeExtractor;

    public SapherPlaylist(String playListId, YoutubeExtractor youtubeExtractor) {
        this.playListId = playListId;
        this.youtubeExtractor = youtubeExtractor;
    }

    @Override
    public List<BasicVideoInfo> getBasicVideoInfos() throws YoutubeException {
        return this.youtubeExtractor.getBasicVideoInfos(this.playListId);
    }
    
}
