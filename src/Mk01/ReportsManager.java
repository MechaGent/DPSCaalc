package Mk01;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.PriorityQueue;

import HandyStuff.FileParser;
import Mk01.Reports.Report;

public class ReportsManager
{
	private final Reporter[] reporters;
	private final File[] tempFiles;
	private final PrintStream fullReportStream;
	
	public ReportsManager(int numChannels, int logBufferSize, String fullReportStreamPath)
	{
		final Reporter[] locReporters = new Reporter[numChannels];
		final File[] locTempFiles = new File[numChannels];
		//System.out.println(Integer.toString(numChannels));
		
		for(int i = 0; i < numChannels; i++)
		{
			final File temp = createTempFile(i);
			//temp.deleteOnExit();
			
			locTempFiles[i] = temp;
			locReporters[i] = new Reporter(logBufferSize, temp);
		}
		
		this.reporters = locReporters;
		this.tempFiles = locTempFiles;
		
		if(fullReportStreamPath.length() > 0)
		{
			this.fullReportStream = FileParser.getPrintStream(fullReportStreamPath);
		}
		else
		{
			this.fullReportStream = System.out;
		}
	}
	
	private static File createTempFile(int channelNum)
	{
		final File temp;
		final String fileName = "runRecord_" + Integer.toString(channelNum) + "_";
		//System.out.println(fileName);
		
		try
		{
			temp = File.createTempFile(fileName, ".tmp");
			//temp.deleteOnExit();
		}
		catch(IOException e)
		{
			throw new IllegalArgumentException();
		}
		
		return temp;
	}
	
	public void commitRecordsToReportThenClose()
	{
		for(int i = 0; i < this.reporters.length; i++)
		{
			this.reporters[i].flushAndClose();
			final String record = getRecordAsString(this.tempFiles[i]);
			
			//System.out.println("record charLength: " + record.length());
			
			this.fullReportStream.println(record);
			this.tempFiles[i].delete();
			//System.out.println("tempFile " + i + " was deleted successfully: " + wasDeleted);
			this.tempFiles[i] = null;
		}
		
		this.fullReportStream.flush();
		this.fullReportStream.close();
	}
	
	public Reporter getReporterForChannel(int channel)
	{
		return this.reporters[channel];
	}
	
	/**
	 * abstracted this out for when I go back and convert the tempFiles to be written in binary rather than chars
	 * @param tempFileRecord
	 * @return
	 */
	private static String getRecordAsString(File tempFileRecord)
	{
		return FileParser.parseFileAsString(tempFileRecord);
	}

	public static class Reporter
	{
		private final int logBufferSize;
		private final PriorityQueue<Report> reports;
		private final PrintStream printy;

		private long lastTime;

		private Reporter(int inLogBufferSize, File inTemp)
		{
			this(inLogBufferSize, FileParser.getPrintStream(inTemp));
		}
		
		private Reporter(int inLogBufferSize, PrintStream inPrinty)
		{
			this.logBufferSize = inLogBufferSize;
			this.reports = new PriorityQueue<Report>();
			this.printy = inPrinty;
			this.lastTime = Long.MIN_VALUE;
		}

		public void publish(Report in)
		{
			//System.out.println("triggered");
			
			if (!in.isNull() && in.getCatagory().isAllowed())
			{
				
				this.reports.add(in);

				if (this.reports.size() > this.logBufferSize)
				{
					while (!this.reports.isEmpty())
					{
						this.publish_Internal(this.reports.poll());
					}
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
		
		public void flushAndClose()
		{
			this.flush();
			
			this.printy.close();
		}
	}
}
