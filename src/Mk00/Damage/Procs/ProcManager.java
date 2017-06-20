package Mk00.Damage.Procs;

import Enums.AfflictionTypes;
import Enums.TickIntervals;
import Enums.locDamTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Mk00.Damage.Packets.CrittableDamagePacket;
import Mk00.Damage.Packets.ScaledDamagePacket;
import Mk00.Enemy.MeatPuppet;
import Mk00.Enemy.basicMeatPuppet;
import Mk00.Weapons.ModFolio;
import Mk00.Weapons.basicWeapon.ShotReport;

public class ProcManager
{
	private static final double ColdProcApplicatorMult = 0.5;
	private static final double ColdProcDeApplicatorMult = 2.0;
	private static final double MagneticProcApplicatorMult = 0.25;
	private static final double MagneticProcDeApplicatorMult = 4.0;
	private static final double ViralProcApplicatorMult = 0.5;
	private static final double ViralProcDeApplicatorMult = 2.0;
	private static final double WeaknessProcApplicatorMult = 0.7;
	private static final double WeaknessProcDeApplicatorMult = 1.0 / WeaknessProcApplicatorMult;
	private static final int numTicks_FireAtTheDisco_Humanoid = 4;
	private static final int numTicks_FireAtTheDisco_Infested = 3;
	private static final int numTicks_FireAtTheDisco_MoasAndChargers = 2;

	private static final long tickInterval_Standard = TickIntervals.OneSecond.getValueAsInt();

	public static double getColdProcApplicatorMult()
	{
		return ColdProcApplicatorMult;
	}

	public static double getColdProcDeApplicatorMult()
	{
		return ColdProcDeApplicatorMult;
	}

	public static double getMagneticProcApplicatorMult()
	{
		return MagneticProcApplicatorMult;
	}

	public static double getMagneticProcDeApplicatorMult()
	{
		return MagneticProcDeApplicatorMult;
	}

	public static double getViralProcApplicatorMult()
	{
		return ViralProcApplicatorMult;
	}

	public static double getViralProcDeApplicatorMult()
	{
		return ViralProcDeApplicatorMult;
	}

	public static double getWeaknessProcApplicatorMult()
	{
		return WeaknessProcApplicatorMult;
	}

	public static double getWeaknessProcDeApplicatorMult()
	{
		return WeaknessProcDeApplicatorMult;
	}

	public static int getNumTicks_FireAtTheDisco_Humanoid()
	{
		return numTicks_FireAtTheDisco_Humanoid;
	}

	public static int getNumTicks_FireAtTheDisco_Infested()
	{
		return numTicks_FireAtTheDisco_Infested;
	}

	public static int getNumTicks_FireAtTheDisco_Moasandchargers()
	{
		return numTicks_FireAtTheDisco_MoasAndChargers;
	}

	/*
	 * static Factory Methods
	 */

	private static AfflictionTypes type_Blasted = AfflictionTypes.KnockedDown_fromBlast;
	private static int numTicks_Blasted = 7;
	private static long tickInterval_Blasted = tickInterval_Standard;

	public static BlastedProcToken getBlastedProcToken(ShotReport inOrigin, long inTimeStarted)
	{
		return new BlastedProcToken(inOrigin, inTimeStarted);
	}

	private static final int numTicks_StandUpFromKnockdown = 2;
	private static final long tickInterval_StandUpFromKnockdown = tickInterval_Standard;

	public static ProcToken getStandUpFromKnockdownProcToken(ShotReport in, long currentTime)
	{
		return new ProcToken(in, currentTime, AfflictionTypes.StandingUp_fromKnockdown, numTicks_StandUpFromKnockdown, tickInterval_StandUpFromKnockdown);
	}

	private static AfflictionTypes type_Blazing = AfflictionTypes.Ablaze;
	private static int numTicks_Blazing = 7;
	private static long tickInterval_Blazing = tickInterval_Standard;
	private static locDamTypes damType_Blazing = locDamTypes.Heat;

