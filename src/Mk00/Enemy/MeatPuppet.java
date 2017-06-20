package Mk00.Enemy;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.locDamTypes;
import Enums.locMatTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Mk00.Resistances;
import Mk00.Autobots.ScalingAutobot;
import Mk00.Damage.Packets.CrittableDamagePacket;
import Mk00.Damage.Packets.ScaledDamagePacket;
import Mk00.Damage.Packets.UnitDamagePacket;
import Mk00.Damage.Points.CrittableDamagePoint;
import Mk00.Reporting.Report.Actions;
import Mk00.Reporting.Report.ReportAction;
import Mk00.Reporting.ReportsManager;

public class MeatPuppet extends basicMeatPuppet
{
	private static final boolean printDamagesToConsole = false;

	//protected final PriorityQueue<DamageOverTime> DotQueue;

	private double godModeDamage_Health;
	private double numCorrosiveProcs_Armor;
	private double godModeDamage_Shield;

	public MeatPuppet(double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat)
	{
		this(false, false, false, inHealthMax, inArmorMax, inShieldMax, inHealthMat, inArmorMat, inShieldMat, inHealthMax, inArmorMax, inShieldMax);
	}

	public MeatPuppet(boolean godMode_Health, boolean godMode_Armor, boolean godMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, double inHealth, double inArmor, double inShield)
	{
		super(godMode_Health, godMode_Armor, godMode_Shield, inHealthMax, inArmorMax, inShieldMax, inHealthMat, inArmorMat, inShieldMat, inHealth, inArmor, inShield);
		//this.DotQueue = new PriorityQueue<DamageOverTime>();
		this.godModeDamage_Health = 0;
		this.numCorrosiveProcs_Armor = 0;
		this.godModeDamage_Shield = 0;
	}
	
	public MeatPuppet(MeatPuppet in)
	{
		super(in);
		this.godModeDamage_Health = 0;
		this.numCorrosiveProcs_Armor = 0;
		this.godModeDamage_Shield = 0;
	}

	@Override
	public void inflictDamage(long timeStamp, BodyPart inPartHit, CrittableDamagePacket inDamage, String cause)
	{
		if (printDamagesToConsole)
		{
			System.out.println("method 1");
		}

		// this.processAllCurrentDots(timeStamp);

		final double scale = inDamage.getScalar();
		final double critMult = calculateBodyLocalizedCritMult(inPartHit, inDamage.getCritLevel(), inDamage.getCritMult());

		if (this.hasShield())
		{
			final ScaledDamagePacket shieldAppliedProps = calculateTrueDamage_helper_ShieldCalcs(inDamage, this.ShieldMat);
			final double shieldDam = scale * critMult * shieldAppliedProps.getScalar();
			final double shieldDelta = this.getShield() - shieldDam;

			if (!(shieldDelta < 0))
			{
				if (printDamagesToConsole)
				{
					System.out.println("Pure Shield Damage: " + shieldDam);
				}

				this.modifyShield(timeStamp, -shieldDam, cause);
			}
			else
			{
				if (printDamagesToConsole)
				{
					System.out.println("Partial Shield Damage: " + this.getShield() + " of " + shieldDam);
				}

				this.modifyShield(timeStamp, -this.getShield(), cause);
				final double adjustedHealthDamMult = calculateTrueDamage_helper_calculateArmoredHealthDamageMultiplier(shieldAppliedProps, this.getArmor(), this.HealthMat, this.ArmorMat);
				final double healthDam = -shieldDelta * adjustedHealthDamMult;

				if (printDamagesToConsole)
				{
					System.out.println("Partial Health Damage: " + healthDam);
				}

				this.modifyHealth(timeStamp, -healthDam, cause);
			}
		}
		else
		{
			final double adjustedHealthDamMult = calculateTrueDamage_helper_calculateArmoredHealthDamageMultiplier(inDamage, this.getArmor(), this.HealthMat, this.ArmorMat);
			final double healthDam = inDamage.getScalar() * adjustedHealthDamMult * critMult;

			if (printDamagesToConsole)
			{
				System.out.println("Pure Health Damage: " + healthDam);
			}

			this.modifyHealth(timeStamp, -healthDam, cause);
		}
	}

	@Override
	public void inflictDamage(long inTimeStamp, BodyPart inPartHit, CrittableDamagePoint inDamage, String cause)
	{
		final double locCritMult = calculateBodyLocalizedCritMult(inPartHit, inDamage.getCritLevel(), inDamage.getCritMult());

		if (printDamagesToConsole)
		{
			System.out.println("method 2");
		}

		this.inflictDamage(inTimeStamp, inDamage.getValue() * locCritMult, inDamage.getFlavor(), cause);
	}

