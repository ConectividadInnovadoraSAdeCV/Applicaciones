package mx.nanocode.puntojaliscoabierto.model;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RutaIntentSerializer {

    private Context context;
    private String fileName;

    public RutaIntentSerializer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void saveRutas(List<Ruta> rutas) throws IOException, JSONException {
        JSONArray jsonArray = convertRutasToJSON(rutas);
        writeCrimesToFile(jsonArray);
    }

    private void writeCrimesToFile(JSONArray jsonArray) throws IOException {
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

    private JSONArray convertRutasToJSON(List<Ruta> rutas) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Ruta crime : rutas) {
            jsonArray.put(crime.toJSON());
        }
        return jsonArray;
    }

    public List<Ruta> loadRutas() throws IOException, JSONException {
        List<Ruta> rutas = new ArrayList<Ruta>();
        BufferedReader reader = null;

        try {
            InputStream inputStream = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            JSONArray jsonArray = readRutasFromFile(reader);

            populateRutas(rutas, jsonArray);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return rutas;
    }

    private void populateRutas(List<Ruta> rutas, JSONArray jsonArray) throws JSONException {
        for (int i=0; i<jsonArray.length(); i++) {
            Ruta crime = new Ruta(jsonArray.getJSONObject(i));
            rutas.add(crime);
        }
    }

    private JSONArray readRutasFromFile(BufferedReader reader) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        String line = null;

        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return (JSONArray) new JSONTokener(builder.toString()).nextValue();
    }
}
