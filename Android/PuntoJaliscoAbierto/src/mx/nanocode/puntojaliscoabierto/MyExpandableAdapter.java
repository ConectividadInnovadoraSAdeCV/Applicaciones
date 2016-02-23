package mx.nanocode.puntojaliscoabierto;

import java.util.ArrayList;

import mx.nanocode.puntojaliscoabierto.model.Ruta;
import mx.nanocode.puntojaliscoabierto.model.RutaLab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Ruta> childtems;
	private LayoutInflater inflater;
	private ArrayList<String> parentItems;
	private Ruta child;

	public MyExpandableAdapter(ArrayList<String> parents, ArrayList<Ruta> childern) {
		this.parentItems = parents;
		this.childtems = childern;
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		child =childtems.get(groupPosition);

		TextView basetx = null;
		TextView salida1tx=null;
		TextView salidantx=null;
		TextView duraciontx=null;
		CheckBox defaultcb=null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group, null);
		}
		//aqui se hacer el trabajo de lenar el hijo

		basetx = (TextView) convertView.findViewById(R.id.group_base_text);
		basetx.setText(child.getBase());
		
		salida1tx = (TextView) convertView.findViewById(R.id.group_salida1_text);
		salida1tx.setText(child.getSalida1());

		salidantx = (TextView) convertView.findViewById(R.id.group_salidan_text);
		salidantx.setText(child.getSalidan());
		

		duraciontx = (TextView) convertView.findViewById(R.id.group_duracion_text);
		duraciontx.setText(child.getDuracion());
		
		defaultcb= (CheckBox)convertView.findViewById(R.id.group_default_check);
		defaultcb.setChecked(child.isInicial());
		
		//intento guardar la nueva ruta predeterminada
		defaultcb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					 RutaLab.getInstance(activity).MakeInicial(child.getId());
					 Toast.makeText(activity,child+" "+activity.getString(R.string.toast_ruta_seleccionada), Toast.LENGTH_SHORT).show();
					 activity.finish();
				}
			}
		});


//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				Toast.makeText(activity, child.getRuta(),
//						Toast.LENGTH_SHORT).show();
//			}
//		});

		return convertView;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row, null);
		}

		((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;//solo un objeto hijo por grupo
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}