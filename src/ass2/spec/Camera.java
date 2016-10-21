package ass2.spec;

import com.jogamp.opengl.glu.GLU;

public class Camera {
	private double [] position;
	private double angle; // in degrees
	private double [] lookAt;
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
	}
	public double [] getPosition(){
		return position;
	}
	public void addToPosition(double [] add){
		position[0] += add[0];
		position[1] += add[1];
		position[2] += add[2];
	}
	public void subToPosition(double [] add){
		position[0] -= add[0];
		position[1] -= add[1];
		position[2] -= add[2];
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
			if (angle > 270){
				//mX = -mX;
			}
			if (angle > 90 && angle < 180){
				//mX = -mX;
			}
			lookAt[0] = mX;
			lookAt[2] = mZ;
			lookAt = MathUtil.normalise(lookAt);
		}
	}
	
	
	public void turnRight(double i){
		angle -= i;
		angle = MathUtil.normaliseAngle(angle);
		changeLookAt();
		System.out.println(angle);
	}
	public void turnLeft(double i){
		angle += i;
		angle = MathUtil.normaliseAngle(angle);
		changeLookAt();
		System.out.println(angle);
	}
	public void moveForward(double i){
		addToPosition(lookAt);
	}
	public void moveBackward(double i){
		subToPosition(lookAt);
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
}
