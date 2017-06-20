package Mk00.Control;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.AfflictionTypes;
import Enums.TickIntervals;
import Manifests.Enemy.Mk01.BodyPart;
import Manifests.Enemy.Mk01.BodyPart.AccuracyMapper;
import Mk00.Control.Manifests.EnemyManifest;
import Mk00.Control.Manifests.EnemyManifest.Enemies;
import Mk00.Control.Manifests.ModBuildManifest.PartialBuilds;
import Mk00.Control.Manifests.WeaponManifest;
import Mk00.Control.Manifests.WeaponManifest.Weapons;
import Mk00.Enemy.WeinerPuppet;
import Mk00.Reporting.Report;
import Mk00.Reporting.Report.ReportGeneral;
import Mk00.Reporting.ReportsManager;
import Mk00.Reporting.ReportsManager.ReportCatagories;
import Mk00.Weapons.ModFolio;
import Mk00.Weapons.basicWeapon;
import Random.XorShiftStar.Mk02.XorShiftStar;
import Random.XorShiftStar.Mk02.XorShiftStar.Generators;

public class Trial
{
	private final int numRuns;
	private final int runCyclesLimit;
	private final Weapons weaponChoice;
	private final basicWeapon weapon;
	private final ModFolio buildChoice;
	private final basicWeapon moddedWeapon;
	private final Enemies enemyChoice;
	private final WeinerPuppet enemyProgenitor;
	private final int enemyLevel;
	private final TrialModes trialMode;
	private final XorShiftStar diceProgenitor;

	private Trial(int inNumRuns, int inRunCyclesLimit, Weapons inWeaponChoice, ModFolio inBuildChoice, Enemies inEnemyChoice, int inEnemyLevel, boolean inEnemyGodMode_Health, boolean inEnemyGodMode_Armor, boolean inEnemyGodMode_Shield, TrialModes inTrialMode)
	{
		this.numRuns = inNumRuns;
		this.runCyclesLimit = inRunCyclesLimit;
		this.weaponChoice = inWeaponChoice;
		this.weapon = WeaponManifest.getWeapon(this.weaponChoice);
		this.buildChoice = inBuildChoice;
		this.moddedWeapon = basicWeapon.getModdedInstance(this.weapon, this.buildChoice);
		this.enemyChoice = inEnemyChoice;
		this.enemyProgenitor = EnemyManifest.getEnemy(inEnemyChoice, inEnemyLevel, inEnemyGodMode_Health, inEnemyGodMode_Armor, inEnemyGodMode_Shield);
		System.out.println(this.enemyProgenitor.summarizeCurrentState(-1).toString());
		this.enemyLevel = inEnemyLevel;
		this.trialMode = inTrialMode;
		this.diceProgenitor = XorShiftStar.getNewInstance(Generators.XorShiftStar1024);
	}

	/**
	 * TrialMode:
	 * numRuns
	 * 
	 * @return
	 */
	public Report generateInitialReport()
	{
		final CharList result = new CharList();

		result.add("\r\n");
		result.add("TrialMode: ");
		result.add(this.trialMode.toString());
		result.add("\r\n");
		result.add("Number of runs: ");
		result.add(Integer.toString(this.numRuns));
		result.add("\r\n");
		result.add("Maximum steps per run: ");
		result.add(Integer.toString(this.runCyclesLimit));
		result.add("\r\n");
		result.add("Weapon to be tested: ");
		result.add(this.weaponChoice.toString());
		result.add("\r\n");
		result.add("with base stats: ");
		result.add("\r\n\t");
		result.add(this.weapon.statsToCharList("\r\n\t", false, false, true), true);
		result.add("\r\n");
		result.add("Equipped with mods: ");
		result.add("\r\n\t");
		result.add(this.buildChoice.getModsAsCharList(), true);
		result.add("\r\n");
		result.add("Modded Weapon Stats: ");
		result.add("\r\n\t");
		result.add(this.moddedWeapon.statsToCharList("\r\n\t", false, false, true), true);
		result.add("\r\n");
		result.add("Progenitor RNG seed: {");

		final long[] seed = this.diceProgenitor.getSeed();
		for (int i = 0; i < seed.length; i++)
		{
			if (i != 0)
			{
				result.add(", ");
			}

			result.add(Long.toString(seed[i]));
		}

		result.add('}');

		return new ReportGeneral(-1, result);
	}
	