	@Override
	public void inflictDamage(long timeStamp, double inAmount, locDamTypes inFlavor, String cause)
	{
		if (printDamagesToConsole)
		{
			System.out.println("method 3: amount = " + inAmount);
		}

		// this.processAllCurrentDots(timeStamp);
		final double shieldMult = Resistances.getResistance(inFlavor, this.ShieldMat);
		final boolean bypassesShield = Resistances.isBypassed(shieldMult);

		if (!bypassesShield && this.hasShield())
		{
			final double shieldDam = inAmount * (1.0 + shieldMult);
			final double shieldDelta = this.getShield() - shieldDam;

			if (!(shieldDelta < 0))
			{
				if (printDamagesToConsole)
				{
					System.out.println("Pure Shield Damage: " + shieldDam);
				}

				this.modifyShield(timeStamp, -shieldDam, cause);
			}
			else
			{
				if (printDamagesToConsole)
				{
					System.out.println("Partial Shield Damage: " + this.getShield());
				}

				this.modifyShield(timeStamp, -this.getShield(), cause);
				final double healthDamage;
				final double healthMult = Resistances.getResistance(inFlavor, this.HealthMat);

				if (this.getArmor() > 0)
				{
					final double armorMult = Resistances.getResistance(inFlavor, this.ArmorMat);
					healthDamage = inAmount * (((1.0 + healthMult) * (1.0 + armorMult)) / (1.0 + ((this.getArmor() * (1.0 - armorMult)) / 300.0)));
				}
				else
				{
					healthDamage = inAmount * (1.0 + healthMult);
				}

				if (printDamagesToConsole)
				{
					System.out.println("Partial Health Damage: " + healthDamage);
				}

				this.modifyHealth(timeStamp, -healthDamage, cause);
			}
		}
		else
		{
			final double healthDamage;
			final double healthMult = Resistances.getResistance(inFlavor, this.HealthMat);
			final double armorMult = Resistances.getResistance(inFlavor, this.ArmorMat);

			if (!Resistances.isBypassed(armorMult) && this.getArmor() > 0)
			{
				healthDamage = inAmount * (((1.0 + healthMult) * (1.0 + armorMult)) / (1.0 + ((this.getArmor() * (1.0 - armorMult)) / 300.0)));
			}
			else
			{
				healthDamage = inAmount * (1.0 + healthMult);
			}

			if (printDamagesToConsole)
			{
				System.out.println("Pure Health Damage: " + healthDamage);
			}

			this.modifyHealth(timeStamp, -healthDamage, cause);
		}
	}

	/*
	@Override
	public void inflictDamageOverTime(long timeStamp, BodyPart inPartHit, CrittableDamagePoint damage, int inNumTicks, long inTickInterval, String cause)
	{
		final DamageOverTime Dot = new DamageOverTime(damage, inPartHit, timeStamp, inTickInterval, inNumTicks);
		this.DotQueue.add(Dot);
	}
	*/

	/*
	@Override
	public void processAllCurrentDots(long currentTime)
	{
		// System.out.println("isEmpty: " + isEmpty + " tickTimeIsValid: " + tickTimeIsValid);
	
		while (!this.DotQueue.isEmpty() && this.DotQueue.peek().getNextTickTime() <= currentTime)
		{
			// System.out.println("going in");
			final DamageOverTime cur = this.DotQueue.poll();
	
			if (!cur.isExpired())
			{
				final double locCritMult = calculateBodyLocalizedCritMult(cur.getPartHit(), cur.getCritLevel(), cur.getCritMult());
				final long timeOccurred = cur.getNextTickTime();
				final CrittableDamagePoint damage = cur.tick();
				this.inflictDamage(timeOccurred, locCritMult * damage.getValue(), damage.getFlavor(), cause);
	
				if (!cur.isExpired())
				{
					this.DotQueue.add(cur);
				}
			}
			else
			{
				System.out.println("isExpired: " + cur.getCurrTick() + " against " + cur.isExpired());
			}
		}
	}
	*/

	/*
	@Override
	public boolean hasActiveDots()
	{
		return !this.DotQueue.isEmpty();
	}
	*/

	@Override
	public void corrodeArmor(long timeStamp, double corrosionPercent)
	{
		this.modifyArmor(timeStamp, this.getArmor() * corrosionPercent, "from corrosive proc");
		this.numCorrosiveProcs_Armor++;
	}

