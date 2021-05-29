package nanojson;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParserException;
import extraction.ExtractionException;
import json.JsonParserI;

public class JsonParserAdapter implements JsonParserI {

    @Override
    public JsonObjectAdapter jsonObjectFrom(String s) throws ExtractionException {
        try {
            return new JsonObjectAdapter(JsonParser.object().from(s));
        } catch (JsonParserException e) {
            throw new ExtractionException(e);
        }
    }

    public class JsonObjectAdapter implements JsonObjectI {
        private final JsonObject jsonObject;

        public JsonObjectAdapter(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public JsonArrayI getArray(String key) throws ExtractionException {
            return new JsonArrayAdapter(JsonParserAdapter.this.assertNonNull(key, this.jsonObject.getArray(key)));
        }

        @Override
        public String getString(String key) throws ExtractionException {
            return JsonParserAdapter.this.assertNonNull(key, this.jsonObject.getString(key));
        }

        @Override
        public int getInt(String key) throws ExtractionException {
            return JsonParserAdapter.this.assertNonNull(key, this.jsonObject.getInt(key));
        }
    }


    private class JsonArrayAdapter  implements JsonArrayI{
        private final JsonArray jsonArray;

        public JsonArrayAdapter(JsonArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @Override
        public JsonArrayI getArray(int key) throws ExtractionException {
            return new JsonArrayAdapter(JsonParserAdapter.this.assertNonNull(key, this.jsonArray.getArray(key)));
        }

        @Override
        public String getString(int key) throws ExtractionException {
            return JsonParserAdapter.this.assertNonNull(key, this.jsonArray.getString(key));
        }

        @Override
        public JsonObjectI getObject(int key) throws ExtractionException {
            return new JsonObjectAdapter(JsonParserAdapter.this.assertNonNull(key, this.jsonArray.getObject(key)));
        }

        @Override
        public int getInt(int key) throws ExtractionException {
            return JsonParserAdapter.this.assertNonNull(key, this.jsonArray.getInt(key));
        }

        @Override
        public int size(){
            return this.jsonArray.size();
        }
    }

    private <T> T assertNonNull(String key, T value) throws ExtractionException {
        if (value != null) return value;
        else throw new ExtractionException("Value for key \"" + key + "\" is null or key is not present");
    }

    private <T> T assertNonNull(Integer key, T value) throws ExtractionException {
        return this.assertNonNull(key.toString(), value);
    }
}
