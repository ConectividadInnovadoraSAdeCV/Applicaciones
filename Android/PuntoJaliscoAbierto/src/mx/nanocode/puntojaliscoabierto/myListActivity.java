package mx.nanocode.puntojaliscoabierto;

import java.util.ArrayList;

import mx.nanocode.puntojaliscoabierto.model.Ruta;
import mx.nanocode.puntojaliscoabierto.model.RutaLab;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ExpandableListView;

public class myListActivity extends ExpandableListActivity{

	private ArrayList<String> parentItems = new ArrayList<String>();
	private ArrayList<Ruta> childItems = new ArrayList<Ruta>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		// this is not really  necessary as ExpandableListActivity contains an ExpandableList
		//setContentView(R.layout.rutas_list_latout);

        //ExpandableListView expandableList=(ExpandableListView) findViewById(R.id.list);
		ExpandableListView expandableList = getExpandableListView(); // you can use (ExpandableListView) findViewById(R.id.list)

		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		setGroupParents();

		MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);

		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableList.setAdapter(adapter);
		expandableList.setOnChildClickListener(this);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.barratitulo);

	}
	
	public void setGroupParents() {
		childItems=(ArrayList<Ruta>) RutaLab.getInstance(getApplicationContext()).getRutas();
		for(Ruta ruta : childItems) {
            	parentItems.add(ruta.toString());
        }
	}


}