	public Report generateClosingReport(StatsBundle in)
	{
		final CharList result = new CharList();
		final String unit = this.trialMode.getTrialModeResultUnit();

		result.add('*', 10);
		result.add("Input Variables:");
		result.add("\r\n");
		result.add("Weapon: ");
		result.add(this.weaponChoice.toString());
		result.add("\r\n");
		result.add("Enemy: ");
		result.add(this.enemyChoice.toString());
		result.add('[');
		result.add(Integer.toString(this.enemyLevel));
		result.add(']');
		result.add("\r\n");
		result.add("Trial Mode: ");
		result.add(this.trialMode.toString());
		result.add("\r\n", 2);
		result.add('*', 10);
		result.add("AVERAGE RESULTS (in ");
		result.add(unit);
		result.add("):");
		result.add("\r\n");
		result.add(in.mainStat.toCharList("\r\n"), true);
		result.add("\r\n");
		result.add(StatHelper_Int.arrToCharList("maxConcurrentProcs", in.maxProcsActive, "\r\n"), true);
		result.add("\r\n");
		result.add(StatHelper_Int.arrToCharList("TokenTotals", in.tokenTotals, "\r\n"), true);
		result.add("\r\n");
		result.add(StatHelper_Dub.arrToCharList("ProcsPerSecond", in.procsPerSec, "\r\n"), true);
		result.add("\r\n");
		result.add(in.diag_numericCritPercent.toCharList("\r\n"), true);

		return new ReportGeneral(Long.MAX_VALUE, result);
	}

	public Report generateClosingReport(StatHelper_Dub inMainStat, StatHelper_Int[] inMaxProcs, StatHelper_Int[] inTokenTotals, StatHelper_Dub[] inProcsPerSec)
	{
		final CharList result = new CharList();
		final String unit = this.trialMode.getTrialModeResultUnit();

		result.add('*', 10);
		result.add("Input Variables:");
		result.add("\r\n");
		result.add("Weapon: ");
		result.add(this.weaponChoice.toString());
		result.add("\r\n");
		result.add("Enemy: ");
		result.add(this.enemyChoice.toString());
		result.add('[');
		result.add(Integer.toString(this.enemyLevel));
		result.add(']');
		result.add("\r\n");
		result.add("Trial Mode: ");
		result.add(this.trialMode.toString());
		result.add("\r\n", 2);
		result.add('*', 10);
		result.add("AVERAGE RESULTS (in ");
		result.add(unit);
		result.add("):");
		result.add("\r\n");
		result.add(inMainStat.toCharList("\r\n"), true);
		result.add("\r\n");
		result.add(StatHelper_Int.arrToCharList("maxConcurrentProcs", inMaxProcs, "\r\n"), true);
		result.add("\r\n");
		result.add(StatHelper_Int.arrToCharList("TokenTotals", inTokenTotals, "\r\n"), true);
		result.add("\r\n");
		result.add(StatHelper_Dub.arrToCharList("ProcsPerSecond", inProcsPerSec, "\r\n"), true);

		return new ReportGeneral(Long.MAX_VALUE, result);
	}

