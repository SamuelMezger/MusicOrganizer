package nanojson;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParserException;
import json.JsonParserI;

public class JsonParserAdapter implements JsonParserI {

    @Override
    public JsonObjectAdapter jsonObjectFrom(String s) throws JsonParserI.JsonParserAdapterException {
        try {
            return new JsonObjectAdapter(JsonParser.object().from(s));
        } catch (JsonParserException e) {
            throw new JsonParserAdapterException(e);
        }
    }

    public class JsonObjectAdapter implements JsonObjectI {
        private final JsonObject jsonObject;

        public JsonObjectAdapter(JsonObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public JsonArrayI getArray(String key) {
            return new JsonArrayAdapter(this.jsonObject.getArray(key));
        }

        @Override
        public String getString(String key) {
            return this.jsonObject.getString(key);
        }

        @Override
        public int getInt(String key) {
            return this.jsonObject.getInt(key);
        }
    }


    private class JsonArrayAdapter implements JsonArrayI{
        private final JsonArray jsonArray;

        public JsonArrayAdapter(JsonArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @Override
        public JsonArrayI getArray(int key) {
            return new JsonArrayAdapter(this.jsonArray.getArray(key));
        }

        @Override
        public String getString(int key) {
            return this.jsonArray.getString(key);
        }

        @Override
        public JsonObjectI getObject(int key) {
            return new JsonObjectAdapter(this.jsonArray.getObject(key));
        }


        @Override
        public int getInt(int key) {
            return this.jsonArray.getInt(key);
        }
    }
}
