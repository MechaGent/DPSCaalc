package Mk00.Reporting;

import java.util.Comparator;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.TickIntervals;

public abstract class Report implements Comparable<Report>
{
	// private static final boolean showDiagnostics_Local = false;
	private static final int minTimeChars = 15;

	protected final long time;
	//protected long lastReportTime;

	public Report(long inTime)
	{
		this.time = inTime;
		//this.lastReportTime = Long.MIN_VALUE;
	}

	protected abstract CharList summarize_Internal();

	public long getTime()
	{
		return this.time;
	}

	public CharList summarize(long lastTime)
	{
		final CharList result = this.summarize_Internal();

		final String timeString;

		if (lastTime != this.time)
		{
			timeString = this.timeToString();
			//lastReportTime = this.time;
		}
		else
		{
			timeString = "";
		}

		//timeString = this.timeToString();

		result.push(']');
		result.push(timeString);

		if (timeString.length() < minTimeChars)
		{
			result.push(' ', minTimeChars - timeString.length());
		}

		result.push('[');

		/*
		if (lastTime != this.time)
		{
			result.push("\r\n");
			result.push(Double.toString(NanoToSeconds(this.time - lastTime)));
			result.push("\tStepsize: ");
			//lastReportTime = this.time;
		}
		*/

		return result;
	}

	protected String timeToString()
	{
		return Double.toString(NanoToSeconds(this.time));
		// return Long.toString(this.time);
	}

	/**
	 * ensures natural time ordering
	 * 
	 * @param other
	 */
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

		// publishDiagnosticReport(-1, showDiagnostics_Local, "ReportsManager.Report.NanoToSeconds", "long inTime", new DiagAction_NanoToSeconds(inTime, TickIntervals.OneSecond.getValue()));

		return numer / denom;
	}

	public static enum DamageSources
	{
		Weapon,
		Proc;
	}

	public static enum DamageTargets
	{
		Health,
		Armor,
		Shield;
	}

	public static enum Actions
	{
		Damaged,
		Healed,
		Gently_Caressed // neutral setting
		;
	}

	public static class ReportGeneral extends Report
	{
		private final CharList message;

		public ReportGeneral(long inTime, String inMessage)
		{
			this(inTime, new CharList(inMessage));
		}

		public ReportGeneral(long inTime, CharList inMessage)
		{
			super(inTime);
			this.message = inMessage;
		}

		@Override
		protected CharList summarize_Internal()
		{
			return this.message;
		}
	}

	public static class ReportDiagnostic extends ReportGeneral
	{
		public ReportDiagnostic(long inTime, CharList inMessage)
		{
			this(inTime, inMessage.toString());
		}

		public ReportDiagnostic(long inTime, String inMessage)
		{
			super(inTime, inMessage);
		}
		
		@Override
		protected CharList summarize_Internal()
		{
			final CharList result = super.summarize_Internal();
			result.push("[DIAG]");
			return result;
		}
		
		@Override
		public int compareTo(Report other)
		{
			final long delta;
			
			if(other instanceof ReportDiagnostic)
			{
				delta = this.time - other.time;
			}
			else
			{
				delta = (this.time + 1) - other.time;	//the +1 is to bump it to the end of the timestep
			}
			
			if(delta < 0)
			{
				return -1;
			}
			else if(delta > 0)
			{
				return 1;
			}
			else if(delta == 0)
			{
				return 0;
			}
			else
			{
				throw new IllegalArgumentException();
			}
		}
	}

	public static class ReportAction extends Report
	{
		private final String ActionTaker;
		private final Actions Action;
		private final String ActionReceiver;
		private final double magnitude;

		public ReportAction(long inTime, String inActionTaker, Actions inAction, String inActionReceiver, double inMagnitude)
		{
			super(inTime);
			this.ActionTaker = inActionTaker;
			this.Action = inAction;
			this.ActionReceiver = inActionReceiver;
			this.magnitude = inMagnitude;
		}

		@Override
		protected CharList summarize_Internal()
		{
			final CharList result = new CharList();

			result.add('[');
			result.add(this.ActionTaker);
			result.add("][");
			result.add(this.Action.toString());
			result.add("][");
			result.add(this.ActionReceiver);
			result.add("] for magnitude ");
			result.add(Double.toString(this.magnitude));

			return result;
		}
	}

	public static class ReportInitialization extends Report
	{
		private final String createdObjectName;

		public ReportInitialization(long inTime, String inCreatedObjectName)
		{
			super(inTime);
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

	public static class ReportForcedExpiration extends Report
	{
		private final String ObjectName;
		private final String reason;

		public ReportForcedExpiration(long inTime, String inObjectName, String inReason)
		{
			super(inTime);
			this.ObjectName = inObjectName;
			this.reason = inReason;
		}

		@Override
		protected CharList summarize_Internal()
		{
			final CharList result = new CharList();

			result.add('[');
			result.add(this.ObjectName);
			result.add("] was taken behind the shed, because ");
			result.add(this.reason);

			return result;
		}
	}

	public static class YoungestFirstComparator implements Comparator<Report>
	{
		@Override
		public int compare(Report inArg0, Report inArg1)
		{
			final long delta = inArg0.time - inArg1.time;

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
	}

	public static class OldestFirstComparator implements Comparator<Report>
	{
		@Override
		public int compare(Report inArg0, Report inArg1)
		{
			final long delta = inArg0.time - inArg1.time;

			if (delta < 0)
			{
				return 1;
			}
			else if (delta > 0)
			{
				return -1;
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
	}
}
