package Mk00.Enemy;

import java.util.PriorityQueue;

import DataStructures.Linkages.CharList.Mk03.CharList;
import DataStructures.Linkages.SingleLinkedList.Unsorted.Mk03.SingleLinkedList;
import Enums.AfflictionTypes;
import Enums.locMatTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Mk00.Autobots.ScalingAutobot;
import Mk00.Damage.Procs.ProcManager;
import Mk00.Damage.Procs.ProcManager.TickingProcToken;
import Mk00.Damage.Procs.ProcToken;

public class WeinerPuppet extends MeatPuppet
{
	private final PriorityQueue<ProcToken>[] QueueArr;
	private final int[] totals;
	private final int[] maxActive;
	private final PriorityQueue<ProcToken> MainQueue;
	private final BodyPart[] body;
	private long nextProcSigTime;

	public WeinerPuppet(double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat)
	{
		super(inHealthMax, inArmorMax, inShieldMax, inHealthMat, inArmorMat, inShieldMat);
		this.QueueArr = initQueues();
		this.totals = new int[AfflictionTypes.getNumTypes()];
		this.maxActive = new int[Queues.getNumTypes()];
		this.MainQueue = new PriorityQueue<ProcToken>();
		this.nextProcSigTime = -1;
		this.body = BodyPart.getStandardHumanoidDummy();
	}

	private WeinerPuppet(boolean inGodMode_Health, boolean inGodMode_Armor, boolean inGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, double inHealth, double inArmor, double inShield)
	{
		super(inGodMode_Health, inGodMode_Armor, inGodMode_Shield, inHealthMax, inArmorMax, inShieldMax, inHealthMat, inArmorMat, inShieldMat, inHealth, inArmor, inShield);
		this.totals = new int[AfflictionTypes.getNumTypes()];
		this.QueueArr = initQueues();
		this.maxActive = new int[Queues.getNumTypes()];
		this.MainQueue = new PriorityQueue<ProcToken>();
		this.nextProcSigTime = -1;
		this.body = BodyPart.getStandardHumanoidDummy();
	}

	public WeinerPuppet(WeinerPuppet in)
	{
		super(in);
		this.totals = new int[AfflictionTypes.getNumTypes()];
		this.QueueArr = initQueues();
		this.maxActive = new int[Queues.getNumTypes()];
		this.MainQueue = new PriorityQueue<ProcToken>();
		this.nextProcSigTime = -1;
		this.body = in.body;
	}

	private static PriorityQueue<ProcToken>[] initQueues()
	{
		@SuppressWarnings("unchecked")
		final PriorityQueue<ProcToken>[] result = new PriorityQueue[Queues.getNumTypes()];

		for (Queues cur : Queues.getAll())
		{
			result[cur.ordinal()] = new PriorityQueue<ProcToken>();
		}

		return result;
	}

	public WeinerPuppet cloneSelf()
	{
		return new WeinerPuppet(this);
	}

	public BodyPart[] getBodyParts()
	{
		return this.body;
	}

	public CharList getMaxProcsActiveAsCharList()
	{
		final CharList result = new CharList();
		boolean hasFirst = false;

		for (Queues cur : Queues.getAll())
		{
			final int curMax = this.maxActive[cur.ordinal()];
			
			if(curMax != 0)
			{
				if(hasFirst)
				{
					result.add('\t');
				}
				else
				{
					hasFirst = true;
				}
				
				result.add(cur.toString());
				result.add('(');
				result.add(Integer.toString(curMax));
				result.add(')');
			}
		}

		return result;
	}
	
	public int getMaxProcsActive(AfflictionTypes index)
	{
		return this.maxActive[Queues.mapFromAfflictionTypes(index).ordinal()];
	}
	
	public int getTokenTotal(AfflictionTypes index)
	{
		return this.totals[index.ordinal()];
	}
	
	public CharList getTokenTotalsAsCharList()
	{
		final CharList result = new CharList();
		boolean hasFirst = false;

		for (AfflictionTypes cur : AfflictionTypes.getAll())
		{
			final int curMax = this.totals[cur.ordinal()];
			
			if(curMax != 0)
			{
				if(hasFirst)
				{
					result.add('\t');
				}
				else
				{
					hasFirst = true;
				}
				
				result.add(cur.toString());
				result.add('(');
				result.add(Integer.toString(curMax));
				result.add(')');
			}
		}

		return result;
	}

	public int getNumTicks_FireAtTheDiscoProc()
	{
		return ProcManager.getNumTicks_FireAtTheDisco_Humanoid();
	}

	public long getNextProcSigTime()
	{
		return this.nextProcSigTime;
	}

