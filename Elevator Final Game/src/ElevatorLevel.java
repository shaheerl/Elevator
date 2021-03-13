import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.audio.Wav.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import java.util.ArrayList;
import java.util.Random;

/**
 * The basis of every level is contained in this class.
 * Every level has a ball, platform, pump, etc.
 * There are lines of code that change the level accordingly.
 * @author Shaheer Lone
 *
 */
public class ElevatorLevel extends BaseScreen
{
	// The world
	public World world;
	
	// Static actor(s).
	private StaticActor winText;
	private StaticActor loseText;
	private StaticActor background;
    
    // Physics actors.
    private PhysicsActor hole;
    private PhysicsActor blade;
    private PhysicsActor blades;
    private PhysicsActor ballFollower;
    private ArrayList<PhysicsActor> bladesList;
    
    // Box2DPhysics actors.
    private Box2DPhysics pump1;
    private Box2DPhysics pump2;
    private Box2DPhysics ball;
    private Box2DPhysics platform;
    
    // Text labels
    BitmapFont font;
    // 
    private Label timeLabel;
    private float timeElapsed;
    String timeText;
    //
    private Label levelLabel;
    private int currentLevel;
    String levelText;
    // 
    private Label nextLevelLabel;
    String nextLevelText;
    //
    private Label restartLabel;
    String restartText;
    //
    private Label mainMenuLabel;
    String mainMenuText;
    
    // Map height and width
    private static final int MAP_WIDTH = 500;
    private int mapHeight;
    
    // Win/lose are false as default
    boolean win = false;
    boolean lose = false;

    /**
     * Constructor for class. 
     * @param g The game.
     * @param level The level for the game.
     */
    public ElevatorLevel(Game g, int level)
    {  
    	super(g, level);  
    	currentLevel = level;
    }
    
    /**
     * Produces a random float between two values.
     * @param max The maximum value.
     * @param min The minimum value.
     * @return The value
     */
    public float randomFloatGenerator(float max, float min) {
        float generatedFloat = min + new Random().nextFloat() * (max - min);
        return generatedFloat;
    }
    
