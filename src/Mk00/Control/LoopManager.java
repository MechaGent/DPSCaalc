package Mk00.Control;

import DataStructures.Linkages.CharList.Mk03.CharList;
import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.AfflictionTypes;
import Enums.TickIntervals;
import Manifests.Enemy.Mk01.BodyPart.AccuracyMapper;
import Mk00.Damage.Procs.ProcManager;
import Mk00.Damage.Procs.ProcManager.BlastedProcToken;
import Mk00.Damage.Procs.ProcManager.FartyDamageProcToken;
import Mk00.Damage.Procs.ProcToken;
import Mk00.Enemy.WeinerPuppet;
import Mk00.Reporting.ReportsManager;
import Mk00.Reporting.Report.ReportGeneral;
import Mk00.Weapons.ModFolio;
import Mk00.Weapons.basicWeapon;
import Mk00.Weapons.basicWeapon.ShotReport;
import Mk00.Weapons.basicWeapon.WeaponState;
//import MonteCarloDPSCaalc.Mk04_WarframeHardcoded_Working.Reporting.Report.ReportDiagnostic;
import Random.XorShiftStar.Mk02.XorShiftStar;
import Random.XorShiftStar.Mk02.XorShiftStar.Generators;

public class LoopManager
{
	private final basicWeapon testWeap;
	private final ModFolio mods;
	private final basicWeapon moddedWeapon;
	private final WeaponState moddedWeaponState;
	private final WeinerPuppet enemy;
	private final XorShiftStar dice;
	// private final DumpingReporterClass reporty;

	private long currentTime;

	public LoopManager(basicWeapon inBaseWeapon, ModFolio inMods, WeinerPuppet inEnemy)
	{
		this(inBaseWeapon, inMods, basicWeapon.getModdedInstance(inBaseWeapon, inMods), inEnemy);
	}

	public LoopManager(basicWeapon inBaseWeapon, ModFolio inMods, basicWeapon inModdedWeapon, WeinerPuppet inEnemy)
	{
		this(inBaseWeapon, inMods, inModdedWeapon, inEnemy, XorShiftStar.getNewInstance(Generators.XorShiftStar1024));
	}

	public LoopManager(basicWeapon inBaseWeapon, ModFolio inMods, basicWeapon inModdedWeapon, WeinerPuppet inEnemy, XorShiftStar inDice)
	{
		this.testWeap = inBaseWeapon;
		this.mods = inMods;
		this.moddedWeapon = inModdedWeapon;
		this.moddedWeaponState = this.moddedWeapon.getWeaponStateTracker(0);
		this.enemy = inEnemy;
		this.currentTime = 0;
		this.dice = inDice;
		// this.reporty = inReporty;
	}

	public String summarizeEnemy()
	{
		return this.enemy.summarizeCurrentState(this.currentTime).toString();
	}

	public long getTime()
	{
		return this.currentTime;
	}
	
	public WeinerPuppet getEnemy()
	{
		return this.enemy;
	}
	
	public double diag_getNumericCritPercent()
	{
		return this.moddedWeaponState.getCurrentCritPercent();
	}

	/*
	public DumpingReporterClass getReporter()
	{
		return this.reporty;
	}
	*/

	public CharList analysisToCharList()
	{
		final CharList result = new CharList();

		final int totalTriggerpulls = this.moddedWeaponState.getBulletsFired();
		// final int totalBulletsFired = ProcToken.getCurrentTotal(AfflictionTypes.BulletDamage);
		final double totalFireTime = (double) (((double) this.currentTime) / ((double) TickIntervals.OneSecond.getValueAsInt()));
		final double totalDamageDone = this.enemy.getTotalDamageMagnitude();
		final double AveSusEffDps = totalDamageDone / totalFireTime;

		result.add("*****\r\n");
		result.add(this.summarizeEnemy());
		result.add("\r\nTotal Triggerpulls: ");
		result.add(Integer.toString(totalTriggerpulls));
		result.add("\r\nToken Totals:");

		for (AfflictionTypes curType : AfflictionTypes.getAll())
		{
			final int total = ProcToken.getCurrentTotal(curType);

			if (total != 0)
			{
				result.add('\t');
				result.add(curType.toString());
				result.add('(');
				result.add(Integer.toString(total));
				result.add(')');
			}
		}

		result.add("\r\nTotal FireTime: ");
		result.add(Double.toString(totalFireTime));
		result.add("\r\nTotal Damage Done: ");
		result.add(Double.toString(totalDamageDone));
		result.add("\r\nAverage Sustained Effective DPS: ");
		result.add(Double.toString(AveSusEffDps));

		return result;
	}
	
	public double getAverageSustainedEffectiveDps(double totalFireTime, double totalDamageDone)
	{
		return totalDamageDone / totalFireTime;
	}
	
