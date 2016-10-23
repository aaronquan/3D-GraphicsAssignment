package ass2.spec;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Enemy {
    private float[] vertices = { -10, 0, 0,
            10,  0, 0,
            10, 10, 0,
            -10, 10, 0};
    private float[] texCoords = new float[3];

	// RGBA colors
	private float vertex_colors[][] = {
			{ 0.0f, 0.0f, 0.0f, 1.0f },  // black
			{ 1.0f, 0.0f, 0.0f, 1.0f },  // red
			{ 1.0f, 1.0f, 0.0f, 1.0f },  // yellow
			{ 0.0f, 1.0f, 0.0f, 1.0f },  // green
			{ 0.0f, 0.0f, 1.0f, 1.0f },  // blue
			{ 1.0f, 0.0f, 1.0f, 1.0f },  // magenta
			{ 1.0f, 1.0f, 1.0f, 1.0f },  // white
			{ 0.0f, 1.0f, 1.0f, 1.0f }   // cyan
	};
		 
    private String textureFileName1 = "rock.bmp";
    private String textureExt1 = "bmp";
    private FloatBuffer vertexBuffer; 
    private FloatBuffer colorBuffer; 
    private int shaderprogram;
	private static final String VERTEX_SHADER = "VertexTex.glsl";
    private static final String FRAGMENT_SHADER = "FragmentTex.glsl";
    private int texUnitLoc;
    private MyTexture myTextures;
	private int imageSize = 64;
	private int currIndex = 0;
    private int[] bufferIds = new int[1];
    private static final int rgba = 4;
    private static final int SLICES = 32;
    private double myPos[];
    private ByteBuffer chessImageBuf = Buffers.newDirectByteBuffer(imageSize*imageSize* rgba);
	



	public Enemy(double x, double y, double z) {
		texCoords[0] = (float) x;
		texCoords[1] = (float) y;
		texCoords[2] = (float) z;
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
		// TODO Auto-generated constructor stub
	}
	
	private void init(GL2 gl) {
    	gl.glGenBuffers(1, bufferIds, 0);
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferIds[0]);
    	
    	gl.glBufferData(GL.GL_ARRAY_BUFFER, (vertices.length + texCoords.length)*4, null, GL2.GL_STATIC_DRAW);
    	FloatBuffer verticesBuffer = Buffers.newDirectFloatBuffer(vertices);
    	FloatBuffer texBuffer = Buffers.newDirectFloatBuffer(texCoords);
    	gl.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, vertices.length*4, verticesBuffer);
    	gl.glBufferSubData(GL.GL_ARRAY_BUFFER, vertices.length*4, texCoords.length*4, texBuffer);
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

	}

	private void createRock(GL2 gl)
	{
		Sphere s = new Sphere(5);
		s.draw(gl);
	}
    
	public void draw(GL2 gl) {
//		init(gl);
//		myTextures = new MyTexture(gl, textureFileName1, textureExt1, true);
//        double angleIncrement = (Math.PI * 2.0) / SLICES;
//        double zFront = -1;
//        double zBack = -3;
//        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures.getTextureId());
//
//        gl.glBegin(GL2.GL_POLYGON);{
//        	
//       
//        for(int i = 0; i < SLICES; i++)
//        {
//            double angle0 = i*angleIncrement;
//        
//            gl.glNormal3d(0.0, 0.0, 1);
//            gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
//            gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zFront);
//        }
//        }gl.glEnd();
//      
//        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures.getTextureId());
//        gl.glBegin(GL2.GL_QUAD_STRIP);{      
//	        for(int i=0; i<= SLICES; i++){
//	        	double angle0 = i*angleIncrement;
//	        	double angle1 = (i+1)*angleIncrement;
//	        	double xPos0 = Math.cos(angle0);
//	        	double yPos0 = Math.sin(angle0);
//	        	double sCoord = 2.0/SLICES * i; //Or * 2 to repeat label
//	        	
//
//	        	gl.glNormal3d(xPos0, yPos0, 0);
//	        	gl.glTexCoord2d(sCoord,1);
//	        	gl.glVertex3d(xPos0,yPos0,zFront);
//	        	gl.glTexCoord2d(sCoord,0);
//	        	gl.glVertex3d(xPos0,yPos0,zBack);	        	
//	        	
//	        }
//        }gl.glEnd();    
//        
//        //Draw the bottom of the cylinder also with the canTop.bmp texture :)
//        //just for demonstration.
//        gl.glBegin(GL2.GL_POLYGON);{
//           
//            for(int i = 0; i < SLICES; i++)
//            {
//            	double angle0 = -i*angleIncrement;
//            	
//         	    gl.glNormal3d(0.0, 0.0, -1);
//         	 
//         	    gl.glTexCoord2d(0.5+0.5*Math.cos(angle0),0.5+0.5*Math.sin(angle0));
//         	    gl.glVertex3d(Math.cos(angle0), Math.sin(angle0),zBack);
//            }
//            }gl.glEnd();
//	}
//	   
		MyTexture trunkTexture = new MyTexture(gl, textureFileName1, textureExt1, true);
		
		double height = 0.5;
		double trunkRadius = 0.1;
		gl.glPushMatrix();
			gl.glTranslated(myPos[0], myPos[1], myPos[2]);
			Cylinder c = new Cylinder(height, trunkRadius);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, trunkTexture.getTextureId());
			c.draw(gl);
		gl.glPopMatrix();
	}
	   
}
    
   
