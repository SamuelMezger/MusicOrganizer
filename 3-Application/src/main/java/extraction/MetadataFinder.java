package extraction;

import model.metadata.Metadata;

import java.io.IOException;
import java.util.List;

public interface MetadataFinder {
    List<Metadata> searchFor(String searchTerm) throws IOException, ExtractionException;
}