	public static TickingProcToken getBlazingProcToken(ShotReport inOrigin, long inTimeStarted, ScaledDamagePacket unModdedBaseDamage, ModFolio mods)
	{
		return new TickingProcToken(inOrigin, inTimeStarted, type_Blazing, numTicks_Blazing, tickInterval_Blazing, helper_getModdedHeatDamage(unModdedBaseDamage, mods, inOrigin.getPartHit(), inOrigin.getDamage()), damType_Blazing);
	}

	private static double helper_getModdedHeatDamage(ScaledDamagePacket unModdedBaseDamage, ModFolio mods, BodyPart partHit, CrittableDamagePacket report)
	{
		final double locCritMult = MeatPuppet.calculateBodyLocalizedCritMult(partHit, report.getCritLevel(), report.getCritMult());
		return helper_getModdedHeatDamage(unModdedBaseDamage.getScalar(), mods.getBaseDamageMult(), mods.getModdedBaseElemMults().getDamagePortion(damType_Blazing), locCritMult);
	}

	private static double helper_getModdedHeatDamage(double unModdedBaseDamage, double BaseMult, double heatMult, double bodyLocalizedCritMult)
	{
		return unModdedBaseDamage * (1.0 + BaseMult) * (1.0 + heatMult) * bodyLocalizedCritMult * 0.5;
	}

	private static AfflictionTypes type_Bleeding = AfflictionTypes.GreviouslyPapercut;
	private static int numTicks_Bleeding = 7;
	private static long tickInterval_Bleeding = tickInterval_Standard;
	private static locDamTypes damType_Bleeding = locDamTypes.Finisher;

	public static ProcToken getBleedingProcToken(ShotReport inOrigin, long inTimeStarted, ScaledDamagePacket unModdedBaseDamage, ModFolio mods)
	{
		return new TickingProcToken(inOrigin, inTimeStarted, type_Bleeding, numTicks_Bleeding, tickInterval_Bleeding, helper_getModdedBleedingDamage(unModdedBaseDamage, mods, inOrigin.getPartHit(), inOrigin.getDamage()), damType_Bleeding);
	}

	private static double helper_getModdedBleedingDamage(ScaledDamagePacket unModdedBaseDamage, ModFolio mods, BodyPart partHit, CrittableDamagePacket report)
	{
		final double locCritMult = MeatPuppet.calculateBodyLocalizedCritMult(partHit, report.getCritLevel(), report.getCritMult());
		return helper_getModdedBleedingDamage(unModdedBaseDamage.getScalar(), mods.getBaseDamageMult(), locCritMult);
	}

	private static double helper_getModdedBleedingDamage(double unModdedBaseDamage, double BaseMult, double bodyLocalizedCritMult)
	{
		return unModdedBaseDamage * (1.0 + BaseMult) * bodyLocalizedCritMult * 0.35;
	}

	private static AfflictionTypes type_BulletDamage = AfflictionTypes.BulletDamage;
	private static int numTicks_BulletDamage = 1;
	private static long tickInterval_BulletDamage = tickInterval_Standard;

	public static BulletDamageProcToken getBulletDamageProcToken(ShotReport in, long currentTime)
	{
		return new BulletDamageProcToken(in, currentTime);
	}

	private static AfflictionTypes type_Chilled = AfflictionTypes.Chilled;
	private static int numTicks_Chilled = 9;
	private static long tickInterval_Chilled = tickInterval_Standard;

	public static ChilledProcToken getChilledProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
	{
		return new ChilledProcToken(inOrigin, inTimeStarted, isRefresher);
	}

	private static AfflictionTypes type_Rads = AfflictionTypes.Confused_fromRadiation;
	private static int numTicks_Rads = 13;
	private static long tickInterval_Rads = tickInterval_Standard;

	public static ProcToken getConfusedFromRadsProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
	{
		return new RadsProcToken(inOrigin, inTimeStarted, isRefresher);
	}

