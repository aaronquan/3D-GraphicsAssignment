package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Cylinder {
	
	
	private double height;
	private double radius;
	private int slices = 32;
	private double[] myPos;
	
	
	public Cylinder (double h, double r, double[] mP) {
		height = h;
		radius = r;
		myPos = mP;
	}
	
	public void draw(GL2 gl) {
		double y1 = 0;
		double y2 = -height;
		gl.glPolygonMode(GL.GL_BACK,GL2.GL_LINE);
		//Front circle
	
		gl.glBegin(GL2.GL_TRIANGLE_FAN);{
		
			 gl.glNormal3d(0,0,1);
			 gl.glVertex3d(0,0,y1);
			 double angleStep = 2*Math.PI/slices;
	         for (int i = 0; i <= slices ; i++){
	             double a0 = i * angleStep;
	             double x0 = radius * Math.cos(a0);
	             double z0 = radius * Math.sin(a0);
	
	            gl.glVertex3d(x0,y1,z0);
	          
	         }
	             
	             
		}gl.glEnd();
    	//Back circle
    	gl.glBegin(GL2.GL_TRIANGLE_FAN);{
       
   		 gl.glNormal3d(0,0,-1);
   		 gl.glVertex3d(0,0,y2);
   		 double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){
                
            	double a0 = 2*Math.PI - i * angleStep;
                            
                double x0 = radius * Math.cos(a0);
                double z0 = radius * Math.sin(a0);

                gl.glVertex3d(x0,y2,z0);
              //  System.out.println("Back " + x0 + " " + y0);
            }
                
                
    	}gl.glEnd();
    	
    	
    	
    	
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
                //Calculation for face normal for each quad
                //                     (x0,y0,z2)
                //                     ^
                //                     |  u = (0,0,z2-z1) 
                //                     |
                //                     | 
                //(x1,y1,z1)<--------(x0,y0,z1)
                //v = (x1-x0,y1-y0,0)  
                //                     
                //                     
                //                       
                //                    
                //
                // u x v gives us the un-normalised normal
                // u = (0,     0,   z2-z1)
                // v = (x1-x0,y1-y0,0) 
                
                
                //If we want it to be smooth like a cylinder
                //use different normals for each different x and y

                    gl.glNormal3d(x0, 0, z0);
//                }else{
//                    //Use the face normal for all 4 vertices in the quad.
//                	gl.glNormal3d(-(z2-z1)*(y1-y0),(x1-x0)*(z2-z1),0);
//                }
                
                             
                gl.glVertex3d(x0, y1, z0);
                gl.glVertex3d(x0, y2, z0);  
                
                //If we want it to be smooth like a cylinder
                //use different normals for each different x and y
                //if(cylinder)
                    gl.glNormal3d(x1, 0, z1);
                
                gl.glVertex3d(x1, y1, z1);
                gl.glVertex3d(x1, y2, z1);               
//               
//               
            }

        }
        gl.glEnd();
    	
    	
	}
	
}
