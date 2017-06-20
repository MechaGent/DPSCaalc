package Mk01.Reports.DirectStatManip;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Mk01.Enemy.baseMeatPuppet.EnemyDamageTargets;

public class multiplicativeStatChangeReport extends additiveStatChangeReport
{
	private final double originalStat;
	private final double deltaStat;
	
	private multiplicativeStatChangeReport(long inTime, ReportCategories inCat, String inEnemyId, String inDeltaMethod, double inOriginalStat, double inDeltaPercent, double inDeltaStat, EnemyDamageTargets inTarget, String inInflictorId, String inCause)
	{
		super(inTime, inCat, inEnemyId, inDeltaMethod, inDeltaPercent, inTarget, inInflictorId, inCause);
		this.originalStat = inOriginalStat;
		this.deltaStat = inDeltaStat;
	}
	
	public static multiplicativeStatChangeReport getDegradeInstance(long inTime, ReportCategories inCat, String inEnemyId, double inOriginalStat, double inDegradedPercent, double inDegradedStat, EnemyDamageTargets inTarget, String inInflictorId, String inCause)
	{
		return new multiplicativeStatChangeReport(inTime, inCat, inEnemyId, "degraded", inOriginalStat, inDegradedPercent, inDegradedStat, inTarget, inInflictorId, inCause);
	}
	
	public static multiplicativeStatChangeReport getStrengthenInstance(long inTime, ReportCategories inCat, String inEnemyId, double inOriginalStat, double inStrengthenedPercent, double inStrengthenedStat, EnemyDamageTargets inTarget, String inInflictorId, String inCause)
	{
		return new multiplicativeStatChangeReport(inTime, inCat, inEnemyId, "strengthened", inOriginalStat, inStrengthenedPercent, inStrengthenedStat, inTarget, inInflictorId, inCause);
	}
	
	@Override
	protected CharList summarize_Internal()
	{
		final CharList result = new CharList();
		
		result.add('[');
		result.add(this.EnemyId);
		result.add("]'s [");
		result.add(this.target.toString());
		result.add("] was ");
		result.add(this.deltaMethod);
		result.add(" from [");
		result.add(Double.toString(this.originalStat));
		result.add("] to [");
		result.add(Double.toString(this.deltaStat));
		result.add("], a change of [");
		result.add(Double.toString(this.deltaValue * 100.0));
		result.add("]%, from [");
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