	private static AfflictionTypes type_Corrosive = AfflictionTypes.Corroded;
	// private static int numTicks_Corrosive = 1;
	// private static long tickInterval_Corrosive = 0;
	private static double CorrosionPercent = 0.25;

	public static CorrosiveProcToken getCorrosiveProcToken(ShotReport inOrigin, long inTimeStarted)
	{
		return new CorrosiveProcToken(inOrigin, inTimeStarted);
	}

	private static AfflictionTypes type_Electric = AfflictionTypes.Damaged_fromElectricity;
	private static int numTicks_Electric = 7; // listed on wiki as both 3 and 6, WTH
	private static long tickInterval_Electric = tickInterval_Standard;
	private static locDamTypes damType_Electric = locDamTypes.Electricity;

	public static ElectricDamageProcToken getElectricDamageProcToken(ShotReport inOrigin, long inTimeStarted, ScaledDamagePacket unModdedBaseDamage, ModFolio mods)
	{
		return new ElectricDamageProcToken(inOrigin, inTimeStarted, helper_getModdedElectricDamage(unModdedBaseDamage, mods, inOrigin.getPartHit(), inOrigin.getDamage()));
	}

	private static double helper_getModdedElectricDamage(double unModdedBaseDamage, double BaseMult, double electricMult, double bodyLocalizedCritMult)
	{
		return unModdedBaseDamage * (1.0 + BaseMult) * (1.0 + electricMult) * bodyLocalizedCritMult * 0.5;
	}

	private static double helper_getModdedElectricDamage(ScaledDamagePacket unModdedBaseDamage, ModFolio mods, BodyPart partHit, CrittableDamagePacket report)
	{
		final double locCritMult = MeatPuppet.calculateBodyLocalizedCritMult(partHit, report.getCritLevel(), report.getCritMult());
		return helper_getModdedElectricDamage(unModdedBaseDamage.getScalar(), mods.getBaseDamageMult(), mods.getModdedBaseElemMults().getDamagePortion(damType_Electric), locCritMult);
	}

	private static AfflictionTypes type_Poisoned = AfflictionTypes.Poisoned;
	private static int numTicks_Poisoned = 9;
	private static long tickInterval_Poisoned = tickInterval_Standard;
	private static locDamTypes damType_Poisoned = locDamTypes.Toxin;

	public static ProcToken getPoisonedDamageProcToken(ShotReport inOrigin, long inTimeStarted, ScaledDamagePacket unModdedBaseDamage, ModFolio mods)
	{
		return new TickingProcToken(inOrigin, inTimeStarted, type_Poisoned, numTicks_Poisoned, tickInterval_Poisoned, helper_getModdedToxinDamage(unModdedBaseDamage, mods, inOrigin.getPartHit(), inOrigin.getDamage()), damType_Poisoned);
	}

	private static double helper_getModdedToxinDamage(double unModdedBaseDamage, double BaseMult, double toxinMult, double bodyLocalizedCritMult)
	{
		return unModdedBaseDamage * (1.0 + BaseMult) * (1.0 + toxinMult) * bodyLocalizedCritMult * 0.5;
	}

	private static double helper_getModdedToxinDamage(ScaledDamagePacket unModdedBaseDamage, ModFolio mods, BodyPart partHit, CrittableDamagePacket report)
	{
		final double locCritMult = MeatPuppet.calculateBodyLocalizedCritMult(partHit, report.getCritLevel(), report.getCritMult());
		return helper_getModdedToxinDamage(unModdedBaseDamage.getScalar(), mods.getBaseDamageMult(), mods.getModdedBaseElemMults().getDamagePortion(damType_Poisoned), locCritMult);
	}

	private static AfflictionTypes type_Farty = AfflictionTypes.Gassed;
	private static int numTicks_Farty = 1;
	private static long tickInterval_Farty = tickInterval_Standard;
	private static locDamTypes damType_Farty = locDamTypes.Toxin;

