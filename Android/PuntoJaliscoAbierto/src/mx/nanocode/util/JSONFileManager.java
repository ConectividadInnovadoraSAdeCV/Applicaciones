package mx.nanocode.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class JSONFileManager {
    private Context context;
    private String fileName;
    
    public JSONFileManager(Context context,String filename)
    {
    	this.context = context;
        this.fileName = filename;
    }
    public void Guardar(JSONObject datos)
    {
    	Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(datos.toString());
        }catch(Exception e) 
        {
        	Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Exception al ecribir el archivo: "+e.getMessage());
        }
        finally {
                try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Exception acerrar el archivo abierto: "+e.getMessage());
				}
        }
    }
    public JSONObject GetDatos()
    {
    	StringBuilder builder = new StringBuilder();
        String line = null;
        BufferedReader reader = null;
        JSONObject datos=null;
        try
        {
        	InputStream inputStream = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));   
            //leo el archivo
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }
             
            datos=new JSONObject(new JSONTokener(builder.toString()));
        }
        catch(Exception e)
        {
        	Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Exception al leer el archivo: "+e.getMessage());        	
        }
        finally
        {
        	if (reader != null) {
                try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d(mx.nanocode.puntojaliscoabierto.MainActivity.TAG,"Exception al cerrar el archivo de lectura: "+e.getMessage());
				}
            }
        }

        return datos;
    }
}