	public double getTotalFireTime()
	{
		return (double) (((double) this.currentTime) / ((double) TickIntervals.OneSecond.getValueAsInt()));
	}
	
	public double getTotalDamageDone()
	{
		return this.enemy.getTotalDamageMagnitude();
	}

	public double getAverageSustainedEffectiveDps()
	{
		final double totalFireTime = this.getTotalFireTime();
		final double totalDamageDone = this.getTotalDamageDone();
		
		return this.getAverageSustainedEffectiveDps(totalFireTime, totalDamageDone);
	}
	
	public int getNumberOfTriggerpulls()
	{
		return this.moddedWeaponState.getBulletsFired();
	}

	public void runLoop(int limit)
	{
		final AccuracyMapper accMap = AccuracyMapper.getInstance(this.enemy.getBodyParts());
		this.runLoop(limit, accMap);
	}

	public void runLoop(int limit, AccuracyMapper accMap)
	{
		// this.reporty.publish(new ReportGeneral(this.currentTime, "**********Starting Loop"));
		ReportsManager.reporty.publish(new ReportGeneral(this.currentTime, "\r\n**********Starting Loop\r\n"));

		int i = 0;
		boolean isProcStep = false;

		for (i = 0; i < limit && this.enemy.stillLives(); i++)
		{
			if (!isProcStep)
			{
				final SingleLinkedList<ShotReport> curShots = this.moddedWeaponState.pullTrigger(this.currentTime, this.dice, accMap);
				long nextWeapShootTime = -2;
				long nextProcTime = -2;

				for (ShotReport curReport : curShots)
				{
					// ReportsManager.reporty.publish(new ReportDiagnostic(this.currentTime, curReport.toCharList()));
					this.handleShotReport(curReport);
					this.enemy.processProcs(this.currentTime);
				}

				nextWeapShootTime = this.moddedWeaponState.getNextFireTime(this.currentTime);
				nextProcTime = this.enemy.getNextProcSigTime();

				if (nextProcTime < nextWeapShootTime)
				{
					this.currentTime = nextProcTime;
					isProcStep = true;
				}
				else
				{
					this.currentTime = nextWeapShootTime;
				}
			}
			else
			{
				this.enemy.processProcs(this.currentTime);

				final long nextWeapShootTime = this.moddedWeaponState.getNextFireTime(this.currentTime);
				final long nextProcTime = this.enemy.getNextProcSigTime();

				if (nextProcTime < nextWeapShootTime)
				{
					this.currentTime = nextProcTime;
				}
				else
				{
					this.currentTime = nextWeapShootTime;
					isProcStep = false;
				}
			}
		}

		ReportsManager.reporty.flush();
		ReportsManager.reporty.publish(new ReportGeneral(this.currentTime, "**********Ending Loop\r\nBullets fired: " + this.moddedWeaponState.getBulletsFired()));

		// System.out.println("numCycles: " + i);
	}

	private void handleShotReport(ShotReport in)
	{
		if (in.hasProc())
		{
			this.handleShotReport_Proc(in);
		}

		this.enemy.addProc(in.getBulletDamageProcToken(this.currentTime));
	}

