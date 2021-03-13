import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

/**
 * The main menu of the game
 * Contains start, instruction, and music options
 * @author Shaheer Lone
 */
public class MainMenu extends BaseScreen
{
	private StaticActor instructionScreen;
	private PhysicsActor hole;
	private PhysicsActor ball;
	static Music music;
	
    public MainMenu(Game g)
    {  
    	super(g, 1);  
    }
    
    /**
     * Game directions based on key input
     */
    public boolean keyDown(int keyPressed)
    {
    	// Start the game
    	if(keyPressed == Keys.S)
    		game.setScreen(new ElevatorLevel(game, 1));
    	// Pause music
    	if(keyPressed == Keys.M)
    		pauseMusic();
		return false;
    }
    
    /**
     * Pause/play music based on key input
     */
    private void pauseMusic()
    {
    	if(Gdx.input.isKeyPressed(Keys.M))
    		music.pause();
    	if(Gdx.input.isKeyPressed(Keys.N))
    		music.play();
    }

    public void create() 
    {        
    	music = Gdx.audio.newMusic(Gdx.files.internal("mainMenu/ElevatorMusic.mp3"));
    	music.setLooping(true);
    	music.play();
    	
    	StaticActor mainMenuScreen = new StaticActor();
    	mainMenuScreen.setTexture(new Texture(Gdx.files.internal("mainMenu/ELEVATOR (2).png")));
    	mainMenuScreen.setPosition(0, 0);
    	mainStage.addActor(mainMenuScreen);
    	
    	ball = new PhysicsActor();
	    Texture ballTex = new Texture(Gdx.files.internal("assets/ball.png"));
	    ball.setTexture(ballTex);
	    ball.storeAnimation("", ballTex);
	    ball.setMaxSpeed(50);
	    ball.setDeceleration(50);
	    ball.setPosition(0, 100);
	    mainStage.addActor(ball);
	    
	    hole = new PhysicsActor();
	    Texture holeTex = new Texture(Gdx.files.internal("assets/hole.png"));
	    hole = new PhysicsActor();
        hole.storeAnimation( "", holeTex );
        hole.setSize(180, 180);
        hole.setPosition(300, 30);
        hole.setOriginCenter();
        hole.rectangularBoundary();
        hole.setMaxSpeed(125);
        hole.setDeceleration(200);
        mainStage.addActor(hole);
        
        instructionScreen = new StaticActor();
    	instructionScreen.setTexture(new Texture(Gdx.files.internal("mainmenu/instScreen.png")));
    	instructionScreen.setPosition(0, 0);
    	instructionScreen.setVisible(false);
    	mainStage.addActor(instructionScreen);
    }
    
    public void update(float dt) 
    {   
    	// How the actors should move on screen
    	hole.rotateBy(45);
    	if(ball.getX() < 50)
    		ball.setAccelerationXY(50, 0);
    	if(ball.getX() > 220)
    		ball.setAccelerationXY(-50, 0);
    	
    	// Bring up the instructions actor
    	if(Gdx.input.isKeyPressed(Keys.I))
    		instructionScreen.setVisible(true);
    	if(Gdx.input.isKeyPressed(Keys.K))
			instructionScreen.setVisible(false);
    }
}