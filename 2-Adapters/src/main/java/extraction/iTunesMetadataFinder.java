package extraction;

import json.JsonParserI;
import model.metadata.Metadata;
import model.metadata.Metadatum;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class iTunesMetadataFinder implements MetadataFinder {
    final static String baseUrl = "https://itunes.apple.com/search?media=music&term=";
    final Downloader downloader;
    final JsonParserI jsonParser;

    public iTunesMetadataFinder(Downloader downloader, JsonParserI jsonParser) {
        this.downloader = downloader;
        this.jsonParser = jsonParser;
    }

    @Override
    public List<Metadata> searchFor(String searchTerm) throws IOException, ExtractionException {
        String url = this.buildUrl(searchTerm);
        String itunesResponseBody = this.downloader.getOkString(url);
        JsonParserI.JsonArrayI jsonResults =  this.jsonParser.jsonObjectFrom(itunesResponseBody).getArray("results");
        return this.parseJsonResults(jsonResults);
    }

    private String buildUrl(String searchTerm) throws UnsupportedEncodingException {
        return baseUrl + URLEncoder.encode(searchTerm, StandardCharsets.UTF_8.toString());
    }

    private List<Metadata> parseJsonResults(JsonParserI.JsonArrayI jsonResults) throws IOException, ExtractionException {
        List<Metadata> results= new ArrayList<>();

        for (int i = 0; i < Math.min(3, jsonResults.size()); i++) {
            JsonParserI.JsonObjectI jsonResult = jsonResults.getObject(i);
            List<Metadatum> info = new ArrayList<>();

            String coverUrl = jsonResult.getString("artworkUrl100");
            System.out.println(coverUrl);
            info.add(new Metadatum.Cover(this.downloader.getOkImage(coverUrl)));

            info.add(new Metadatum.Title(jsonResult.getString("trackName")));
            info.add(new Metadatum.Artist(jsonResult.getString("artistName")));
            info.add(new Metadatum.Album(jsonResult.getString("collectionName")));
            info.add(new Metadatum.TrackNumber(jsonResult.getInt("trackNumber")));
            info.add(new Metadatum.ReleaseYear(Integer.parseInt(jsonResult.getString("releaseDate").substring(0, 4))));
            info.add(new Metadatum.Genre(jsonResult.getString("primaryGenreName")));

            results.add(new Metadata(info));
        }
        return results;
    }
}
