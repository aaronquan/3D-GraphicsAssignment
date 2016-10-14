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



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener {

    private Terrain myTerrain;
    private Camera myCamera;

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
		GLU glu = new GLU();
		myCamera.updateCamera(glu);
		//gl.glScaled(0.1, 0.1, 0.1);
		
		
		//gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_LINE);
		myTerrain.drawTerrain(gl);
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
		myTerrain.setLighting(gl);
		//gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, myTerrain.getSunlight(), 0);
		
		//cull back faces
		gl.glEnable(GL2.GL_CULL_FACE);
    	gl.glCullFace(GL2.GL_BACK);
		
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
    		myCamera.moveForward(0.2);
    		break;
    	case KeyEvent.VK_DOWN:
    		myCamera.moveBackward(0.2);
    		break;
    	case KeyEvent.VK_LEFT:
    		myCamera.turnLeft(2);
    		break;
    	case KeyEvent.VK_RIGHT:
    		myCamera.turnRight(2);
    		break;
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
