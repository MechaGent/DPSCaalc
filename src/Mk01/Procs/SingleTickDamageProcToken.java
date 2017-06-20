package Mk01.Procs;

import DamagePackets.Points.crittableDamagePoint;
import Enums.AfflictionTypes;
import Enums.locDamTypes;
import Mk01.Enemy.MeatPuppet;
import Mk01.Weapons.ShotRecord;

public class SingleTickDamageProcToken extends SingleTickProcToken
{
	protected final crittableDamagePoint damage;
	
	public SingleTickDamageProcToken(ShotRecord inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, double inDamageValue, locDamTypes inDamageFlavor)
	{
		this(inOrigin, inTimeStarted, inProcFlavor, crittableDamagePoint.getInstance(inDamageFlavor, inDamageValue));
	}
	
	public SingleTickDamageProcToken(ShotRecord inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, double inDamageValue, locDamTypes inDamageFlavor, int inDamageCritLevel, double inDamageCritMult)
	{
		this(inOrigin, inTimeStarted, inProcFlavor, crittableDamagePoint.getInstance(inDamageFlavor, inDamageValue, inDamageCritLevel, inDamageCritMult));
	}
	
	public SingleTickDamageProcToken(ShotRecord inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, crittableDamagePoint inDamage)
	{
		super(inOrigin, inTimeStarted, inProcFlavor);
		this.damage = inDamage;
	}
	
	@Override
	public void tick_Internal(long currentTime, MeatPuppet target)
	{
		if(this.isFirstTick())
		{
			target.inflictDamage(currentTime, this.origin.getPartHit(), this.damage, this.getProcName(), "");
		}
	}
}
