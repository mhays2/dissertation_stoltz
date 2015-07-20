package jp.androidgroup.nyartoolkit.utils.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


/**
 * ������������������������������������
 *
 */
public class AndGLSphere  implements AndGLView.IGLViewEventListener
{
	private GL10 _ref_gl;
	
	private FloatBuffer _vertex;
	private FloatBuffer _color;
	private ByteBuffer _index;
	protected float vertices[];
    
	// Our vertex buffer.
	protected FloatBuffer vertexBuffer;

	public AndGLSphere(AndGLView i_context) {
		i_context._evl.add(this);
		float a=(float)(1.0f/(2.0f-2f*Math.cos(72f*Math.PI/180.f)));
		float bx=(float)(a*Math.cos(18*Math.PI/180.0f));
		float by=(float)(a*Math.sin(18*Math.PI/180f));
		float cy=(float)(-a * Math.cos(18*Math.PI/180f));
		vertices=new float[]{
				0,a,0.5f,cy,-bx,by,bx,by,-0.5f,cy
		};
		
		ByteBuffer vbb 
		  = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		
	}

	/**
	 * This function draws our square on screen.
	 * @param gl
	 */
	public void draw(float i_x,float i_y,float i_z) {
		GL10 gl=this._ref_gl;
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW); 
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE); 
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK); 

		// Enabled the vertices buffer for writing 
		//and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of 
		//an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, 
                                 vertexBuffer);

		gl.glTranslatef(i_x,i_y,i_z);
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0,5);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); 
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE); 
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



}