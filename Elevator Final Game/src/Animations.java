import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.HashMap;

/**
 * @author Shaheer Lone
 */
public class Animations extends StaticActor
{
	private Animation anim;
    private String animName;
    private HashMap<String,Animation> animaStorage;
    private float timePassed;
    
    public Animations()
    {
        super();
        anim = null;
        animName = null;
        animaStorage = new HashMap<String,Animation>();
        timePassed = 0;
    }

    /**
     * Stores the animations of an actor given the animation.
     * @param name Name of the animation
     * @param anim The animation
     */
    public void storeAnimation(String name, Animation anim)
    {
        animaStorage.put(name, anim);
        if (animName == null)
            setActiveAnimation(name);
    }
    
    /**
     * Stores the animations of an actor given the texture.
     * @param name Name of animation
     * @param tex The texture
     */
    public void storeAnimation(String name, Texture tex)
    {
        TextureRegion reg = new TextureRegion(tex);
        TextureRegion[] frames = { reg };
        Animation anim = new Animation(1.0f, frames);
        storeAnimation(name, anim);
    }
    
    /**
     * Sets the animation of the actor.
     * If there is no animation name, return nothing.
     * @param name The animation name
     */
    public void setActiveAnimation(String name)
    {
        if ( !animaStorage.containsKey(name) )
        {
            return;
        }
            
        animName = name;
        anim = animaStorage.get(name);
        timePassed = 0;
        
        Texture tex = anim.getKeyFrame(0).getTexture();
        setWidth( tex.getWidth() );
        setHeight( tex.getHeight() );
    }
    
    public String getAnimationName()
    {  
        return animName;  
    }
    
    /**
     * Copies the animated actors' stored information.
     * @param original The original actor to be copied
     */
    public void copy(Animations original)
    {
        super.copy(original);
        this.timePassed = 0;
        this.animaStorage = original.animaStorage; // sharing a reference
        this.animName = new String(original.animName);
        this.anim = this.animaStorage.get( this.animName );
    }

    @Override
    public void act(float dt)
    {
        super.act( dt );
        timePassed += dt;
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) 
    {
        texRegion.setRegion( anim.getKeyFrame(timePassed) );
        super.draw(batch, parentAlpha);
    }
}