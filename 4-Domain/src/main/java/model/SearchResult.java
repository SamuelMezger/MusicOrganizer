package model;

public class SearchResult {
    final int uncertainty;
    final String url;
    final Metadata metadata;

    public SearchResult(int uncertainty, String url, Metadata metadata) {
        this.uncertainty = uncertainty;
        this.url = url;
        this.metadata = metadata;
    }
}
