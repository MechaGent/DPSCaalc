package Mk01.Procs;

import Enums.AfflictionTypes;
import Enums.StatusProcEffectValues;
import Mk01.Enemy.MeatPuppet;
import Mk01.Enemy.baseMeatPuppet.EnemyDamageTargets;
import Mk01.Weapons.ShotRecord;

public class MagnetizedProcToken extends statWarpingProcToken
{
	public MagnetizedProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Magnetized, inMaxNumTicks, inProcInterval);
	}
	
	@Override
	protected void tick_Internal(long currentTime, MeatPuppet target)
	{
		if(this.isFirstTick() && !this.isRefresher)
		{
			this.restoreAmount = target.getShield() * StatusProcEffectValues.MagneticShieldReductionMult.getValue();
			target.damageHealthComponent(currentTime, EnemyDamageTargets.Shield, this.restoreAmount, this.getProcName(), "firstTick of Magnetic Proc");
		}
		else if(this.isLastTick())
		{
			target.healHealthComponent(currentTime, EnemyDamageTargets.Shield, this.restoreAmount, this.getProcName(), "lastTick of Magnetic Proc");
		}
	}
}
