package mx.nanocode.util;

import java.util.List;

import mx.nanocode.puntojaliscoabierto.R;

import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;

public class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem> {
	 
	Context myContext;
    public MyItemizedIconOverlay(
            List<OverlayItem> pList,
            org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
            ResourceProxy pResourceProxy,Context myContext) {
 
        super(pList, pOnItemGestureListener, pResourceProxy);
        // TODO Auto-generated constructor stub
        this.myContext=myContext;
    }
 
    @Override
    public void draw(Canvas canvas, MapView mapview, boolean arg2) {
        // TODO Auto-generated method stub
        //super.draw(canvas, mapview, arg2);
 
        if (this.size() > 0) {
 
            for (int i = 0; i < this.size(); i++) {
                GeoPoint in = getItem(i).getPoint();
                Point out = new Point();
                mapview.getProjection().toPixels(in, out);
                Bitmap bm;
                int full=Integer.parseInt(getItem(i).getTitle());
                //esta linea cambia el color del camion de acuerdo a cuan lleno va
                if(full==-1)
                	bm=BitmapFactory.decodeResource(myContext.getResources(), R.drawable.aqui);
                else if(full <5)
                	if(getItem(i).getSnippet().startsWith("T"))
                		bm=BitmapFactory.decodeResource(myContext.getResources(), R.drawable.camionverderampa);
                	else bm=BitmapFactory.decodeResource(myContext.getResources(), R.drawable.camionverde);
                else if (full <10)
                	if(getItem(i).getSnippet().startsWith("T"))
                		bm=BitmapFactory.decodeResource(myContext.getResources(), R.drawable.camionnarajarampa);
                	else bm=BitmapFactory.decodeResource(myContext.getResources(), R.drawable.camionnaranja);
                else 
                	if(getItem(i).getSnippet().startsWith("T"))
                		bm=BitmapFactory.decodeResource(myContext.getResources(), R.drawable.camionrojorampa);
                	else bm=BitmapFactory.decodeResource(myContext.getResources(), R.drawable.camionrojo);
                canvas.drawBitmap(bm, out.x - bm.getWidth() / 2, out.y - bm.getHeight() / 2, null);
            }
        }
    }
 
    @Override
    public boolean onSingleTapUp(MotionEvent event, MapView mapView) {
        // TODO Auto-generated method stub
        // return super.onSingleTapUp(event, mapView);
        return true;
    }
}

