package ass2.spec;

public class Camera {
	private double [] position;
	private double angle;
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
	public double getAngle(){
		return angle;
	}
	public void turnRight(double i){
		
	}
	public void turnLeft(double i){
		
	}
	public void moveForward(double i){
		
	}
	public void moveBackward(double i){
		
	}
}
