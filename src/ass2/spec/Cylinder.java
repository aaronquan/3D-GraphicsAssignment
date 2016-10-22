package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Cylinder {
	
	
	private double height;
	private double radius;
	private int slices = 32;
	
	public Cylinder (double h, double r) {
		height = h;
		radius = r;
	}
	
	public void draw(GL2 gl) {
		double y1 = 0;
		double y2 = height;
		gl.glPolygonMode(GL.GL_BACK,GL2.GL_LINE);
		
		//bottom circle
		gl.glBegin(GL2.GL_TRIANGLE_FAN);{
		
			 gl.glNormal3d(0,1,0);
			 gl.glVertex3d(0,y1,0);
			 double angleStep = 2*Math.PI/slices;
	         for (int i = 0; i <= slices ; i++){
	             double a0 = i * angleStep;
	             double x0 = radius * Math.cos(a0);
	             double z0 = radius * Math.sin(a0);
	
	            gl.glVertex3d(x0,y1,z0);
	          
	         }             
		}gl.glEnd();
		
    	//top circle
    	gl.glBegin(GL2.GL_TRIANGLE_FAN);{
       
   		 gl.glNormal3d(0,-1,0);
   		 gl.glVertex3d(0,y2,0);
   		 double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){
                
            	double a0 = 2*Math.PI - i * angleStep;
                            
                double x0 = radius * Math.cos(a0);
                double z0 = radius * Math.sin(a0);

                gl.glVertex3d(x0,y2,z0);
            }             
    	}gl.glEnd();
    	
    	//cylinder sides    	
       	gl.glBegin(GL2.GL_QUADS);
        {
            double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){
                double a0 = i * angleStep;
                double a1 = ((i+1) % slices) * angleStep;
                
                //Calculate vertices for the quad
                double x0 = radius * Math.cos(a0);
                double z0 = radius * Math.sin(a0);

                double x1 = radius * Math.cos(a1);
                double z1 = radius * Math.sin(a1);
                
                gl.glNormal3d(x0, 0, z0);
                gl.glTexCoord2d((double)i/slices, 0);
                gl.glVertex3d(x0, y1, z0);
                gl.glTexCoord2d((double)i/slices, 1);
                gl.glVertex3d(x0, y2, z0);  
                gl.glNormal3d(x1, 0, z1);
                gl.glTexCoord2d(((double)(i+1)%slices)/slices, 1);
                gl.glVertex3d(x1, y2, z1);
                gl.glTexCoord2d(((double)(i+1)%slices)/slices, 0);
                gl.glVertex3d(x1, y1, z1);     
                System.out.println(slices);
            }

        }
        gl.glEnd();
    	
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
	}
	
}
