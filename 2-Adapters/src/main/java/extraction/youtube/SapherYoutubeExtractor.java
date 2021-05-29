package extraction.youtube;

import extraction.Downloader;
import extraction.MyDownloadProgressCallback;
import extraction.YoutubeExtractor;
import extraction.ExtractionException;
import image.Crop;
import model.metadata.Metadata;
import model.metadata.Metadatum;
import model.youtube.BasicVideoInfo;
import model.youtube.FullVideoInfo;

import java.io.IOException;
import java.util.ArrayList;
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
            throw new ExtractionException("Could not find any videos");
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
    public Metadata getFullVideoInfo(String id) throws ExtractionException, IOException {
        FullVideoInfo videoInfo = this.youtubeRequestFactory.getFullVideoInfo(id);

        List<Metadatum> info = new ArrayList<>();

        info.add(new Metadatum.Cover(Crop.centerSquare(
                this.downloader.getOkImage(videoInfo.getVideoThumbnailURL()
                        .replace("maxresdefault", "mqdefault"))
        )));

        info.add(new Metadatum.Title(videoInfo.getTitle().orElse(videoInfo.getVideoTitle())));

        videoInfo.getArtist().ifPresent(artistString -> info.add(new Metadatum.Artist(artistString)));
        videoInfo.getAlbum().ifPresent(albumString -> info.add(new Metadatum.Album(albumString)));

        videoInfo.getReleaseYear().ifPresent(
                yearString -> info.add(new Metadatum.ReleaseYear(Integer.parseInt(yearString)))
        );

        return new Metadata(info);
    }
}
