package Mk01.Enemy;

import DamagePackets.Packets.crittableDamagePacket;
import DamagePackets.Packets.scaledDamagePacket;
import DamagePackets.Packets.unitDamagePacket;
import DamagePackets.Points.crittableDamagePoint;
import DamagePackets.Points.unitDamagePoint;
import Enums.locDamTypes;
import Enums.locMatTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Manifests.Enemy.Mk01.EnemyStats;
import Manifests.Resistances.Mk01.Resistances;
import Mk01.ReportsManager.Reporter;
import Mk01.Weapons.ShotRecord;

/**
 * This class handles all damage calculations, before handing off the trueDamage to baseMeatPuppet.
 * 
 * @author MechaGent
 *
 */
public class MeatPuppet extends baseMeatPuppet
{
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
	public MeatPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, EnemyStats inStats)
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
	public MeatPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, EnemyStats inStats, double curHealth, double curArmor, double curShield)
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
	public MeatPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, BodyPart[] inBodyParts)
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
	public MeatPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, double inHealth, double inArmor, double inShield, BodyPart[] inBodyParts)
	{
		super(inId, inReporty,

				inEngageGodMode_Health, inEngageGodMode_Armor, inEngageGodMode_Shield,

				inHealthMax, inArmorMax, inShieldMax,

				inHealthMat, inArmorMat, inShieldMat,

				inHealth, inArmor, inShield,

				inBodyParts);
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
	public MeatPuppet(coreMeatPuppet old, String inId, Reporter inReporty)
	{
		super(old, inId, inReporty);
	}

	public void inflictDamage(long inTime, ShotRecord inDamage, String InflictorId, String cause)
	{
		this.inflictDamage(inTime, inDamage.getDamage(), inDamage.getPartHit(), InflictorId, cause);
	}
	
	public void inflictDamage(long inTimeStamp, BodyPart inPartHit, crittableDamagePoint inDamage, String InflictorId, String cause)
	{
		final double locCritMult = calculateBodyLocalizedCritMult(inPartHit, inDamage.getCritLevel(), inDamage.getCritMult());

		this.inflictDamage(inTimeStamp, inDamage.getValue() * locCritMult, inDamage.getFlavor(), InflictorId, cause);
	}

	public void inflictDamage(long inTime, crittableDamagePacket inDamage, BodyPart partHit, String InflictorId, String cause)
	{
		final double scale = inDamage.getScalar();
		final double critMult = calculateBodyLocalizedCritMult(partHit, inDamage.getCritLevel(), inDamage.getCritMult());
		
		if (this.hasShield())
		{
			final scaledDamagePacket shieldAppliedProps = calculateTrueDamage_helper_ShieldCalcs(inDamage, this.ShieldMat);
			final double shieldDam = scale * critMult * shieldAppliedProps.getScalar();
			final double shieldDelta = this.getShield() - shieldDam;

			if (!(shieldDelta < 0))
			{
				this.damageHealthComponent(inTime, EnemyDamageTargets.Shield, shieldDam, InflictorId, cause);
			}
			else
			{
				this.damageHealthComponent(inTime, EnemyDamageTargets.Shield, this.getShield(), InflictorId, cause);
				final double adjustedHealthDamMult = calculateTrueDamage_helper_calculateArmoredHealthDamageMultiplier(shieldAppliedProps, this.getArmor(), this.HealthMat, this.ArmorMat);
				final double healthDam = -shieldDelta * adjustedHealthDamMult;

				this.damageHealthComponent(inTime, EnemyDamageTargets.Health, healthDam, InflictorId, cause);
			}
		}
		else
		{
			final double adjustedHealthDamMult = calculateTrueDamage_helper_calculateArmoredHealthDamageMultiplier(inDamage, this.getArmor(), this.HealthMat, this.ArmorMat);
			final double healthDam = inDamage.getScalar() * adjustedHealthDamMult * critMult;

			this.damageHealthComponent(inTime, EnemyDamageTargets.Health, healthDam, InflictorId, cause);
		}
	}
	
	public void inflictDamage(long timeStamp, double inAmount, locDamTypes inFlavor, String InflictorId, String cause)
	{
		final double shieldMult = Resistances.getResistance(inFlavor, this.ShieldMat);
		final boolean bypassesShield = Resistances.isBypassed(shieldMult);

		if (!bypassesShield && this.hasShield())
		{
			final double shieldDam = inAmount * (1.0 + shieldMult);
			final double shieldDelta = this.getShield() - shieldDam;

			if (!(shieldDelta < 0))
			{
				this.damageHealthComponent(timeStamp, EnemyDamageTargets.Shield, shieldDam, InflictorId, cause);
			}
			else
			{
				this.damageHealthComponent(timeStamp, EnemyDamageTargets.Shield, this.getShield(), InflictorId, cause);
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

				this.damageHealthComponent(timeStamp, EnemyDamageTargets.Health, healthDamage, InflictorId, cause);
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

			this.damageHealthComponent(timeStamp, EnemyDamageTargets.Health, healthDamage, InflictorId, cause);
		}
	}

	/*
	 * Static methods
	 */

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

	private static scaledDamagePacket calculateTrueDamage_helper_ShieldCalcs(unitDamagePacket in, locMatTypes matType)
	{
		// final BasicDamagePacket result = new BasicDamagePacket();
		final unitDamagePoint[] resultProps = new unitDamagePoint[in.getNumFlavors()];
		double propSum = 0;

		for (int i = 0; i < resultProps.length; i++)
		{
			final unitDamagePoint cur = in.getPointAt(i);

			final double resist = Resistances.getResistance(cur.getFlavor(), matType);
			final double locResult;

			if (Resistances.isBypassed(resist))
			{
				locResult = cur.getValue();
			}
			else if (Resistances.isInvulnerable(resist))
			{
				locResult = cur.getValue();
			}
			else
			{
				locResult = cur.getValue() / (1.0 + resist);

				propSum += cur.getValue() * (1.0 + resist);
			}

			resultProps[i] = new unitDamagePoint(cur.getFlavor(), locResult);
		}

		return new scaledDamagePacket(propSum, resultProps);
	}

	private static double calculateTrueDamage_helper_calculateArmoredHealthDamageMultiplier(scaledDamagePacket adjustedProps, double armorLeft, locMatTypes HealthType, locMatTypes ArmorType)
	{
		double result = 0;

		if (armorLeft > 0)
		{
			for (int i = 0; i < adjustedProps.getNumFlavors(); i++)
			{
				final unitDamagePoint curProp = adjustedProps.getPointAt(i);

				final double healthMult = Resistances.getResistance(curProp.getFlavor(), HealthType);

				if (Resistances.isBypassed(healthMult))
				{
					throw new IllegalArgumentException();
				}
				else if (!Resistances.isInvulnerable(healthMult))
				{
					final double armorMult = Resistances.getResistance(curProp.getFlavor(), ArmorType);

					result += curProp.getValue() * (((1.0 + healthMult) * (1.0 + armorMult)) / (1.0 + ((armorLeft * (1.0 - armorMult)) / 300.0)));
				}
			}
		}
		else
		{
			for (int i = 0; i < adjustedProps.getNumFlavors(); i++)
			{
				final unitDamagePoint curProp = adjustedProps.getPointAt(i);

				final double healthMult = Resistances.getResistance(curProp.getFlavor(), HealthType);

				if (Resistances.isBypassed(healthMult))
				{
					throw new IllegalArgumentException();
				}
				else if (!Resistances.isInvulnerable(healthMult))
				{
					result += curProp.getValue() * (1.0 + healthMult);
				}
			}
		}

		return result;
	}
}