	public Report generateClosingReport(double min, double max, double average)
	{
		final CharList result = new CharList();

		result.add('*', 10);
		result.add("Input Variables:");
		result.add("\r\n");
		result.add("Weapon: ");
		result.add(this.weaponChoice.toString());
		result.add("\r\n");
		result.add("Enemy: ");
		result.add(this.enemyChoice.toString());
		result.add('[');
		result.add(Integer.toString(this.enemyLevel));
		result.add(']');
		result.add("\r\n");
		result.add("Trial Mode: ");
		result.add(this.trialMode.toString());
		result.add("\r\n", 2);
		result.add('*', 10);
		result.add("RESULTS (in ");
		result.add(this.trialMode.getTrialModeResultUnit());
		result.add("):");
		result.add("\r\n");
		result.add("Average: ");
		result.add(Double.toString(average));
		result.add("\r\n");
		result.add("Minimum: ");
		result.add(Double.toString(min));
		result.add(" (delta = ");
		result.add(Double.toString(average - min));
		result.add(')');
		result.add("\r\n");
		result.add("Maximum: ");
		result.add(Double.toString(max));
		result.add(" (delta = ");
		result.add(Double.toString(max - average));
		result.add(')');

		return new ReportGeneral(Long.MAX_VALUE, result);
	}

	public void runTrial()
	{
		// System.out.println(this.generateInitialReport().summarize(-1).toString());
		ReportsManager.reporty.publish(this.generateInitialReport(), ReportCatagories.MetaInfo);
		// ReportsManager.reporty.flush();
		final XorShiftStar[] diceArr = this.diceProgenitor.bootstrapNewInstances(this.numRuns);
		// AccuracyMapper accMap = AccuracyMapper.getInstance(this.enemyProgenitor.getBodyParts());
		AccuracyMapper accMap = AccuracyMapper.getInstance(BodyPart.getStandardTorso());

		/*
		final StatHelper_Dub mainStat = new StatHelper_Dub(this.trialMode.getTrialModeResultUnit());
		final StatHelper_Int[] maxProcsActive = new StatHelper_Int[AfflictionTypes.getNumTypes()];
		final StatHelper_Int[] tokenTotals = new StatHelper_Int[AfflictionTypes.getNumTypes()];
		final StatHelper_Dub[] procsPerSec = new StatHelper_Dub[AfflictionTypes.getNumTypes()];
		final StatHelper_Dub diag_numericCritPercent = new StatHelper_Dub("numericCritPercent");
		*/
		
		final StatsBundle statsTracked = new StatsBundle(this.trialMode);

		for (int i = 0; i < this.numRuns; i++)
		{
			final LoopManager loopy = this.initLoopy(diceArr[i]);
			final double curStat;

			switch (this.trialMode)
			{
				case findTTK:
				{
					System.out.println("StartState:\r\n" + loopy.summarizeEnemy() + "\r\n");

					loopy.runLoop(this.runCyclesLimit, accMap);
					curStat = loopy.getTime() / TickIntervals.OneSecond.getValueAsInt();

					break;
				}
				case findDPS_Sustained:
				{
					loopy.runLoop(this.runCyclesLimit, accMap);
					curStat = loopy.getAverageSustainedEffectiveDps();
					break;
				}
				default:
				{
					throw new IllegalArgumentException("Unknown trial mode: " + this.trialMode.toString());
				}
			}

			statsTracked.mainStat.addDataPoint(curStat);
			statsTracked.diag_numericCritPercent.addDataPoint(loopy.diag_getNumericCritPercent());

			int j = 0;
			final double curTime = ((double) loopy.getTime()) / ((double) TickIntervals.OneSecond.getValueAsInt());

			for (AfflictionTypes curType : AfflictionTypes.getAll())
			{
				if (curType != AfflictionTypes.BulletDamage && curType != AfflictionTypes.None)
				{
					final WeinerPuppet current = loopy.getEnemy();
					statsTracked.maxProcsActive[j].addDataPoint(current.getMaxProcsActive(curType));
					final int totalProcs = current.getTokenTotal(curType);
					statsTracked.tokenTotals[j].addDataPoint(totalProcs);
					statsTracked.procsPerSec[j].addDataPoint(((double) totalProcs) / curTime);
				}
				j++;
			}

			// loopy.getReporter().flush(System.out);
			ReportsManager.reporty.flush();
			// System.out.println(loopy.summarizeEnemy());
		}

		ReportsManager.reporty.publish(this.generateClosingReport(statsTracked), ReportCatagories.MetaInfo);
		//ReportsManager.reporty.publish(this.generateClosingReport(mainStat, maxProcsActive, tokenTotals, procsPerSec), ReportCatagories.MetaInfo);
		ReportsManager.reporty.flush();
		//System.out.println(this.generateClosingReport(mainStat, maxProcsActive, tokenTotals).summarize(Long.MAX_VALUE).toString());
		// System.out.println(this.generateClosingReport(mainStat.getMin(), mainStat.getMax(), mainStat.getCurrentMean()).summarize(1).toString());
	}

