import com.badlogic.gdx.Game;

/**
 * Sets the screen from the main menu class constructor.
 * @author Shaheer Lone
 */
public class ElevatorGame extends Game
{
    public void create() 
    {  
		MainMenu tl = new MainMenu(this);
		setScreen(tl);
    }
}
