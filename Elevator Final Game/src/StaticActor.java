import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

/**
 * A simple actor that does not move.
 * Used as for non-significant actors.
 * @author Shaheer Lone
 *
 */
public class StaticActor extends Actor
{
	// Texture region of actor
    public TextureRegion texRegion;
    // Polygon boundary of actor
    public Polygon poly;

    public StaticActor()
    {
        super();
        texRegion = new TextureRegion();
        poly = null;
    }
    
    /**
     * Set the origin of the actor at the centre.
     */
    public void setOriginCenter()
    {
		if (getWidth() == 0)
			System.err.println("error: actor size not set");
		else
			setOrigin(getWidth() / 2, getHeight() / 2);
    }
    
    /**
     * Set the texture of the actor.
     * @param tex The texture to set
     */
    public void setTexture(Texture tex)
    { 
		int w = tex.getWidth();
		int h = tex.getHeight();
		setWidth(w);
		setHeight(h);
		texRegion.setRegion(tex);
    }
    
    /**
     * Create a rectangular boundary for the actor.
     */
    public void rectangularBoundary()
    {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = {0,0, w,0, w,h, 0,h};
        poly = new Polygon(vertices);
        poly.setOrigin( getOriginX(), getOriginY() );
    }
    
    /**
     * Create a boundary that is very precise/circular for the actor.
     */
    public void circularBoundary()
    {
		int n = 12; // number of vertices
		float w = getWidth();
		float h = getHeight();
		float[] vertices = new float[2 * n];
		for (int i = 0; i < n; i++) {
			float t = i * 6.28f / n;
			// x-coordinate
			vertices[2 * i] = w / 2 * MathUtils.cos(t) + w / 2;
			// y-coordinate
			vertices[2 * i + 1] = h / 2 * MathUtils.sin(t) + h / 2;
		}
		poly = new Polygon(vertices);
		poly.setOrigin(getOriginX(), getOriginY());
    }
    
    /**
     * Get the polygon boundary for the actor created.
     * @return
     */
    public Polygon getBoundingPolygon()
    {          
        poly.setPosition( getX(), getY() );
        poly.setRotation( getRotation() );
        return poly;
    }

    /**
     *  Determine if the collision polygons of two BaseActor objects overlap.
     *  If (resolve == true), then when there is overlap, move this BaseActor
     *  along the shortest distance that the colliding object can be moved in 
     *  order to no longer be colliding with the object.
     */
    public boolean overlaps(StaticActor other, boolean resolve)
    {
        Polygon poly1 = this.getBoundingPolygon();
        Polygon poly2 = other.getBoundingPolygon();
        
        if ( !poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()) )
            return false;
        
        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polyOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        if (polyOverlap && resolve)
        {
        	// depth is the distance of the translation required for the separation
            this.moveBy( mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth );
        }
        float significant = 0.5f;
        return (polyOverlap && (mtv.depth > significant));
    }
    
    /**
     * Copies a BaseActors' information.
     * @param original
     */
    public void copy(StaticActor original)
    {
        if (original.texRegion.getTexture() != null )
            this.texRegion = new TextureRegion( original.texRegion );
        if (original.poly != null)
        {
            this.poly = new Polygon( original.poly.getVertices() );
            this.poly.setOrigin( original.getOriginX(), original.getOriginY() );
        }
        this.setPosition( original.getX(), original.getY() );
        this.setOriginX( original.getOriginX() );
        this.setOriginY( original.getOriginY() );
        this.setWidth( original.getWidth() );
        this.setHeight( original.getHeight() );
        this.setColor( original.getColor() );
        this.setVisible( original.isVisible() );
    }
    
    @Override
    public void act(float dt)
    {
        super.act( dt );
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) 
    {
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if ( isVisible() )
            batch.draw( texRegion, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
    }
}