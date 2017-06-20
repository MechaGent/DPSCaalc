package Mk01.Control;

import CustomExceptions.UnhandledEnum.Mk01.UnhandledEnumException;
import Manifests.Enemy.Mk01.AvailableEnemies;
import Manifests.Enemy.Mk01.BodyPart;
import Manifests.Enemy.Mk01.EnemyStats;
import Manifests.Mods.Mk01.ModFolio;
import Manifests.Weapons.Mk02.AvailableWeapons;
import Mk01.ReportsManager;
import Mk01.Enemy.WeinerPuppet;
import Mk01.Enemy.coreMeatPuppet;
import Mk01.ReportsManager.Reporter;
import Mk01.Weapons.GunWeaponInstance;
import Mk01.Weapons.AccuracyMappers.AccuracyMapper;
import Mk01.Weapons.AccuracyMappers.FlatAccuracyMapper;
import Mk01.Weapons.AccuracyMappers.FullBodyAccuracyMapper;
import Random.XorShiftStar.Mk02.XorShiftStar;
import Random.XorShiftStar.Mk02.XorShiftStar.Generators;

public class Trial
{
	private final TrialModes currentMode;
	private final TrialPreset presets;
	
	private final AvailableEnemies enemyChoice;
	private final int enemyLevel;
	private final AvailableWeapons weaponChoice;
	
	private final coreMeatPuppet enemyPrime;
	private final GunWeaponInstance gunPrime;
	private final ReportsManager reportyPrime;
	private final XorShiftStar[] AllDice;
	private final AccuracyMapper accMapPrime;
	
	private final static long[] dummySeed = new long[0];

	public Trial(TrialModes inMode, AvailableEnemies inEnemyChoice, int inEnemyLevel, AvailableWeapons inWeaponChoice, ModFolio inWeaponMods, String finalReportPath)
	{
		this(inMode, inEnemyChoice, inEnemyLevel, inWeaponChoice, inWeaponMods, finalReportPath, dummySeed);
	}
	
	public Trial(TrialModes inMode, AvailableEnemies inEnemyChoice, int inEnemyLevel, AvailableWeapons inWeaponChoice, ModFolio inWeaponMods, String finalReportPath, long[] seed)
	{
		this.currentMode = inMode;
		this.presets = this.currentMode.getTrialPreset();
		
		this.enemyChoice = inEnemyChoice;
		this.enemyLevel = inEnemyLevel;
		this.weaponChoice = inWeaponChoice;
		
		final boolean[] toggles = new boolean[]{this.presets.engageGodMode_Health, this.presets.engageGodMode_Armor, this.presets.engageGodMode_Shield};
		
		this.enemyPrime = new coreMeatPuppet(toggles, this.enemyChoice.getStats(), inEnemyLevel);
		
		this.gunPrime = new GunWeaponInstance(inWeaponChoice.getWeaponStats(), inWeaponMods);
		
		this.reportyPrime = new ReportsManager(this.presets.numRuns, this.presets.logBufferSize, finalReportPath);
		
		final XorShiftStar dicePrime;
		
		if(seed.length == 0)
		{
			dicePrime = XorShiftStar.getNewInstance(Generators.XorShiftStar1024);
		}
		else
		{
			dicePrime = XorShiftStar.getNewInstance(Generators.XorShiftStar1024, seed);
		}
		
		this.AllDice = dicePrime.bootstrapNewInstances(this.presets.numRuns);
		
		//this.accMapPrime = new FullBodyAccuracyMapper(this.enemyPrime.getBodyParts());
		this.accMapPrime = new FlatAccuracyMapper(BodyPart.getStandardTorso());
	}
	
	public void runTrial()
	{
		for(int i = 0; i < this.presets.numRuns; i++)
		{
			final RunReferee locRef = this.initLocalRef(i);
			locRef.runLoop(this.presets.maxNumCyclesPerRun, this.accMapPrime);
		}
	}
	
	public void collateReports()
	{
		this.reportyPrime.commitRecordsToReportThenClose();
	}
	
	private RunReferee initLocalRef(int i)
	{
		final WeinerPuppet locEnemy = new WeinerPuppet(this.enemyPrime, "Enemy_" + i, this.reportyPrime.getReporterForChannel(i));
		final GunWeaponInstance locGun = new GunWeaponInstance(this.gunPrime);
		return new RunReferee(locEnemy, locGun, this.AllDice[i]);
	}

	public static enum TrialModes
	{
		find_sustained_DPS_DegradableArmor("sustained DPS"),
		find_sustained_DPS_NonDegradableArmor("sustained DPS"),
		find_TTK("TTK");
		
		private final String resultUnit;
		
		private TrialModes(String inResultUnit)
		{
			this.resultUnit = inResultUnit;
		}

		public String getResultUnit()
		{
			return this.resultUnit;
		}

		public TrialPreset getTrialPreset()
		{
			final int numRuns;
			final int maxNumCyclesPerRun;
			final int logBufferSize;

			final boolean engageGodMode_Health;
			final boolean engageGodMode_Armor;
			final boolean engageGodMode_Shield;
			
			switch (this)
			{
				case find_TTK:
				{
					numRuns = 500;
					maxNumCyclesPerRun = 5000;
					logBufferSize = 70;

					engageGodMode_Health = false;
					engageGodMode_Armor = false;
					engageGodMode_Shield = false;
					
					break;
				}
				case find_sustained_DPS_DegradableArmor:
				{
					numRuns = 70;
					maxNumCyclesPerRun = 100000;
					logBufferSize = 70;

					engageGodMode_Health = true;
					engageGodMode_Armor = false;
					engageGodMode_Shield = false;
					
					break;
				}
				case find_sustained_DPS_NonDegradableArmor:
				{
					numRuns = 70;
					maxNumCyclesPerRun = 100000;
					logBufferSize = 70;

					engageGodMode_Health = true;
					engageGodMode_Armor = true;
					engageGodMode_Shield = false;
					
					break;
				}
				default:
				{
					throw new UnhandledEnumException(this);
				}
			}

			return new TrialPreset(numRuns, maxNumCyclesPerRun, logBufferSize, engageGodMode_Health, engageGodMode_Armor, engageGodMode_Shield);
		}
	}

	private static class TrialPreset
	{
		private final int numRuns;
		private final int maxNumCyclesPerRun;
		private final int logBufferSize;

		private final boolean engageGodMode_Health;
		private final boolean engageGodMode_Armor;
		private final boolean engageGodMode_Shield;
		
		public TrialPreset(int inNumRuns, int inMaxNumCyclesPerRun, int inLogBufferSize, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield)
		{
			this.numRuns = inNumRuns;
			this.maxNumCyclesPerRun = inMaxNumCyclesPerRun;
			this.logBufferSize = inLogBufferSize;
			this.engageGodMode_Health = inEngageGodMode_Health;
			this.engageGodMode_Armor = inEngageGodMode_Armor;
			this.engageGodMode_Shield = inEngageGodMode_Shield;
		}
	}
}
