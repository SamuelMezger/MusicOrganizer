package json;

public interface JsonParserI {
    JsonObjectI jsonObjectFrom(String s) throws JsonParserAdapterException;

    interface JsonObjectI {
        JsonArrayI getArray(String key);

        String getString(String key);

        int getInt(String key);
    }

    interface JsonArrayI {
        JsonArrayI getArray(int key);

        String getString(int key);

        JsonObjectI getObject(int key);

        int getInt(int key);
    }

    class JsonParserAdapterException extends Throwable {
        private final Exception jsonParserException;

        public JsonParserAdapterException(Exception e) {
            this.jsonParserException = e;
        }
    }
}
