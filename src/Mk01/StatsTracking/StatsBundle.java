package Mk01.StatsTracking;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.AfflictionTypes;
import Mk01.Control.Trial.TrialModes;

public class StatsBundle
{
	final StatHelper_Dub mainStat;
	final StatHelper_Int[] maxProcsActive;
	final StatHelper_Int[] tokenTotals;
	final StatHelper_Dub[] procsPerSec;
	final StatHelper_Dub diag_numericCritPercent;

	public StatsBundle(TrialModes inMode)
	{
		mainStat = new StatHelper_Dub(inMode.getResultUnit());
		maxProcsActive = new StatHelper_Int[AfflictionTypes.getNumTypes()];
		tokenTotals = new StatHelper_Int[AfflictionTypes.getNumTypes()];
		procsPerSec = new StatHelper_Dub[AfflictionTypes.getNumTypes()];
		diag_numericCritPercent = new StatHelper_Dub("diag_numericCritPercent");
		
		for (AfflictionTypes curType : AfflictionTypes.getAll())
		{
			final int index = curType.ordinal();
			final String name = curType.toString();

			maxProcsActive[index] = new StatHelper_Int(name);
			tokenTotals[index] = new StatHelper_Int(name);
			procsPerSec[index] = new StatHelper_Dub(name);
		}
	}
	
	private static class StatHelper_Dub
	{
		private final String name;
		private double RunningTotal;
		private int count;
		private double min;
		private double max;

		public StatHelper_Dub(String inName)
		{
			this.name = inName;
			this.RunningTotal = 0;
			this.min = Double.MAX_VALUE;
			this.max = Double.MIN_VALUE;
		}

		public void addDataPoint(double in)
		{
			this.RunningTotal += in;
			this.count++;

			if (in < min)
			{
				min = in;
			}

			if (in > max)
			{
				max = in;
			}
		}

		public double getCurrentMean()
		{
			return (this.RunningTotal / ((double) this.count));
		}

		public double getMin()
		{
			return this.min;
		}

		public double getMax()
		{
			return this.max;
		}

		public CharList toCharList(String delim)
		{
			final CharList result = new CharList();
			final double mean = this.getCurrentMean();

			result.add(this.name);
			result.add("_Mean: ");
			result.add(Double.toString(mean));
			result.add(delim);
			result.add(this.name);
			result.add("_Minimum: ");
			result.add(Double.toString(this.min));
			result.add(" (delta = ");
			result.add(Double.toString(mean - this.min));
			result.add(')');
			result.add(delim);
			result.add(this.name);
			result.add("_Maximum: ");
			result.add(Double.toString(this.max));
			result.add(" (delta = ");
			result.add(Double.toString(this.max - mean));
			result.add(')');

			return result;
		}
		
		public static CharList arrToCharList(String arrName, StatHelper_Dub[] in, String delim)
		{
			final CharList result = new CharList();
			result.add(arrName);
			result.add("_Stats: {");

			for (int i = 0; i < in.length; i++)
			{
				final StatHelper_Dub curr = in[i];

				if (curr.RunningTotal != 0)
				{
					result.add(delim);
					final double mean = curr.getCurrentMean();

					result.add(curr.name);
					result.add("_Mean: ");
					result.add(Double.toString(mean));
					result.add(delim);
					result.add(curr.name);
					result.add("_Minimum: ");
					result.add(Double.toString(curr.min));
					result.add(" (delta = ");
					result.add(Double.toString(mean - curr.min));
					result.add(')');
					result.add(delim);
					result.add(curr.name);
					result.add("_Maximum: ");
					result.add(Double.toString(curr.max));
					result.add(" (delta = ");
					result.add(Double.toString(curr.max - mean));
					result.add(')');
				}
			}

			result.add(delim);
			result.add('}');

			return result;
		}
	}
	
	private static class StatHelper_Int
	{
		private final String name;
		private int RunningTotal;
		private int count;
		private int min;
		private int max;

		public StatHelper_Int(String inName)
		{
			this.name = inName;
			this.RunningTotal = 0;
			this.min = Integer.MAX_VALUE;
			this.max = Integer.MIN_VALUE;
		}

		public void addDataPoint(int in)
		{
			this.RunningTotal += in;
			this.count++;

			if (in < min)
			{
				min = in;
			}

			if (in > max)
			{
				max = in;
			}
		}

		public double getCurrentMean()
		{
			return ((double) this.RunningTotal / ((double) this.count));
		}

		public int getMin()
		{
			return this.min;
		}

		public int getMax()
		{
			return this.max;
		}

		public CharList toCharList(String delim)
		{
			final CharList result = new CharList();
			final double mean = this.getCurrentMean();

			result.add(this.name);
			result.add("_Mean: ");
			result.add(Double.toString(mean));
			result.add(delim);
			result.add(this.name);
			result.add("_Minimum: ");
			result.add(Integer.toString(this.min));
			result.add(" (delta = ");
			result.add(Double.toString(mean - this.min));
			result.add(')');
			result.add(delim);
			result.add(this.name);
			result.add("_Maximum: ");
			result.add(Integer.toString(this.max));
			result.add(" (delta = ");
			result.add(Double.toString(this.max - mean));
			result.add(')');

			return result;
		}

		public static CharList arrToCharList(String arrName, StatHelper_Int[] in, String delim)
		{
			final CharList result = new CharList();
			result.add(arrName);
			result.add("_Stats: {");

			for (int i = 0; i < in.length; i++)
			{
				final StatHelper_Int curr = in[i];

				if (curr.RunningTotal != 0)
				{
					result.add(delim);
					final double mean = curr.getCurrentMean();

					result.add(curr.name);
					result.add("_Mean: ");
					result.add(Double.toString(mean));
					result.add(delim);
					result.add(curr.name);
					result.add("_Minimum: ");
					result.add(Integer.toString(curr.min));
					result.add(" (delta = ");
					result.add(Double.toString(mean - curr.min));
					result.add(')');
					result.add(delim);
					result.add(curr.name);
					result.add("_Maximum: ");
					result.add(Integer.toString(curr.max));
					result.add(" (delta = ");
					result.add(Double.toString(curr.max - mean));
					result.add(')');
				}
			}

			result.add(delim);
			result.add('}');

			return result;
		}
	}
}
