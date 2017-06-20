package Mk01.Reports.CircleOfLife;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Mk01.Reports.Report;

public class InitializationReport extends Report
{
	private final String createdObjectName;

	public InitializationReport(long inTime, String inCreatedObjectName)
	{
		super(inTime, ReportCategories.BirthReport);
		this.createdObjectName = inCreatedObjectName;
	}

	@Override
	protected CharList summarize_Internal()
	{
		final CharList result = new CharList();

		result.add("[creation of new][");
		result.add(this.createdObjectName);
		result.add(']');

		return result;
	}
}
