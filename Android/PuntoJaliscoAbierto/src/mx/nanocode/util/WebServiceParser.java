package mx.nanocode.util;

import java.util.ArrayList;
import java.util.List;

public class WebServiceParser {
	public String GetResultado(String Valor)
	{
		if(!Valor.endsWith("</string>"))
			return "";
		
		String res=Valor.substring(Valor.indexOf("Serialization/")+16, Valor.indexOf("</string>"));
		return res;
	}
	public List<String> GetListResult(String Valor)
	{
		String Aux=GetResultado(Valor);
		if(Aux=="")
			return null;
		String Arr[]=Aux.split("[\\x3B]");
		if(Arr.length<1)
			return null;
		List<String> ls=new ArrayList<String>();
		for(int i=0;i<Arr.length;i++)
		{
			ls.add(Arr[i]);
		}
		return ls;
	}
}
