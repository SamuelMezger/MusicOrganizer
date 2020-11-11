package anotherpackage;

public interface YoutubeRequestFactory {
    public YoutubeRequest makeRequest(String id);
    public YoutubeRequest makeRequest(String id, String destinationFolder);
}
