package Mk00.Damage.Procs;

import Enums.AfflictionTypes;
import Mk00.Reporting.ReportsManager;
import Mk00.Reporting.Report.ReportInitialization;
import Mk00.Enemy.basicMeatPuppet;
import Mk00.Reporting.Report.ReportForcedExpiration;
import Mk00.Weapons.basicWeapon.ShotReport;

public class ProcToken implements Comparable<ProcToken>
{
	private static final int[] typeCount = initTypeCount();

	protected final ShotReport origin;
	protected final long timeStarted;
	private final AfflictionTypes procFlavor;
	private final int maxNumTicks;
	private final long procInterval;
	protected int currTick;
	private long nextTickTime;
	private final int Id;

	public ProcToken(ShotReport inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval)
	{
		this.origin = inOrigin;
		this.timeStarted = inTimeStarted;
		this.procFlavor = inProcFlavor;
		this.maxNumTicks = inMaxNumTicks;
		this.procInterval = inProcInterval;
		this.currTick = 0;
		this.nextTickTime = inTimeStarted;
		this.Id = typeCount[this.procFlavor.ordinal()]++;
	}

	private static int[] initTypeCount()
	{
		return new int[AfflictionTypes.getNumTypes()];
	}

	public boolean isFromTheSameOriginAs(ProcToken in)
	{
		return this.origin == in.origin;
	}

	public boolean isExpired()
	{
		return !(this.currTick < this.maxNumTicks);
	}

	public void forceExpired(long currentTime, String reason)
	{
		ReportsManager.reporty.publish(new ReportForcedExpiration(currentTime, this.procFlavor.toString(), reason));
		this.currTick = this.maxNumTicks;
		this.nextTickTime = -1;
	}

	public String getProcName()
	{
		return this.procFlavor.toString() + " Proc " + this.Id;
	}

	public boolean isFirstTick()
	{
		return this.currTick == 0;
	}

	public boolean isLastTick()
	{
		return this.currTick + 1 == this.maxNumTicks;
	}

	public void tick(long currentTime, basicMeatPuppet target)
	{
		final boolean isBullet = this.procFlavor == AfflictionTypes.BulletDamage;
		// final boolean isImpact = this.procFlavor == AfflictionTypes.Stagger_fromImpact;
		final boolean isSingleTick = this.maxNumTicks == 1;
		// final boolean publishDeath = !isImpact && !isBullet && this.isLastTick();
		final boolean publishDeath = !isSingleTick;
		// System.out.println("isBullet: " + isBullet + ", publishDeath: " + publishDeath + ", tick: " + this.currTick + " of " + this.maxNumTicks);

		if (!isBullet)
		{
			if (this.isFirstTick())
			{
				ReportsManager.reporty.publish(new ReportInitialization(currentTime, this.getProcName()));
			}
		}

		this.tick_Internal(currentTime, target);

		this.currTick++;
		this.nextTickTime += this.procInterval;

		if (publishDeath)
		{
			// System.out.println("TORGUE");
			ReportsManager.reporty.publish(new ReportForcedExpiration(currentTime, this.getProcName(), "this proc has ticked its last"));
		}

		// System.out.println("maxNumTicks: " + this.maxNumTicks + " ProcInterval: " + Long.toString(this.procInterval));
	}

	/**
	 * Override this immediately in subclasses
	 * 
	 * @param currentTime
	 * @param target
	 */
	protected void tick_Internal(long currentTime, basicMeatPuppet target)
	{
		// do nothing
	}

	public long getTimeStarted()
	{
		return this.timeStarted;
	}

	public AfflictionTypes getProcFlavor()
	{
		return this.procFlavor;
	}

	public int getMaxNumTicks()
	{
		return this.maxNumTicks;
	}

	public long getProcInterval()
	{
		return this.procInterval;
	}

	public int getCurrTick()
	{
		return this.currTick;
	}

	public long getNextTickTime()
	{
		return this.nextTickTime;
	}

	@Override
	public int compareTo(ProcToken other)
	{
		int result = this.compareTo_Vanilla(other);

		switch (this.procFlavor)
		{
			case KnockedDown_fromBlast:
			{
				if (this.isLastTick() && other.isFirstTick() && other.procFlavor == AfflictionTypes.StandingUp_fromKnockdown)
				{
					result = -1;
				}

				break;
			}
			case StandingUp_fromKnockdown:
			{
				if (this.isFirstTick() && other.isLastTick() && other.procFlavor == AfflictionTypes.KnockedDown_fromBlast)
				{
					result = 1;
				}
				break;
			}
			default:
			{
				break;
			}
		}

		return result;
	}

	private int compareTo_Vanilla(ProcToken other)
	{
		final long delta = this.nextTickTime - other.nextTickTime;

		if (delta < 0)
		{
			return -1;
		}
		else if (delta > 0)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public static int getCurrentTotal(AfflictionTypes in)
	{
		return typeCount[in.ordinal()];
	}

	public static int[] getCurrentTotals()
	{
		return typeCount;
	}
}
