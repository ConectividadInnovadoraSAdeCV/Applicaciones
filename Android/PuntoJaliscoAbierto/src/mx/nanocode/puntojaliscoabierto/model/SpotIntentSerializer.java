package mx.nanocode.puntojaliscoabierto.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SpotIntentSerializer {

    private Context context;
    private String fileName;

    public SpotIntentSerializer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void saveSpots(List<Spot> spot) throws IOException, JSONException {
        JSONArray jsonArray = convertSpotsToJSON(spot);
        writeToFile(jsonArray);
    }

    private void writeToFile(JSONArray jsonArray) throws IOException {
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private JSONArray convertSpotsToJSON(List<Spot> spot) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Spot crime : spot) {
            jsonArray.put(crime.toJSON());
        }
        return jsonArray;
    }

    public List<Spot> loadSpots() throws IOException, JSONException {
        List<Spot> spots = new ArrayList<Spot>();
        BufferedReader reader = null;

        try {
            InputStream inputStream = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            JSONArray jsonArray = readSpotsFromFile(reader);
            populateSpots(spots, jsonArray);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return spots;
    }

    private void populateSpots(List<Spot> spots, JSONArray jsonArray) throws JSONException {
        for (int i=0; i<jsonArray.length(); i++) {

            Spot crime = new Spot(jsonArray.getJSONObject(i));
            spots.add(crime);
        }
    }

    private JSONArray readSpotsFromFile(BufferedReader reader) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        String line = null;

        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return (JSONArray) new JSONTokener(builder.toString()).nextValue();
    }
}
