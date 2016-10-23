package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener {
	
    private Terrain myTerrain;
    private Camera myCamera;
    
    private double moveSpeed = 0.1;
    private double turnSpeed = 2;
    
    private boolean nightMode = false;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
   
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
          panel.addKeyListener(this);
          
          myCamera = new Camera(0, 0, 0);
          
          
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE); 
          
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        myTerrain.setLighting(gl, myCamera.getAngle());
        if(nightMode){
        	gl.glDisable(GL2.GL_LIGHT0);
        	gl.glEnable(GL2.GL_LIGHT1);
        	double [] pos = myCamera.getPosition();
        	double [] dir = myCamera.getLookAt();
        	float [] posF = new float[pos.length+1];
        	float [] dirF = new float[dir.length];
        	for (int i = 0; i<pos.length; i++){
        		posF[i] = (float) pos[i];
        		dirF[i] = (float) dir[i];
        	}
        	//posF[0] += dirF[0]*1;
        	//posF[1] += dirF[1]*1;
        	//posF[2] += dirF[2]*1;
        	posF[3] = 1.0f;
        	float lightDifAndSpec[] = {1.0f, 1.0f, 1.0f, 1.0f};
        	float spotAngle = 10f;
        	float spotExp = 10f;
        	//float lightDifAndSpec[] = {1.0f, 1.0f, 1.0f, 1.0f};
        	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightDifAndSpec,0);
    		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDifAndSpec,0);
    		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightDifAndSpec,0);
        	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, posF, 0);
        	gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, spotAngle);
        	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, dirF,0);    
        	gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, spotExp);
        }else{
        	gl.glEnable(GL2.GL_LIGHT0);
        	gl.glDisable(GL2.GL_LIGHT1);
        }
        
        GLUT glut = new GLUT();
		GLU glu = new GLU();
		myCamera.updateCamera(glu);
		myCamera.drawAvatar(gl, glut);
		//gl.glScaled(0.1, 0.1, 0.1);
		
		//gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_LINE);
		myTerrain.drawTerrain(gl);
		myTerrain.drawTree(gl);
		myTerrain.drawRoad(gl, glut);
		myTerrain.drawEnemy(gl);
		//gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		//lighting
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		//gl.glEnable(GL2.GL_LIGHT1);
		gl.glEnable(GL2.GL_NORMALIZE);
		
		
		float lightDifAndSpec[] = {1.0f, 1.0f, 1.0f, 1.0f};
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec,0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec,0);
		
		float globAmb[] = { 0.2f, 0.2f, 0.2f, 1.0f };
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0);
		//myTerrain.setLighting(gl, myCamera.getAngle());
		
		//cull back faces
		gl.glEnable(GL2.GL_CULL_FACE);
    	gl.glCullFace(GL2.GL_BACK);
    	
    	// Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 

    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D); 
    	
		float [] ad = {1.0f, 1.0f, 1.0f, 1.0f}; 
		float [] sp = {0.2f, 0.2f, 0.2f, 1.0f}; 
		float [] sh = {0f, 0f, 0f, 1.0f}; 
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, ad,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, sp,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, sh,0);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(60, width/height, 1, 20);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		
	}
	@Override
	public void keyPressed(KeyEvent ev) {
    	switch (ev.getKeyCode()) {	
    	case KeyEvent.VK_UP:
    		myCamera.moveForward(moveSpeed, myTerrain);
    		break;
    	case KeyEvent.VK_DOWN:
    		myCamera.moveBackward(moveSpeed, myTerrain);
    		break;
    	case KeyEvent.VK_LEFT:
    		myCamera.turnLeft(turnSpeed);
    		break;
    	case KeyEvent.VK_RIGHT:
    		myCamera.turnRight(turnSpeed);
    		break;
    	case KeyEvent.VK_W:
    		myCamera.moveUp(moveSpeed);
    		break;
    	case KeyEvent.VK_S:
    		myCamera.moveDown(moveSpeed);
    		break;
    	case KeyEvent.VK_SPACE:
    		myCamera.toggleView();
    		break;
    	case KeyEvent.VK_N:
    		nightMode = !nightMode;
    		
    	default:
    		break;
    	}
    }

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}
