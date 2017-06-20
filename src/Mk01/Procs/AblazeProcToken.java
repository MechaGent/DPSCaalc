package Mk01.Procs;

import DamagePackets.Points.crittableDamagePoint;
import Enums.AfflictionTypes;
import Mk01.Enemy.MeatPuppet;
import Mk01.Weapons.ShotRecord;

public class AblazeProcToken extends TickingDamageProcToken
{
	public AblazeProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval, crittableDamagePoint inDamage)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Ablaze, inMaxNumTicks, inProcInterval, inDamage);
	}

	public double compareDamagesWith(AblazeProcToken other)
	{
		return this.getcritAdjDamage() - other.getcritAdjDamage();
	}
	
	private double getcritAdjDamage()
	{
		return MeatPuppet.calculateBodyLocalizedCritMult(this.origin.getPartHit(), this.damage.getCritLevel(), this.damage.getCritMult()) * this.damage.getValue();
	}
}
