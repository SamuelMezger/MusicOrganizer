package somepackage;

import java.util.List;

public interface Playlist {

    List<BasicVideoInfo> getBasicVideoInfos() throws YoutubeException;
}
