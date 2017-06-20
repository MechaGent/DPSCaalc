package Mk00.Autobots;

public class ScalingAutobot
{
	private static final double scaleCoeff_Health = 0.015;
	private static final double scaleExpon_Health = 2.0;
	
	private static final double scaleCoeff_Armor = 0.005;
	private static final double scaleExpon_Armor = 1.75;
	
	private static final double scaleCoeff_Shield = 0.0075;
	private static final double scaleExpon_Shield = 2.0;
	
	private static final double scaleCoeff_DamageMult = 0.015;
	private static final double scaleExpon_DamageMult = 1.55;
	
	private static final double scaleCoeff_Affinity = 0.1425;
	private static final double scaleExpon_Affinity = 0.5;
	
	public static double scaleStat_Health(int baseLevel, int desiredLevel, double healthStat)
	{
		return scaleStat(baseLevel, desiredLevel, scaleCoeff_Health, scaleExpon_Health, healthStat);
	}
	
	public static double scaleStat_Armor(int baseLevel, int desiredLevel, double armorStat)
	{
		return scaleStat(baseLevel, desiredLevel, scaleCoeff_Armor, scaleExpon_Armor, armorStat);
	}
	
	public static double scaleStat_Shield(int baseLevel, int desiredLevel, double shieldStat)
	{
		return scaleStat(baseLevel, desiredLevel, scaleCoeff_Shield, scaleExpon_Shield, shieldStat);
	}
	
	public static double scaleStat_DamageMult(int baseLevel, int desiredLevel, double damageMultStat)
	{
		return scaleStat(baseLevel, desiredLevel, scaleCoeff_DamageMult, scaleExpon_DamageMult, damageMultStat);
	}
	
	public static double scaleStat_Affinity(int desiredLevel, double AffinityStat)
	{
		return Math.floor(scaleStat(0, desiredLevel, scaleCoeff_Affinity, scaleExpon_Affinity, AffinityStat));
	}
	
	private static double scaleStat(int baseLevel, int desiredLevel, double coeff, double expon, double baseStat)
	{
		return baseStat * (1.0 + (coeff * Math.pow(desiredLevel - baseLevel, expon)));
	}
}
