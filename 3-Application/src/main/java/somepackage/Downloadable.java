package somepackage;

import somepackage.MyDownloadProgressCallback;
import somepackage.YoutubeException;

public interface Downloadable {
    void download(String destinationFolder, MyDownloadProgressCallback myDownloadProgressCallback) throws YoutubeException;
}
