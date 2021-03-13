import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class PhysicsActor extends Animations
{
	// Velocity and acceleration of actor
    private Vector2 velocity;
    private Vector2 acceleration;
    // Maximum speed and deceleration of actor
    private float maxSpeed;
    private float deceleration;
    boolean accelerating = false;
    // Actor boundary
    Rectangle boundary;
    
    public PhysicsActor()
    {
        velocity = new Vector2();
        acceleration = new Vector2();
        maxSpeed = 1000;
        deceleration = 0;
    }
    
    // Set velocity of actor
	public void setVelocityXY(float vx, float vy) {
		velocity.set(vx, vy);
	}
    // Set velocity from angle and speed
    public void setVelocityAS(float angleDeg, float speed)
    {
        velocity.x = speed * MathUtils.cosDeg(angleDeg);
        velocity.y = speed * MathUtils.sinDeg(angleDeg);
    }
    // Get actor speed
	public float getSpeed() {
		return velocity.len();
	}
	// Set actor speed
	public void setSpeed(float s) {
		velocity.setLength(s);
	}
	// Set actor max speed
	public void setMaxSpeed(float ms) {
		maxSpeed = ms;
	}


    // acceleration methods
    public void setAccelerationXY(float ax, float ay)
    {  acceleration.set(ax,ay);  }

    public void addAccelerationXY(float ax, float ay)
    {  acceleration.add(ax,ay);  }

    // set acceleration from angle and speed
    public void setAccelerationAS(float angleDeg, float speed)
    {
        acceleration.x = speed * MathUtils.cosDeg(angleDeg);
        acceleration.y = speed * MathUtils.sinDeg(angleDeg);
    }

    /**
     * Deceleration speed of an actor
     * @param d Speed of deceleration
     */
    public void setDeceleration(float d)
    {  
    	deceleration = d;  
    	accelerating = false;
    }
    
    /**
     * 
     * @param other The other actor/object
     * @param resolve Whether or not it can collide/go over the actor/object
     * @return the movement to separate
     */
    public boolean overlapping(PhysicsActor other, boolean resolve)
    {
    	Polygon poly1 = this.getBoundingPolygon();
        Polygon poly2 = other.getBoundingPolygon();
        
        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return false;
        
        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polyOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        // If the polygons are overlapping
        if (polyOverlap && resolve)
        {
        	// depth is the distance of the translation required for the separation
            this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );
        }
        float significant = 0.5f;
        return (polyOverlap && (mtv.depth > significant));
    }
    
    /**
     * Use to create multiple physics actors.
     * Mainly used in array lists to create multiple actors 
     */
    public PhysicsActor clone()
    {
        PhysicsActor newbie = new PhysicsActor();
        newbie.copy( this );
        return newbie;
    }	

    public void act(float dt) 
    {
        super.act(dt);

        // apply acceleration
        velocity.add( acceleration.x * dt, acceleration.y * dt );
        
        // decrease velocity when not accelerating
        if (acceleration.len() < 0.01)
        {
            float decelerateAmount = deceleration * dt;
            if ( getSpeed() < decelerateAmount )
                setSpeed(0);
            else
                setSpeed( getSpeed() - decelerateAmount );
        }
        
        // do not let actor exceed max speed
        if ( getSpeed() > maxSpeed )
            setSpeed(maxSpeed);

        // apply velocity
        moveBy( velocity.x * dt, velocity.y * dt );
    }
    
}