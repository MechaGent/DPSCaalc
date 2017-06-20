package Mk01.Procs;

import DamagePackets.Points.crittableDamagePoint;
import Enums.AfflictionTypes;
import Enums.locDamTypes;
import Mk01.Weapons.ShotRecord;

public class GassedProcToken extends SingleTickDamageProcToken
{
	public GassedProcToken(ShotRecord inOrigin, long inTimeStarted, double inDamageValue)
	{
		this(inOrigin, inTimeStarted, inDamageValue, 0, 0.0);
	}
	
	public GassedProcToken(ShotRecord inOrigin, long inTimeStarted, double inDamageValue, int inDamageCritLevel, double inDamageCritMult)
	{
		this(inOrigin, inTimeStarted, crittableDamagePoint.getInstance(locDamTypes.Toxin, inDamageValue, inDamageCritLevel, inDamageCritMult));
	}
	
	public GassedProcToken(ShotRecord inOrigin, long inTimeStarted, crittableDamagePoint inDamage)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Gassed, inDamage);
	}
	
	@Override
	public int compareTo(ProcToken other)
	{
		final int topResult = super.compareTo(other);
		
		if(topResult == 0 && other.procFlavor == AfflictionTypes.Poisoned)
		{
			final ToxinProcToken castOther = (ToxinProcToken) other;
			
			if(castOther.hasGasParent() && this == castOther.getGasParent())
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return topResult;
		}
	}
}
