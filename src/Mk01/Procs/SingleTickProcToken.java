package Mk01.Procs;

import Enums.AfflictionTypes;
import Mk01.Weapons.ShotRecord;

public class SingleTickProcToken extends ProcToken
{
	public SingleTickProcToken(ShotRecord inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor)
	{
		super(inOrigin, inTimeStarted, inProcFlavor, 1, 0);
	}
}
