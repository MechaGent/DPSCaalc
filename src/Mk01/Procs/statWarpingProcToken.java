package Mk01.Procs;

import Enums.AfflictionTypes;
import Mk01.Weapons.ShotRecord;

public abstract class statWarpingProcToken extends ProcToken
{
	protected double restoreAmount;
	protected boolean isRefresher;

	public statWarpingProcToken(ShotRecord inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval)
	{
		super(inOrigin, inTimeStarted, inProcFlavor, inMaxNumTicks, inProcInterval);
		this.restoreAmount = 0;
		this.isRefresher = false;
	}

	public void setThisProcAsRefresherFor(statWarpingProcToken in)
	{
		this.restoreAmount = in.restoreAmount;

		this.isRefresher = true;
	}
}