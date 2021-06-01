package extraction;

public interface ProgressCallback {
    void onProgressUpdate(float progress, long etaInSeconds);
}
