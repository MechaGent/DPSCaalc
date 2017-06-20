package Manifests.Enemy.Mk01;

import java.util.Arrays;
import java.util.Map.Entry;

import Enums.Factions;
import Enums.locMatTypes;
import HandyStuff.FileParser;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusArray;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFactory;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFormat;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public class EnemyStats
{
	private final String mangledName;
	private final String name;
	private final Factions faction;

	private final double health;
	private final locMatTypes healthMat;
	private final double armor;
	private final locMatTypes armorMat;
	private final double shield;
	private final locMatTypes shieldMat;

	private final int baseLevel;
	private final int maxLevel;
	private final BodyPart[] bodyParts;
	private final int baseAffinity;
	
	public EnemyStats(String inMangledName, LotusFormat in)
	{
		this(inMangledName, (LotusObject) in);
	}

	public EnemyStats(String inMangledName, LotusObject in)
	{
		this.mangledName = inMangledName;
		this.name = in.getVarAsVar("name").getValueAsString();
		this.faction = Factions.parse(in.getVarAsVar("faction"));

		//final LotusObject squishContainer = in.getVarAsObject("health");

		final LotusObject healthObject = in.getVarAsObject("health");
		
		/*
		if(healthObject == null)
		{
			System.out.println("null healthObject in: " + this.mangledName);
		}
		*/
		
		this.health = healthObject.getVarAsVar("value").getValueAsDouble();
		this.healthMat = locMatTypes.parse(healthObject.getVarAsVar("material"));

		final LotusObject armorObject = in.getVarAsObject("armor");
		this.armor = armorObject.getVarAsVar("value").getValueAsDouble();
		this.armorMat = locMatTypes.parse(armorObject.getVarAsVar("material"));

		final LotusObject shieldObject = in.getVarAsObject("shield");
		final LotusVar wrapShieldValue = shieldObject.getVarAsVar("value");
		
		if(wrapShieldValue != null)
		{
			this.shield = wrapShieldValue.getValueAsDouble();
		}
		else
		{
			this.shield = 0;
		}
		
		final LotusVar wrapShieldMat = shieldObject.getVarAsVar("material");
		
		if(wrapShieldMat != null)
		{
			this.shieldMat = locMatTypes.parse(wrapShieldMat);
		}
		else
		{
			this.shieldMat = locMatTypes.Object;
		}
		

		this.baseLevel = in.getVarAsVar("baseLevel").getValueAsInt();

		final LotusVar wrapMaxLevel = in.getVarAsVar("maxLevel");

		if (wrapMaxLevel != null)
		{
			this.maxLevel = wrapMaxLevel.getValueAsInt();
		}
		else
		{
			this.maxLevel = -1;
		}

		final LotusArray wrapBodyParts = in.getVarAsArray("bodyMults");
		
		if(wrapBodyParts == null)
		{
			System.out.println("null wrapBodyParts in: " + this.mangledName);
		}
		
		this.bodyParts = BodyPart.parse(wrapBodyParts, "partName", "isHead", "bodyMult", "relSurfArea");

		final LotusVar wrapBaseAffinity = in.getVarAsVar("baseAffinity");

		if (wrapBaseAffinity != null)
		{
			this.baseAffinity = wrapBaseAffinity.getValueAsInt();
		}
		else
		{
			this.baseAffinity = -1;
		}
	}

	public String getMangledName()
	{
		return this.mangledName;
	}

	public String getName()
	{
		return this.name;
	}

	public Factions getFaction()
	{
		return this.faction;
	}

	public double getBaseHealth()
	{
		return this.health;
	}
	
	public double getHealthAtLevel(int level)
	{
		if(this.isGoodLevel(level))
		{
			return ScalingVars.Health.scaleValueToLevel(this.baseLevel, level, this.health);
		}
		else
		{
			throw new IndexOutOfBoundsException("tried for level " + level + ", when max was " + this.maxLevel);
		}
	}
	
	private boolean isGoodLevel(int desiredLevel)
	{
		final boolean boundedByBase = this.baseLevel <= desiredLevel;
		final boolean boundedByMax = (this.maxLevel == -1) || (desiredLevel <= this.maxLevel);
		return boundedByBase && boundedByMax;
	}

	public locMatTypes getHealthMat()
	{
		return this.healthMat;
	}

	public double getBaseArmor()
	{
		return this.armor;
	}
	
	public double getArmorAtLevel(int level)
	{
		if(this.isGoodLevel(level))
		{
			return ScalingVars.Armor.scaleValueToLevel(this.baseLevel, level, this.armor);
		}
		else
		{
			throw new IndexOutOfBoundsException("tried for level " + level + ", when max was " + this.maxLevel);
		}
	}

	public locMatTypes getArmorMat()
	{
		return this.armorMat;
	}

	public double getBaseShield()
	{
		return this.shield;
	}
	
	public double getShieldAtLevel(int level)
	{
		if(this.isGoodLevel(level))
		{
			return ScalingVars.Shield.scaleValueToLevel(this.baseLevel, level, this.shield);
		}
		else
		{
			throw new IndexOutOfBoundsException("tried for level " + level + ", when max was " + this.maxLevel);
		}
	}

	public locMatTypes getShieldMat()
	{
		return this.shieldMat;
	}

	public int getBaseLevel()
	{
		return this.baseLevel;
	}

	public boolean hasLevelCap()
	{
		return this.maxLevel != -1;
	}
	
	public int getMaxLevel()
	{
		return this.maxLevel;
	}

	public BodyPart[] getBodyParts()
	{
		return this.bodyParts;
	}

	public int getBaseAffinity()
	{
		return this.baseAffinity;
	}
	
	@Override
	public String toString()
	{
		return "EnemyStats [mangledName=" + this.mangledName + ", name=" + this.name + ", faction=" + this.faction + ", health=" + this.health + ", healthMat=" + this.healthMat + ", armor=" + this.armor + ", armorMat=" + this.armorMat + ", shield=" + this.shield + ", shieldMat=" + this.shieldMat + ", baseLevel=" + this.baseLevel + ", maxLevel=" + this.maxLevel + ", bodyParts=" + Arrays.toString(this.bodyParts) + ", baseAffinity=" + this.baseAffinity + "]";
	}
	
	private static enum ScalingVars
	{
		Health(0.015, 2.0),
		Armor(0.005, 1.75),
		Shield(0.0075, 2.0),
		Damage(0.015, 1.55),
		Affinity(0.1425, 0.5);
		
		private final double coefficient;
		private final double exponent;
		
		private ScalingVars(double inCoefficient, double inExponent)
		{
			this.coefficient = inCoefficient;
			this.exponent = inExponent;
		}
		
		public double scaleValueToLevel(int baseLevel, int desiredLevel, double value)
		{
			return value * (1.0 + (this.coefficient * Math.pow(desiredLevel - baseLevel, this.exponent)));
		}
	}

	public static void main(String[] args)
	{
		final String dir = "H:/Users/Thrawnboo/workspace - Java/DPSCaalc/src/Manifests/DataFiles/enemyManifest.txt";
		final String raw = FileParser.parseFileAsString(dir);
		final LotusObject wrap = LotusFactory.createNewLotusObject(raw).getVarAsObject("enemies");
		//System.out.println(wrap.toCharList(0, true).toString());
		
		for(Entry<String, LotusFormat> entry: wrap.getEntrySet())
		{
			final EnemyStats curStats = new EnemyStats(entry.getKey(), (LotusObject) entry.getValue());
			System.out.println(curStats.toString());
		}
	}
}
