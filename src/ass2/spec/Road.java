package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

	public void draw(GL2 gl, GLUT glut, double[][] x) {
		String roadTextureFileName = "road.bmp";
		String ext = "bmp";
		MyTexture roadTexture = new MyTexture(gl, roadTextureFileName, ext, true);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, roadTexture.getTextureId());
        

        	//gl.glLineWidth(2);
//gl.glBegin(GL2.GL_POINTS);
    	gl.glBegin(GL2.GL_POLYGON);
            gl.glColor4d(2f, 0.0, 0.0, 1);

        int numPoints = 32;
        double tIncrement = 1.0/numPoints;
       // System.out.println("numPoints " + numPoints + " " + tIncrement);
        for(int i = 0; i < (numPoints*size() - 1); i++){ 

        	double t = i*tIncrement;   

        	double height = x[(int) point(t)[0]][(int) point(t)[1]];
        	double[] p = {point(t)[0],height, point(t)[1]};
        	double[] p1 = {point(t+tIncrement)[0],height, point(t+tIncrement)[1]};
        	double dist = myWidth;//Math.sqrt((p[0] - p1[0]) *(p[0] - p1[0]) + (p[2] - p1[2]) * (p[2] - p1[2]));
        	//System.out.println("dist =" + dist);
        	double rotateX = ((p1[0]-p[0])*Math.cos(Math.toRadians(90)) + (p1[2]-p[2])*Math.sin(Math.toRadians(90)));
        	double rotateY = ((-Math.sin(Math.toRadians(90)) * (p1[0]-p[0]) + Math.cos(Math.toRadians(90)) * (p1[2] -p[2])));
        	double newX = p[0] + rotateX*dist;
        	double newY = p[2] + rotateY*dist;
        	double newX1 = p1[0] + rotateX*dist;
        	double newY1 = p1[2] + rotateY*dist;
        	//System.out.println("newX =" + newX + " newz " + newY );

        	//        	newX = newX*myWidth/Math.hypot(newX1 - newX, newY1 - newY);
//        	newY = newY*myWidth/Math.hypot(newX1 - newX, newY1 - newY);
//        	newX1 = newX1*myWidth/Math.hypot(newX1 - newX, newY1 - newY);
//        	newY1 = newY1*myWidth/Math.hypot(newX1 - newX, newY1 - newY);
//        	double[] p2 = {newX, height, newY};
//        	double[] p3 = {newX1, height, newY1};
        	
        	gl.glTexCoord2d(0,0);
        	gl.glVertex3d(point(t)[0], height + 0.02, point(t)[1]);
        	gl.glTexCoord2d(0,1);
        	gl.glVertex3d(point(t+tIncrement)[0], height + 0.02, point(t+tIncrement)[1]);
        	gl.glTexCoord2d(1,1);
        	gl.glVertex3d(newX1, height + 0.02, newY1);        	
        	gl.glTexCoord2d(1,0);
        	gl.glVertex3d(newX, height + 0.02, newY);


        	
        }
        //Connect to the final point - we just get the final control 
        //point 
        double[] ctrlPnt = controlPoint(size()*3);
       // gl.glVertex3d(ctrlPnt[0],x[(int) ctrlPnt[0]][(int) ctrlPnt[1]]+ 0.01,ctrlPnt[1]);
       

        gl.glEnd();
	    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

  
	    //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

        
    //Connect to the final point - we just get the final control 
    //point 

	}


}
