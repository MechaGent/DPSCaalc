package Mk01.Enemy;

import java.util.PriorityQueue;

import CustomExceptions.UnhandledEnum.Mk01.UnhandledEnumException;
import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.AfflictionTypes;
import Enums.StatusProcEffectValues;
import Enums.locMatTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Manifests.Enemy.Mk01.EnemyStats;
import Mk01.ReportsManager.Reporter;
import Mk01.Procs.AblazeProcToken;
import Mk01.Procs.ProcToken;
import Mk01.Procs.statWarpingProcToken;
import Mk01.Reports.Report;

public class WeinerPuppet extends MeatPuppet
{
	private final PriorityQueue<ProcToken>[] Lines;
	private long nextProcSigTime;

	// analysis stuff
	private final int[] totals;
	private final int[] maxActiveTokens;

	/**
	 * EnemyStats-bootstrapped constructor - mutable stats initialized to their maximums
	 * 
	 * @param inId
	 *            this object's Id
	 * @param inReporty
	 *            the Reporter to which this object should publish reports
	 * @param inEngageGodMode_Health
	 *            whether this object's health is invulnerable
	 * @param inEngageGodMode_Armor
	 *            whether this object's armor is invulnerable
	 * @param inEngageGodMode_Shield
	 *            whether this object's shield is invulnerable
	 * @param inStats
	 *            an object containing this object's initialization stats, unless otherwise specified
	 */
	public WeinerPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, EnemyStats inStats)
	{
		this(inId, inReporty, inEngageGodMode_Health, inEngageGodMode_Armor, inEngageGodMode_Shield, inStats, inStats.getBaseHealth(), inStats.getBaseArmor(), inStats.getBaseShield());
	}

	/**
	 * EnemyStats-bootstrapped constructor - user-initialized mutable stats
	 * 
	 * @param inId
	 *            this object's Id
	 * @param inReporty
	 *            the Reporter to which this object should publish reports
	 * @param inEngageGodMode_Health
	 *            whether this object's health is invulnerable
	 * @param inEngageGodMode_Armor
	 *            whether this object's armor is invulnerable
	 * @param inEngageGodMode_Shield
	 *            whether this object's shield is invulnerable
	 * @param inStats
	 *            an object containing this object's initialization stats, unless otherwise specified
	 * @param curHealth
	 *            the quantity of health with which this object should start
	 * @param curArmor
	 *            the quantity of armor with which this object should start
	 * @param curShield
	 *            the quantity of shield with which this object should start
	 */
	public WeinerPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, EnemyStats inStats, double curHealth, double curArmor, double curShield)
	{
		this(inId, inReporty, inEngageGodMode_Health, inEngageGodMode_Armor, inEngageGodMode_Shield, inStats.getBaseHealth(), inStats.getBaseArmor(), inStats.getBaseShield(), inStats.getHealthMat(), inStats.getArmorMat(), inStats.getShieldMat(), curHealth, curArmor, curShield, inStats.getBodyParts());
	}

	/**
	 * Constrained-Primitives constructor - assumes mutable stats are to be initialized to their maximums
	 * 
	 * @param inId
	 *            this object's Id
	 * @param inReporty
	 *            the Reporter to which this object should publish reports
	 * @param inEngageGodMode_Health
	 *            whether this object's health is invulnerable
	 * @param inEngageGodMode_Armor
	 *            whether this object's armor is invulnerable
	 * @param inEngageGodMode_Shield
	 *            whether this object's shield is invulnerable
	 * @param inHealthMax
	 *            the maximum quantity of health this object can normally have, and the value at which this object's starting health is initialized
	 * @param inArmorMax
	 *            the maximum quantity of armor this object can normally have, and the value at which this object's starting armor is initialized
	 * @param inShieldMax
	 *            the maximum quantity of shield this object can normally have, and the value at which this object's starting shield is initialized
	 * @param inHealthMat
	 *            the material of which this object's health consists
	 * @param inArmorMat
	 *            the material of which this object's armor consists
	 * @param inShieldMat
	 *            the material of which this object's shield consists
	 * @param inBodyParts
	 *            an array containing all of the body parts of this object
	 */
	public WeinerPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, BodyPart[] inBodyParts)
	{
		this(inId, inReporty, inEngageGodMode_Health, inEngageGodMode_Armor, inEngageGodMode_Shield, inHealthMax, inArmorMax, inShieldMax, inHealthMat, inArmorMat, inShieldMat, inHealthMax, inArmorMax, inShieldMax, inBodyParts);
	}

	/**
	 * All-Vars constructor
	 * 
	 * @param inId
	 *            this object's Id
	 * @param inReporty
	 *            the Reporter to which this object should publish reports
	 * @param inEngageGodMode_Health
	 *            whether this object's health is invulnerable
	 * @param inEngageGodMode_Armor
	 *            whether this object's armor is invulnerable
	 * @param inEngageGodMode_Shield
	 *            whether this object's shield is invulnerable
	 * @param inHealthMax
	 *            the maximum quantity of health this object can normally have
	 * @param inArmorMax
	 *            the maximum quantity of armor this object can normally have
	 * @param inShieldMax
	 *            the maximum quantity of shield this object can normally have
	 * @param inHealthMat
	 *            the material of which this object's health consists
	 * @param inArmorMat
	 *            the material of which this object's armor consists
	 * @param inShieldMat
	 *            the material of which this object's shield consists
	 * @param inHealth
	 *            the quantity of health with which this object should start
	 * @param inArmor
	 *            the quantity of armor with which this object should start
	 * @param inShield
	 *            the quantity of shield with which this object should start
	 * @param inBodyParts
	 *            an array containing all of the body parts of this object
	 */
	public WeinerPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, double inHealth, double inArmor, double inShield, BodyPart[] inBodyParts)
	{
		super(inId, inReporty,

				inEngageGodMode_Health, inEngageGodMode_Armor, inEngageGodMode_Shield,

				inHealthMax, inArmorMax, inShieldMax,

				inHealthMat, inArmorMat, inShieldMat,

				inHealth, inArmor, inShield,

				inBodyParts);

		this.Lines = initLines();
		this.nextProcSigTime = Long.MAX_VALUE;

		this.totals = new int[TokenQueues.getNumTypes()];
		this.maxActiveTokens = new int[TokenQueues.getNumTypes()];
	}
	
	/**
	 * copy constructor
	 * 
	 * @param old
	 *            the object from which to copy values
	 * @param inId
	 *            this object's Id
	 * @param inReporty
	 *            the Reporter to which this object should publish reports
	 */
	public WeinerPuppet(coreMeatPuppet old, String inId, Reporter inReporty)
	{
		super(old, inId, inReporty);
		
		this.Lines = initLines();
		this.nextProcSigTime = Long.MAX_VALUE;

		this.totals = new int[TokenQueues.getNumTypes()];
		this.maxActiveTokens = new int[TokenQueues.getNumTypes()];
	}

	private static PriorityQueue<ProcToken>[] initLines()
	{
		@SuppressWarnings("unchecked")
		final PriorityQueue<ProcToken>[] result = new PriorityQueue[TokenQueues.getNumTypes()];

		for (int i = 0; i < result.length; i++)
		{
			result[i] = new PriorityQueue<ProcToken>();
		}

		return result;
	}

	/**
	 * static factory for runs
	 * @param IdNum
	 * @param ChannelNum
	 * @param godModeToggles [0] = health, [1] = armor, [2] = shield
	 * @param in
	 * @param enemyLevel
	 * @return
	 */
	public static WeinerPuppet getInstance(int IdNum, Reporter reporty, boolean[] godModeToggles, EnemyStats in, int enemyLevel)
	{
		final String inId = baseMeatPuppet.getIdName(IdNum);
		final Reporter inReporty = reporty;
		final boolean inEngageGodMode_Health = godModeToggles[0];
		final boolean inEngageGodMode_Armor = godModeToggles[1];
		final boolean inEngageGodMode_Shield = godModeToggles[2];
		final double inHealthMax = in.getHealthAtLevel(enemyLevel);
		final double inArmorMax = in.getArmorAtLevel(enemyLevel);
		final double inShieldMax = in.getShieldAtLevel(enemyLevel);
		final locMatTypes inHealthMat = in.getHealthMat();
		final locMatTypes inArmorMat = in.getArmorMat();
		final locMatTypes inShieldMat = in.getShieldMat();
		final double inHealth = inHealthMax;
		final double inArmor = inArmorMax;
		final double inShield = inShieldMax;
		final BodyPart[] inBodyParts = in.getBodyParts();

		return new WeinerPuppet(inId, inReporty,

				inEngageGodMode_Health, inEngageGodMode_Armor, inEngageGodMode_Shield,

				inHealthMax, inArmorMax, inShieldMax,

				inHealthMat, inArmorMat, inShieldMat,

				inHealth, inArmor, inShield,

				inBodyParts);

	}

	private ProcToken getNextToken()
	{
		ProcToken result = this.Lines[TokenQueues.Main.ordinal()].poll();

		while (result != null && result.isExpired())
		{
			result = this.Lines[TokenQueues.Main.ordinal()].poll();
		}

		if (result != null)
		{
			final AfflictionTypes flavor = result.getProcFlavor();

			if (flavor != AfflictionTypes.BulletDamage)
			{
				final ProcToken match = this.cycleExpiredAndReturnQueue(TokenQueues.mapFromAfflictionTypes(flavor)).poll();

				if (result != match)
				{
					throw new IllegalArgumentException();
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @param currentTime
	 */
	public void processProcs(long currentTime)
	{
		ProcToken curToken = this.getNextToken();

		while (curToken != null && curToken.getNextTickTime() <= currentTime)
		{
			curToken.tick(currentTime, this);

			if (!curToken.isExpired())
			{
				this.addProc(curToken);
			}

			curToken = this.getNextToken();
		}

		if (curToken != null)
		{
			this.nextProcSigTime = curToken.getNextTickTime();

			this.addProc(curToken); // because loop goes 1 too far, by necessity
		}
		else
		{
			this.nextProcSigTime = Long.MAX_VALUE;
		}
	}

	public long getNextProcSigTime()
	{
		return this.nextProcSigTime;
	}

	/**
	 * has no internal logic, pre-process before calling
	 * 
	 * @param in
	 * @param index
	 */
	private void addProcToQueues_Internal(ProcToken in, TokenQueues index)
	{
		this.addProcToQueues_Internal(in, index.ordinal());
	}

	/**
	 * has no internal logic, pre-process before calling
	 * 
	 * @param in
	 * @param index
	 */
	private void addProcToQueues_Internal(ProcToken in, int index)
	{
		final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(index);
		locQueue.add(in);
		this.Lines[TokenQueues.Main.ordinal()].add(in);

		if (this.maxActiveTokens[index] < locQueue.size())
		{
			this.maxActiveTokens[index] = locQueue.size();
		}

		if (in.isFirstTick())
		{
			this.totals[in.getProcFlavor().ordinal()]++;
		}
	}

	private PriorityQueue<ProcToken> cycleExpiredAndReturnQueue(TokenQueues index)
	{
		return this.cycleExpiredAndReturnQueue(index.ordinal());
	}

	private PriorityQueue<ProcToken> cycleExpiredAndReturnQueue(int index)
	{
		final PriorityQueue<ProcToken> result = this.Lines[index];

		while (!result.isEmpty() && result.peek().isExpired())
		{
			result.poll();
		}

		/*
		 * maxActiveTokens
		 */
		if (this.maxActiveTokens[index] < result.size())
		{
			this.maxActiveTokens[index] = result.size();
		}

		return result;
	}

	/**
	 * 
	 * @param in
	 * @return false if proc bounced, true otherwise
	 */
	public boolean addProc(ProcToken in)
	{
		final boolean result;
		final AfflictionTypes affType = in.getProcFlavor();
		final TokenQueues mappedType = TokenQueues.mapFromAfflictionTypes(affType);

		switch (affType)
		{
			case Ablaze:
			{
				/*
				 * Singleton procs, that displace older ones only if the newer one has higher damage
				 */

				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(mappedType);

				if (locQueue.isEmpty())
				{
					this.addProcToQueues_Internal(in, mappedType);
					result = true;
				}
				else
				{
					final AblazeProcToken newfire = (AblazeProcToken) in;
					final AblazeProcToken oldfire = (AblazeProcToken) locQueue.poll();
					final Report expiredReport;

					if (newfire.compareDamagesWith(oldfire) > 0)
					{
						// newfire replaces oldfire
						expiredReport = oldfire.forceExpired(newfire.getTimeStarted(), "was dislodged by new ablazeProc");
						this.addProcToQueues_Internal(newfire, mappedType);
						result = true;
					}
					else
					{
						expiredReport = newfire.forceExpired(newfire.getTimeStarted(), " bounced off old ablazeProc");
						this.addProcToQueues_Internal(oldfire, mappedType);
						result = false;
					}
					
					this.reporty.publish(expiredReport);
				}

				break;
			}
			case BulletDamage:
			case Corroded:
			case GreviouslyPapercut:
			case Poisoned:
			case Damaged_fromElectricity:
			case Gassed:
			case Weakness:
			{
				/*
				 * Stackable Procs, no restrictions
				 */

				this.addProcToQueues_Internal(in, mappedType);
				result = true;

				break;
			}
			case Chilled:
			case Confused_fromRadiation:
			case Magnetized:
			case Plagued:
			{
				/*
				 * Singleton procs, newer proc replaces old whilst copying the value of the old
				 */

				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(mappedType);

				if (locQueue.isEmpty())
				{
					this.addProcToQueues_Internal(in, mappedType);
					result = true;
				}
				else
				{
					final statWarpingProcToken refresher = (statWarpingProcToken) in;
					final statWarpingProcToken oldProc = (statWarpingProcToken) locQueue.poll();

					refresher.setThisProcAsRefresherFor(oldProc);
					this.addProcToQueues_Internal(refresher, mappedType);
					final Report expiredReport = oldProc.forceExpired(refresher.getTimeStarted(), "proc was refreshed, then replaced");
					
					this.reporty.publish(expiredReport);
					
					result = true;
				}

				break;
			}
			case Stunned_fromElectricity:
			case Stunned_fromFire:
			{
				/*
				 * Singleton procs, older proc blocks all newer procs whilst active
				 */

				if (this.cycleExpiredAndReturnQueue(mappedType).isEmpty())
				{
					this.addProcToQueues_Internal(in, mappedType);
					result = true;
				}
				else
				{
					final Report expiredReport = in.forceExpired(in.getTimeStarted(), "was bounced by old proc");
					
					this.reporty.publish(expiredReport);
					
					result = false;
				}

				break;
			}
			case KnockedDown_fromBlast:
			case Stagger_fromImpact:
			case StandingUp_fromKnockdown:
			{
				/*
				 * Singleton procs, newer proc replaces older, where older could be any from a pool of types
				 */

				final AfflictionTypes[] commonPoolSingletons = AfflictionTypes.getCommonPoolSingletons();
				boolean doneFlag = false;

				for (int i = 0; !doneFlag && i < commonPoolSingletons.length; i++)
				{
					final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(TokenQueues.mapFromAfflictionTypes(commonPoolSingletons[i]));

					if (!locQueue.isEmpty())
					{
						final ProcToken old = locQueue.poll();
						final Report expiredReport = old.forceExpired(in.getTimeStarted(), "was replaced by new proc");
						
						this.reporty.publish(expiredReport);
						
						doneFlag = true;
					}
				}

				this.addProcToQueues_Internal(in, mappedType);
				result = true;

				break;
			}
			case Ragdolled:
			case StandingUp_fromRagdoll:
			case None:
			default:
			{
				throw new UnhandledEnumException(affType);
			}
		}

		return result;
	}

	/**
	 * 
	 * @param x
	 * @param isInclusive
	 * @return a list of every enemy within x meters of this enemy (including this enemy if isInclusive == true)
	 */
	public SingleLinkedList<WeinerPuppet> getAllEnemiesWithin(double x, boolean isInclusive)
	{
		final SingleLinkedList<WeinerPuppet> result = new SingleLinkedList<WeinerPuppet>();

		if (isInclusive)
		{
			result.add(this);
		}

		/*
		 * maaaaagic
		 */

		return result;
	}

	public boolean isMagnetized()
	{
		return !this.cycleExpiredAndReturnQueue(TokenQueues.Magnetized).isEmpty();
	}

	@Override
	public double getHealthMax()
	{
		if (this.isMagnetized())
		{
			return this.HealthMax * StatusProcEffectValues.MagneticShieldReductionMult.getValue();
		}
		else
		{
			return this.HealthMax;
		}
	}

	public boolean isPlagued()
	{
		return !this.cycleExpiredAndReturnQueue(TokenQueues.Plagued).isEmpty();
	}

	@Override
	public double getShieldMax()
	{
		if (this.isPlagued())
		{
			return this.ShieldMax * StatusProcEffectValues.ViralHealthReductionMult.getValue();
		}
		else
		{
			return this.ShieldMax;
		}
	}
	
	public Reporter getReporty()
	{
		return this.reporty;
	}

	public static enum TokenQueues
	{
		Main,
		BulletDamage,
		Bleeding,
		Corroded,
		Ablaze,
		Stunned_fromFire,
		Chilled,
		Staggered_fromImpact,
		Confused_FromRads,
		Stunned_byElectricity,
		Hurt_byElectricity,
		Poisoned,
		Gassed,
		KnockedDown_fromBlast,
		StandUp_fromKnockdown,
		Magnetized,
		Plagued,
		Weakened,
		Ragdolled,
		StandingUp_fromRagdoll;

		private static TokenQueues[] All = TokenQueues.values();

		public static int getNumTypes()
		{
			return All.length;
		}

		public static TokenQueues[] getAll()
		{
			return All;
		}

		public static TokenQueues mapFromAfflictionTypes(AfflictionTypes out)
		{
			final TokenQueues result;

			switch (out)
			{
				case BulletDamage:
				{
					result = TokenQueues.BulletDamage;
					break;
				}
				case Ablaze:
				{
					result = TokenQueues.Ablaze;
					break;
				}
				case GreviouslyPapercut:
				{
					result = TokenQueues.Bleeding;
					break;
				}
				case Chilled:
				{
					result = TokenQueues.Chilled;
					break;
				}
				case Confused_fromRadiation:
				{
					result = TokenQueues.Confused_FromRads;
					break;
				}
				case Corroded:
				{
					result = TokenQueues.Corroded;
					break;
				}
				case Gassed:
				{
					result = TokenQueues.Gassed;
					break;
				}
				case Damaged_fromElectricity:
				{
					result = TokenQueues.Hurt_byElectricity;
					break;
				}
				case KnockedDown_fromBlast:
				{
					result = TokenQueues.KnockedDown_fromBlast;
					break;
				}
				case Magnetized:
				{
					result = TokenQueues.Magnetized;
					break;
				}
				case Plagued:
				{
					result = TokenQueues.Plagued;
					break;
				}
				case Poisoned:
				{
					result = TokenQueues.Poisoned;
					break;
				}
				case Stagger_fromImpact:
				{
					result = TokenQueues.Staggered_fromImpact;
					break;
				}
				case StandingUp_fromKnockdown:
				{
					result = TokenQueues.StandUp_fromKnockdown;
					break;
				}
				case Stunned_fromElectricity:
				{
					result = TokenQueues.Stunned_byElectricity;
					break;
				}
				case Stunned_fromFire:
				{
					result = TokenQueues.Stunned_fromFire;
					break;
				}
				case Weakness:
				{
					result = TokenQueues.Weakened;
					break;
				}
				case Ragdolled:
				{
					result = TokenQueues.Ragdolled;
					break;
				}
				case StandingUp_fromRagdoll:
				{
					result = TokenQueues.StandingUp_fromRagdoll;
					break;
				}
				default:
				{
					throw new IllegalArgumentException(out.toString());
				}
			}

			return result;
		}
	}
}
