package Mk01.Spatial;

public class mutCoord implements Coord
{
	private double X;
	private double Y;
	
	public mutCoord(double inX, double inY)
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

	public void setPosX(double inX)
	{
		this.X = inX;
	}

	public void setPosY(double inY)
	{
		this.Y = inY;
	}
}
