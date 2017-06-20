package Mk01.Reports.DirectStatManip;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Mk01.Enemy.baseMeatPuppet.EnemyDamageTargets;
import Mk01.Reports.Report;

public class additiveStatChangeReport extends Report
{
	protected final String EnemyId;
	protected final String deltaMethod;
	protected final double deltaValue;
	protected final EnemyDamageTargets target;
	protected final String InflictorId;
	protected final String cause;

	protected additiveStatChangeReport(long inTime, ReportCategories inCat, String inEnemyId, String inDeltaMethod, double inDeltaValue, EnemyDamageTargets inTarget, String inInflictorId, String inCause)
	{
		super(inTime, inCat);
		this.EnemyId = inEnemyId;
		this.deltaMethod = inDeltaMethod;
		this.deltaValue = inDeltaValue;
		this.target = inTarget;
		this.InflictorId = inInflictorId;
		this.cause = inCause;
	}
	
	public static additiveStatChangeReport getDamageReportInstance(long inTime, ReportCategories inCat, String inEnemyId, double inDamageValue, EnemyDamageTargets inTarget, String inInflictorId, String inCause)
	{
		return new additiveStatChangeReport(inTime, inCat, inEnemyId, "damage", inDamageValue, inTarget, inInflictorId, inCause);
	}
	
	public static additiveStatChangeReport getHealingReportInstance(long inTime, ReportCategories inCat, String inEnemyId, double inDamageValue, EnemyDamageTargets inTarget, String inInflictorId, String inCause)
	{
		return new additiveStatChangeReport(inTime, inCat, inEnemyId, "healing", inDamageValue, inTarget, inInflictorId, inCause);
	}

	@Override
	protected CharList summarize_Internal()
	{
		final CharList result = new CharList();
		
		result.add('[');
		result.add(this.EnemyId);
		result.add("]'s [");
		result.add(this.target.toString());
		result.add("] took [");
		result.add(Double.toString(this.deltaValue));
		result.add("] ");
		result.add(this.deltaMethod);
		result.add(" from [");
		result.add(this.InflictorId);
		result.add(']');
		
		if(this.cause.length() > 0)
		{
			result.add(", because [");
			result.add(this.cause);
			result.add(']');
		}
		
		return result;
	}
}
