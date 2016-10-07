package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        double altitude = 0;
        
        //no exceptions for out of bounds atm
        double xCeil = Math.ceil(x);
        double xFloor = Math.floor(x);
        double zCeil = Math.ceil(z);
        double zFloor = Math.floor(z);
    	
        //checking whether both x and z are ints
        if (x % 1 == 0 && z % 1 == 0){
        	//take altitude from the array
        	System.out.println("test");
        	altitude = myAltitude[(int) x][(int) z];
        }
        else if(x % 1 == 0){     	
        	altitude = ((z-zFloor)/(zCeil-zFloor))*myAltitude[(int) x][(int) zCeil]
        				+ ((zCeil-z)/(zCeil-zFloor))*myAltitude[(int) x][(int) zFloor];
        }
        else if(z % 1 == 0){
        	altitude = ((x-xFloor)/(xCeil-xFloor))*myAltitude[(int) xCeil][(int) z]
    				+ ((xCeil-x)/(xCeil-xFloor))*myAltitude[(int) xFloor][(int) z];
        }
        else{
        	double dl = ((z-zFloor)/(zCeil-zFloor))*myAltitude[(int) xFloor][(int) zCeil]
    				+ ((zCeil-z)/(zCeil-zFloor))*myAltitude[(int) xFloor][(int) zFloor];
        	double dr = ((z-zFloor)/(zCeil-zFloor))*myAltitude[(int) xCeil][(int) zCeil]
    				+ ((zCeil-z)/(zCeil-zFloor))*myAltitude[(int) xCeil][(int) zFloor];
        	
        	altitude = ((x-xFloor)/(xCeil-xFloor))*dr
    				+ ((xCeil-x)/(xCeil-xFloor))*dl;
        	
        }
        
        
        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }
    
    public void drawTerrain(GL2 gl){
    	gl.glBegin(GL2.GL_TRIANGLES);
    	
    	for (int x = 0; x < myAltitude.length - 1; x++){
    		for(int z = 0; z < myAltitude[x].length - 1; z++){
    			gl.glVertex3d(x, myAltitude[x][z], z);
    			gl.glVertex3d(x, myAltitude[x][z + 1], z + 1);
    			gl.glVertex3d(x + 1, myAltitude[x + 1][z], z);
    			
    			gl.glVertex3d(x, myAltitude[x][z + 1], z + 1);
    			gl.glVertex3d(x + 1, myAltitude[x + 1][z + 1], z + 1);
    			gl.glVertex3d(x + 1, myAltitude[x + 1][z], z);
    		}
    	}
    	
    	gl.glEnd();
    }


}