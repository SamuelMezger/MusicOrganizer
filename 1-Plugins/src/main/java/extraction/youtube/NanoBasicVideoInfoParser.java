package extraction.youtube;

import extraction.ExtractionException;
import extraction.json.JsonParserI;
import model.youtube.BasicVideoInfo;

public class NanoBasicVideoInfoParser implements BasicVideoInfoParser {
    private final JsonParserI jsonParser;

    public NanoBasicVideoInfoParser(JsonParserI jsonParser) {
        this.jsonParser = jsonParser;
    }

    @Override
    public BasicVideoInfo fromJson(String json) throws ExtractionException {
        JsonParserI.JsonObjectI basicVideoJson = this.jsonParser.jsonObjectFrom(json);
        String id = basicVideoJson.getString("id");
        String title = basicVideoJson.getString("title");
        return new BasicVideoInfo(id, title);
    }
}
