package Mk01.Procs;

import Enums.AfflictionTypes;
import Enums.StatusProcEffectValues;
import Mk01.Enemy.MeatPuppet;
import Mk01.Enemy.baseMeatPuppet.EnemyDamageTargets;
import Mk01.Weapons.ShotRecord;

public class PlaguedProcToken extends statWarpingProcToken
{
	public PlaguedProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Plagued, inMaxNumTicks, inProcInterval);
	}
	
	@Override
	protected void tick_Internal(long currentTime, MeatPuppet target)
	{
		if(this.isFirstTick() && !this.isRefresher)
		{
			this.restoreAmount = target.getHealth() * StatusProcEffectValues.ViralHealthReductionMult.getValue();
			target.damageHealthComponent(currentTime, EnemyDamageTargets.Health, this.restoreAmount, this.getProcName(), "firstTick of Viral Proc");
		}
		else if(this.isLastTick())
		{
			target.healHealthComponent(currentTime, EnemyDamageTargets.Health, this.restoreAmount, this.getProcName(), "lastTick of Viral Proc");
		}
	}
}
