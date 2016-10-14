package ass2.spec;

public class Camera {
	private double [] position;
	private double angle; // in degrees
	public Camera(double posX, double posY, double posZ){
		position = new double[3];
		position[0] = posX;
		position[1] = posY;
		position[2] = posZ;
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
	public double getAngle(){
		return angle;
	}
	public void turnRight(double i){
		angle += i;
		angle = MathUtil.normaliseAngle(angle);
		System.out.println(angle);
	}
	public void turnLeft(double i){
		angle -= i;
		angle = MathUtil.normaliseAngle(angle);
		System.out.println(angle);
	}
	public double [] moveForward(double i){
		double mX;
		double mZ;
		if (angle <= 90 || angle >= 270){
			mZ = 1;
		}else{
			mZ = -1;
		}
		mX = Math.tan(Math.toRadians(angle))*mZ;
		double [] vector = {mX, 0, mZ};
		if (angle == 90){
			vector[0] = 1;
			vector[2] = 0;
		}if(angle == 270){
			vector[0] = -1;
			vector[2] = 0;
		}if(angle == 0){
			vector[0] = 0;
			vector[2] = 1;
		}if (angle == 180){
			vector[0] = 0;
			vector[2] = -1;
		}else{
			vector = MathUtil.normalise(vector);
		}
		addToPosition(vector);
		return vector;
	}
	public void moveBackward(double i){
		
	}
}
