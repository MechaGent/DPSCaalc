package Mk01.Procs;

import Enums.AfflictionTypes;
import Enums.StatusProcEffectValues;
import Mk01.Enemy.MeatPuppet;
import Mk01.Enemy.baseMeatPuppet.EnemyDamageTargets;
import Mk01.Weapons.ShotRecord;

public class WeaknessProcToken extends ProcToken
{
	public WeaknessProcToken(ShotRecord inOrigin, long inTimeStarted, int inMaxNumTicks, long inProcInterval)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Weakness, inMaxNumTicks, inProcInterval);
	}
	
	@Override
	protected void tick_Internal(long currentTime, MeatPuppet target)
	{
		if(this.isFirstTick())
		{
			final double lossAmount = target.getOutputDamageMult() * StatusProcEffectValues.WeaknessReductionMult.getValue();
			target.damageHealthComponent(currentTime, EnemyDamageTargets.outputDamageMult, lossAmount, this.getProcName(), "firstTick of Weakness Proc");
		}
		else if(this.isLastTick())
		{
			target.healHealthComponent(currentTime, EnemyDamageTargets.outputDamageMult, target.getOutputDamageMult() * (2.0 - StatusProcEffectValues.WeaknessReductionMult.getValue()), this.getProcName(), "lastTick of Weakness Proc");
		}
	}
}
