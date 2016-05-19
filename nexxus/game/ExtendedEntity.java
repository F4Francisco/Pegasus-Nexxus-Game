
package game;


public abstract class ExtendedEntity extends Entity
{

	public ExtendedEntity(String ref, int x, int y)
	{
		this(ref, x, y, false);
	}

	public ExtendedEntity(String ref, int x, int y, boolean hasGravity) {
		super(ref, x, y, hasGravity);
	}


	@Override
	public abstract void collidedWith(Entity other);

	public abstract void notifyEnemyKilled(Entity killed);
}