    public void create() 
    {   
        world = new World(new Vector2(0, -9.8f), true);
        timeElapsed = 0;
        mapHeight = 750;
        int stayHeight = 750;
        
    	// Textures:
		Texture pumpTex = new Texture(Gdx.files.internal("assets/pumps.png"));
		Texture startingPumpTex = new Texture((Gdx.files.internal("assets/pump.png")));
	    Texture exTex = new Texture(Gdx.files.internal("assets/blade.png"));
	    Texture holeTex = new Texture(Gdx.files.internal("assets/hole.png"));
	    // Box2D textures
	    Texture ballTex = new Texture(Gdx.files.internal("assets/ball.png"));
	        ballTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	    Texture platformTex = new Texture(Gdx.files.internal("assets/platf (2).png"));
	        platformTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        background = new StaticActor();
	        background.setTexture(new Texture(Gdx.files.internal("assets/bckg.jpg")));
	        background.setPosition(0, 0);
	        mainStage.addActor(background);
	        
	    //Physics actors
	    ballFollower = new PhysicsActor();
	    ballFollower.storeAnimation("", ballTex);
	    ballFollower.setOriginCenter();
	    ballFollower.circularBoundary();
	    ballFollower.setPosition(-100, -100);
	    mainStage.addActor(ballFollower);
	    
	    blade = new PhysicsActor();
	        blade.storeAnimation( "", exTex );
		    blade.setOriginCenter();
		    blade.circularBoundary();
		    blade.setMaxSpeed(50);
		    blade.setDeceleration(50);
		
	    bladesList = new ArrayList<PhysicsActor>();
	        for (int i = 0; i < 3 ; i++)
	        {
	        	float xCoord = randomFloatGenerator(440, 20);
	        	float yCoord = randomFloatGenerator(mapHeight - 200, 300);
	            blades = blade.clone();
	            blades.setPosition(xCoord, yCoord);
	            mainStage.addActor(blades);
	            bladesList.add(blades);
	        }
        
        hole = new PhysicsActor();
	        hole.storeAnimation( "", holeTex );
	        hole.setOriginCenter();
	        hole.rectangularBoundary();
	        hole.setMaxSpeed(125);
	        hole.setDeceleration(200);
	        mainStage.addActor(hole);

		// Box2d Physics Actors
	    Box2DPhysics floor = new Box2DPhysics();
	        floor.storeAnimation( "default", platformTex );
	        floor.setPosition(-100,0);
	        mainStage.addActor( floor );
	        floor.setStatic();
	        floor.setShape("rectangle");
	        floor.initializePhysics(world);
	        
		ball = new Box2DPhysics();
	        ball.storeAnimation( "default", ballTex );
	        ball.setPosition(225, 250);
	        mainStage.addActor(ball);
	        ball.setShape("circle");
	        ball.setDynamic();
	        ball.actorPhysics(3, 0.01f, 0.5f);
	        ball.initializePhysics(world);
	        
	    platform = new Box2DPhysics();
	        platform.storeAnimation( "default", platformTex );
	        platform.setPosition(-100, 190);
	        mainStage.addActor(platform);
	        platform.setShape("rectangle");
	        platform.setDynamic();
	        platform.actorPhysics(0.6f, 1f, 0.1f);
	        platform.initializePhysics(world);
	        
	    pump1 = new Box2DPhysics();
			pump1.storeAnimation("", pumpTex);
			pump1.setOrigin(pump1.getWidth() / 2, pump1.getHeight() / 2);
			pump1.setPosition(25, 50);
			mainStage.addActor(pump1);
			pump1.setDynamic();
			pump1.setShape("rectangle");
			pump1.notRotatableBody();
			pump1.actorPhysics(0.5f, 0.5f, 0.1f);
	        pump1.initializePhysics(world);
	        
	    pump2 = new Box2DPhysics();
			pump2.storeAnimation("", pumpTex);
			pump2.setOrigin(pump2.getWidth() / 2, pump2.getHeight() / 2);
			pump2.setPosition(420, 50);
			mainStage.addActor(pump2);
			pump2.setDynamic();
			pump2.setShape("rectangle");
			pump2.notRotatableBody();
			pump2.actorPhysics(0.5f, 0.5f, 0.001f);
	        pump2.initializePhysics(world);
	        
        StaticActor restart = new StaticActor();
	    	restart.setTexture(new Texture(Gdx.files.internal("assets/rKey.png")));
	    	restart.setPosition(300, 90);
	    	restart.setSize(50, 50);
    	StaticActor menu = new StaticActor();
	    	menu.setTexture(new Texture(Gdx.files.internal("assets/mKey.jpg")));
	    	menu.setPosition(130, 90);
	    	menu.setSize(50, 50);
	    StaticActor startingPump1 = new StaticActor();
	        startingPump1.setTexture(startingPumpTex);
	        startingPump1.setPosition(0, 0);
	    StaticActor startingPump2 = new StaticActor();
	        startingPump2.setTexture(startingPumpTex);
	        startingPump2.setPosition(400, 0);
        mainStage.addActor(startingPump1);
	    mainStage.addActor(startingPump2);
    	uiStage.addActor(menu);
    	uiStage.addActor(restart);
	    
	    winText = new StaticActor();
	        winText.setTexture( new Texture(Gdx.files.internal("assets/you-win.png")) );
	        winText.setPosition( 20, mapHeight - 350 );
	        winText.setVisible( false );
	        uiStage.addActor( winText );
        loseText = new StaticActor();
	    	loseText.setTexture(new Texture(Gdx.files.internal("assets/game-over.png")));
	    	loseText.setPosition(110, mapHeight/2-100);
	    	loseText.setVisible(false);
	    	uiStage.addActor(loseText);
	    
        // Text labels
	    font = new BitmapFont();
	    // Timer
	        timeText = "Time: 0";
	        LabelStyle timerStyle = new LabelStyle(font, Color.WHITE);
	        timeLabel = new Label(timeText, timerStyle);
	        timeLabel.setFontScale(2);
	        timeLabel.setPosition(350, stayHeight - 25);
	        uiStage.addActor(timeLabel);
	   // Level
	        levelText = "Level: " + currentLevel;
	        LabelStyle levelStyle = new LabelStyle(font, Color.WHITE);
	        levelLabel = new Label(levelText, levelStyle);
	        levelLabel.setFontScale(2);
	        levelLabel.setPosition(50, stayHeight - 25);
	        uiStage.addActor(levelLabel);
	   // Next level
	        nextLevelText = "Press ENTER to go to the next level.";
	        LabelStyle nextLevelStyle = new LabelStyle(font, Color.WHITE);
	        nextLevelLabel = new Label(nextLevelText, nextLevelStyle);
	        nextLevelLabel.setFontScale(2);
	        nextLevelLabel.setPosition(15, stayHeight - 400);
	        nextLevelLabel.setVisible(false);
	        uiStage.addActor(nextLevelLabel);
	   // Restart 
	        restartText = "restart level";
	        LabelStyle restartStyle = new LabelStyle(font, Color.WHITE);
	        restartLabel = new Label(restartText, restartStyle);
	        restartLabel.setFontScale(1.5f);
	        restartLabel.setPosition(275, 50);
	        uiStage.addActor(restartLabel);
        // Main menu
	        mainMenuText = "   return to \n main menu";
	        LabelStyle mainMenuStyle = new LabelStyle(font, Color.WHITE);
	        mainMenuLabel = new Label(mainMenuText, mainMenuStyle);
	        mainMenuLabel.setFontScale(1.5f);
	        mainMenuLabel.setPosition(100, 27);
	        uiStage.addActor(mainMenuLabel);
    }
    
