package mx.nanocode.puntojaliscoabierto.model;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class RutaLab {

    private static RutaLab instance;
    private Context context;
    private List<Ruta> rutas;

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "rutas.json";

    private static final int RUTAS_COUNT=1;
    private RutaLab(Context appContext) {
        this.context = appContext;
        loadRutas();
    }

    private void loadRutas() {
        RutaIntentSerializer crimeSerializer = new RutaIntentSerializer(this.context, FILENAME);
        try {
            this.rutas = crimeSerializer.loadRutas();
            if(this.rutas.size()<RUTAS_COUNT)
            {
            	RefrescarRutas();
            }
        } catch (Exception e) {
            rutas = new ArrayList<Ruta>();
            RutasDisponibles();
        }
    }
    private void RefrescarRutas()
    {
    	Ruta r= new Ruta();
    	r.setId(1);
    	r.setBase("Trompo Magico");
    	r.setDuracion("02:30 hrs");
    	r.setInicial(true);
    	r.setRuta("RUTA 368");
    	r.setSalida1("05:50 am");
    	r.setSalidan("10:59 pm");
    	this.rutas.add(r);
    	saveRutas();
    }
    private void RutasDisponibles()
    {
    	//este metodo define las rutas que tenemos disponibles
    	Ruta r= new Ruta();
    	r.setId(1);
    	r.setBase("Trompo Magico");
    	r.setDuracion("02:30 hrs");
    	r.setInicial(true);
    	r.setRuta("RUTA 368");
    	r.setSalida1("05:50 am");
    	r.setSalidan("10:59 pm");
    	this.rutas.add(r);
    	saveRutas();
    }
    public void addRuta(Ruta ruta) {
        this.rutas.add(ruta);
    }

    public void deleteRuta(Ruta ruta) {
        this.rutas.remove(ruta);
    }

    public static RutaLab getInstance(Context appContext) {
        if (instance == null) {
            instance = new RutaLab(appContext.getApplicationContext());
        }

        return instance;
    }

    public List<Ruta> getRutas() {
        return rutas;
    }


    public Ruta getRuta(int id) {
        for(Ruta ruta : rutas) {
            if (ruta.getId()==id) {
                return ruta;
            }
        }
        return null;
    }
    public Ruta getRuta(String RutaText) {
        for(Ruta ruta : rutas) {
            if (ruta.toString()==RutaText) {
                return ruta;
            }
        }
        return null;
    }
    public Ruta getRuta() {
        for(Ruta ruta : rutas) {
            if (ruta.isInicial()) {
                return ruta;
            }
        }
        return rutas.get(0);
    }
    public void MakeInicial(int id)
    {
    	for(Ruta ruta : rutas) {
            if (ruta.getId()==id) {
                ruta.setInicial(true);
            }
            else
            {
            	ruta.setInicial(false);
            }
        }
    	saveRutas();
    }
    public boolean saveRutas() {
        RutaIntentSerializer rutaSerializer = new RutaIntentSerializer(this.context, FILENAME);
        try {
            Log.d(TAG, "Saving rutas...");
            rutaSerializer.saveRutas(this.rutas);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "There was an error saving the rutas", e);
            return false;
        }
    }
}