	public void setNextProcSigTime(long inNextProcSigTime)
	{
		this.nextProcSigTime = inNextProcSigTime;
	}

	/**
	 * 
	 * @param currentTime
	 * @return next significant tick time
	 */
	public long processProcs(long currentTime)
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

		return this.nextProcSigTime;
	}

	private ProcToken getNextToken()
	{
		ProcToken result = this.MainQueue.poll();

		while (result != null && result.isExpired())
		{
			result = this.MainQueue.poll();
		}

		if (result != null)
		{
			final AfflictionTypes flavor = result.getProcFlavor();

			if (flavor != AfflictionTypes.BulletDamage)
			{
				final Queues cur = Queues.mapFromAfflictionTypes(flavor);
				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(cur);
				locQueue.poll();
			}

			return result;
		}
		else
		{
			return null;
		}
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
		this.MainQueue.add(in);
		
		if (this.maxActive[index] < locQueue.size())
		{
			this.maxActive[index] = locQueue.size();
		}
		
		if(in.isFirstTick())
		{
			this.totals[in.getProcFlavor().ordinal()]++;
		}
		
		// this.maxActive[index]++;
	}

	/**
	 * has no internal logic, pre-process before calling
	 * 
	 * @param in
	 * @param index
	 */
	private void addProcToQueues_Internal(ProcToken in, Queues index)
	{
		this.addProcToQueues_Internal(in, index.ordinal());
	}

	/**
	 * 
	 * @param in
	 * @return false if proc bounced, true otherwise
	 */
	public boolean addProc(ProcToken in)
	{
		final boolean result;
		final AfflictionTypes type = in.getProcFlavor();

		switch (type)
		{
			case GreviouslyPapercut:
			{
				this.addProcToQueues_Internal(in, Queues.Bleeding);
				result = true;
				break;
			}
			case Corroded:
			{
				this.addProcToQueues_Internal(in, Queues.Corroded);
				result = true;
				break;
			}
			case Ablaze:
			{
				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Ablaze);

				if (locQueue.isEmpty())
				{
					this.addProcToQueues_Internal(in, Queues.Ablaze);
					result = true;
				}
				else
				{
					final TickingProcToken fireDamage = (TickingProcToken) in;
					final TickingProcToken oldfire = (TickingProcToken) locQueue.poll();

					if (oldfire.getDamage() < fireDamage.getDamage())
					{
						oldfire.forceExpired(in.getTimeStarted(), "this was displaced by new " + AfflictionTypes.Ablaze.toString() + "Proc");

						this.addProcToQueues_Internal(in, Queues.Ablaze);
						result = true;
					}
					else
					{
						locQueue.add(oldfire);
						in.forceExpired(in.getTimeStarted(), "this was bounced by old " + AfflictionTypes.Ablaze.toString() + "Proc");
						result = false;
					}
				}

				break;
			}
			case Stunned_fromFire:
			{
				result = !bounceIfAlreadyProcced(in, Queues.Stunned_fromFire);

				break;
			}
			case Chilled:
			{
				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Chilled);

				if (!locQueue.isEmpty())
				{
					locQueue.poll().forceExpired(in.getTimeStarted(), "this was displaced by new ChilledProc");
				}

				this.addProcToQueues_Internal(in, Queues.Chilled);
				result = true;
				break;
			}
			case Stagger_fromImpact:
			{
				result = !bounceIfAlreadyProcced(in, Queues.Staggered_fromImpact);

				break;
			}
			case Confused_fromRadiation:
			{
				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Confused_FromRads);

				if (!locQueue.isEmpty())
				{
					locQueue.poll().forceExpired(in.getTimeStarted(), "this was displaced by new " + AfflictionTypes.Confused_fromRadiation.toString() + "Proc");
				}

				this.addProcToQueues_Internal(in, Queues.Confused_FromRads);
				result = true;

				break;
			}
			case Stunned_fromElectricity:
			{
				result = !bounceIfAlreadyProcced(in, Queues.Stunned_byElectricity);

				break;
			}
			case Damaged_fromElectricity:
			{
				this.addProcToQueues_Internal(in, Queues.Hurt_byElectricity);
				result = true;

				break;
			}
			case Poisoned:
			{
				this.addProcToQueues_Internal(in, Queues.Poisoned);
				result = true;

				break;
			}
			case Gassed:
			{
				this.addProcToQueues_Internal(in, Queues.Gassed);
				result = true;

				break;
			}
			case KnockedDown_fromBlast:
			{
				this.addProcToQueues_Internal(in, Queues.KnockedDown_fromBlast);
				result = true;

				break;
			}
			case StandingUp_fromKnockdown:
			{
				this.addProcToQueues_Internal(in, Queues.StandUp_fromKnockdown);
				result = true;

				break;
			}
			case Magnetized:
			{
				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Magnetized);

				if (!locQueue.isEmpty())
				{
					locQueue.poll().forceExpired(in.getTimeStarted(), "this was displaced by new MagnetizedProc");
				}

				this.addProcToQueues_Internal(in, Queues.Magnetized);
				result = true;

				break;
			}
			case Plagued:
			{
				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Plagued);

				if (!locQueue.isEmpty())
				{
					locQueue.poll().forceExpired(in.getTimeStarted(), "this was displaced by new PlaguedProc");
				}

				this.addProcToQueues_Internal(in, Queues.Plagued);
				result = true;
				// throw new IllegalArgumentException();
				break;
			}
			case Weakness:
			{
				final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Weakened);

				if (!locQueue.isEmpty())
				{
					locQueue.poll().forceExpired(in.getTimeStarted(), "this was displaced by new WeaknessProc");
				}

				this.addProcToQueues_Internal(in, Queues.Weakened);
				result = true;

				break;
			}
			case BulletDamage:
			{
				this.MainQueue.add(in);
				result = true;
				break;
			}
			case Ragdolled:
			case StandingUp_fromRagdoll:
			case None:
			default:
			{
				throw new IllegalArgumentException(in.getProcFlavor().toString());
			}
		}

		return result;
	}

	public boolean isChilled()
	{
		final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Chilled);

		return !locQueue.isEmpty();
	}

	public boolean isMagnetized()
	{
		final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Magnetized);

		return !locQueue.isEmpty();
	}

	public boolean isPlagued()
	{
		final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Plagued);

		return !locQueue.isEmpty();
	}

	public boolean isWeakened()
	{
		final PriorityQueue<ProcToken> locQueue = this.cycleExpiredAndReturnQueue(Queues.Weakened);

		return !locQueue.isEmpty();
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

	/*
	private static void cycleExpired(PriorityQueue<ProcToken> locQueue)
	{
		while (!locQueue.isEmpty() && locQueue.peek().isExpired())
		{
			locQueue.poll();
		}
	}
	*/

	@SuppressWarnings("unused")
	private void cycleExpired(Queues index)
	{
		final PriorityQueue<ProcToken> locQueue = this.QueueArr[index.ordinal()];

		while (!locQueue.isEmpty() && locQueue.peek().isExpired())
		{
			locQueue.poll();
		}

		if (this.maxActive[index.ordinal()] < locQueue.size())
		{
			this.maxActive[index.ordinal()] = locQueue.size();
		}
	}

	private PriorityQueue<ProcToken> cycleExpiredAndReturnQueue(int index)
	{
		final PriorityQueue<ProcToken> locQueue = this.QueueArr[index];

		while (!locQueue.isEmpty() && locQueue.peek().isExpired())
		{
			locQueue.poll();
		}

		if (this.maxActive[index] < locQueue.size())
		{
			this.maxActive[index] = locQueue.size();
		}

		return locQueue;
	}

	private PriorityQueue<ProcToken> cycleExpiredAndReturnQueue(Queues index)
	{
		return this.cycleExpiredAndReturnQueue(index.ordinal());
	}

	/**
	 * 
	 * @param in
	 * @param locQueue
	 * @return true if bounced, false otherwise
	 */
	private boolean bounceIfAlreadyProcced(ProcToken in, Queues index)
	{
		final boolean result = !this.cycleExpiredAndReturnQueue(index).isEmpty();

		if (result)
		{
			in.forceExpired(in.getTimeStarted(), "this was bounced by old " + in.getProcFlavor().toString() + "Proc");
		}

		return result;
	}

	public static class WeinerPuppetBuilder extends MeatPuppetBuilder
	{
		public WeinerPuppetBuilder()
		{
			super();
		}

		public WeinerPuppetBuilder(int baseLevel)
		{
			super(baseLevel);
		}

		public WeinerPuppetBuilder(MeatPuppet old)
		{
			super(old);
		}

		@Override
		public WeinerPuppet buildScaledToLevel(int desiredLevel)
		{
			final double maxHealth = ScalingAutobot.scaleStat_Health(this.baseLevel, desiredLevel, this.HealthMax);
			final double maxArmor = ScalingAutobot.scaleStat_Armor(this.baseLevel, desiredLevel, this.ArmorMax);
			final double maxShield = ScalingAutobot.scaleStat_Shield(this.baseLevel, desiredLevel, this.ShieldMax);

			final double curHealth = ScalingAutobot.scaleStat_Health(this.baseLevel, desiredLevel, this.Health);
			final double curArmor = ScalingAutobot.scaleStat_Armor(this.baseLevel, desiredLevel, this.Armor);
			final double curShield = ScalingAutobot.scaleStat_Shield(this.baseLevel, desiredLevel, this.Shield);

			return new WeinerPuppet(this.engageGodMode_Health, this.engageGodMode_Armor, this.engageGodMode_Shield,

					maxHealth, maxArmor, maxShield,

					this.HealthMat, this.ArmorMat, this.ShieldMat,

					curHealth, curArmor, curShield);
		}
	}

	private static enum Queues
	{
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

		private static final Queues[] All = Queues.values();

		public static Queues[] getAll()
		{
			return All;
		}

		public static int getNumTypes()
		{
			return All.length;
		}

		@SuppressWarnings("unused")
		public static AfflictionTypes mapToAfflictionTypes(Queues in)
		{
			final AfflictionTypes result;

			switch (in)
			{
				case Ablaze:
				{
					result = AfflictionTypes.Ablaze;
					break;
				}
				case Bleeding:
				{
					result = AfflictionTypes.GreviouslyPapercut;
					break;
				}
				case Chilled:
				{
					result = AfflictionTypes.Chilled;
					break;
				}
				case Confused_FromRads:
				{
					result = AfflictionTypes.Confused_fromRadiation;
					break;
				}
				case Corroded:
				{
					result = AfflictionTypes.Corroded;
					break;
				}
				case Gassed:
				{
					result = AfflictionTypes.Gassed;
					break;
				}
				case Hurt_byElectricity:
				{
					result = AfflictionTypes.Damaged_fromElectricity;
					break;
				}
				case KnockedDown_fromBlast:
				{
					result = AfflictionTypes.KnockedDown_fromBlast;
					break;
				}
				case Magnetized:
				{
					result = AfflictionTypes.Magnetized;
					break;
				}
				case Plagued:
				{
					result = AfflictionTypes.Plagued;
					break;
				}
				case Poisoned:
				{
					result = AfflictionTypes.Poisoned;
					break;
				}
				case Staggered_fromImpact:
				{
					result = AfflictionTypes.Stagger_fromImpact;
					break;
				}
				case StandUp_fromKnockdown:
				{
					result = AfflictionTypes.StandingUp_fromKnockdown;
					break;
				}
				case Stunned_byElectricity:
				{
					result = AfflictionTypes.Stunned_fromElectricity;
					break;
				}
				case Stunned_fromFire:
				{
					result = AfflictionTypes.Stunned_fromFire;
					break;
				}
				case Weakened:
				{
					result = AfflictionTypes.Weakness;
					break;
				}
				default:
				{
					throw new IllegalArgumentException(in.toString());
				}
			}

			return result;
		}

		public static Queues mapFromAfflictionTypes(AfflictionTypes out)
		{
			final Queues result;

			switch (out)
			{
				case Ablaze:
				{
					result = Queues.Ablaze;
					break;
				}
				case GreviouslyPapercut:
				{
					result = Queues.Bleeding;
					break;
				}
				case Chilled:
				{
					result = Queues.Chilled;
					break;
				}
				case Confused_fromRadiation:
				{
					result = Queues.Confused_FromRads;
					break;
				}
				case Corroded:
				{
					result = Queues.Corroded;
					break;
				}
				case Gassed:
				{
					result = Queues.Gassed;
					break;
				}
				case Damaged_fromElectricity:
				{
					result = Queues.Hurt_byElectricity;
					break;
				}
				case KnockedDown_fromBlast:
				{
					result = Queues.KnockedDown_fromBlast;
					break;
				}
				case Magnetized:
				{
					result = Queues.Magnetized;
					break;
				}
				case Plagued:
				{
					result = Queues.Plagued;
					break;
				}
				case Poisoned:
				{
					result = Queues.Poisoned;
					break;
				}
				case Stagger_fromImpact:
				{
					result = Queues.Staggered_fromImpact;
					break;
				}
				case StandingUp_fromKnockdown:
				{
					result = Queues.StandUp_fromKnockdown;
					break;
				}
				case Stunned_fromElectricity:
				{
					result = Queues.Stunned_byElectricity;
					break;
				}
				case Stunned_fromFire:
				{
					result = Queues.Stunned_fromFire;
					break;
				}
				case Weakness:
				{
					result = Queues.Weakened;
					break;
				}
				case Ragdolled:
				{
					result = Queues.Ragdolled;
					break;
				}
				case StandingUp_fromRagdoll:
				{
					result = Queues.StandingUp_fromRagdoll;
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
