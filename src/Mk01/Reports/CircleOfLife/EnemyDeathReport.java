package Mk01.Reports.CircleOfLife;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Mk01.Reports.Report;

public class EnemyDeathReport extends ExpirationReport
{
	public EnemyDeathReport(long inTime, String inEnemyId)
	{
		super(inTime, inEnemyId);
	}

	@Override
	protected CharList summarize_Internal()
	{
		final CharList result = new CharList();
		
		result.add(this.ExpiratorId);
		result.add(" has died!");
		
		return result;
	}
	
	@Override
	public int compareTo(Report other)
	{
		int result = super.compareTo(other);
		
		if(result == 0 && !(other instanceof EnemyDeathReport))
		{
			result = 1;
		}
		
		return result;
	}
}
