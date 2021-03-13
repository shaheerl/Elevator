import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.math.Vector2;

/**
 * An actor that is far more dynamic than Physics Actor.
 * Has gravity, friction, density, restitution, etc
 * Set to certain shapes and create collisions.
 * @author Shaheer Lone
 */
public class Box2DPhysics extends Animations {
	// body definition - used to initialize body
	protected BodyDef bodyDef;
	protected Body body;
	// fixture - attached to body
	protected FixtureDef fixtureDef;

	public Box2DPhysics() {
		bodyDef = new BodyDef();
		body = null;
		fixtureDef = new FixtureDef();
	}

	// return the body
	public Body getBody() {
		return body;
	}
	
	/**
	 * Determine whether or not this body rotates upon movement / collisions.
	 */
	public void notRotatableBody() {
		bodyDef.fixedRotation = true;
	}
	
	/**
	 * Set the body type to static. 
	 * Static bodies do not obey gravity vector
	 */
	public void setStatic() {
		bodyDef.type = BodyType.StaticBody;
	}

	/**
	 * Set the body type to dynamic (obeys gravity).
	 */
	public void setDynamic() {
		bodyDef.type = BodyType.DynamicBody;
	}

	/**
	 * Set the shape of the actor object
	 * @param shape The shape to be set to (circle/rectangle)
	 */
	public void setShape(String shape) {
		setOriginCenter();
		bodyDef.position.set((getX() + getOriginX()) / 100, (getY() + getOriginY()) / 100);
		if (shape.equals("circle")) {
			CircleShape circ = new CircleShape();
			circ.setRadius(getWidth() / 200);
			fixtureDef.shape = circ;
		} else if (shape.equals("rectangle")) {
			PolygonShape rect = new PolygonShape();
			rect.setAsBox(getWidth() / 200, getHeight() / 200);
			fixtureDef.shape = rect;
		}
	}

	/**
	 * The required information for the fixture of the actor
	 * @param density of the actor (weight)
	 * @param friction the actor goes against
	 * @param restitution of the actor (how bouncy/resistant to other actors)
	 */
	public void actorPhysics(float density, float friction, float restitution) {
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
	}

	/**
	 * Uses data to initialize actor and add to world
	 * @param wThe world to add to
	 */
	public void initializePhysics(World w) {
		// add the body to the world
		body = w.createBody(bodyDef);
		Fixture f = body.createFixture(fixtureDef);
		f.setUserData("main");
		body.setUserData(this);
	}

	// Get actor velocity
	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}
	// Get actor speed
	public float getSpeed() {
		return getVelocity().len();
	}
	// Set actor velocity
	public void setVelocity(Vector2 v) {
		body.setLinearVelocity(v);
	}
	// Set actor speed
	public void setSpeed(float s) {
		setVelocity(getVelocity().setLength(s));
	}
	// Move the actor in a certain direction with a Vector2
	public void moveActor(Vector2 force) {
		body.applyForceToCenter(force, true);
	}
	
	// Update the actor based on physics information.
	public void act(float dt) {
		super.act(dt);
		Vector2 center = body.getWorldCenter();
		setPosition(100 * center.x - getOriginX(), 100 * center.y - getOriginY());
		float a = body.getAngle();
		setRotation(a * 360 / 6.28f);
	}
	
	/**
	 * Clones/creates an exact replica of a Box2D actor.
	 */
	public Box2DPhysics clone() {
		Box2DPhysics newbie = new Box2DPhysics();
		newbie.copy(this);
		return newbie;
	}

}