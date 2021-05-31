package extraction.youtube;

import extraction.ExtractionException;
import model.youtube.BasicVideoInfo;

import java.util.Optional;

public interface BasicVideoInfoParser {
    BasicVideoInfo fromJson(String json) throws ExtractionException;
}