	public static FartyDamageProcToken getFartyDamageProcToken(ShotReport inOrigin, long inTimeStarted, ScaledDamagePacket unModdedBaseDamage, ModFolio mods)
	{
		return new FartyDamageProcToken(inOrigin, inTimeStarted, helper_getModdedFartyDamage(unModdedBaseDamage, mods, inOrigin.getPartHit(), inOrigin.getDamage()), mods.getModdedBaseElemMults().getDamagePortion(locDamTypes.Toxin));
	}

	private static double helper_getModdedFartyDamage(ScaledDamagePacket unModdedBaseDamage, ModFolio mods, BodyPart partHit, CrittableDamagePacket report)
	{
		final double locCritMult = MeatPuppet.calculateBodyLocalizedCritMult(partHit, report.getCritLevel(), report.getCritMult());
		return helper_getModdedFartyDamage(unModdedBaseDamage.getScalar(), mods.getBaseDamageMult(), mods.getModdedBaseElemMults().getDamagePortion(damType_Farty), locCritMult);
	}

	/**
	 * 
	 * @param unModdedBaseDamage
	 * @param BaseMult
	 * @param toxinMult
	 *            yes, toxinMult, not gasMult
	 * @param bodyLocalizedCritMult
	 * @return
	 */
	private static double helper_getModdedFartyDamage(double unModdedBaseDamage, double BaseMult, double toxinMult, double bodyLocalizedCritMult)
	{
		return unModdedBaseDamage * (1.0 + BaseMult) * (1.0 + toxinMult) * bodyLocalizedCritMult * 0.5;
	}

	private static AfflictionTypes type_StunnedFromFire = AfflictionTypes.Stunned_fromFire;
	// private static int numTicks_StunnedFromFire = 1;
	private static long tickInterval_StunnedFromFire = tickInterval_Standard;

	// ShotReport inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval
	public static ProcToken getFireAtTheDiscoProcToken(ShotReport in, long currentTime, int numTicks)
	{
		final ProcToken result = new ProcToken(in, currentTime, type_StunnedFromFire, numTicks, tickInterval_StunnedFromFire);

		//final double convertTime = ((double) currentTime) / ((double) TickIntervals.OneSecond.getValue());
		//System.out.println(" SEARCH triggered: " + result.getProcName() + " at time: " + convertTime);
		
		return result;
	}

	private static AfflictionTypes type_StaggeredFromImpact = AfflictionTypes.Stagger_fromImpact;
	// private static int numTicks_StaggeredFromImpact = 1;
	// private static long tickInterval_StaggeredFromImpact = tickInterval_Standard;

	// ShotReport inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval
	public static ProcToken getImpactStaggerProcToken(ShotReport in, long currentTime)
	{
		return new SingleTickProcToken(in, currentTime, type_StaggeredFromImpact);
	}

	private static AfflictionTypes type_Magnetized = AfflictionTypes.Magnetized;
	private static int numTicks_Magnetized = 5;
	private static long tickInterval_Magnetized = tickInterval_Standard;

	public static MagnetizedProcToken getMagnetizedProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
	{
		return new MagnetizedProcToken(inOrigin, inTimeStarted, isRefresher);
	}

	private static AfflictionTypes type_Plagued = AfflictionTypes.Plagued;
	private static int numTicks_Plagued = 7;
	private static long tickInterval_Plagued = tickInterval_Standard;

	public static PlaguedProcToken getPlaguedProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
	{
		return new PlaguedProcToken(inOrigin, inTimeStarted, isRefresher);
	}

	private static AfflictionTypes type_StunnedFromLightning = AfflictionTypes.Stunned_fromElectricity;
	private static int numTicks_StunnedFromLightning = 4;
	private static long tickInterval_StunnedFromLightning = tickInterval_Standard;

	public static ProcToken getStunnedFromLightningProcToken(ShotReport inOrigin, long inTimeStarted)
	{
		return new ProcToken(inOrigin, inTimeStarted, type_StunnedFromLightning, numTicks_StunnedFromLightning, tickInterval_StunnedFromLightning);
	}