	private void modifyHealth(long timeStamp, double delta, String cause)
	{
		if (!this.engageGodMode_Health)
		{
			this.setHealth(this.getHealth() + delta);

			if (this.getHealth() < 0)
			{
				delta += this.getHealth();
				this.setHealth(0);
			}
		}
		else
		{
			this.godModeDamage_Health += delta;
		}

		ReportsManager.reporty.publish(this.generateReport(timeStamp, cause, delta, "Health"));
	}

	private void modifyArmor(long timeStamp, double delta, String cause)
	{
		if (!this.engageGodMode_Armor)
		{
			this.setArmor(this.getArmor() + delta);

			if (this.getArmor() < 0)
			{
				delta += this.getArmor();
				this.setArmor(0);
			}
		}
		else
		{
			//this.numCorrosiveProcs_Armor++;
		}

		ReportsManager.reporty.publish(this.generateReport(timeStamp, cause, delta, "Armor"));
	}

	private void modifyShield(long timeStamp, double delta, String cause)
	{
		if (!this.engageGodMode_Shield)
		{
			this.setShield(this.getShield() + delta);

			if (this.getShield() < 0)
			{
				delta += this.getShield();
				this.setShield(0);
			}
		}
		else
		{
			this.godModeDamage_Shield += delta;
		}

		ReportsManager.reporty.publish(this.generateReport(timeStamp, cause, delta, "Shield"));
	}
	
	public double getNetDamage_Health()
	{
		return this.godModeDamage_Health;
	}
	
	public double getNumCorrosiveProcs_Armor()
	{
		return this.numCorrosiveProcs_Armor;
	}
	
	public double getNetDamage_Shield()
	{
		return this.godModeDamage_Shield;
	}

	public double getTotalDamageMagnitude()
	{
		double result = 0;

		if (!this.engageGodMode_Shield)
		{
			if (this.ShieldMax > 0 && this.getShield() != this.ShieldMax)
			{
				result += this.ShieldMax - this.getShield();
			}
		}
		else
		{
			result += -godModeDamage_Shield;
		}

		if (!this.engageGodMode_Health)
		{
			if (this.HealthMax != this.getHealth())
			{
				result += this.HealthMax - this.getHealth();
			}
		}
		else
		{
			result += -godModeDamage_Health;
		}

		return result;
	}

	private String getIdString()
	{
		return "MeatPuppet";
	}

	private ReportAction generateReport(long timeStamp, String cause, double delta, String subTarget)
	{
		final Actions action = decideAction(delta);
		final double locDelta = (action == Actions.Damaged) ? -delta : delta;

		return new ReportAction(timeStamp, cause, action, this.getIdString() + "'s " + subTarget, locDelta);
	}

	private static Actions decideAction(double delta)
	{
		final Actions action;

		if (delta < 0)
		{
			action = Actions.Damaged;
		}
		else if (delta > 0)
		{
			action = Actions.Healed;
		}
		else if (delta == 0)
		{
			action = Actions.Gently_Caressed;
		}
		else
		{
			throw new IllegalArgumentException();
		}

		return action;
	}

	public static double calculateBodyLocalizedCritMult(BodyPart partHit, int critLevel, double critMult)
	{
		double result;

		if (critLevel != 0)
		{
			final double mult = partHit.getMultiplier();
			result = mult * ((critLevel * critMult) - (critLevel - 1));

			if (partHit.isHead())
			{
				result *= mult;
			}
		}
		else
		{
			result = partHit.getMultiplier();
		}

		// System.out.println("hit part: " + partHit.getName() + " with mult: " + partHit.getMultiplier() + " and critLevel: " + critLevel + " to get a final mult of: " + result);

		return result;
	}

	private static ScaledDamagePacket calculateTrueDamage_helper_ShieldCalcs(UnitDamagePacket in, locMatTypes matType)
	{
		// final BasicDamagePacket result = new BasicDamagePacket();
		final double[] resultProps = new double[locDamTypes.getNumTypes()];
		double propSum = 0;

		for (locDamTypes curType : locDamTypes.getAll())
		{
			final double prop = in.getDamagePortion(curType);

			if (prop != 0.0)
			{
				final double resist = Resistances.getResistance(curType, matType);
				final double locResult;

				if (Resistances.isBypassed(resist))
				{
					locResult = prop;
				}
				else if (Resistances.isInvulnerable(resist))
				{
					locResult = prop;
				}
				else
				{
					locResult = prop / (1.0 + resist);

					propSum += prop * (1.0 + resist);
				}

				resultProps[curType.ordinal()] = locResult;
			}
		}

		return new ScaledDamagePacket(resultProps, propSum);
	}

