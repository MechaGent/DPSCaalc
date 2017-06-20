package Mk01.Procs;

import DamagePackets.Points.crittableDamagePoint;
import Enums.AfflictionTypes;
import Mk01.Enemy.MeatPuppet;
import Mk01.Weapons.ShotRecord;

public class TickingDamageProcToken extends ProcToken
{
	protected final crittableDamagePoint damage;
	
	public TickingDamageProcToken(ShotRecord inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval, crittableDamagePoint inDamage)
	{
		super(inOrigin, inTimeStarted, inProcFlavor, inMaxNumTicks, inProcInterval);
		this.damage = inDamage;
	}

	public crittableDamagePoint getDamage()
	{
		return this.damage;
	}
	
	@Override
	protected void tick_Internal(long currentTime, MeatPuppet target)
	{
		target.inflictDamage(currentTime, this.origin.getPartHit(), this.damage, this.getProcName(), "");
	}
}