	private static AfflictionTypes type_Weakened = AfflictionTypes.Weakness;
	private static int numTicks_Weakened = 7;
	private static long tickInterval_Weakened = tickInterval_Standard;

	public static WeakenedProcToken getWeakenedProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
	{
		return new WeakenedProcToken(inOrigin, inTimeStarted, isRefresher);
	}

	/*
	 * subClasses
	 */

	public static class SingleTickProcToken extends ProcToken
	{
		private SingleTickProcToken(ShotReport inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor)
		{
			super(inOrigin, inTimeStarted, inProcFlavor, 1, 0);
		}

		@Override
		protected void tick_Internal(long inCurrentTime, basicMeatPuppet inTarget)
		{
			if (this.isFirstTick())
			{
				this.process_OnFirstTick(inCurrentTime, inTarget);
			}
		}

		protected void process_OnFirstTick(long inCurrentTime, basicMeatPuppet inTarget)
		{
			// nothing
		}
	}

	public static class BlastedProcToken extends ProcToken
	{
		private final ProcToken subToken;

		private BlastedProcToken(ShotReport inOrigin, long inTimeStarted)
		{
			super(inOrigin, inTimeStarted, type_Blasted, numTicks_Blasted, tickInterval_Blasted);
			this.subToken = ProcManager.getStandUpFromKnockdownProcToken(inOrigin, inTimeStarted);
		}

		public ProcToken getSubsequentToken()
		{
			return this.subToken;
		}

		@Override
		public void forceExpired(long currentTime, String reason)
		{
			super.forceExpired(currentTime, reason);
			this.subToken.forceExpired(currentTime, "parent BlastProc was, too");
		}
	}

	public static class BulletDamageProcToken extends ProcToken
	{
		private BulletDamageProcToken(ShotReport inOrigin, long inTimeStarted)
		{
			super(inOrigin, inTimeStarted, type_BulletDamage, numTicks_BulletDamage, tickInterval_BulletDamage);
		}

		@Override
		protected void tick_Internal(long inCurrentTime, basicMeatPuppet inTarget)
		{
			inTarget.inflictDamage(inCurrentTime, this.origin.getPartHit(), this.origin.getDamage(), "Bullet");
		}

		@Override
		public int compareTo(ProcToken other)
		{
			int result = super.compareTo(other);

			if (result == 0)
			{
				switch (other.getProcFlavor())
				{
					case Corroded:
					{
						result = 1;
						break;
					}
					case Magnetized:
					{
						result = -1;
						break;
					}
					case Plagued:
					{
						result = -1;
						break;
					}
					default:
					{
						break;
					}
				}
			}

			return result;
		}
	}

	public static class TickingProcToken extends ProcToken
	{
		protected final double damage;
		protected final locDamTypes flavor;

		private TickingProcToken(ShotReport inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval, double inDamage, locDamTypes inFlavor)
		{
			super(inOrigin, inTimeStarted, inProcFlavor, inMaxNumTicks, inProcInterval);
			this.damage = inDamage;
			this.flavor = inFlavor;
		}

		public double getDamage()
		{
			return this.damage;
		}

		@Override
		protected void tick_Internal(long currentTime, basicMeatPuppet target)
		{
			target.inflictDamage(currentTime, this.damage, this.flavor, this.getProcName());
		}
	}

	public static abstract class statWarpingProcToken extends ProcToken
	{
		protected final boolean isRefresher;

		private statWarpingProcToken(ShotReport inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval, boolean inIsRefresher)
		{
			super(inOrigin, inTimeStarted, inProcFlavor, inMaxNumTicks, inProcInterval);
			this.isRefresher = inIsRefresher;
		}

		protected abstract void process_OnFirstTick(basicMeatPuppet inTarget);

		protected abstract void process_OnLastTick(basicMeatPuppet inTarget);

