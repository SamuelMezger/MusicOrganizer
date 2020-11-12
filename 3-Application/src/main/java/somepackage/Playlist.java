package somepackage;

import java.util.List;

public interface Playlist {

    List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws YoutubeException;
}
