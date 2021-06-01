package extraction.youtube;

import extraction.Downloader;
import extraction.ProgressCallback;
import extraction.YoutubeExtractor;
import extraction.ExtractionException;
import extraction.Crop;
import model.metadata.Metadata;
import model.metadata.MetadataField;
import model.youtube.BasicVideoInfo;
import model.youtube.FullVideoInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YtDLYoutubeExtractor implements YoutubeExtractor {

    private final YoutubeRequestFactory youtubeRequestFactory;
    private final BasicVideoInfoParser basicVideoInfoParser;
    private final Downloader downloader;

    public YtDLYoutubeExtractor(YoutubeRequestFactory youtubeRequestFactory, BasicVideoInfoParser basicVideoInfoParser, Downloader downloader) {
        this.youtubeRequestFactory = youtubeRequestFactory;
        this.basicVideoInfoParser = basicVideoInfoParser;
        this.downloader = downloader;
    }

    @Override
    public File downloadAudio(String videoId, String destinationFolderPath, ProgressCallback progressCallback) throws ExtractionException, FileNotFoundException {
        YoutubeRequest downloadRequest = this.youtubeRequestFactory.makeRequest(videoId, destinationFolderPath);
        downloadRequest.setOption("format", "m4a");
        downloadRequest.execute(progressCallback);
//        If there is no exception thrown to this point the file should be there,
//        but if the file was already downloaded youtube-dl doesn't update the progress
//        so we do it manually
        progressCallback.onProgressUpdate(100, 0);
        File folder = new File(destinationFolderPath);
        return Arrays.stream(folder.listFiles((dir, name) -> name.endsWith(videoId + ".m4a"))).findFirst()
                .orElseThrow(FileNotFoundException::new);
    }

    @Override
    public List<BasicVideoInfo> getBasicVideoInfos(String playListId) throws ExtractionException {
        String flatPlayListRawOutput = this.getFlatPlayListRawOutput(playListId);
        if (!flatPlayListRawOutput.isEmpty()) {
            List<BasicVideoInfo> basicInfos = new ArrayList<>();
            for (String json : flatPlayListRawOutput.split("\n")) {
                BasicVideoInfo basicVideoInfo = this.basicVideoInfoParser.fromJson(json);
                basicInfos.add(basicVideoInfo);
            }
            return basicInfos;
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

        List<MetadataField> info = new ArrayList<>();

        info.add(new MetadataField.Cover(Crop.centerSquare(
                this.downloader.getOkImage(videoInfo.getVideoThumbnailURL()
                        .replace("maxresdefault", "mqdefault"))
        )));

        info.add(new MetadataField.Title(videoInfo.getTitle().orElse(videoInfo.getVideoTitle())));

        videoInfo.getArtist().ifPresent(artistString -> info.add(new MetadataField.Artist(artistString)));
        videoInfo.getAlbum().ifPresent(albumString -> info.add(new MetadataField.Album(albumString)));

        videoInfo.getReleaseYear().ifPresent(
                yearString -> info.add(new MetadataField.ReleaseYear(Integer.parseInt(yearString)))
        );

        return new Metadata(info);
    }
}
