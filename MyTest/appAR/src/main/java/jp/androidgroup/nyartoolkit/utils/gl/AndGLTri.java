package jp.androidgroup.nyartoolkit.utils.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class AndGLTri  implements AndGLView.IGLViewEventListener
{
	private GL10 _ref_gl;
	
	private FloatBuffer _vertex;
	private FloatBuffer _color;
	private ByteBuffer _index;
	public AndGLTri(AndGLView i_context,float i_size)
	{
		i_context._evl.add(this);
		float s=i_size/2;

		float[] square = {
		2*s, -2*s, 0, 
		-2*s, -2*s, 0,  
		0,  2*s, 0,  
		0,  0, 2*s};
		
		this._vertex=AndGLHelper.makeFloatBuffer(square);

		
		float[] colors = {
				 1.0f, 0.0f, 0.0f, 1.0f,  1.0f, 1.0f, 0.0f, 1.0f,  0.0f, 1.0f, 0.0f, 1.0f, 
			       0.0f, 0.0f, 1.0f, 1.0f,  1.0f, 0.0f, 1.0f, 1.0f,  1.0f, 1.0f, 1.0f, 1.0f, 
			};
		this._color=AndGLHelper.makeFloatBuffer(colors);
		
		byte[] indices = {
				1, 3, 0,  2, 0, 3,  4, 0, 2,  4, 2, 3,  4, 3, 1,  4, 1, 0};

		this._index=AndGLHelper.makeByteBuffer(indices);
	}

	public void draw(float i_x,float i_y,float i_z)
	{
		GL10 gl=this._ref_gl;
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_NORMALIZE);  
		gl.glDisable(GL10.GL_LIGHTING); 			
		//Log.w("TRACE","9");
		gl.glColorPointer( 4, GL10.GL_FLOAT, 0,this._color);
		gl.glVertexPointer( 3, GL10.GL_FLOAT, 0,this._vertex);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glTranslatef(i_x,i_y,i_z);
		//gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE,this._index);
		gl.glDrawElements(GL10.GL_TRIANGLES, 18, 
			       GL10.GL_UNSIGNED_BYTE, this._index); 
		gl.glPopMatrix();		

	}
	@Override
	public void onGlChanged(GL10 i_gl, int i_width, int i_height)
	{
		if(this._ref_gl!=null){
		}
		this._ref_gl=i_gl;
	}
	@Override
	public void onGlMayBeStop()
	{
		this._ref_gl=null;
	}

public void drawSp() { 
	 Log.w("Trace","In drawSp!");
	 GL10 gl=this._ref_gl;
	 float    theta, pai;  
	 float    co, si;  
	 float    r1, r2;  
	 float    h1, h2;  
	 float    step = 2.0f;  
	 float[][] v = new float[32][3];  
	 ByteBuffer vbb;  
	 FloatBuffer vBuf;  
	 Log.w("TRACE","1");
	 vbb = ByteBuffer.allocateDirect(v.length * v[0].length * 4);  
	 vbb.order(ByteOrder.nativeOrder());  
	 vBuf = vbb.asFloatBuffer();  
	   
	 gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
	 gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);  
	   
	 for (pai = -90.0f; pai < 90.0f; pai += step) {  
	 int    n = 0;  
	// Log.w("TRACE","2");
	 r1 = (float)Math.cos(pai * Math.PI / 180.0);  
	 r2 = (float)Math.cos((pai + step) * Math.PI / 180.0);  
	 h1 = (float)Math.sin(pai * Math.PI / 180.0);  
	 h2 = (float)Math.sin((pai + step) * Math.PI / 180.0);  
	   
	 for (theta = 0.0f; theta <= 360.0f; theta += step) {  
	 co = (float)Math.cos(theta * Math.PI / 180.0);  
	 si = -(float)Math.sin(theta * Math.PI / 180.0);  
	
	 v[n][0] = (r2 * co);  
	 v[n][1] = (h2);  
	 v[n][2] = (r2 * si);  
	 v[n + 1][0] = (r1 * co);  
	 v[n + 1][1] = (h1);  
	 v[n + 1][2] = (r1 * si);  
	   
	 vBuf.put(v[n]);  
	 vBuf.put(v[n + 1]);  
	   
	 n += 2;  
	   
	 if(n>31){  
	 vBuf.position(0);  
	   
	 gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);  
	 gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);  
	 gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);  
	 gl.glColorPointer( 4, GL10.GL_FLOAT, 0,this._color);
	 n = 0;  
	 theta -= step;  
	 }  
	 
	 }  
	 vBuf.position(0);  
	 
	 gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);  
	 gl.glNormalPointer(GL10.GL_FLOAT, 0, vBuf);  
	 gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);  
	 }  
	   
	 gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);  
	 gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	 }  
} 