	private void handleShotReport_Proc(ShotReport in)
	{
		switch (in.getProcFlavor())
		{
			case Slash:
			{
				// final ProcToken curProc = new BleedingDamageProcToken(in, this.currentTime, this.baseWeapon.getBaseDamage(), this.mods, this.getCurrentBodyPart(), in.getDamage());
				final ProcToken curProc = ProcManager.getBleedingProcToken(in, this.currentTime, this.testWeap.getBaseDamage(), this.mods);
				this.enemy.addProc(curProc);
				break;
			}
			case Corrosive:
			{
				final ProcToken curProc = ProcManager.getCorrosiveProcToken(in, this.currentTime);
				this.enemy.addProc(curProc);
				break;
			}
			case Toxin:
			{
				// long inTimeStarted, ScaledDamagePacket unModdedBaseDamage, ModFolio mods, BodyPart partHit, CrittableDamagePacket report
				// this.enemy.addProc(new PoisonedDamageProcToken(in, this.currentTime, this.baseWeapon.getBaseDamage(), this.mods, this.getCurrentBodyPart(), in.getDamage()));
				final ProcToken curProc = ProcManager.getPoisonedDamageProcToken(in, this.currentTime, this.testWeap.getBaseDamage(), this.mods);
				this.enemy.addProc(curProc);
				break;
			}
			case Heat:
			{
				// final BlazingDamageProcToken damToken = new BlazingDamageProcToken(in, this.currentTime, this.baseWeapon.getBaseDamage(), this.mods, this.getCurrentBodyPart(), in.getDamage());
				// final FireAtTheDiscoProcToken panicToken = new FireAtTheDiscoProcToken(in, this.currentTime);

				final ProcToken damToken = ProcManager.getBlazingProcToken(in, this.currentTime, this.testWeap.getBaseDamage(), this.mods);
				final ProcToken panicToken = ProcManager.getFireAtTheDiscoProcToken(in, this.currentTime, this.enemy.getNumTicks_FireAtTheDiscoProc());

				this.enemy.addProc(damToken);
				this.enemy.addProc(panicToken);

				break;
			}
			case Cold:
			{
				// this.enemy.addProc(new ChilledProcToken(in, this.currentTime));
				final ProcToken curProc = ProcManager.getChilledProcToken(in, this.currentTime, this.enemy.isChilled());
				this.enemy.addProc(curProc);

				break;
			}
			case Impact:
			{
				// this.enemy.addProc(new ImpactStaggerProcToken(in, this.currentTime));
				final ProcToken curProc = ProcManager.getImpactStaggerProcToken(in, this.currentTime);
				this.enemy.addProc(curProc);

				break;
			}
			case Radiation:
			{
				// this.enemy.addProc(new ConfusedFromRadsProcToken(in, this.currentTime));
				final ProcToken curProc = ProcManager.getConfusedFromRadsProcToken(in, this.currentTime, this.enemy.isConfused());
				this.enemy.addProc(curProc);
				break;
			}
			case Electricity:
			{
				final SingleLinkedList<WeinerPuppet> Aoe = this.enemy.getAllEnemiesWithin(5.0, true);

				for (WeinerPuppet Tony : Aoe)
				{
					// final ProcToken stunToken = new StunnedFromLightningProcToken(in, this.currentTime);
					// final ProcToken damToken = new ElectricDamageProcToken(in, this.currentTime, this.baseWeapon.getBaseDamage(), this.mods, this.getCurrentBodyPart(), in.getDamage());

					final ProcToken stunToken = ProcManager.getStunnedFromLightningProcToken(in, this.currentTime);
					final ProcToken damToken = ProcManager.getElectricDamageProcToken(in, this.currentTime, this.testWeap.getBaseDamage(), this.mods);

					Tony.addProc(stunToken);
					Tony.addProc(damToken);
				}

				break;
			}
			case Gas:
			{
				// final FartyDamageProcToken fart = new FartyDamageProcToken(in, this.currentTime, this.baseWeapon.getBaseDamage(), this.mods, this.getCurrentBodyPart(), in.getDamage());

				final FartyDamageProcToken fart = ProcManager.getFartyDamageProcToken(in, this.currentTime, this.testWeap.getBaseDamage(), this.mods);

				this.enemy.addProc(fart);

				final SingleLinkedList<WeinerPuppet> Aoe = this.enemy.getAllEnemiesWithin(5.0, true);

				for (WeinerPuppet Tony : Aoe)
				{
					final ProcToken subFart = fart.getSubFart(this.currentTime);
					Tony.addProc(subFart);
				}

				break;
			}
			case Blast:
			{
				/*
				 * I could swear blast radius was supposed to be 2.0, not 5.0
				 */
				final SingleLinkedList<WeinerPuppet> Aoe = this.enemy.getAllEnemiesWithin(5.0, true);

				for (WeinerPuppet Tony : Aoe)
				{
					// final BlastedProcToken stunToken = new BlastedProcToken(in, this.currentTime);
					final BlastedProcToken stunToken = ProcManager.getBlastedProcToken(in, this.currentTime);

					final ProcToken getUpToken = stunToken.getSubsequentToken();
					Tony.addProc(stunToken);
					Tony.addProc(getUpToken);
				}

				break;
			}
			case Magnetic:
			{
				// this.enemy.addProc(new MagnetizedProcToken(in, this.currentTime, this.enemy.isMagnetized()));
				final ProcToken curProc = ProcManager.getMagnetizedProcToken(in, this.currentTime, this.enemy.isMagnetized());
				this.enemy.addProc(curProc);
				break;
			}
			case Viral:
			{
				// this.enemy.addProc(new ViralProcToken(in, this.currentTime, this.enemy.isPlagued()));
				final ProcToken curProc = ProcManager.getPlaguedProcToken(in, this.currentTime, this.enemy.isPlagued());
				this.enemy.addProc(curProc);
				break;
			}
			case Puncture:
			{
				// this.enemy.addProc(new WeaknessProcToken(in, this.currentTime, this.enemy.isWeakened()));
				final ProcToken curProc = ProcManager.getWeakenedProcToken(in, this.currentTime, this.enemy.isWeakened());
				this.enemy.addProc(curProc);
				break;
			}
			default:
			{
				throw new IllegalArgumentException(in.getProcFlavor().toString());
			}
		}
	}
}
