package jp.nyatla.nyartoolkit.and;

import javax.microedition.khronos.opengles.GL10;



import org.takanolab.kGLModel.KGLModelData;

//import jp.androidgroup.nyartoolkit.R;
import jp.androidgroup.nyartoolkit.markersystem.NyARAndMarkerSystem;
import jp.androidgroup.nyartoolkit.markersystem.NyARAndSensor;
import jp.androidgroup.nyartoolkit.sketch.AndSketch;
import jp.androidgroup.nyartoolkit.utils.camera.CameraPreview;
import jp.androidgroup.nyartoolkit.utils.gl.*;
import jp.nyatla.nyartoolkit.core.types.NyARDoublePoint3d;
import jp.nyatla.nyartoolkit.markersystem.NyARMarkerSystemConfig;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
* Hiro���������������������������������������������������������������
* ������������������������������������������������������������������
*/
public class SimpleLiteActivity extends AndSketch implements AndGLView.IGLFunctionEvent
{
	CameraPreview _camera_preview;
	AndGLView _glv;
	Camera.Size _cap_size;
	Integer mon_choix;
	Integer checking = 0;
	TextView mon_texte;
	FrameLayout fr;
	private static int TIME_OUT = 3000;
	
	/**
	 * onStart���������View���������������������������������������������
	 */
	@Override
	public void onStart()
	{

		super.onStart();
		fr=((FrameLayout)this.findViewById(R.id.sketchLayout));
		this._camera_preview=new CameraPreview(this);
		this._cap_size=this._camera_preview.getRecommendPreviewSize(320,320);
		int h = this.getWindowManager().getDefaultDisplay().getHeight();
		int screen_w,screen_h;
		screen_w=(this._cap_size.width*h/this._cap_size.height);
		screen_h=h;

		//camera

			mon_texte = new TextView(this);
		    mon_texte.setText("Look at the markers for the cube");
			fr.addView(mon_texte);
			fr.addView(this._camera_preview, 0, new LayoutParams(screen_w, screen_h));

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				fr.removeView(mon_texte);
			}
		}, TIME_OUT);


			//GLview
			this._glv = new AndGLView(this);
			fr.addView(this._glv, 0, new LayoutParams(screen_w, screen_h));

		fr.setFocusable(true);
		fr.setFocusableInTouchMode(true);
		fr.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("checking", checking);
				//data.setData(Uri.EMPTY);
				setResult(Activity.RESULT_OK, data);
				finish();
			}
		});
	}

	NyARAndSensor _ss;
	NyARAndMarkerSystem _ms;
	//private int _mid;
	private int[] _mid = new int[2];
	AndGLTextLabel text;
	AndGLTri tri;
	AndGLBox box;
	AndGLSphere sp;
	AndGLFpsLabel fps;
	AndGLBitmapSprite _bmp;
	private KGLModelData[] model_data = new KGLModelData[1];

	public void setupGL(GL10 gl)
	{
		Intent myIntent = getIntent(); // gets the previously created intent
		mon_choix = myIntent.getIntExtra("mon_choix",0);
		try
		{
			AssetManager assetMng = getResources().getAssets();
			//create sensor controller.
			this._ss=new NyARAndSensor(this._camera_preview,this._cap_size.width,this._cap_size.height,30);
			//create marker system
			this._ms=new NyARAndMarkerSystem(new NyARMarkerSystemConfig(this._cap_size.width,this._cap_size.height));
			if (mon_choix == 1) {
				this._mid[0] = this._ms.addARMarker(assetMng.open("AR/data/hiro.pat"), 16, 25, 80);
			}
			else if (mon_choix == 2) {
				this._mid[0] = this._ms.addARMarker(assetMng.open("AR/data/kanji.pat"), 16, 25, 80);
			}
			this._ss.start();
			//setup openGL Camera Frustum
			gl.glMatrixMode(GL10.GL_PROJECTION);
			this._bmp=new AndGLBitmapSprite(this._glv,64,64);
			gl.glLoadMatrixf(this._ms.getGlProjectionMatrix(),0);
			this.text=new AndGLTextLabel(this._glv);
			this.tri=new AndGLTri(this._glv,40);
			this.box=new AndGLBox(this._glv,80);
			this.sp = new AndGLSphere(this._glv);
			this._debug=new AndGLDebugDump(this._glv);
			this.fps=new AndGLFpsLabel(this._glv,"MarkerPlaneActivity");
			this.fps.prefix=this._cap_size.width+"x"+this._cap_size.height+":";
			//model_data[0].reloadTexture(gl);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.finish();
		}
	}
	AndGLDebugDump _debug=null;
	
	public void drawGL(GL10 gl)
	{
		try{
			
			gl.glClearColor(0,0,0,0);
	        gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
	        if(ex!=null){
	        	_debug.draw(ex);
	        	return;
	        }
	        //fps.draw(0, 0);
	        synchronized(this._ss){
				this._ms.update(this._ss);

				if(this._ms.isExistMarker(_mid[0])){
						//this.text.draw("found" + this._ms.getConfidence(_mid[0]), 0, 16);
						gl.glMatrixMode(GL10.GL_MODELVIEW);
						gl.glLoadMatrixf(this._ms.getGlMarkerMatrix(_mid[0]),0);
						
						this.box.draw(0, 0, 20);
						checking = 1;
					gl.glTranslatef(0,0,20);
				}
				/*else if(this._ms.isExistMarker(_mid[1])){
					this.text.draw("found"+this._ms.getConfidence(_mid[1]),0,16);
					gl.glMatrixMode(GL10.GL_MODELVIEW);
					gl.glLoadMatrixf(this._ms.getGlMarkerMatrix(_mid[1]),0);
					
					
			
					this.tri.draw(0,0,20);
					
					gl.glTranslatef(0,0,20);
				}*/
				
			}
			if (checking ==1) {
				this.text.draw("Click to continue", 150, 16);
			}

		}catch(Exception e)
		{
			ex=e;
		}
	}


	Exception ex=null;

}
