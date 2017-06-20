package Mk01.Reports.CircleOfLife;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Mk01.Reports.Report;

public class ForcedExpirationReport extends Report
{
	private final String ExpiratorId;
	private final String cause;

	public ForcedExpirationReport(long inTime, String inExpiratorId, String inCause)
	{
		super(inTime, ReportCategories.DeathReport);
		this.ExpiratorId = inExpiratorId;
		this.cause = inCause;
	}

	@Override
	protected CharList summarize_Internal()
	{
		final CharList result = new CharList();

		result.add('[');
		result.add(this.ExpiratorId);
		result.add("] was offed, because ");
		result.add(this.cause);

		return result;
	}
}
