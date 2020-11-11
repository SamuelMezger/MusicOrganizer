package sapher;

import anotherpackage.YoutubeRequest;
import anotherpackage.YoutubeRequestFactory;

public class SapherYoutubeRequestFactory implements YoutubeRequestFactory {
    public YoutubeRequest makeRequest(String id) {
        return new SapherYoutubeRequest(id);
    }

    public YoutubeRequest makeRequest(String id, String destinationFolder) {
        return new SapherYoutubeRequest(id, destinationFolder);
    }
}
