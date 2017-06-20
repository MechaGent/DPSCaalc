package Mk00.Damage.Points;

import Enums.locDamTypes;

public class UnitDamagePoint
{
	private final locDamTypes flavor;
	private final double value;
	
	public UnitDamagePoint(locDamTypes inFlavor, double inValue)
	{
		this.flavor = inFlavor;
		this.value = inValue;
	}

	public locDamTypes getFlavor()
	{
		return this.flavor;
	}

	public double getValue()
	{
		return this.value;
	}
}
