package Mk01.Procs;

import Enums.AfflictionTypes;
import Mk01.Enemy.MeatPuppet;
import Mk01.Weapons.ShotRecord;

public class BulletDamageProcToken extends SingleTickProcToken
{
	public BulletDamageProcToken(ShotRecord inOrigin, long inTimeStarted)
	{
		super(inOrigin, inTimeStarted, AfflictionTypes.BulletDamage);
	}

	@Override
	public void tick_Internal(long currentTime, MeatPuppet target)
	{
		target.inflictDamage(currentTime, this.origin, this.getProcName(), "");
	}

	@Override
	public int compareTo(ProcToken var2)
	{
		int result = super.compareTo(var2);

		if (result == 0)
		{
			switch (var2.getProcFlavor())
			{
				case Corroded:
				{
					result = 1;
					break;
				}
				case Magnetized:
				case Plagued:
				{
					result = -1;
					break;
				}
				default:
				{
					break;
				}
			}
		}
		
		return result;
	}
}
