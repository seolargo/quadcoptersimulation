package renderEngine;
 
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
 
public class DisplayManager {
     
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    
    private static final int FPS_CAP = 120; //we can set the FPS that we want the game to run at.
     
    private static long lastFrameTime;
    private static float delta; 
    
    
    public static void createDisplay(){     
    	/*
    	 * It is going to be to open the display at the beginning when we start up the game.
    	 */
    	
        ContextAttribs attribs = new ContextAttribs(3,2) //the constructor here actually takes in the version of OPENGL that we want to use, so we use OpenGL 3.2
        .withForwardCompatible(true)
        .withProfileCore(true);
         
        try {
        	/*
        	 * The first thing we need to do when we create a display is to determine the size
        	 * of the display. 
        	 */
            Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT)); 
            
            /*
             * Once we've determined the WIDTH and the HEIGHT of the display, we need to actually create the display.
             */
            Display.create(new PixelFormat(), attribs);
            
            /*
             * Baþlýk ekleyebilirsin :)
             */
            Display.setTitle("Quadcopter Simulation");
            
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        
        /*
         * Tell the OpenGL where in the display it can render the game.
         * And we want to choose whole display.
         */
        GL11.glViewport(0,0, WIDTH, HEIGHT); //0, 0 --> This needs to know the bottom left of the display and the top right of the display,
        //so we start from very bottom of the display.
        
        
        lastFrameTime = getCurrentTime();
    }
     
    public static void updateDisplay() { 
    	/*
    	 * We need to a method to update the display every single frame.
    	 */
    	
    	/*
    	 * This synchronizes the game to run at a steady FPS. 
    	 */
        Display.sync(FPS_CAP);   
        Display.update();
        
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f; 
        lastFrameTime = currentFrameTime;
    }
    
    public static float getFrameTimeSeconds() {
    	return delta;
    }
     
    public static void closeDisplay(){         	
    	/*
    	 * When we finish the game, we want to exit and close it.
    	 */
    	
        Display.destroy();  
    }
    
    private static long getCurrentTime() {
    	return Sys.getTime() *1000/ Sys.getTimerResolution();
    }
 
}