package Mk01.Enemy;

import Enums.locMatTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Manifests.Enemy.Mk01.EnemyStats;

public class coreMeatPuppet
{
	protected final boolean engageGodMode_Health;
	protected final boolean engageGodMode_Armor;
	protected final boolean engageGodMode_Shield;

	protected final double HealthMax;
	protected final double ArmorMax;
	protected final double ShieldMax;

	protected double speedMult_Movement;
	protected double confusionLevel;
	protected double outputDamageMult;

	protected final locMatTypes HealthMat;
	protected final locMatTypes ArmorMat;
	protected final locMatTypes ShieldMat;

	protected double Health;
	protected double Armor;
	protected double Shield;

	protected final BodyPart[] bodyParts;

	public coreMeatPuppet(boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, double inSpeedMult_Movement, double inConfusionLevel, double inOutputDamageMult, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, double inHealth, double inArmor, double inShield, BodyPart[] inBodyParts)
	{
		this.engageGodMode_Health = inEngageGodMode_Health;
		this.engageGodMode_Armor = inEngageGodMode_Armor;
		this.engageGodMode_Shield = inEngageGodMode_Shield;
		this.HealthMax = inHealthMax;
		this.ArmorMax = inArmorMax;
		this.ShieldMax = inShieldMax;
		this.speedMult_Movement = inSpeedMult_Movement;
		this.confusionLevel = inConfusionLevel;
		this.outputDamageMult = inOutputDamageMult;
		this.HealthMat = inHealthMat;
		this.ArmorMat = inArmorMat;
		this.ShieldMat = inShieldMat;
		this.Health = inHealth;
		this.Armor = inArmor;
		this.Shield = inShield;
		this.bodyParts = inBodyParts;
	}
	
	public coreMeatPuppet(boolean[] godModeToggles, EnemyStats inStats, int enemyLevel)
	{
		this.engageGodMode_Health = godModeToggles[0];
		this.engageGodMode_Armor = godModeToggles[1];
		this.engageGodMode_Shield = godModeToggles[2];
		
		this.HealthMax = inStats.getHealthAtLevel(enemyLevel);
		this.ArmorMax = inStats.getArmorAtLevel(enemyLevel);
		this.ShieldMax = inStats.getShieldAtLevel(enemyLevel);
		
		this.speedMult_Movement = 1.0;
		this.confusionLevel = 0.0;
		this.outputDamageMult = 1.0;
		
		this.HealthMat = inStats.getHealthMat();
		this.ArmorMat = inStats.getArmorMat();
		this.ShieldMat = inStats.getShieldMat();
		
		this.Health = this.HealthMax;
		this.Armor = this.ArmorMax;
		this.Shield = this.ShieldMax;
		this.bodyParts = inStats.getBodyParts();
	}
	
	public coreMeatPuppet(coreMeatPuppet old)
	{
		this.engageGodMode_Health = old.engageGodMode_Health;
		this.engageGodMode_Armor = old.engageGodMode_Armor;
		this.engageGodMode_Shield = old.engageGodMode_Shield;
		
		this.HealthMax = old.HealthMax;
		this.ArmorMax = old.ArmorMax;
		this.ShieldMax = old.ShieldMax;
		
		this.speedMult_Movement = old.speedMult_Movement;
		this.confusionLevel = old.confusionLevel;
		this.outputDamageMult = old.outputDamageMult;
		
		this.HealthMat = old.HealthMat;
		this.ArmorMat = old.ArmorMat;
		this.ShieldMat = old.ShieldMat;
		
		this.Health = old.Health;
		this.Armor = old.Armor;
		this.Shield = old.Shield;
		this.bodyParts = old.bodyParts;
	}
	
	public BodyPart[] getBodyParts()
	{
		return this.bodyParts;
	}
}