    // Update the level as time passes
    public void update(float dt) 
    {   
    	// Update the maps' height according to the level which it is at.
    	int total = currentLevel * 750;
    	mapHeight = total;
    	
    	// time to simulate.
        world.step(1/90f, 6, 2);
        
        // Rotate all of the blades in the map.
		for (PhysicsActor blades : bladesList) {
			blades.rotateBy(70);
		}

        hole.rotateBy(45);
        // Move the hole left and right according to position
        hole.setPosition(220, mapHeight - 100);
    	if(hole.getX() > 375)
        	hole.setAccelerationXY(-75, 0);
        if (hole.getX() < 75)
        	hole.setAccelerationXY(75, 0);
        
        // Bounding the pumps and platform to the screen so they do not go entirely off map
        pump1.setX( MathUtils.clamp( pump1.getX(), 25,  pump1.getWidth() - 25));
        pump1.setY( MathUtils.clamp( pump1.getY(), 0,  mapHeight));
        pump2.setX( MathUtils.clamp( pump2.getX(), 420, MAP_WIDTH - 50));
        pump2.setY( MathUtils.clamp( pump2.getY(), 0,  mapHeight));
        platform.setX( MathUtils.clamp( platform.getX(), -110,  610 ));
        platform.setY( MathUtils.clamp( platform.getY(), 0,  mapHeight ));
        
        // Movement of the left and right pump.
        if(Gdx.input.isKeyPressed(Keys.LEFT))
        	pump1.moveActor(new Vector2(0, 30));
        else if(Gdx.input.isKeyPressed(Keys.RIGHT))
        	pump2.moveActor(new Vector2(0, 30));
        
        // Sound effects
        Music warpSound = Gdx.audio.newMusic(Gdx.files.internal("assets/warp.wav"));
        Music bladeSound = Gdx.audio.newMusic(Gdx.files.internal("assets/blade.wav"));
        Music gameOverSound = Gdx.audio.newMusic(Gdx.files.internal("assets/game-over-sound.wav"));
        
        // Move the background as the ball moves
        if(ball.getY() > 400)
        	background.setPosition(0, ball.getY()-400);
        
        // Losing scenarios and results.
        if(ball.getX() > 570 || ball.getX() < -70) {
        	ball.setPosition(-60, 0);
        	gameOverSound.play();
        	lose = true;
        }
        
		for (PhysicsActor blades : bladesList)
			if (ballFollower.overlapping(blades, true)) {
				lose = true;
				bladeSound.play();
			}
        
		if (lose && !win) {
			ball.remove();
			ballFollower.remove();
			loseText.setVisible(true);
		}
		// If the ball touches the hole, you've won the game.
		if (ballFollower.overlapping(hole, true)) {
			warpSound.play();
			win = true;
			ballFollower.remove();
			ball.remove();
			currentLevel += 1;
			Action fadeIn = Actions.sequence(Actions.alpha(0), Actions.show(), Actions.fadeIn(2),
					Actions.forever(Actions.sequence(Actions.color(new Color(1, 0, 0, 1), 1),
							Actions.color(new Color(0, 0, 1, 1), 1))));
			winText.addAction(fadeIn);
			System.out.println(currentLevel);
		}
        
        // If won, give an option to move onto the next level.
        if(win)
        {
        	int previousLevel = currentLevel - 1;
        	levelLabel.setVisible(false);
        	nextLevelLabel.setVisible(true);
        	timeLabel.setText( "Time to complete level  " + previousLevel + ": \n           " + (int)timeElapsed  + " seconds!");
        	timeLabel.setPosition(100, winText.getY() + 130);
        	if(Gdx.input.isKeyPressed(Keys.ENTER))
        	{
        		// currentLevel increments by 1 once the level is won.
        		game.setScreen(new ElevatorLevel(game, currentLevel));
        		this.dispose();
        	}
        }
        
        // The timer continues to increase while the game is not won or lost.
        if(!win && !lose)
        {
            ballFollower.setPosition(ball.getX(), ball.getY());
        	timeElapsed += dt;
            timeLabel.setText( "Time: " + (int)timeElapsed );
        	levelLabel.setText("Level: " + currentLevel);
        }
        
        Camera cam = mainStage.getCamera();
        // Camera will follow the ball.
	    cam.position.set(ball.getX() + ball.getOriginX(), ball.getY() + ball.getOriginY(), 0);
	    // Camera is bound to stay aligned with the screen.
	    cam.position.x = MathUtils.clamp(cam.position.x, 250, MAP_WIDTH-250);
	    cam.position.y = MathUtils.clamp(cam.position.y, 400, mapHeight-250);
	    // Update the camera given information.
	    cam.update();
	    
	    // Return to main menu if M is pressed
	    if(Gdx.input.isKeyPressed(Keys.M))
	    {
	    	MainMenu.music.dispose();
	    	game.setScreen(new MainMenu(game));
	    }
	    // Restart level if R is pressed
	    if(Gdx.input.isKeyPressed(Keys.R))
	    {
	    	int previousLevel = currentLevel - 1;
	    	if(previousLevel < 1)
	    	{
	    		previousLevel = 1;
	    	}
	    	game.setScreen(new ElevatorLevel(game, previousLevel));
	    }
    }
}