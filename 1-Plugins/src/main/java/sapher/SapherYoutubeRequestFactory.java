package sapher;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.mapper.VideoInfo;
import somepackage.FullVideoInfo;
import somepackage.YoutubeException;

import java.util.Optional;

public class SapherYoutubeRequestFactory implements YoutubeRequestFactory {
    
    @Override
    public YoutubeRequest makeRequest(String id) {
        return new SapherYoutubeRequest(id);
    }

    @Override
    public YoutubeRequest makeRequest(String id, String destinationFolder) {
        return new SapherYoutubeRequest(id, destinationFolder);
    }

    @Override
    public FullVideoInfo getFullVideoInfo(String id) throws YoutubeException {
        try {
            VideoInfo videoInfo = YoutubeDL.getVideoInfo(id);
            return new FullVideoInfo(
                    videoInfo.id,
                    videoInfo.title,
                    videoInfo.thumbnail,
                    videoInfo.description,
                    Optional.ofNullable(videoInfo.track),
                    Optional.ofNullable(videoInfo.artist),
                    Optional.ofNullable(videoInfo.album),
                    Optional.ofNullable(videoInfo.releaseDate)
            );
        } catch (YoutubeDLException e) {
            e.printStackTrace();
            throw new YoutubeException(e);
        }
    }
}
