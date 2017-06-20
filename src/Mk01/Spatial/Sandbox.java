package Mk01.Spatial;

import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Mk01.Enemy.WeinerPuppet;

/**
 * This class will handle all spatial stuff, like hitboxing and enemy positioning and such.
 * <br> 2d for now
 * @author MechaGent
 *
 */
public class Sandbox
{
	private final WeinerBot[] enemies;
	private final immutCoord sandboxCenter;
	private final immutCoord farRightCorner;
	private final immutCoord gunPos;
	
	public Sandbox(WeinerPuppet[] inEnemies, immutCoord inSandboxCenter, immutCoord inFarRightCorner, immutCoord inGunPos)
	{
		this.enemies = WeinerBot.convert(inEnemies);
		this.sandboxCenter = inSandboxCenter;
		this.farRightCorner = inFarRightCorner;
		this.gunPos = inGunPos;
	}
	
	public SingleLinkedList<WeinerPuppet> getAllWthin(double inX, double inY, double radiusInMeters)
	{
		return new SingleLinkedList<WeinerPuppet>();
	}

	private static class WeinerBot
	{
		private final WeinerPuppet puppet;
		private double X;
		private double Y;
		
		public WeinerBot(WeinerPuppet inPuppet)
		{
			this(inPuppet, 0.0, 0.0);
		}
		
		public WeinerBot(WeinerPuppet inPuppet, double inX, double inY)
		{
			this.puppet = inPuppet;
			this.X = inX;
			this.Y = inY;
		}
		
		public static WeinerBot[] convert(WeinerPuppet[] in)
		{
			final WeinerBot[] result = new WeinerBot[in.length];
			
			for(int i = 0; i < result.length; i++)
			{
				result[i] = new WeinerBot(in[i]);
			}
			
			return result;
		}
	}
}
