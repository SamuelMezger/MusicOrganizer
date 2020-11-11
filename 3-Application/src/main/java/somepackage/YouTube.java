package somepackage;

import somepackage.FlatVideoInfo;
import somepackage.YoutubeException;

import java.util.List;

public interface YouTube {

    List<FlatVideoInfo> flatPlaylistInfo(String playListId) throws YoutubeException;
}
