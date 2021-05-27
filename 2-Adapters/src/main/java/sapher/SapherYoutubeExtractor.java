package sapher;

import extraction.Downloader;
import extraction.MyDownloadProgressCallback;
import extraction.YoutubeExtractor;
import extraction.ExtractionException;
import image.Crop;
import model.Uncertainty;
import model.youtube.BasicVideoInfo;
import model.Metadata;
import model.youtube.FullVideoInfo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SapherYoutubeExtractor implements YoutubeExtractor {

    private final YoutubeRequestFactory youtubeRequestFactory;
    private final BasicVideoInfoParser basicVideoInfoParser;
    private final Downloader downloader;

    public SapherYoutubeExtractor(YoutubeRequestFactory youtubeRequestFactory, BasicVideoInfoParser basicVideoInfoParser, Downloader downloader) {
        this.youtubeRequestFactory = youtubeRequestFactory;
        this.basicVideoInfoParser = basicVideoInfoParser;
        this.downloader = downloader;
    }

    @Override
    public void downloadAudio(String videoId, String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws ExtractionException {
        YoutubeRequest downloadRequest = this.youtubeRequestFactory.makeRequest(videoId, destinationFolder);
        downloadRequest.setOption("format", "m4a");
        downloadRequest.execute(myDownloadProgressCallback);
    }

    @Override
    public List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws ExtractionException {
        String flatPlayListRawOutput = this.getFlatPlayListRawOutput(playListId);
        if (!flatPlayListRawOutput.isEmpty()) {
            return Arrays.stream(flatPlayListRawOutput.split("\n"))
                    .map(this.basicVideoInfoParser::fromJson)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } else {
            throw new extraction.ExtractionException("Could not find any videos");
        }
    }

    public String getFlatPlayListRawOutput(String playListId) throws ExtractionException {
        YoutubeRequest flatPlaylistInfoRequest = this.youtubeRequestFactory.makeRequest(playListId);
        flatPlaylistInfoRequest.setOption("skip-download");
        flatPlaylistInfoRequest.setOption("flat-playlist");
        flatPlaylistInfoRequest.setOption("dump-json");

        YoutubeResponse response = flatPlaylistInfoRequest.execute();

        return response.getOut();
    }
    
    @Override
    public Metadata getFullVideoInfo(String id) throws ExtractionException {
        FullVideoInfo videoInfo = this.youtubeRequestFactory.getFullVideoInfo(id);

        BufferedImage thumbnail = null;
        try {
            thumbnail = Crop.centerSquare(
                    this.downloader.getOkImage(videoInfo.getVideoThumbnailURL().replace("maxresdefault", "mqdefault"))
            );
//            TODO Fix exception handling
        } catch (IOException e) {
            e.printStackTrace();
        }

        int uncertainty;
        if (videoInfo.getReleaseYear().isPresent()) uncertainty = Uncertainty.YT_MUSIC;
        else if (videoInfo.getTitle().isPresent()) uncertainty = Uncertainty.MUSIC_IN_VID;
        else uncertainty = Uncertainty.UNCERTAIN;

        Optional<Integer> releaseYear;
        if (videoInfo.getReleaseYear().isPresent()) releaseYear = Optional.of(Integer.parseInt(videoInfo.getReleaseYear().get()));
        else releaseYear = Optional.empty();

        Optional<String> title = Optional.of(videoInfo.getTitle().orElse(videoInfo.getVideoTitle()));
        Optional<String> artist = videoInfo.getArtist();
        Optional<String> album = videoInfo.getAlbum();

        return new Metadata(
                Optional.of(thumbnail),
                title,
                artist,
                album,
                Optional.empty(),
                releaseYear,
                Optional.empty()
        );
    }
}
