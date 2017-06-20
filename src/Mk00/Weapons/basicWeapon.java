package Mk00.Weapons;

import DataStructures.Linkages.CharList.Mk03.CharList;
import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.TickIntervals;
import Enums.locDamTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Manifests.Enemy.Mk01.BodyPart.AccuracyMapper;
import Mk00.Damage.Packets.CrittableDamagePacket;
import Mk00.Damage.Packets.ScaledDamagePacket;
import Mk00.Damage.Procs.ProcManager;
import Mk00.Damage.Procs.ProcManager.BulletDamageProcToken;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class basicWeapon
{
	private final long fireInterval;
	private final long reloadInterval;
	private final int maxMagSize;
	private final int bulletsConsumedPerTriggerPull;

	private final ScaledDamagePacket baseDamage;
	private final int baseCritLevel;
	private final double partialCritChance;
	private final double critMult;
	private final int baseMultishotLevel;
	private final double partialMultishotChance;
	private final ProcRoller Proccy;

	public basicWeapon(long inFireInterval, long inReloadInterval, int inMaxMagSize, ScaledDamagePacket inBaseDamage, double inCritChance, double inCritMult, double inProcChance)
	{
		this(inFireInterval, inReloadInterval, inMaxMagSize, 1, inBaseDamage, inCritChance, inCritMult, 1.0, inProcChance);
	}

	public basicWeapon(long inFireInterval, long inReloadInterval, int inMaxMagSize, int inBulletsConsumedPerTriggerPull, ScaledDamagePacket inBaseDamage, double inCritChance, double inCritMult, double inMultishotChance, double inProcChance)
	{
		this.fireInterval = inFireInterval;
		this.reloadInterval = inReloadInterval;
		this.maxMagSize = inMaxMagSize;
		this.bulletsConsumedPerTriggerPull = inBulletsConsumedPerTriggerPull;
		this.baseDamage = inBaseDamage;
		this.baseCritLevel = (int) inCritChance;
		this.partialCritChance = inCritChance - this.baseCritLevel;
		this.critMult = inCritMult;
		this.baseMultishotLevel = (int) inMultishotChance;
		this.partialMultishotChance = inMultishotChance - this.baseMultishotLevel;
		this.Proccy = new ProcRoller(inProcChance, this.baseDamage);
	}

	public static basicWeapon getModdedInstance(basicWeapon base, ModFolio mods)
	{
		final long fireInterval = (long) (base.fireInterval * (1.0 + mods.getFireRateMult()));
		final long reloadInterval = (long) (base.reloadInterval * (1.0 + mods.getReloadTimeMult()));
		final int maxMagSize = (int) (base.maxMagSize * (1.0 + mods.getMagSizeMult()));
		final int bulletsConsumedPerTriggerPull = base.bulletsConsumedPerTriggerPull;
		final ScaledDamagePacket damage = mods.getModdedPacket(base.baseDamage);
		final double critChance = (base.baseCritLevel + base.partialCritChance) * (1.0 + mods.getCritChanceMult());
		final double critMult = base.critMult * (1.0 + mods.getCritMultMult());
		final double multishotMult = (base.baseMultishotLevel + base.partialMultishotChance) * (1.0 + mods.getMultiShotMult());
		final double procChance = (base.Proccy.getProcChance() * (1.0 + mods.getProcChanceMult())) + mods.getProcChanceFlatAdditive();

		return new basicWeapon(fireInterval, reloadInterval, maxMagSize, bulletsConsumedPerTriggerPull, damage, critChance, critMult, multishotMult, procChance);
	}

	private ShotReport rollForDamage(XorShiftStar dice, AccuracyMapper acc)
	{
		final double diceRoll = dice.nextDouble(1.0);
		final int critLevel;

		if (diceRoll <= this.partialCritChance)
		{
			critLevel = this.baseCritLevel + 1;
			// System.out.println("HIGH_CRIT Rolled!");
		}
		else
		{
			critLevel = this.baseCritLevel;
		}

		final CrittableDamagePacket curDam = new CrittableDamagePacket(this.baseDamage, critLevel, this.critMult);
		final BodyPart partHit = acc.partHit(dice);
		final ShotReport result;

		if (this.Proccy.rollToSeeIfProcced(dice))
		{
			result = new ShotReport_withProc(curDam, partHit, this.Proccy.rollForProc(dice));
		}
		else
		{
			result = new ShotReport_noProc(curDam, partHit);
		}

		return result;
	}

	public WeaponState getWeaponStateTracker(long creationTime)
	{
		return new WeaponState(this, creationTime);
	}

	public ScaledDamagePacket getBaseDamage()
	{
		return this.baseDamage;
	}

	public static basicWeapon getDefaultWeapon()
	{
		/*
		 * final result = 1 / ShotsPerSec
		 * ShotsPerSec = ShotsPerMin / 60
		 * ShotsPerSec_Long = ShotsPerSec * ConversionConstant
		 */
		long inFireInterval = (long) ((60.0 / 575) * TickIntervals.OneSecond.getValueAsInt());
		long inReloadInterval = (long) (2.15 * TickIntervals.OneSecond.getValueAsInt());
		int inMaxMagSize = 75;

		final double damImpact = 1.75;
		final double damPuncture = 12.25;
		final double damSlash = 21.0;
		final double totalDamage = damImpact + damPuncture + damSlash;
		ScaledDamagePacket inBaseDamage = new ScaledDamagePacket(totalDamage);
		inBaseDamage.setDamagePortion(locDamTypes.Impact, damImpact / totalDamage);
		inBaseDamage.setDamagePortion(locDamTypes.Puncture, damPuncture / totalDamage);
		inBaseDamage.setDamagePortion(locDamTypes.Slash, damSlash / totalDamage);

		double inCritChance = 0.1;
		double inCritMult = 2.0;
		double inProcChance = 0.2;

		return new basicWeapon(inFireInterval, inReloadInterval, inMaxMagSize, inBaseDamage, inCritChance, inCritMult, inProcChance);
	}

	public CharList statsToCharList(String delim, boolean wantsIntervalsAsLongs, boolean wantsDamageAsProps, boolean wantsProbsAsPercents)
	{
		final CharList result = new CharList();
		final String fireIntervalString;
		final String reloadIntervalString;

		if (wantsIntervalsAsLongs)
		{
			fireIntervalString = Long.toString(this.fireInterval);
			reloadIntervalString = Long.toString(this.reloadInterval);
		}
		else
		{
			fireIntervalString = Double.toString(((double) this.fireInterval) / ((double) TickIntervals.OneSecond.getValueAsInt()));
			reloadIntervalString = Double.toString(((double) this.reloadInterval) / ((double) TickIntervals.OneSecond.getValueAsInt()));
		}

		result.add("Fire Interval: ");
		result.add(fireIntervalString);
		result.add(delim);
		result.add("Reload Interval: ");
		result.add(reloadIntervalString);
		result.add(delim);
		result.add("Mag Size: ");
		result.add(Integer.toString(this.maxMagSize));
		result.add(delim);
		result.add("Bullets Consumed Per Triggerpull: ");
		result.add(Integer.toString(this.bulletsConsumedPerTriggerPull));
		result.add(delim);
		result.add("*****Damage(");
		final double damSum = this.baseDamage.getScalar();
		result.add(Double.toString(damSum));
		result.add("): {");

		for (locDamTypes damType : locDamTypes.getAll())
		{

			double prop = this.baseDamage.getDamagePortion(damType);

			if (prop != 0)
			{
				result.add(delim);
				result.add(damType.toString());
				result.add('(');

				if (!wantsDamageAsProps)
				{
					prop *= damSum;
				}

				result.add(Double.toString(prop));
				result.add(')');
			}
		}

		result.add('}');
		result.add(delim);
		result.add("*****");
		result.add(delim);
		result.add("Crit Chance: ");
		result.add(probToCharList(this.baseCritLevel + this.partialCritChance, wantsProbsAsPercents), true);
		result.add(delim);
		result.add("Crit Multiplier: ");
		result.add(Double.toString(this.critMult));
		result.add(delim);
		result.add("Multishot Chance: ");
		result.add(probToCharList(this.baseMultishotLevel + this.partialMultishotChance, wantsProbsAsPercents), true);
		result.add(delim);
		result.add("*****Proc Chances: ");
		result.add(delim);
		result.add("Overall: ");
		
		final double totalOdds = this.Proccy.getTotalOdds();
		final double procChance = this.Proccy.getProcChance();
		
		result.add(probToCharList(procChance, wantsProbsAsPercents), true);
		result.add(delim);
		
		for (locDamTypes damType : locDamTypes.getAll())
		{
			double prop = this.Proccy.getOdds(damType);

			if (prop != 0)
			{
				double normedToOddsTotal = prop / totalOdds;
				double normedToProcChance = normedToOddsTotal * procChance;
				
				result.add(damType.toString());
				result.add('(');
				result.add("rel=");

				result.add(probToCharList(normedToOddsTotal, wantsProbsAsPercents), true);
				result.add(",abs=");
				result.add(probToCharList(normedToProcChance, wantsProbsAsPercents), true);

				//result.add(Double.toString(prop));
				result.add(')');
				result.add(delim);
			}
		}
		
		result.add("*****");

		return result;
	}

	private static CharList probToCharList(double prob, boolean wantsAsPercent)
	{
		final CharList result = new CharList();

		if (wantsAsPercent)
		{
			result.add(Double.toString(prob * 100.0));
			result.add('%');
		}
		else
		{
			result.add(Double.toString(prob));
		}

		return result;
	}

	public static class SuperPositionedWeapon
	{
		private final basicWeapon baseStats;
		private final basicWeapon moddedStats;
		private final ModFolio mods;

		public SuperPositionedWeapon(basicWeapon inBaseStats, basicWeapon inModdedStats, ModFolio inMods)
		{
			this.baseStats = inBaseStats;
			this.moddedStats = inModdedStats;
			this.mods = inMods;
		}

		public basicWeapon getBaseStats()
		{
			return this.baseStats;
		}

		public basicWeapon getModdedStats()
		{
			return this.moddedStats;
		}

		public ModFolio getMods()
		{
			return this.mods;
		}
	}

	public static class WeaponState
	{
		private final basicWeapon target;

		private int bulletsInMag;
		private long nextFireTime;

		/*
		 * Analytics
		 */
		private int numTriggerpulls;
		private int bulletsFired;	//includes multishot bullets
		private long sumFireTime;
		private int numCrits;

		public WeaponState(basicWeapon inTarget, long creationTime)
		{
			this.target = inTarget;
			this.nextFireTime = creationTime;
			this.bulletsInMag = this.target.maxMagSize;
			this.numTriggerpulls = 0;
			this.bulletsFired = 0;
			this.sumFireTime = 0;
			this.numCrits = 0;
		}

		public long getNextFireTime(long current)
		{
			return this.nextFireTime;
		}

		public int getNumTriggerpulls()
		{
			return this.numTriggerpulls;
		}
		
		public double getCurrentCritPercent()
		{
			return ((double) this.numCrits) / ((double) this.bulletsFired);
		}

		public SingleLinkedList<ShotReport> pullTrigger(long currentTime, XorShiftStar dice, AccuracyMapper accMap)
		{
			final double diceRoll = dice.nextDouble(1.0);
			final int numBullets;

			if (diceRoll <= this.target.partialMultishotChance)
			{
				numBullets = this.target.baseMultishotLevel + 1;
			}
			else
			{
				numBullets = this.target.baseMultishotLevel;
			}
			
			this.bulletsFired += numBullets;

			final SingleLinkedList<ShotReport> result = new SingleLinkedList<ShotReport>();

			for (int i = 0; i < numBullets; i++)
			{
				final ShotReport cur = this.target.rollForDamage(dice, accMap);
				
				if(cur.damage.getCritLevel() > this.target.baseCritLevel)
				{
					this.numCrits++;
				}
				
				result.add(cur);
			}

			this.bulletsInMag -= this.target.bulletsConsumedPerTriggerPull;
			this.numTriggerpulls += this.target.bulletsConsumedPerTriggerPull;

			if (this.bulletsInMag > 0)
			{
				this.nextFireTime = currentTime + this.target.fireInterval;
				this.sumFireTime += this.target.fireInterval;
			}
			else
			{
				this.bulletsInMag = this.target.maxMagSize;
				this.nextFireTime = currentTime + this.target.reloadInterval;
			}

			//ReportsManager.reporty.publish(new ReportDiagnostic(currentTime, "pelletsFired: " + numBullets));
			
			return result;
		}

		public int getBulletsFired()
		{
			return this.bulletsFired;
		}

		public long getSumFireTime()
		{
			return this.sumFireTime;
		}
		
		public int getNumCrits()
		{
			return this.numCrits;
		}
	}

	public static abstract class ShotReport
	{
		private final CrittableDamagePacket damage;
		private final BodyPart partHit;

		public ShotReport(CrittableDamagePacket inDamage, BodyPart inPartHit)
		{
			this.damage = inDamage;
			this.partHit = inPartHit;
		}

		public CrittableDamagePacket getDamage()
		{
			return this.damage;
		}

		public BodyPart getPartHit()
		{
			return this.partHit;
		}

		public abstract boolean hasProc();

		public abstract locDamTypes getProcFlavor();
		
		public CharList toCharList()
		{
			final CharList result = new CharList();
			
			result.add("Damage(");
			result.add(this.damage.toCharList(), true);
			result.add(')');
			result.add('\t');
			result.add("partHit(");
			result.add(this.partHit.toCharList(), true);
			result.add(')');
			
			return result;
		}

		public BulletDamageProcToken getBulletDamageProcToken(long currentTime)
		{
			return ProcManager.getBulletDamageProcToken(this, currentTime);
		}
	}

	private static class ShotReport_noProc extends ShotReport
	{
		public ShotReport_noProc(CrittableDamagePacket inDamage, BodyPart inPartHit)
		{
			super(inDamage, inPartHit);
		}

		@Override
		public boolean hasProc()
		{
			return false;
		}

		@Override
		public locDamTypes getProcFlavor()
		{
			return null;
		}
		
		@Override
		public CharList toCharList()
		{
			final CharList result = super.toCharList();
			
			result.push("ShotReport_NoProc: ");
			
			return result;
		}
	}

	private static class ShotReport_withProc extends ShotReport
	{
		public final locDamTypes procFlavor;

		public ShotReport_withProc(CrittableDamagePacket inDamage, BodyPart inPartHit, locDamTypes inProcFlavor)
		{
			super(inDamage, inPartHit);
			this.procFlavor = inProcFlavor;
		}

		@Override
		public boolean hasProc()
		{
			return true;
		}

		@Override
		public locDamTypes getProcFlavor()
		{
			return this.procFlavor;
		}

		@Override
		public CharList toCharList()
		{
			final CharList result = super.toCharList();
			
			result.push("ShotReport_Proc: ");
			result.add('\t');
			result.add("Proc(");
			result.add(this.procFlavor.toString());
			result.add(')');
			
			return result;
		}
	}
}