	// public LoopManager(Weapon inBaseWeapon, ModFolio inMods, WeinerPuppet inEnemy)
	private LoopManager initLoopy(XorShiftStar dice)
	{
		return new LoopManager(this.weapon, this.buildChoice, this.moddedWeapon, this.enemyProgenitor.cloneSelf(), dice);
	}

	public static Trial initTrial(TrialModes in)
	{
		return initTrial(in, Weapons.Braton, PartialBuilds.Empty.getPartialBuild(), Enemies.Grineer_Heavy_Gunner, 50);
	}

	public static Trial initTrial(TrialModes in, Weapons weaponChoice, ModFolio buildChoice, Enemies enemyChoice, int enemyLevel)
	{
		final TrialBuilder builder = new TrialBuilder();
		builder.setWeaponChoice(weaponChoice);
		builder.setBuildChoice(buildChoice);
		builder.setEnemyChoice(enemyChoice);
		builder.setEnemyLevel(enemyLevel);
		builder.setTrialMode(in);

		switch (in)
		{
			case findTTK:
			{
				builder.setNumRuns(500);
				builder.setRunCyclesLimit(5000);
				builder.setEnemyGodMode_Health(false);
				builder.setEnemyGodMode_Armor(false);
				builder.setEnemyGodMode_Shield(false);
				break;
			}
			case findDPS_Sustained:
			{
				builder.setNumRuns(100);
				// builder.setNumRuns(1);
				builder.setRunCyclesLimit(100000);
				// builder.setRunCyclesLimit(10);
				builder.setEnemyGodMode_Health(true);
				builder.setEnemyGodMode_Armor(false);
				builder.setEnemyGodMode_Shield(false);
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Unknown trialMode: " + in.toString());
			}
		}

		return builder.build();
	}

	public static Trial initTrial_Ttk()
	{
		final Weapons defaultWeapon = Weapons.Braton;
		final PartialBuilds defaultBuild = PartialBuilds.Empty;
		final Enemies defaultEnemy = Enemies.Grineer_Heavy_Gunner;
		final int defaultEnemyLevel = 50;

		return initTrial_Ttk(defaultWeapon, defaultBuild.getPartialBuild(), defaultEnemy, defaultEnemyLevel);
	}

	public static Trial initTrial_Ttk(Weapons weaponChoice, ModFolio buildChoice, Enemies enemyChoice, int enemyLevel)
	{
		final TrialBuilder builder = new TrialBuilder();

		builder.setNumRuns(500);
		builder.setRunCyclesLimit(5000);
		builder.setWeaponChoice(weaponChoice);
		builder.setBuildChoice(buildChoice);
		builder.setEnemyChoice(enemyChoice);
		builder.setEnemyLevel(enemyLevel);
		builder.setEnemyGodMode_Health(false);
		builder.setEnemyGodMode_Armor(false);
		builder.setEnemyGodMode_Shield(false);

		return builder.build();
	}

