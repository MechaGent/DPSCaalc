package Enums;

public enum TickIntervals
{
	OneSecond(1000000000);
	
	private final int IntValue;
	private final double DubValue;
	
	private TickIntervals(int inValue)
	{
		this.IntValue = inValue;
		this.DubValue = inValue;
	}

	public int getValueAsInt()
	{
		return this.IntValue;
	}
	
	public double getValueAsDouble()
	{
		return this.DubValue;
	}
	
	public static double fromNanoToSeconds(long in)
	{
		return fromNanoToSeconds((double) in);
	}
	
	public static double fromNanoToSeconds(double in)
	{
		return in / TickIntervals.OneSecond.DubValue;
	}
	
	public static long fromSecondsToNano(double in)
	{
		return (long) (in * TickIntervals.OneSecond.DubValue);
	}
}
