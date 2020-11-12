package somepackage;

public interface BasicVideo extends BasicVideoInfo{

    FullVideoInfo getFullVideoInfo() throws YoutubeException;
}
