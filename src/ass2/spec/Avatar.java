package ass2.spec;

import com.jogamp.opengl.GL2;

public class Avatar {
	private double [] myPos;
	
	public Avatar(double [] pos){
		myPos = new double[3];
		myPos[0] = pos[0];
		myPos[1] = pos[1];
		myPos[2] = pos[2];
	}
	public void moveForward(double [] dir, double m){
		myPos[0] += dir[0]*m;
		myPos[1] += dir[1]*m;
		myPos[2] += dir[2]*m;
	}
	public void moveBackward(double [] dir, double m){
		myPos[0] -= dir[0]*m;
		myPos[1] -= dir[1]*m;
		myPos[2] -= dir[2]*m;
	}
	public void setAltitude(double i){
		myPos[1] = i;
	}
	public void draw(GL2 gl){
		double rad = 0.4;
		gl.glPushMatrix();
			gl.glTranslated(myPos[0], myPos[1]+rad/2, myPos[2]);
			Sphere s = new Sphere(rad);
			s.draw(gl);
		gl.glPopMatrix();
	}
}
