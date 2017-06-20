package Mk01.Weapons.AccuracyMappers;

import Manifests.Enemy.Mk01.BodyPart;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class FullBodyAccuracyMapper implements AccuracyMapper
{
	private final BodyPart[] body;
	private final double totalArea;
	
	public FullBodyAccuracyMapper(BodyPart[] inBody)
	{
		this.body = inBody;
		this.totalArea = computeTotalArea(inBody);
	}

	private static double computeTotalArea(BodyPart[] inBody)
	{
		double result = 0;
		
		for(BodyPart part: inBody)
		{
			result += part.getRelativeSurfaceArea();
		}
		
		return result;
	}

	@Override
	public BodyPart rollNextPartHit(XorShiftStar in)
	{
		return this.rollNextPartHit(in, 1.0);
	}

	@Override
	public BodyPart rollNextPartHit(XorShiftStar in, double inAccuracyPercentage)
	{
		final BodyPart result;
		double dice = in.nextDouble(this.totalArea + (1.0 - inAccuracyPercentage));
		boolean isDone = false;
		int i = 0;
		
		while(!isDone && (i < this.body.length))
		{
			final double curArea = this.body[i].getRelativeSurfaceArea();
			
			if(curArea < dice)
			{
				dice -=curArea;
				i++;
			}
			else
			{
				isDone = true;
			}
		}
		
		if(isDone)
		{
			result = this.body[i];
		}
		else
		{
			result = BodyPart.getMissedPart();
		}
		
		return result;
	}
}
