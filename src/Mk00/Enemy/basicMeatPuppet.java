package Mk00.Enemy;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.locDamTypes;
import Enums.locMatTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Mk00.Damage.Packets.CrittableDamagePacket;
import Mk00.Damage.Points.CrittableDamagePoint;
import Mk00.Damage.Procs.ProcManager;

public abstract class basicMeatPuppet
{
	protected final boolean engageGodMode_Health;
	protected final boolean engageGodMode_Armor;
	protected final boolean engageGodMode_Shield;

	protected final double HealthMax;
	protected final double ArmorMax;
	protected final double ShieldMax;

	protected final locMatTypes HealthMat;
	protected final locMatTypes ArmorMat;
	protected final locMatTypes ShieldMat;

	private double Health;
	private double Armor;
	private double Shield;
	
	private boolean isConfused;

	public basicMeatPuppet(double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat)
	{
		this(false, false, false, inHealthMax, inArmorMax, inShieldMax, inHealthMat, inArmorMat, inShieldMat, inHealthMax, inArmorMax, inShieldMax);
	}
	
	public basicMeatPuppet(basicMeatPuppet in)
	{
		this.engageGodMode_Health = in.engageGodMode_Health;
		this.engageGodMode_Armor = in.engageGodMode_Armor;
		this.engageGodMode_Shield = in.engageGodMode_Shield;
		this.HealthMax = in.HealthMax;
		this.ArmorMax = in.ArmorMax;
		this.ShieldMax = in.ShieldMax;
		this.HealthMat = in.HealthMat;
		this.ArmorMat = in.ArmorMat;
		this.ShieldMat = in.ShieldMat;
		this.Health = in.Health;
		this.Armor = in.Armor;
		this.Shield = in.Shield;
		this.isConfused = false;
	}

	public basicMeatPuppet(boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, double inHealth, double inArmor, double inShield)
	{
		this.engageGodMode_Health = inEngageGodMode_Health;
		this.engageGodMode_Armor = inEngageGodMode_Armor;
		this.engageGodMode_Shield = inEngageGodMode_Shield;
		this.HealthMax = inHealthMax;
		this.ArmorMax = inArmorMax;
		this.ShieldMax = inShieldMax;
		this.HealthMat = inHealthMat;
		this.ArmorMat = inArmorMat;
		this.ShieldMat = inShieldMat;
		this.Health = inHealth;
		this.Armor = inArmor;
		this.Shield = inShield;
		this.isConfused = false;
	}

