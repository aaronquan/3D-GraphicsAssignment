package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class Camera {
	private boolean firstPerson;
	private double [] position;
	private double angle; // in degrees
	private double [] lookAt;
	private Avatar avatar;
	
	private double thirdPersonDistance = 2;
	public Camera(double posX, double posY, double posZ){
		position = new double[3];
		lookAt = new double[3];
		position[0] = posX;
		position[1] = posY;
		position[2] = posZ;
		lookAt[0] = 0;
		lookAt[1] = 0;
		lookAt[2] = 1;
		
		angle = 0;
		
		firstPerson = true;
		avatar = new Avatar(position);
	}
	public void toggleView(){
		if (firstPerson){
			//change to third person
			subToPosition(lookAt, thirdPersonDistance);
		}else{
			//change to first person
			addToPosition(lookAt, thirdPersonDistance);
		}
		firstPerson = !firstPerson;
	}
	public double [] getPosition(){
		return position;
	}
	public void addToPosition(double [] add, double multiplier){
		position[0] += add[0]*multiplier;
		position[1] += add[1]*multiplier;
		position[2] += add[2]*multiplier;
	}
	public void subToPosition(double [] add, double multiplier){
		position[0] -= add[0]*multiplier;
		position[1] -= add[1]*multiplier;
		position[2] -= add[2]*multiplier;
	}
	public double getAngle(){
		return angle;
	}
	public double [] getLookAt(){
		return lookAt;
	}
	private void changeLookAt(){
		double mX, mZ;
		if (angle <= 90 || angle >= 270){
			mZ = 1;
		}else{
			mZ = -1;
		}
		mX = Math.tan(Math.toRadians(angle))*mZ;
		if(angle == 90){
			lookAt[0] = 1;
			lookAt[2] = 0;
		}if (angle == 270){
			lookAt[0] = -1;
			lookAt[2] = 0;
		}else{
			lookAt[0] = mX;
			lookAt[2] = mZ;
			lookAt = MathUtil.normalise(lookAt);
		}
	}
	
	
	public void turnRight(double i){
		angle -= i;
		angle = MathUtil.normaliseAngle(angle);
		if (!firstPerson){
			addToPosition(lookAt, thirdPersonDistance);
		}
		changeLookAt();
		if (!firstPerson){
			subToPosition(lookAt, thirdPersonDistance);
		}
	}
	public void turnLeft(double i){
		angle += i;
		angle = MathUtil.normaliseAngle(angle);
		if (!firstPerson){
			addToPosition(lookAt, thirdPersonDistance);
		}
		changeLookAt();
		if (!firstPerson){
			subToPosition(lookAt, thirdPersonDistance);
		}
	}
	public void moveForward(double i, Terrain t){
		addToPosition(lookAt, i);
		//if (position[1] < t.altitude(position[0], position[2]) + 1.5)
		position[1] = t.altitude(position[0], position[2]) + 1;
		avatar.moveForward(lookAt, i);
		if (!firstPerson){
			position[1] = t.altitude(position[0]+lookAt[0]*thirdPersonDistance, position[2]+lookAt[2]*thirdPersonDistance)+1;
			avatar.setAltitude(t.altitude(position[0]+lookAt[0]*thirdPersonDistance, position[2]+lookAt[2]*thirdPersonDistance));
		}
		else{
			avatar.setAltitude(t.altitude(position[0], position[2]));
		}
	}
	public void moveBackward(double i, Terrain t){
		subToPosition(lookAt, i);
		//if (position[1] < t.altitude(position[0], position[2]) + 1.5)
		position[1] = t.altitude(position[0], position[2]) + 1;
		avatar.moveBackward(lookAt, i);
		if (!firstPerson){
			position[1] = t.altitude(position[0]+lookAt[0]*thirdPersonDistance, position[2]+lookAt[2]*thirdPersonDistance)+1;
			avatar.setAltitude(t.altitude(position[0]+lookAt[0]*thirdPersonDistance, position[2]+lookAt[2]*thirdPersonDistance));
		}
		else{
			avatar.setAltitude(t.altitude(position[0], position[2]));
		}
	}
	public void moveUp(double i){
		position[1] += i;
	}
	public void moveDown(double i){
		position[1] -= i;
	}
	public void updateCamera(GLU glu){
		glu.gluLookAt(
				position[0], position[1], position[2], 
				position[0]+lookAt[0], position[1]+lookAt[1], position[2]+lookAt[2], 
				0, 1, 0
				);
	}
	public void drawAvatar(GL2 gl, GLUT glut){
		if (!firstPerson)
		avatar.draw(gl, glut, angle);
	}
}