	public static Trial initTrial_sustainedDps(Weapons weaponChoice, ModFolio buildChoice, Enemies enemyChoice, int enemyLevel, boolean testShields)
	{
		final TrialBuilder builder = new TrialBuilder();

		builder.setNumRuns(50);
		// builder.setNumRuns(1);
		builder.setRunCyclesLimit(10000);
		builder.setWeaponChoice(weaponChoice);
		builder.setBuildChoice(buildChoice);
		builder.setEnemyChoice(enemyChoice);
		builder.setEnemyLevel(enemyLevel);
		builder.setEnemyGodMode_Health(true);
		builder.setEnemyGodMode_Armor(false);
		builder.setEnemyGodMode_Shield(testShields);

		return builder.build();
	}

	public static enum TrialModes
	{
		findTTK,
		findDPS_Sustained;

		public String getTrialModeResultUnit()
		{
			final String result;

			switch (this)
			{
				case findTTK:
				{
					result = "TTK";
					break;
				}
				case findDPS_Sustained:
				{
					result = "sustained DPS";
					break;
				}
				default:
				{
					throw new IllegalArgumentException("Unknown enum: " + this.toString());
				}
			}

			return result;
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
	
	private static class StatsBundle
	{
		final StatHelper_Dub mainStat;
		final StatHelper_Int[] maxProcsActive;
		final StatHelper_Int[] tokenTotals;
		final StatHelper_Dub[] procsPerSec;
		final StatHelper_Dub diag_numericCritPercent;

		public StatsBundle(TrialModes inMode)
		{
			mainStat = new StatHelper_Dub(inMode.getTrialModeResultUnit());
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
	}

	private static class TrialBuilder
	{
		private int numRuns;
		private int runCyclesLimit;
		private Weapons weaponChoice;
		private ModFolio buildChoice;
		private Enemies enemyChoice;
		private int enemyLevel;
		private boolean enemyGodMode_Health;
		private boolean enemyGodMode_Armor;
		private boolean enemyGodMode_Shield;
		private TrialModes trialMode;

		public TrialBuilder()
		{
			this.numRuns = 0;
			this.runCyclesLimit = 0;
			this.weaponChoice = null;
			this.buildChoice = null;
			this.enemyChoice = null;
			this.enemyLevel = 0;
			this.enemyGodMode_Health = false;
			this.enemyGodMode_Armor = false;
			this.enemyGodMode_Shield = false;
			this.trialMode = null;
		}

		public void setNumRuns(int inNumRuns)
		{
			this.numRuns = inNumRuns;
		}

		public void setRunCyclesLimit(int inRunCyclesLimit)
		{
			this.runCyclesLimit = inRunCyclesLimit;
		}

		public void setWeaponChoice(Weapons inWeaponChoice)
		{
			this.weaponChoice = inWeaponChoice;
		}

		public void setBuildChoice(ModFolio inBuildChoice)
		{
			this.buildChoice = inBuildChoice;
		}

		public void setEnemyChoice(Enemies inEnemyChoice)
		{
			this.enemyChoice = inEnemyChoice;
		}

		public void setEnemyLevel(int inEnemyLevel)
		{
			this.enemyLevel = inEnemyLevel;
		}

		public void setEnemyGodMode_Health(boolean inEnemyGodMode_Health)
		{
			// System.out.println("triggered");
			this.enemyGodMode_Health = inEnemyGodMode_Health;
		}

		public void setEnemyGodMode_Armor(boolean inEnemyGodMode_Armor)
		{
			this.enemyGodMode_Armor = inEnemyGodMode_Armor;
		}

		public void setEnemyGodMode_Shield(boolean inEnemyGodMode_Shield)
		{
			this.enemyGodMode_Shield = inEnemyGodMode_Shield;
		}

		public void setTrialMode(TrialModes in)
		{
			this.trialMode = in;
		}

		public Trial build()
		{
			return new Trial(this.numRuns, this.runCyclesLimit, this.weaponChoice, this.buildChoice, this.enemyChoice, this.enemyLevel, this.enemyGodMode_Health, this.enemyGodMode_Armor, this.enemyGodMode_Shield, this.trialMode);
		}
	}
}