	public void setHealth(double inHealth)
	{
		if (!engageGodMode_Health)
		{
			this.Health = inHealth;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	public void setArmor(double inArmor)
	{
		this.Armor = inArmor;
	}

	public void setShield(double inShield)
	{
		this.Shield = inShield;
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
	
	public boolean isConfused()
	{
		return this.isConfused;
	}

	public boolean hasShield()
	{
		return this.Shield > 0;
	}

	public boolean stillLives()
	{
		return this.Health > 0;
	}

	protected static CharList getMinSpacedTimeStamp(long currentTime, int minSpace)
	{
		final CharList result = new CharList();

		if (currentTime >= 0)
		{
			result.add(Long.toString(currentTime));
			final int delta = minSpace - result.charSize();

			if (delta > 0)
			{
				result.push('0', delta);
			}
		}
		else
		{
			result.add(Long.toString(-currentTime));

			final int delta = minSpace - result.charSize() - 1;

			if (delta > 0)
			{
				result.push('0', delta);
			}

			result.push('-');
		}

		result.push('[');
		result.add(']');

		return result;
	}
	
	public void applyColdProc()
	{
		//ProcManager.getColdProcApplicatorMult();
	}
	
	public void unapplyColdProc()
	{
		//ProcManager.getColdProcDeApplicatorMult();
	}
	
	public void applyConfusion()
	{
		this.isConfused = true;
	}
	
	public void unapplyConfusion()
	{
		this.isConfused = false;
	}

	public void applyMagneticProc()
	{
		this.Shield *= ProcManager.getMagneticProcApplicatorMult();
	}

	public void unapplyMagneticProc()
	{
		this.Shield *= ProcManager.getMagneticProcDeApplicatorMult();
	}

	public void applyViralProc()
	{
		this.Health *= ProcManager.getViralProcApplicatorMult();
	}

	public void unapplyViralProc()
	{
		this.Health *= ProcManager.getViralProcDeApplicatorMult();
	}
	
	public void applyWeaknessProc()
	{
		//ProcManager.getWeaknessProcApplicatorMult();
	}
	
	public void unapplyWeaknessProc()
	{
		//ProcManager.getWeaknessProcDeApplicatorMult();
	}

	public abstract void inflictDamage(long timeStamp, BodyPart partHit, CrittableDamagePacket damage, String cause);

	public abstract void inflictDamage(long timeStamp, BodyPart partHit, CrittableDamagePoint damage, String cause);

	public abstract void inflictDamage(long timeStamp, double amount, locDamTypes flavor, String cause);

	//public abstract void inflictDamageOverTime(long timeStamp, BodyPart partHit, CrittableDamagePoint damage, int numTicks, long tickInterval, String cause);

	public abstract CharList summarizeCurrentState(long currentTime);

	//public abstract long getNextSigTime(long nextWeaponFireTime);

	// public abstract void processAllCurrentDots(long currentTime);

	//public abstract boolean hasActiveDots();

	public abstract void corrodeArmor(long timeStamp, double corrosionPercent);

	public static basicMeatPuppetBuilder getBuilderInstance()
	{
		return MeatPuppet.getBuilderInstance();
	}

	public static basicMeatPuppetBuilder getBuilderInstance(int baseLevel)
	{
		return MeatPuppet.getBuilderInstance(baseLevel);
	}

	public static basicMeatPuppetBuilder getBuilderInstance(basicMeatPuppet old)
	{
		return MeatPuppet.getBuilderInstance(old);
	}

	public static abstract class basicMeatPuppetBuilder
	{
		protected boolean engageGodMode_Health;
		protected boolean engageGodMode_Armor;
		protected boolean engageGodMode_Shield;
		
		protected double HealthMax;
		protected double ArmorMax;
		protected double ShieldMax;

		protected locMatTypes HealthMat;
		protected locMatTypes ArmorMat;
		protected locMatTypes ShieldMat;

		protected double Health;
		protected double Armor;
		protected double Shield;

		protected final int baseLevel;

		public basicMeatPuppetBuilder()
		{
			this(1);
		}

		public basicMeatPuppetBuilder(int inBaseLevel)
		{
			this(false, false, false,
					
					0, 0, 0,

					locMatTypes.Object, locMatTypes.Object, locMatTypes.Object,

					0, 0, 0, inBaseLevel);
		}

		public basicMeatPuppetBuilder(basicMeatPuppet old)
		{

			this(old.engageGodMode_Health, old.engageGodMode_Armor, old.engageGodMode_Shield,
					
					old.HealthMax, old.ArmorMax, old.ShieldMax,

					old.HealthMat, old.ArmorMat, old.ShieldMat,

					old.Health, old.Armor, old.Shield, 1);
		}

		private basicMeatPuppetBuilder(boolean inEngageGodMode_Health, boolean inEngageGodMode_Armor, boolean inEngageGodMode_Shield, double inHealthMax, double inArmorMax, double inShieldMax, locMatTypes inHealthMat, locMatTypes inArmorMat, locMatTypes inShieldMat, double inHealth, double inArmor, double inShield, int inBaseLevel)
		{
			this.engageGodMode_Health = inEngageGodMode_Health;
			this.engageGodMode_Armor = inEngageGodMode_Armor;
			this.engageGodMode_Shield = inEngageGodMode_Shield;
			this.HealthMax = inHealthMax;
			this.ArmorMax = inArmorMax;
			this.ShieldMax = inShieldMax;
			this.HealthMat = inHealthMat;
			this.ArmorMat = inArmorMat;
			this.ShieldMat = inShieldMat;
			this.Health = inHealth;
			this.Armor = inArmor;
			this.Shield = inShield;
			this.baseLevel = inBaseLevel;
		}

		public void setEngageGodMode_Health(boolean inEngageGodMode_Health)
		{
			this.engageGodMode_Health = inEngageGodMode_Health;
		}

		public void setEngageGodMode_Armor(boolean inEngageGodMode_Armor)
		{
			this.engageGodMode_Armor = inEngageGodMode_Armor;
		}

		public void setEngageGodMode_Shield(boolean inEngageGodMode_Shield)
		{
			this.engageGodMode_Shield = inEngageGodMode_Shield;
		}

		public void setHealthMax(double inHealthMax, boolean syncCurrent)
		{
			this.HealthMax = inHealthMax;

			if (syncCurrent)
			{
				this.Health = inHealthMax;
			}
		}

		public void setArmorMax(double inArmorMax, boolean syncCurrent)
		{
			this.ArmorMax = inArmorMax;

			if (syncCurrent)
			{
				this.Armor = inArmorMax;
			}
		}

		public void setShieldMax(double inShieldMax, boolean syncCurrent)
		{
			this.ShieldMax = inShieldMax;

			if (syncCurrent)
			{
				this.Shield = inShieldMax;
			}
		}

		public void setHealthMat(locMatTypes inHealthMat)
		{
			this.HealthMat = inHealthMat;
		}

		public void setArmorMat(locMatTypes inArmorMat)
		{
			this.ArmorMat = inArmorMat;
		}

		public void setShieldMat(locMatTypes inShieldMat)
		{
			this.ShieldMat = inShieldMat;
		}

		public void setHealth(double inHealth)
		{
			this.Health = inHealth;
		}

		public void setArmor(double inArmor)
		{
			this.Armor = inArmor;
		}

		public void setShield(double inShield)
		{
			this.Shield = inShield;
		}

		public basicMeatPuppet build()
		{
			return this.buildScaledToLevel(this.baseLevel);
		}

		public abstract basicMeatPuppet buildScaledToLevel(int desiredLevel);
	}
}
