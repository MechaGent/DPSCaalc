package Mk01.Procs;

import Enums.AfflictionTypes;
import Mk01.Enemy.MeatPuppet;
import Mk01.Enemy.baseMeatPuppet.EnemyDamageTargets;
import Mk01.Weapons.ShotRecord;

public class RadProcToken extends statWarpingProcToken
{
	public RadProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Confused_fromRadiation, inMaxNumTicks, inProcInterval);
	}
	
	@Override
	protected void tick_Internal(long currentTime, MeatPuppet target)
	{
		if(this.isFirstTick() && !this.isRefresher)
		{
			this.restoreAmount = target.getConfusionLevel() * 1.0;
			target.damageHealthComponent(currentTime, EnemyDamageTargets.ConfusionLevel, this.restoreAmount, this.getProcName(), "firstTick of Radiation Proc");
		}
		else if(this.isLastTick())
		{
			target.healHealthComponent(currentTime, EnemyDamageTargets.ConfusionLevel, this.restoreAmount, this.getProcName(), "lastTick of Radiation Proc");
		}
	}
}