		@Override
		protected void tick_Internal(long inCurrentTime, basicMeatPuppet inTarget)
		{
			if (this.isFirstTick())
			{
				if (!this.isRefresher)
				{
					this.process_OnFirstTick(inTarget);
				}
			}
			else if (this.isLastTick())
			{
				this.process_OnLastTick(inTarget);
			}
		}
	}

	public static class ChilledProcToken extends statWarpingProcToken
	{
		private ChilledProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
		{
			super(inOrigin, inTimeStarted, type_Chilled, numTicks_Chilled, tickInterval_Chilled, isRefresher);
		}

		@Override
		protected void process_OnFirstTick(basicMeatPuppet inTarget)
		{
			inTarget.applyColdProc();
		}

		@Override
		protected void process_OnLastTick(basicMeatPuppet inTarget)
		{
			inTarget.unapplyColdProc();
		}
	}

	public static class MagnetizedProcToken extends statWarpingProcToken
	{
		private MagnetizedProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
		{
			super(inOrigin, inTimeStarted, type_Magnetized, numTicks_Magnetized, tickInterval_Magnetized, isRefresher);
		}

		@Override
		protected void process_OnFirstTick(basicMeatPuppet inTarget)
		{
			inTarget.applyMagneticProc();
		}

		@Override
		protected void process_OnLastTick(basicMeatPuppet inTarget)
		{
			inTarget.unapplyMagneticProc();
		}
	}

	public static class PlaguedProcToken extends statWarpingProcToken
	{
		private PlaguedProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
		{
			super(inOrigin, inTimeStarted, type_Plagued, numTicks_Plagued, tickInterval_Plagued, isRefresher);
		}

		@Override
		protected void process_OnFirstTick(basicMeatPuppet inTarget)
		{
			inTarget.applyMagneticProc();
		}

		@Override
		protected void process_OnLastTick(basicMeatPuppet inTarget)
		{
			inTarget.unapplyMagneticProc();
		}
	}

	public static class WeakenedProcToken extends statWarpingProcToken
	{
		private WeakenedProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
		{
			super(inOrigin, inTimeStarted, type_Weakened, numTicks_Weakened, tickInterval_Weakened, isRefresher);
		}

		@Override
		protected void process_OnFirstTick(basicMeatPuppet inTarget)
		{
			inTarget.applyMagneticProc();
		}

		@Override
		protected void process_OnLastTick(basicMeatPuppet inTarget)
		{
			inTarget.unapplyMagneticProc();
		}
	}

	public static class RadsProcToken extends statWarpingProcToken
	{
		private RadsProcToken(ShotReport inOrigin, long inTimeStarted, boolean isRefresher)
		{
			super(inOrigin, inTimeStarted, type_Rads, numTicks_Rads, tickInterval_Rads, isRefresher);
		}

		@Override
		protected void process_OnFirstTick(basicMeatPuppet inTarget)
		{
			inTarget.applyConfusion();
		}

		@Override
		protected void process_OnLastTick(basicMeatPuppet inTarget)
		{
			inTarget.unapplyConfusion();
		}
	}

	public static class CorrosiveProcToken extends SingleTickProcToken
	{
		private CorrosiveProcToken(ShotReport inOrigin, long inTimeStarted)
		{
			super(inOrigin, inTimeStarted, type_Corrosive);
		}

		@Override
		protected void process_OnFirstTick(long inCurrentTime, basicMeatPuppet inTarget)
		{
			inTarget.corrodeArmor(inCurrentTime, CorrosionPercent);
		}
	}

	/*
	 * private TickingProcToken(ShotReport inOrigin, long inTimeStarted, AfflictionTypes inProcFlavor, int inMaxNumTicks, long inProcInterval, double inDamage, locDamTypes inFlavor)
		{
	 */
	public static class ElectricDamageProcToken extends TickingProcToken
	{
		private ElectricDamageProcToken(ShotReport inOrigin, long inTimeStarted, double inDamage)
		{
			super(inOrigin, inTimeStarted, type_Electric, numTicks_Electric, tickInterval_Electric, inDamage, damType_Electric);
		}

