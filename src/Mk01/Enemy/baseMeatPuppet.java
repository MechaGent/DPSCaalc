package Mk01.Enemy;

import CustomExceptions.UnhandledEnum.Mk01.UnhandledEnumException;
import Enums.locMatTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Manifests.Enemy.Mk01.EnemyStats;
import Mk01.ReportsManager.Reporter;
import Mk01.Reports.DirectStatManip.multiplicativeStatChangeReport;
import Mk01.Reports.DirectStatManip.additiveStatChangeReport;
import Mk01.Reports.Report;
import Mk01.Reports.Report.ReportCategories;
import Mk01.Reports.CircleOfLife.EnemyDeathReport;

/**
 * This class handles an enemy's healthComponent state, namely their health, armor, and shield stats, and is intended to be responsible for direct manipulation only.
 * 
 * @author MechaGent
 *
 */
public abstract class baseMeatPuppet extends coreMeatPuppet
{
	protected final String Id;
	protected final Reporter reporty;

	private double godModeDamage_Health;
	private double godModeDamage_Shield;
	
	private boolean hasDied;

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
	public baseMeatPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, EnemyStats inStats)
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
	public baseMeatPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, EnemyStats inStats, double curHealth, double curArmor, double curShield)
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
	public baseMeatPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, BodyPart[] inBodyParts)
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
	public baseMeatPuppet(String inId, Reporter inReporty, boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, double inHealth, double inArmor, double inShield, BodyPart[] inBodyParts)
	{
		super(inEngageGodMode_Health, inEngageGodMode_Armor, inEngageGodMode_Shield, inHealthMax, inArmorMax, inShieldMax, 1.0, 0.0, 1.0, inHealthMat, inArmorMat, inShieldMat, inHealth, inArmor, inShield, inBodyParts);
		this.Id = inId;
		this.reporty = inReporty;

		this.godModeDamage_Health = 0;
		this.godModeDamage_Shield = 0;
		
		this.hasDied = false;
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
	public baseMeatPuppet(coreMeatPuppet old, String inId, Reporter inReporty)
	{
		super(old);
		this.Id = inId;
		this.reporty = inReporty;

		this.godModeDamage_Health = 0;
		this.godModeDamage_Shield = 0;
	}

	protected static String getIdName(int IdNum)
	{
		return "Enemy" + Integer.toString(IdNum);
	}

	/**
	 * Convenience overload for when no additional cause explanation is needed
	 * <br>
	 * exactly like calling {@link #damageHealthComponent(long, EnemyDamageTargets, double, String, String) this.damageHealthComponent(inTime, healthComponent, delta, InflictorId, "")}
	 * 
	 * @param inTime
	 * @param healthComponent
	 * @param delta
	 * @param InflictorId
	 */
	public void damageHealthComponent(long inTime, EnemyDamageTargets healthComponent, double delta, String InflictorId)
	{
		this.damageHealthComponent(inTime, healthComponent, delta, InflictorId, "");
	}

	/**
	 * subtracts {@code delta} from health component indicated by {@code healthComponent}, and dispatches appropriate report
	 * 
	 * @param inTime
	 *            the timestamp during which the damage occurred
	 * @param healthComponent
	 *            the component being effected
	 * @param delta
	 *            the magnitude of the effect
	 * @param InflictorId
	 *            the Id of the inflicting object
	 * @param cause
	 *            any additional clarification, if needed. Pass an empty {@code String} if unneeded.
	 */
	public void damageHealthComponent(long inTime, EnemyDamageTargets healthComponent, double delta, String InflictorId, String cause)
	{
		switch (healthComponent)
		{
			case Health:
			{
				if (!this.engageGodMode_Health)
				{
					this.Health -= delta;
					
					if(!this.hasDied && !(this.Health > 0))
					{
						final Report deathReport = new EnemyDeathReport(inTime, this.Id);
						this.hasDied = true;
						this.reporty.publish(deathReport);
					}
				}
				else
				{
					this.godModeDamage_Health += delta;
				}

				break;
			}
			case Armor:
			{
				if (!this.engageGodMode_Armor)
				{
					this.Armor -= delta;
				}
				break;
			}
			case Shield:
			{
				if (!this.engageGodMode_Shield)
				{
					this.Shield -= delta;
				}
				else
				{
					this.godModeDamage_Shield += delta;
				}

				break;
			}
			case SpeedMult_Movement:
			{
				this.speedMult_Movement -= delta;
				break;
			}
			case ConfusionLevel:
			{
				this.confusionLevel += delta;
				break;
			}
			case outputDamageMult:
			{
				this.outputDamageMult -= delta;
				break;
			}
			default:
			{
				throw new UnhandledEnumException(healthComponent);
			}
		}

		final additiveStatChangeReport repo = additiveStatChangeReport.getDamageReportInstance(inTime, ReportCategories.DamageReport, this.Id, delta, healthComponent, InflictorId, cause);
		this.reporty.publish(repo);
		//System.out.println("triggered");
	}

	/**
	 * Convenience overload for when no additional cause explanation is needed
	 * <br>
	 * exactly like calling {@link #healHealthComponent(long, EnemyDamageTargets, double, String, String) this.healHealthComponent(inTime, healthComponent, delta, InflictorId, "")}
	 * 
	 * @param inTime
	 * @param healthComponent
	 * @param delta
	 * @param InflictorId
	 */
	public void healHealthComponent(long inTime, EnemyDamageTargets healthComponent, double delta, String InflictorId)
	{
		this.healHealthComponent(inTime, healthComponent, delta, InflictorId, "");
	}

	/**
	 * adds {@code delta} to health component indicated by {@code healthComponent}, and dispatches appropriate report
	 * 
	 * @param inTime
	 *            the timestamp during which the healing occurred
	 * @param healthComponent
	 *            the component being effected
	 * @param delta
	 *            the magnitude of the effect
	 * @param InflictorId
	 *            the Id of the inflicting object
	 * @param cause
	 *            any additional clarification, if needed. Pass an empty {@code String} if unneeded.
	 */
	public void healHealthComponent(long inTime, EnemyDamageTargets healthComponent, double delta, String InflictorId, String cause)
	{
		switch (healthComponent)
		{
			case Health:
			{
				if (!this.engageGodMode_Health)
				{
					this.Health += delta;
				}

				break;
			}
			case Armor:
			{
				if (!this.engageGodMode_Armor)
				{
					this.Armor += delta;
				}
				break;
			}
			case Shield:
			{
				if (!this.engageGodMode_Shield)
				{
					this.Shield += delta;
				}
				break;
			}
			case SpeedMult_Movement:
			{
				this.speedMult_Movement += delta;
				break;
			}
			case ConfusionLevel:
			{
				this.confusionLevel -= delta;
				break;
			}
			case outputDamageMult:
			{
				this.outputDamageMult += delta;
				break;
			}
			default:
			{
				throw new UnhandledEnumException(healthComponent);
			}
		}

		final additiveStatChangeReport repo = additiveStatChangeReport.getHealingReportInstance(inTime, ReportCategories.DamageReport, this.Id, delta, healthComponent, InflictorId, cause);
		this.reporty.publish(repo);
	}

	/**
	 * Convenience overload for when no additional cause explanation is needed
	 * <br>
	 * exactly like calling {@link #degradeHealthComponent(long, EnemyDamageTargets, double, String, String) this.degradeHealthComponent(inTime, healthComponent, degradeBy, InflictorId, "")}
	 * 
	 * @param inTime
	 *            the timestamp during which the debuff occurred
	 * @param healthComponent
	 *            the component being effected
	 * @param degradeBy
	 *            the percentage by which the healthComponent is being degraded
	 * @param InflictorId
	 *            the Id of the inflicting object
	 */
	public void degradeHealthComponent(long inTime, EnemyDamageTargets healthComponent, double degradeBy, String InflictorId)
	{
		this.degradeHealthComponent(inTime, healthComponent, degradeBy, InflictorId, "");
	}

	/**
	 * reduces health component (indicated by {@code healthComponent}) by ({@code degradeBy} * 100)%, and dispatches appropriate report
	 * 
	 * @param inTime
	 *            the timestamp during which the debuff occurred
	 * @param healthComponent
	 *            the component being effected
	 * @param degradeBy
	 *            the percentage by which the healthComponent is being degraded
	 * @param InflictorId
	 *            the Id of the inflicting object
	 * @param cause
	 *            any additional clarification, if needed. Pass an empty {@code String} if unneeded.
	 * 
	 */
	public void degradeHealthComponent(long inTime, EnemyDamageTargets healthComponent, double degradeBy, String InflictorId, String cause)
	{
		final double oldStat;
		final double newStat;

		switch (healthComponent)
		{
			case Health:
			{
				oldStat = this.Health;

				if (!this.engageGodMode_Health)
				{
					this.Health *= 1.0 - degradeBy;
				}

				newStat = this.Health;

				break;
			}
			case Armor:
			{
				oldStat = this.Armor;

				if (!this.engageGodMode_Armor)
				{
					this.Armor *= 1.0 - degradeBy;
				}

				newStat = this.Armor;

				break;
			}
			case Shield:
			{
				oldStat = this.Shield;

				if (!this.engageGodMode_Shield)
				{
					this.Shield *= 1.0 - degradeBy;
				}

				newStat = this.Shield;

				break;
			}
			default:
			{
				throw new UnhandledEnumException(healthComponent);
			}
		}

		final multiplicativeStatChangeReport repo = multiplicativeStatChangeReport.getDegradeInstance(inTime, ReportCategories.DamageReport, this.Id, oldStat, degradeBy, newStat, healthComponent, InflictorId, cause);
		this.reporty.publish(repo);
	}

	/**
	 * Convenience overload for when no additional cause explanation is needed
	 * <br>
	 * exactly like calling {@link #strengthenHealthComponent(long, EnemyDamageTargets, double, String, String) this.strengthenHealthComponent(inTime, healthComponent, strengthenBy, InflictorId, "")}
	 * 
	 * @param inTime
	 *            the timestamp during which the buff occurred
	 * @param healthComponent
	 *            the component being effected
	 * @param strengthenBy
	 *            the percentage by which the healthComponent is being strengthened
	 * @param InflictorId
	 *            the Id of the inflicting object
	 */
	public void strengthenHealthComponent(long inTime, EnemyDamageTargets healthComponent, double strengthenBy, String InflictorId)
	{
		this.strengthenHealthComponent(inTime, healthComponent, strengthenBy, InflictorId, "");
	}

	/**
	 * strengthens health component (indicated by {@code healthComponent}) by ({@code strengthenBy} * 100)%, and dispatches appropriate report
	 * 
	 * @param inTime
	 *            the timestamp during which the buff occurred
	 * @param healthComponent
	 *            the component being effected
	 * @param strengthenBy
	 *            the percentage by which the healthComponent is being strengthened
	 * @param InflictorId
	 *            the Id of the inflicting object
	 * @param cause
	 *            any additional clarification, if needed. Pass an empty {@code String} if unneeded.
	 */
	public void strengthenHealthComponent(long inTime, EnemyDamageTargets healthComponent, double strengthenBy, String InflictorId, String cause)
	{
		final double oldStat;
		final double newStat;

		switch (healthComponent)
		{
			case Health:
			{
				oldStat = this.Health;

				if (!this.engageGodMode_Health)
				{
					this.Health *= 1.0 + strengthenBy;
				}

				newStat = this.Health;

				break;
			}
			case Armor:
			{
				oldStat = this.Armor;

				if (!this.engageGodMode_Armor)
				{
					this.Armor *= 1.0 + strengthenBy;
				}

				newStat = this.Armor;

				break;
			}
			case Shield:
			{
				oldStat = this.Shield;

				if (!this.engageGodMode_Shield)
				{
					this.Shield *= 1.0 + strengthenBy;
				}

				newStat = this.Shield;

				break;
			}
			default:
			{
				throw new UnhandledEnumException(healthComponent);
			}
		}

		final multiplicativeStatChangeReport repo = multiplicativeStatChangeReport.getStrengthenInstance(inTime, ReportCategories.DamageReport, this.Id, oldStat, strengthenBy, newStat, healthComponent, InflictorId, cause);
		this.reporty.publish(repo);
	}

	public boolean GodMode_Health_isEngaged()
	{
		return this.engageGodMode_Health;
	}

	public boolean GodMode_Armor_isEngaged()
	{
		return this.engageGodMode_Armor;
	}

	public boolean GodMode_Shield_isEngaged()
	{
		return this.engageGodMode_Shield;
	}

	public double getHealthMax()
	{
		return this.HealthMax;
	}

	public double getArmorMax()
	{
		return this.ArmorMax;
	}

	public double getShieldMax()
	{
		return this.ShieldMax;
	}

	public locMatTypes getHealthMat()
	{
		return this.HealthMat;
	}

	public locMatTypes getArmorMat()
	{
		return this.ArmorMat;
	}

	public locMatTypes getShieldMat()
	{
		return this.ShieldMat;
	}

	public double getSpeedMult_Movement()
	{
		return this.speedMult_Movement;
	}

	public double getConfusionLevel()
	{
		return this.confusionLevel;
	}

	public double getOutputDamageMult()
	{
		return this.outputDamageMult;
	}

	public double getHealth()
	{
		return this.Health;
	}

	public double getArmor()
	{
		return this.Armor;
	}

	public double getShield()
	{
		return this.Shield;
	}

	public double getGodModeDamage_Health()
	{
		return this.godModeDamage_Health;
	}

	public double getGodModeDamage_Shield()
	{
		return this.godModeDamage_Shield;
	}

	public Reporter getReporty()
	{
		return this.reporty;
	}

	public boolean hasHealth()
	{
		return this.Health > 0;
	}

	public boolean hasArmor()
	{
		return this.Armor > 0;
	}

	public boolean hasShield()
	{
		return this.Shield > 0;
	}

	public static enum EnemyDamageTargets
	{
		Health,
		Armor,
		Shield,
		SpeedMult_Movement,
		ConfusionLevel,
		outputDamageMult;
	}
}
