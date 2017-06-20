package Mk01.Weapons.AccuracyMappers;

import Manifests.Enemy.Mk01.BodyPart;
import Random.XorShiftStar.Mk02.XorShiftStar;

public interface AccuracyMapper
{
	public BodyPart rollNextPartHit(XorShiftStar in);
	public BodyPart rollNextPartHit(XorShiftStar in, double accuracyPercentage);
}
