package Mk01.Control;

import CustomExceptions.UnhandledEnum.Mk01.UnhandledEnumException;
import DamagePackets.Packets.crittableDamagePacket;
import DamagePackets.Points.crittableDamagePoint;
import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.AfflictionTypes;
import Enums.Procs_StandardTicks;
import Enums.StatusProcEffectValues;
import Enums.TickIntervals;
import Enums.locDamTypes;
import Mk01.ReportsManager.Reporter;
import Mk01.Enemy.WeinerPuppet;
import Mk01.Procs.AblazeProcToken;
import Mk01.Procs.ChilledProcToken;
import Mk01.Procs.CorrodedProcToken;
import Mk01.Procs.GassedProcToken;
import Mk01.Procs.MagnetizedProcToken;
import Mk01.Procs.PlaguedProcToken;
import Mk01.Procs.ProcToken;
import Mk01.Procs.RadProcToken;
import Mk01.Procs.SingleTickDamageProcToken;
import Mk01.Procs.SingleTickProcToken;
import Mk01.Procs.TickingDamageProcToken;
import Mk01.Procs.ToxinProcToken;
import Mk01.Procs.WeaknessProcToken;
import Mk01.Weapons.GunWeaponInstance;
import Mk01.Weapons.ShotRecord;
import Mk01.Weapons.AccuracyMappers.AccuracyMapper;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class RunReferee
{
	private final WeinerPuppet targetEnemy;
	private final GunWeaponInstance gun;
	private final XorShiftStar dice;
	private final Reporter reporty;

	private long currentTime;

	public RunReferee(WeinerPuppet inTargetEnemy, GunWeaponInstance inGun, XorShiftStar inDice)
	{
		this.targetEnemy = inTargetEnemy;
		this.gun = inGun;
		this.dice = inDice;
		this.reporty = inTargetEnemy.getReporty();

		this.currentTime = 0;
	}

	public void runLoop(int limit, AccuracyMapper accMap)
	{
		boolean isProcStep = false;

		for (int i = 0; (i < limit) && this.targetEnemy.hasHealth(); i++)
		{
			if (!isProcStep)
			{
				final ShotRecord[] shots = this.gun.pullTrigger(this.currentTime, this.dice, accMap);

				for (ShotRecord shot : shots)
				{
					this.processShotRecord(shot);
					this.targetEnemy.processProcs(this.currentTime);
				}

				final long nextProcTime = this.targetEnemy.getNextProcSigTime();
				final long nextFireTime = this.gun.getNextFireTime();

				if (nextProcTime < nextFireTime)
				{
					this.currentTime = nextProcTime;
					isProcStep = true;
				}
				else
				{
					this.currentTime = nextFireTime;
				}
			}
			else
			{
				this.targetEnemy.processProcs(this.currentTime);

				final long nextProcTime = this.targetEnemy.getNextProcSigTime();
				final long nextFireTime = this.gun.getNextFireTime();

				if (nextProcTime < nextFireTime)
				{
					this.currentTime = nextProcTime;
				}
				else
				{
					this.currentTime = nextFireTime;
					isProcStep = false;
				}
			}
		}
	}

	private void processShotRecord(ShotRecord in)
	{
		if (in.hasProc())
		{
			for (locDamTypes curType : in.getProcFlavors())
			{
				this.processShotRecordProc(in, curType);
			}
		}

		this.targetEnemy.addProc(in.getBulletDamageProcToken(this.currentTime));
	}

	private void processShotRecordProc(ShotRecord in, locDamTypes flavor)
	{
		switch (flavor)
		{
			case Blast:
			{
				/*
				 * Implemented as:
				 * 	-Aoe(5 meters)
				 * 	-Knockdown(1 tick), then standUp(1 tick)
				 */

				final SingleLinkedList<WeinerPuppet> AoE = this.targetEnemy.getAllEnemiesWithin(5.0, true);

				for (WeinerPuppet Tony : AoE)
				{
					final ProcToken knockdown = new ProcToken(in, this.currentTime, AfflictionTypes.KnockedDown_fromBlast, Procs_StandardTicks.Blast_Knockdown.getMaxTicks());
					final ProcToken standUp = new ProcToken(in, knockdown.getEndTime(), AfflictionTypes.StandingUp_fromKnockdown, Procs_StandardTicks.Blast_Standup.getMaxTicks());
					Tony.addProc(knockdown);
					Tony.addProc(standUp);
				}

				break;
			}
			case Cold:
			{
				this.targetEnemy.addProc(new ChilledProcToken(in, this.currentTime));
				break;
			}
			case Corrosive:
			{
				this.targetEnemy.addProc(new CorrodedProcToken(in, this.currentTime, StatusProcEffectValues.CorrosiveDegradeMult.getValue()));
				break;
			}
			case Electricity:
			{
				final SingleLinkedList<WeinerPuppet> AoE = this.targetEnemy.getAllEnemiesWithin(5.0, true);

				final double damValue = this.gun.getElectricityProcDamageValue();
				final crittableDamagePacket shotDamage = in.getDamage();
				final int critLevel = shotDamage.getCritLevel();
				final double critMult = shotDamage.getCritMult();

				for (WeinerPuppet Tony : AoE)
				{
					final SingleTickDamageProcToken damToken = new SingleTickDamageProcToken(in, this.currentTime, AfflictionTypes.Damaged_fromElectricity, damValue, locDamTypes.Electricity, critLevel, critMult);
					final ProcToken stunToken = new ProcToken(in, this.currentTime, AfflictionTypes.Stunned_fromElectricity, Procs_StandardTicks.Stunned_FromElectricity.getMaxTicks());

					Tony.addProc(damToken);
					Tony.addProc(stunToken);
				}

				break;
			}
			case Finisher:
			{
				throw new UnsupportedOperationException("Tried to proc Finisher!");
			}
			case Gas:
			{
				final crittableDamagePacket shotDamage = in.getDamage();
				final int critLevel = shotDamage.getCritLevel();
				final double critMult = shotDamage.getCritMult();

				final double damValue_Gas = this.gun.getGasProcDamageValue();
				final double damValue_Toxin = this.gun.getToxinProcViaGasProcDamageValue(damValue_Gas);
				final SingleLinkedList<WeinerPuppet> AoE = this.targetEnemy.getAllEnemiesWithin(5.0, true);

				final GassedProcToken parent = new GassedProcToken(in, this.currentTime, damValue_Gas, critLevel, critMult);
				this.targetEnemy.addProc(parent);

				for (WeinerPuppet Tony : AoE)
				{
					final ToxinProcToken child = ToxinProcToken.getInstance(parent, Procs_StandardTicks.Poisoned.getMaxTicks(), damValue_Toxin, critLevel, critMult);
					Tony.addProc(child);
				}

				break;
			}
			case Heat:
			{
				final crittableDamagePacket shotDamage = in.getDamage();
				final int critLevel = shotDamage.getCritLevel();
				final double critMult = shotDamage.getCritMult();
				final double inValue = this.gun.getHeatProcDamageValue();
				final crittableDamagePoint dam = crittableDamagePoint.getInstance(locDamTypes.Heat, inValue, critLevel, critMult);

				final AblazeProcToken damToken = new AblazeProcToken(in, this.currentTime, Procs_StandardTicks.Ablaze.getMaxTicks(), TickIntervals.OneSecond.getValueAsInt(), dam);
				this.targetEnemy.addProc(damToken);

				break;
			}
			case Impact:
			{
				final SingleTickProcToken token = new SingleTickProcToken(in, this.currentTime, AfflictionTypes.Stagger_fromImpact);
				this.targetEnemy.addProc(token);

				break;
			}
			case Magnetic:
			{
				final MagnetizedProcToken token = new MagnetizedProcToken(in, this.currentTime, Procs_StandardTicks.Magnetized.getMaxTicks(), TickIntervals.OneSecond.getValueAsInt());
				this.targetEnemy.addProc(token);
				break;
			}
			case Puncture:
			{
				final WeaknessProcToken token = new WeaknessProcToken(in, this.currentTime, Procs_StandardTicks.Weakened.getMaxTicks(), TickIntervals.OneSecond.getValueAsInt());
				this.targetEnemy.addProc(token);
				break;
			}
			case Radiation:
			{
				final RadProcToken token = new RadProcToken(in, this.currentTime, Procs_StandardTicks.Confused.getMaxTicks(), TickIntervals.OneSecond.getValueAsInt());
				this.targetEnemy.addProc(token);
				break;
			}
			case Slash:
			{
				final crittableDamagePacket shotDamage = in.getDamage();
				final int critLevel = shotDamage.getCritLevel();
				final double critMult = shotDamage.getCritMult();
				final double inValue = this.gun.getSlashProcDamageValue();
				final crittableDamagePoint dam = crittableDamagePoint.getInstance(locDamTypes.Finisher, inValue, critLevel, critMult);

				final TickingDamageProcToken token = new TickingDamageProcToken(in, this.currentTime, AfflictionTypes.GreviouslyPapercut, Procs_StandardTicks.Bleeding.getMaxTicks(), TickIntervals.OneSecond.getValueAsInt(), dam);
				this.targetEnemy.addProc(token);
				break;
			}
			case Toxin:
			{
				final crittableDamagePacket shotDamage = in.getDamage();
				final int critLevel = shotDamage.getCritLevel();
				final double critMult = shotDamage.getCritMult();
				final double inValue = this.gun.getToxinProcDamageValue();
				final crittableDamagePoint dam = crittableDamagePoint.getInstance(locDamTypes.Toxin, inValue, critLevel, critMult);

				final TickingDamageProcToken token = new TickingDamageProcToken(in, this.currentTime, AfflictionTypes.GreviouslyPapercut, Procs_StandardTicks.Bleeding.getMaxTicks(), TickIntervals.OneSecond.getValueAsInt(), dam);
				this.targetEnemy.addProc(token);
				break;
			}
			case Viral:
			{
				final PlaguedProcToken token = new PlaguedProcToken(in, this.currentTime, Procs_StandardTicks.Plagued.getMaxTicks(), TickIntervals.OneSecond.getValueAsInt());
				this.targetEnemy.addProc(token);
				break;
			}
			default:
			{
				throw new UnhandledEnumException(flavor);
			}
		}
	}
}
