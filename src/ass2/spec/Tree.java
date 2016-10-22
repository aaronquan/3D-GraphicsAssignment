package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }

	public void draw(GL2 gl) {
		String trunkTextureFileName = "bark.bmp";
		String leafTextureFileName = "leaves.bmp";
		String ext = "bmp";
		
		MyTexture trunkTexture = new MyTexture(gl, trunkTextureFileName, ext, true);
		MyTexture leafTexture = new MyTexture(gl, leafTextureFileName, ext, true);
		
		double height = 1;
		double trunkRadius = 0.1;
		double leavesRadius = 0.5;
		gl.glPushMatrix();
			gl.glTranslated(myPos[0], myPos[1], myPos[2]);
			Cylinder c = new Cylinder(height, trunkRadius);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, trunkTexture.getTextureId());
			c.draw(gl);
			gl.glPushMatrix();
				gl.glTranslated(0, height, 0);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, leafTexture.getTextureId());
				Sphere s = new Sphere(leavesRadius);
				s.draw(gl);
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
    

}
