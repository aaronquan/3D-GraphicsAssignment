package ass2.spec;
import com.jogamp.opengl.GL2;


public class Sphere {
	private double radius;
	private double stacks = 32;
	private double slices = 32;
	public Sphere(double r){
		radius = r;
	}
	public void draw(GL2 gl){
		double deltaT;
    
    	
    	deltaT = 0.5/stacks;
    	int ang;  
    	int delang = (int) (360/slices);
    	double x1,x2,z1,z2,y1,y2;
    	for (int i = 0; i < stacks; i++) 
    	{ 
    		double t = -0.25 + i*deltaT;
    		
    		gl.glBegin(GL2.GL_TRIANGLE_STRIP); 
    		for(int j = 0; j <= slices; j++)  
    		{  
    			ang = j*delang;
    			x1=radius * r(t)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			x2=radius * r(t+deltaT)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			y1 = radius * getY(t);

    			z1=radius * r(t)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			z2= radius * r(t+deltaT)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			y2 = radius * getY(t+deltaT);

    			double normal[] = {x1,y1,z1};


    			MathUtil.normalise(normal);    

    			gl.glNormal3dv(normal,0);  
    			double tCoord = 1.0/stacks * i; //Or * 2 to repeat label
    			double sCoord = 1.0/slices * j;
    			gl.glTexCoord2d(sCoord,tCoord);
    			gl.glVertex3d(x1,y1,z1);
    			normal[0] = x2;
    			normal[1] = y2;
    			normal[2] = z2;

    			
    			MathUtil.normalise(normal);    
    			gl.glNormal3dv(normal,0); 
    			tCoord = 1.0/stacks * (i+1); //Or * 2 to repeat label
    			gl.glTexCoord2d(sCoord,tCoord);
    			gl.glVertex3d(x2,y2,z2); 

    		}
    	}
    	gl.glEnd();  
	}
    	
	double r(double t){
    	double x  = Math.cos(2 * Math.PI * t);
        return x;
    }
    
    double getY(double t){
    	
    	double y  = Math.sin(2 * Math.PI * t);
        return y;
    }
}
