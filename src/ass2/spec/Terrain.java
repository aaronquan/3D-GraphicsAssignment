package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;



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
    private List<Enemy> myEnemies;
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
        myEnemies = new ArrayList<Enemy>();
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
        if (x < 0 || x > myAltitude.length-1 || z < 0 || z > myAltitude[0].length-1){
        	return altitude;
        }
        //System.out.println(x + "," + z);
        
        double xCeil = Math.ceil(x);
        double xFloor = Math.floor(x);
        double zCeil = Math.ceil(z);
        double zFloor = Math.floor(z);
    	
        //checking whether both x and z are ints
        if (x % 1 == 0 && z % 1 == 0){
        	//take altitude from the array
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
    
	public void addEnemy(double x, double z) {
		// TODO Auto-generated method stub
        double y = altitude(x, z);
        Enemy e = new Enemy(x, y, z);
        myEnemies.add(e);
	}

    public void drawTerrain(GL2 gl){
    	String terrainTextureFileName = "grass.bmp";
    	String terrainTextureExt = "bmp";
    	MyTexture terrainTexture = new MyTexture(gl, terrainTextureFileName, terrainTextureExt, true);
    	
    	//gl.glBindTexture(GL2.GL_TEXTURE_2D, terrainTexture.getTextureId());
    	gl.glBegin(GL2.GL_TRIANGLES);
    	
    	for (int x = 0; x < myAltitude.length - 1; x++){
    		for(int z = 0; z < myAltitude[x].length - 1; z++){
    			double [] p1 = {x, myAltitude[x][z], z};
    			double [] p2 = {x+1, myAltitude[x+1][z], z};
    			double [] p3 = {x, myAltitude[x][z+1], z+1};
    			double [] p4 = {x+1, myAltitude[x+1][z+1], z+1};
    			
    			double [] n1 = MathUtil.getNormal(p1, p3, p2);
    			double [] n2 = MathUtil.getNormal(p3, p4, p2);
    			
    			n1 = MathUtil.normalise(n1);
    			n2 = MathUtil.normalise(n2);
    			gl.glNormal3dv(n1, 0);
    			gl.glTexCoord2d(0, 0);
    			gl.glVertex3dv(p1, 0);
    			gl.glTexCoord2d(0, 1);
    			gl.glVertex3dv(p3, 0);
    			gl.glTexCoord2d(1, 0);
    			gl.glVertex3dv(p2, 0);
    			
    			gl.glNormal3dv(n2, 0);
    			gl.glTexCoord2d(0, 1);
    			gl.glVertex3dv(p3,0);
    			gl.glTexCoord2d(1, 1);
    			gl.glVertex3dv(p4, 0);
    			gl.glTexCoord2d(1, 0);
    			gl.glVertex3dv(p2, 0);
    		}
    	}
    	
    	gl.glEnd();
    }
    
    public void setLighting(GL2 gl, double angle){
    	float [] dir = {-mySunlight[0], mySunlight[1], -mySunlight[2], 0};
    	gl.glPushMatrix();
    		System.out.println(angle);
    		gl.glRotated(angle, 0, 1, 0);
    		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, dir, 0);
    	gl.glPopMatrix();
    }

	public void drawTree(GL2 gl) {
		for(Tree t : myTrees) {
			t.draw(gl);
		}
	}

	public void drawRoad(GL2 gl, GLUT glut) {
		for(Road r : myRoads) {
			r.draw(gl, glut, myAltitude);
		}
	}
	public void drawEnemy(GL2 gl) {
		for(Enemy e : myEnemies) {
			e.draw(gl);
		}
	}



}
