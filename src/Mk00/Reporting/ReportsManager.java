package Mk00.Reporting;

import java.io.PrintStream;
import java.util.PriorityQueue;

public class ReportsManager
{
	public static final ReporterClass reporty = initReporty();

	private static ReporterClass initReporty()
	{
		return new ReporterClass(System.out);
	}

	public static final class ReporterClass
	{
		private static final int logBufferSize = 70;

		private final PriorityQueue<Report> reports;
		private final PrintStream printy;

		private long lastTime;

		private ReporterClass(PrintStream inPrinty)
		{
			this.reports = new PriorityQueue<Report>();
			this.printy = inPrinty;
			this.lastTime = Long.MIN_VALUE;
		}

		public void publish(Report in)
		{
			this.publish(in, ReportCatagories.RunLog);
		}

		public void publish(Report in, ReportCatagories cat)
		{
			if (cat.isAllowed())
			{
				this.reports.add(in);

				while (this.reports.size() > logBufferSize)
				{
					this.publish_Internal(this.reports.poll());
				}
			}
		}

		private void publish_Internal(Report curReport)
		{
			this.printy.println(curReport.summarize(this.lastTime).toString());
			this.lastTime = curReport.getTime();
		}

		public void flush()
		{
			while (!this.reports.isEmpty())
			{
				this.publish_Internal(this.reports.poll());
			}

			this.printy.flush();
		}
	}

	public static enum ReportCatagories
	{
		RunInfo(
				//true
		false
		),
		RunLog(
				//true
		false
		),
		MetaInfo(
				true
		//false
		);

		private final boolean isAllowed;

		private ReportCatagories(boolean inIsAllowed)
		{
			this.isAllowed = inIsAllowed;
		}

		public boolean isAllowed()
		{
			return this.isAllowed;
		}
	}

	/*
	public static class DumpingReporterClass
	{
		private final PriorityQueue<Report> reports;
	
		public DumpingReporterClass()
		{
			this.reports = new PriorityQueue<Report>();
		}
	
		public void publish(Report in)
		{
			this.reports.add(in);
		}
	
		public void write(PrintStream in)
		{
			long lastTime = -1;
	
			while (!this.reports.isEmpty())
			{
				final Report curReport = this.reports.poll();
				in.println(curReport.summarize(lastTime).toString());
				lastTime = curReport.getTime();
			}
		}
	
		public void flush(PrintStream in)
		{
			this.write(in);
			in.flush();
		}
	}
	*/
}
