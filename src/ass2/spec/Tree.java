package ass2.spec;

import com.jogamp.opengl.GL2;

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
		// TODO Auto-generated method stub
		Cylinder c = new Cylinder(2, 0.1, myPos);
		c.draw(gl);
		
	}
    

}
