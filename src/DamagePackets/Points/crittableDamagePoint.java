package DamagePackets.Points;

import Enums.locDamTypes;

public abstract class crittableDamagePoint extends unitDamagePoint
{
	public crittableDamagePoint(locDamTypes inFlavor, double inValue)
	{
		super(inFlavor, inValue);
	}
	
	public abstract int getCritLevel();
	public abstract double getCritMult();
	
	public static crittableDamagePoint getInstance(locDamTypes inFlavor, double inValue, int inCritLevel, double inCritMult)
	{
		return new crittableDamagePoint_withCrit(inFlavor, inValue, inCritLevel, inCritMult);
	}
	
	public static crittableDamagePoint getInstance(locDamTypes inFlavor, double inValue)
	{
		return new crittableDamagePoint_noCrit(inFlavor, inValue);
	}
	
	private static class crittableDamagePoint_withCrit extends crittableDamagePoint
	{
		private final int critLevel;
		private final double critMult;
		
		public crittableDamagePoint_withCrit(locDamTypes inFlavor, double inValue, int inCritLevel, double inCritMult)
		{
			super(inFlavor, inValue);
			this.critLevel = inCritLevel;
			this.critMult = inCritMult;
		}

		@Override
		public int getCritLevel()
		{
			return this.critLevel;
		}

		@Override
		public double getCritMult()
		{
			return this.critMult;
		}
	}
	
	private static class crittableDamagePoint_noCrit extends crittableDamagePoint
	{
		public crittableDamagePoint_noCrit(locDamTypes inFlavor, double inValue)
		{
			super(inFlavor, inValue);
		}

		@Override
		public int getCritLevel()
		{
			return 0;
		}

		@Override
		public double getCritMult()
		{
			return 0.0;
		}
	}
}
