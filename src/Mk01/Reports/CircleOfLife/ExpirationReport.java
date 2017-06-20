package Mk01.Reports.CircleOfLife;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Mk01.Reports.Report;

public class ExpirationReport extends Report
{
	protected final String ExpiratorId;

	public ExpirationReport(long inTime, String inExpiratorId)
	{
		super(inTime, ReportCategories.DeathReport);
		this.ExpiratorId = inExpiratorId;
	}

	@Override
	protected CharList summarize_Internal()
	{
		final CharList result = new CharList();

		result.add('[');
		result.add(this.ExpiratorId);
		result.add("] died peacefully");

		return result;
	}
	
	@Override
	public int compareTo(Report other)
	{
		int result = super.compareTo(other);
		
		if(result == 0 && !(other instanceof ExpirationReport))
		{
			result = 1;
		}
		
		return result;
	}
}
