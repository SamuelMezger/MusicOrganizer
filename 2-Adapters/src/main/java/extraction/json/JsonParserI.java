package extraction.json;

import extraction.ExtractionException;


public interface JsonParserI {
    JsonObjectI jsonObjectFrom(String s) throws ExtractionException;

    interface JsonObjectI {
        JsonArrayI getArray(String key) throws ExtractionException;

        String getString(String key) throws ExtractionException;

        int getInt(String key) throws ExtractionException;
    }

    interface JsonArrayI {
        JsonArrayI getArray(int key) throws ExtractionException;

        String getString(int key) throws ExtractionException;

        JsonObjectI getObject(int key) throws ExtractionException;

        int getInt(int key) throws ExtractionException;

        int size();
    }
}
