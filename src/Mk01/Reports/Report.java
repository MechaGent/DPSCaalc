package Mk01.Reports;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.TickIntervals;

public abstract class Report implements Comparable<Report>
{
	private static final Report NullReport = new NullReportClass();
	
	private static final int minTimeChars = 15;

	protected final long time;
	private final ReportCategories Cat;

	public Report(long inTime, ReportCategories inCat)
	{
		this.time = inTime;
		this.Cat = inCat;
	}

	protected abstract CharList summarize_Internal();

	public long getTime()
	{
		return this.time;
	}

	public ReportCategories getCatagory()
	{
		return this.Cat;
	}

	public CharList summarize(long lastTime)
	{
		final CharList result = this.summarize_Internal();

		final String timeString;

		if (lastTime != this.time)
		{
			timeString = this.timeToString();
		}
		else
		{
			timeString = "";
		}

		result.push(']');
		result.push(timeString);

		if (timeString.length() < minTimeChars)
		{
			result.push(' ', minTimeChars - timeString.length());
		}

		result.push('[');

		return result;
	}

	protected String timeToString()
	{
		return Double.toString(NanoToSeconds(this.time));
	}

	/**
	 * ensures natural time ordering
	 * 
	 * @param other
	 */
	@Override
	public int compareTo(Report other)
	{
		final long delta = this.time - other.time;

		if (delta < 0)
		{
			return -1;
		}
		else if (delta > 0)
		{
			return 1;
		}
		else if (delta == 0)
		{
			return 0;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	private static double NanoToSeconds(long inTime)
	{
		final double numer = inTime;
		final double denom = TickIntervals.OneSecond.getValueAsInt();

		return numer / denom;
	}
	
	public boolean isNull()
	{
		return this == NullReport;
	}
	
	public static Report getNull()
	{
		return NullReport;
	}
	
	private static class NullReportClass extends Report
	{
		public NullReportClass()
		{
			super(0, null);
		}

		@Override
		protected CharList summarize_Internal()
		{
			return new CharList("this is a nullReport");
		}
	}
	
	public static enum ReportCategories
	{
		TrialInitReport(true),
		TrialCloseReport(true),
		RunIntReport(true),
		RunCloseReport(true),
		DamageReport(true),
		GeneralReport(true),
		DeathReport(true),
		BirthReport(true),;

		private final boolean isAllowed;

		private ReportCategories(boolean inIsAllowed)
		{
			this.isAllowed = inIsAllowed;
		}

		public boolean isAllowed()
		{
			return this.isAllowed;
		}
	}
}