	private static double calculateTrueDamage_helper_calculateArmoredHealthDamageMultiplier(ScaledDamagePacket adjustedProps, double armorLeft, locMatTypes HealthType, locMatTypes ArmorType)
	{
		double result = 0;

		if (armorLeft > 0)
		{
			for (locDamTypes curType : locDamTypes.getAll())
			{
				final double curProp = adjustedProps.getDamagePortion(curType);

				if (curProp > 0)
				{
					final double healthMult = Resistances.getResistance(curType, HealthType);

					if (Resistances.isBypassed(healthMult))
					{
						throw new IllegalArgumentException();
					}
					else if (!Resistances.isInvulnerable(healthMult))
					{
						final double armorMult = Resistances.getResistance(curType, ArmorType);

						result += curProp * (((1.0 + healthMult) * (1.0 + armorMult)) / (1.0 + ((armorLeft * (1.0 - armorMult)) / 300.0)));
					}
				}
			}
		}
		else
		{
			for (locDamTypes curType : locDamTypes.getAll())
			{
				final double curProp = adjustedProps.getDamagePortion(curType);

				if (curProp > 0)
				{
					final double healthMult = Resistances.getResistance(curType, HealthType);

					if (Resistances.isBypassed(healthMult))
					{
						throw new IllegalArgumentException();
					}
					else if (!Resistances.isInvulnerable(healthMult))
					{
						result += curProp * (1.0 + healthMult);
					}
				}
			}
		}

		return result;
	}

	@Override
	public CharList summarizeCurrentState(long inCurrentTime)
	{
		final CharList result = getMinSpacedTimeStamp(inCurrentTime, 15);

		result.add(':');
		result.add("\r\n\t");
		result.add("Health: ");
		result.add(Double.toString(this.getHealth()));
		result.add("\r\n\t");
		result.add("Armor: ");
		result.add(Double.toString(this.getArmor()));
		result.add(" (Damage reduction of: ");
		result.add(Double.toString(this.getArmor() / (300 + this.getArmor())));
		result.add(")");
		result.add("\r\n\t");
		result.add("Shield: ");
		result.add(Double.toString(this.getShield()));
		result.add("\r\n");
		result.add("GodMode_Health: ");
		result.add(this.engageGodMode_Health + "");

		return result;
	}

	/*
	@Override
	public long getNextSigTime(long inNextWeaponFireTime)
	{
		if (!this.DotQueue.isEmpty())
		{
			long result = this.DotQueue.peek().getNextTickTime();
	
			if (!(result < inNextWeaponFireTime))
			{
				result = inNextWeaponFireTime;
			}
	
			return result;
		}
		else
		{
			//System.out.println("going into this branch");
			return inNextWeaponFireTime;
		}
	}
	*/

	/*
	@Override
	public long getNextSigTime(long nextWeaponFireTime)
	{
		if (!this.DotQueue.isEmpty())
		{
			return this.DotQueue.peek().getNextTickTime();
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	*/

	public static basicMeatPuppetBuilder getBuilderInstance()
	{
		return new MeatPuppetBuilder();
	}

	public static basicMeatPuppetBuilder getBuilderInstance(int baseLevel)
	{
		return new MeatPuppetBuilder(baseLevel);
	}

	public static basicMeatPuppetBuilder getBuilderInstance(MeatPuppet old)
	{
		return new MeatPuppetBuilder(old);
	}

	public static class MeatPuppetBuilder extends basicMeatPuppetBuilder
	{
		public MeatPuppetBuilder()
		{
			super();
		}

		public MeatPuppetBuilder(int baseLevel)
		{
			super(baseLevel);
		}

		public MeatPuppetBuilder(MeatPuppet old)
		{
			super(old);
		}

		@Override
		public MeatPuppet buildScaledToLevel(int desiredLevel)
		{
			final double maxHealth = ScalingAutobot.scaleStat_Health(this.baseLevel, desiredLevel, this.HealthMax);
			final double maxArmor = ScalingAutobot.scaleStat_Armor(this.baseLevel, desiredLevel, this.ArmorMax);
			final double maxShield = ScalingAutobot.scaleStat_Shield(this.baseLevel, desiredLevel, this.ShieldMax);

			final double curHealth = ScalingAutobot.scaleStat_Health(this.baseLevel, desiredLevel, this.Health);
			final double curArmor = ScalingAutobot.scaleStat_Armor(this.baseLevel, desiredLevel, this.Armor);
			final double curShield = ScalingAutobot.scaleStat_Shield(this.baseLevel, desiredLevel, this.Shield);

			return new MeatPuppet(this.engageGodMode_Health, this.engageGodMode_Armor, this.engageGodMode_Shield,
					
					maxHealth, maxArmor, maxShield,

					this.HealthMat, this.ArmorMat, this.ShieldMat,

					curHealth, curArmor, curShield);
		}
	}
}
