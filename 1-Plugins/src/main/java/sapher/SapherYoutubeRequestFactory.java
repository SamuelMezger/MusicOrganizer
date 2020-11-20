package sapher;

import com.sapher.youtubedl.YoutubeDL;
import com.sapher.youtubedl.YoutubeDLException;
import com.sapher.youtubedl.mapper.VideoInfo;
import somepackage.FullVideoInfo;
import somepackage.SapherFullVideoInfo;
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
}
