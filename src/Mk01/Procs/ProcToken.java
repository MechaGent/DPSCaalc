package Mk01.Procs;

import Enums.AfflictionTypes;
import Enums.TickIntervals;
import Mk01.Enemy.MeatPuppet;
import Mk01.Reports.Report;
import Mk01.Reports.CircleOfLife.ExpirationReport;
import Mk01.Reports.CircleOfLife.ForcedExpirationReport;
import Mk01.Reports.CircleOfLife.InitializationReport;
import Mk01.Weapons.ShotRecord;

public class ProcToken implements Comparable<ProcToken>
{
	private static final int[] typeCount = initTypeCount();

	protected final ShotRecord origin;
	protected final long timeStarted;
	protected final AfflictionTypes procFlavor;
	private final int maxNumTicks;
	protected final long procInterval;
	protected int currTick;
	private long nextTickTime;
	private final int Id;

	public ProcToken(ShotRecord inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks)
	{
		this(inOrigin, inTimeStarted, inProcFlavor, inMaxNumTicks, TickIntervals.OneSecond.getValueAsInt());
	}

	public ProcToken(ShotRecord inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval)
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

	public Report forceExpired(long currentTime, String reason)
	{
		this.currTick = this.maxNumTicks;
		this.nextTickTime = -1;
		return new ForcedExpirationReport(currentTime, this.procFlavor.toString(), reason);
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

	public void tick(long currentTime, MeatPuppet target)
	{
			final boolean isBullet = this.procFlavor == AfflictionTypes.BulletDamage;
			final boolean isSingleTick = this.maxNumTicks == 1;
			final boolean publishDeath = !isSingleTick;

			if (!isBullet)
			{
				if (this.isFirstTick())
				{
					target.getReporty().publish(new InitializationReport(currentTime, this.getProcName()));
				}
			}

			this.tick_Internal(currentTime, target);

			this.currTick++;
			this.nextTickTime += this.procInterval;

			if (publishDeath)
			{
				target.getReporty().publish(new ExpirationReport(currentTime, this.getProcName()));
			}
	}

	/**
	 * Override this immediately in subclasses
	 * 
	 * @param currentTime
	 * @param target
	 */
	protected void tick_Internal(long currentTime, MeatPuppet target)
	{
		// do nothing
	}

	public long getTimeStarted()
	{
		return this.timeStarted;
	}

	public long getEndTime()
	{
		return this.timeStarted + (this.maxNumTicks * this.procInterval);
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