package Mk01.Procs;

import DamagePackets.Points.crittableDamagePoint;
import Enums.AfflictionTypes;
import Enums.locDamTypes;
import Mk01.Weapons.ShotRecord;

public class ToxinProcToken extends TickingDamageProcToken
{
	private ToxinProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval, double inDamageValue)
	{
		this(inOrigin, inTimeStarted, inMaxNumTicks, inProcInterval, inDamageValue, 0, 0.0);
	}
	
	private ToxinProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval, double inDamageValue, int inDamageCritLevel, double inDamageCritMult)
	{
		this(inOrigin, inTimeStarted, inMaxNumTicks, inProcInterval, crittableDamagePoint.getInstance(locDamTypes.Toxin, inDamageValue, inDamageCritLevel, inDamageCritMult));
	}
	
	private ToxinProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval, crittableDamagePoint inDamage)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Poisoned, inMaxNumTicks, inProcInterval, inDamage);
	}
	
	public boolean hasGasParent()
	{
		return false;
	}
	
	public ProcToken getGasParent()
	{
		return null;
	}
	
	public static ToxinProcToken getInstance(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval, double inDamageValue, int inDamageCritLevel, double inDamageCritMult)
	{
		return new ToxinProcToken(inOrigin, inTimeStarted, inMaxNumTicks, inProcInterval, crittableDamagePoint.getInstance(locDamTypes.Toxin, inDamageValue, inDamageCritLevel, inDamageCritMult));
	}
	
	public static ToxinProcToken getInstance(GassedProcToken inGasParent, int inMaxNumTicks, double inDamageValue, int inDamageCritLevel, double inDamageCritMult)
	{
		return new ToxinProcToken_viaGas(inGasParent, inMaxNumTicks, inDamageValue, inDamageCritLevel, inDamageCritMult);
	}
	
	private static class ToxinProcToken_viaGas extends ToxinProcToken
	{
		private final GassedProcToken GasParent;
		
		private ToxinProcToken_viaGas(GassedProcToken inGasParent, int inMaxNumTicks, double inDamageValue)
		{
			this(inGasParent, inMaxNumTicks, inDamageValue, 0, 0.0);
		}
		
		private ToxinProcToken_viaGas(GassedProcToken inGasParent, int inMaxNumTicks, double inDamageValue, int inDamageCritLevel, double inDamageCritMult)
		{
			super(inGasParent.origin, inGasParent.timeStarted, inMaxNumTicks, inGasParent.procInterval, inDamageValue, inDamageCritLevel, inDamageCritMult);
			this.GasParent = inGasParent;
		}
		
		@Override
		public boolean hasGasParent()
		{
			return true;
		}
		
		@Override
		public GassedProcToken getGasParent()
		{
			return this.GasParent;
		}
		
		@Override
		public int compareTo(ProcToken other)
		{
			final int result = super.compareTo(other);
			
			if((result == 0) && (other.procFlavor == AfflictionTypes.Gassed) && (other == this.GasParent))
			{
				return 1;
			}
			else
			{
				return result;
			}
		}
	}
}
