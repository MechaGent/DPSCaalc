package Mk01.Procs;

import Enums.AfflictionTypes;
import Mk01.Enemy.MeatPuppet;
import Mk01.Enemy.baseMeatPuppet.EnemyDamageTargets;
import Mk01.Weapons.ShotRecord;

public class CorrodedProcToken extends SingleTickProcToken
{
	private final double corrodeBy;
	
	public CorrodedProcToken(ShotRecord inOrigin, long inTimeStarted, double inCorrodeBy)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.Corroded);
		this.corrodeBy = inCorrodeBy;
	}
	
	@Override
	public void tick_Internal(long currentTime, MeatPuppet target)
	{
		if(this.isFirstTick())
		{
			target.degradeHealthComponent(currentTime, EnemyDamageTargets.Armor, this.corrodeBy, this.getProcName(), "firstTick of Corrosive Proc");
		}
	}
}
