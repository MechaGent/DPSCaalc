package Mk01.Spatial;

public class immutCoord implements Coord
{
	private final double X;
	private final double Y;
	
	public immutCoord(double inX, double inY)
	{
		this.X = inX;
		this.Y = inY;
	}

	@Override
	public double getPosX()
	{
		return this.X;
	}

	@Override
	public double getPosY()
	{
		return this.Y;
	}
}
