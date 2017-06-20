package Mk00.Damage.Points;

import Enums.locDamTypes;

public class CrittableDamagePoint extends UnitDamagePoint
{
	private final int critLevel;
	private final double critMult;
	
	public CrittableDamagePoint(locDamTypes inFlavor, double inValue, int inCritLevel, double inCritMult)
	{
		super(inFlavor, inValue);
		this.critLevel = inCritLevel;
		this.critMult = inCritMult;
	}

	public int getCritLevel()
	{
		return this.critLevel;
	}

	public double getCritMult()
	{
		return this.critMult;
	}
}
