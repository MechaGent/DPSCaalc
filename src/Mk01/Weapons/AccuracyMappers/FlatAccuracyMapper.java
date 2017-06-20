package Mk01.Weapons.AccuracyMappers;

import Manifests.Enemy.Mk01.BodyPart;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class FlatAccuracyMapper implements AccuracyMapper
{
	private final BodyPart target;

	public FlatAccuracyMapper(BodyPart inTarget)
	{
		this.target = inTarget;
	}

	@Override
	public BodyPart rollNextPartHit(XorShiftStar inIn)
	{
		return this.target;
	}

	@Override
	public BodyPart rollNextPartHit(XorShiftStar inIn, double inAccuracyPercentage)
	{
		return this.target;
	}
}