		@Override
		protected void tick_Internal(long inCurrentTime, basicMeatPuppet inTarget)
		{
			if (this.isFirstTick())
			{
				inTarget.inflictDamage(inCurrentTime, this.damage, this.flavor, this.getProcName());
			}
		}
	}

	public static class PoisonedDamageProcToken extends TickingProcToken
	{
		private final String isChildProcOf;

		/**
		 * ONLY use this if you are absolutely sure moddedToxinDamage was calculated correctly
		 * <br>
		 * ie, this should only be used by Gas procs
		 * 
		 * @param inTimeStarted
		 * @param moddedToxinDamage
		 */
		private PoisonedDamageProcToken(ShotReport inOrigin, long inTimeStarted, double moddedToxinDamage, String inIsChildProcOf)
		{
			super(inOrigin, inTimeStarted, type_Poisoned, numTicks_Poisoned, tickInterval_Poisoned, moddedToxinDamage, damType_Poisoned);
			this.isChildProcOf = inIsChildProcOf;
		}

		private PoisonedDamageProcToken(ShotReport inOrigin, long inTimeStarted, double unModdedBaseDamage, double BaseMult, double toxinMult, double bodyLocalizedCritMult)
		{
			this(inOrigin, inTimeStarted, helper_getModdedToxinDamage(unModdedBaseDamage, BaseMult, toxinMult, bodyLocalizedCritMult), "");
		}

		@Override
		protected void tick_Internal(long currentTime, basicMeatPuppet target)
		{
			target.inflictDamage(currentTime, this.damage, damType_Poisoned, this.getCauseString());
		}

		private String getCauseString()
		{
			if (this.isChildProcOf.length() == 0)
			{
				return this.getProcName();
			}
			else
			{
				return this.getProcName() + " via " + this.isChildProcOf;
			}
		}

		@Override
		public int compareTo(ProcToken other)
		{
			int result = super.compareTo(other);

			if (result == 0)
			{
				switch (other.getProcFlavor())
				{
					case Gassed:
					{
						if (this.isFromTheSameOriginAs(other))
						{
							result = 1;
						}
						break;
					}
					default:
					{
						break;
					}
				}
			}

			return result;
		}
	}

	public static class FartyDamageProcToken extends TickingProcToken
	{
		private final double subsequentToxinDamage;

		private FartyDamageProcToken(ShotReport inOrigin, long inTimeStarted, double inDamage, double toxinMult)
		{
			super(inOrigin, inTimeStarted, type_Farty, numTicks_Farty, tickInterval_Farty, inDamage, damType_Farty);
			this.subsequentToxinDamage = helper_getSubsequentToxinDamage(this.damage, toxinMult);
		}

		private static double helper_getSubsequentToxinDamage(double initialGasDamage, double toxinMult)
		{
			return initialGasDamage * 0.5 * (1.0 + toxinMult);
		}

		@Override
		protected void tick_Internal(long inCurrentTime, basicMeatPuppet inTarget)
		{
			if (this.isFirstTick())
			{
				inTarget.inflictDamage(inCurrentTime, this.damage, this.flavor, this.getProcName());
			}
		}

		public PoisonedDamageProcToken getSubFart(long inCurrentTime)
		{
			return new PoisonedDamageProcToken(this.origin, inCurrentTime, this.subsequentToxinDamage, this.getProcName());
		}

		@Override
		public int compareTo(ProcToken other)
		{
			int result = super.compareTo(other);

			if (result == 0)
			{
				switch (other.getProcFlavor())
				{
					case Poisoned:
					{
						if (this.isFromTheSameOriginAs(other))
						{
							result = -1;
						}
						break;
					}
					default:
					{
						break;
					}
				}
			}

			return result;
		}
	}
}
