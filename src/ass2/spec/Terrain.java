package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;



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
    	double zFromFloor = z-Math.floor(z);
    	double xFromFloor = x-Math.floor(x);
    	
        //checking whether both x and z are ints
        if (x % 1 == 0 && z % 1 == 0){
        	//take altitude from the array
        	altitude = myAltitude[(int) x][(int) z];
        }
        else if(x % 1 == 0){
        	
        	double d1 = myAltitude[(int) x][(int) Math.floor(z)];
        	double d2 = myAltitude[(int) x][(int) Math.ceil(z)];
        	altitude = d1 + (d1 - d2)*zFromFloor;
        }
        else if(z % 1 == 0){
        	double d1 = myAltitude[(int) Math.floor(x)][(int) z];
        	double d2 = myAltitude[(int) Math.ceil(x)][(int) z];
        	altitude = d1 + (d1 - d2)*xFromFloor;
        }
        else{
        	//use interpolation
        	//get x depth
        	double p1 = myAltitude[(int) Math.floor(x)][(int) Math.floor(z)];
        	double p2 = myAltitude[(int) Math.floor(x)][(int) Math.ceil(z)];
        	double p3 = myAltitude[(int) Math.ceil(x)][(int) Math.floor(z)];
        	double p4 = myAltitude[(int) Math.ceil(x)][(int) Math.ceil(z)];
        	
        	double d1 = p1 + (p1-p2)*zFromFloor;
        	double d2 = p3 + (p3-p4)*zFromFloor;
        	
        	altitude = d1 + (d1 - d2)*xFromFloor;
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


}
