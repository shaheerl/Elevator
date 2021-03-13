import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
/**
 * Launches the game
 * @author Shaheer Lone
 */
public class ElevatorLauncher
{
    public static void main (String[] args)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Elevator";
        config.width = 500;
        config.height = 750;
        ElevatorGame myProgram = new ElevatorGame();
        LwjglApplication launcher = new LwjglApplication( myProgram, config );
    }
}