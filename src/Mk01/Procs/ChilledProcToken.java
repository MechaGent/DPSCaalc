package Mk01.Procs;

import Enums.AfflictionTypes;
import Enums.Procs_StandardTicks;
import Enums.StatusProcEffectValues;
import Enums.TickIntervals;
import Mk01.Enemy.MeatPuppet;
import Mk01.Enemy.baseMeatPuppet.EnemyDamageTargets;
import Mk01.Weapons.ShotRecord;

public class ChilledProcToken extends statWarpingProcToken
{
	public ChilledProcToken(ShotRecord inOrigin, long inTimeStarted)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Chilled, Procs_StandardTicks.Chilled.getMaxTicks(), TickIntervals.OneSecond.getValueAsInt());
	}
	
	public ChilledProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Chilled, inMaxNumTicks, inProcInterval);
	}
	
	@Override
	protected void tick_Internal(long currentTime, MeatPuppet target)
	{
		if(this.isFirstTick() && !this.isRefresher)
		{
			this.restoreAmount = target.getSpeedMult_Movement() * StatusProcEffectValues.ChilledMovementSpeedReductionMult.getValue();
			target.damageHealthComponent(currentTime, EnemyDamageTargets.SpeedMult_Movement, this.restoreAmount, this.getProcName(), "firstTick of Chilled Proc");
		}
		else if(this.isLastTick())
		{
			target.healHealthComponent(currentTime, EnemyDamageTargets.SpeedMult_Movement, this.restoreAmount, this.getProcName(), "lastTick of Chilled Proc");
		}
	}
}
