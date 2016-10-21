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

	public void draw(GL2 gl, GLUT glut) {
		double height = 0.5;
		double trunkRadius = 0.04;
		double leavesRadius = 0.2;
		gl.glPushMatrix();
			gl.glTranslated(myPos[0], myPos[1], myPos[2]);
			Cylinder c = new Cylinder(height, trunkRadius);
			c.draw(gl);
			gl.glPushMatrix();
				gl.glTranslated(0, height, 0);
				glut.glutSolidSphere(leavesRadius, 40, 40);
			gl.glPopMatrix();
		gl.glPopMatrix();
	}
    

}
