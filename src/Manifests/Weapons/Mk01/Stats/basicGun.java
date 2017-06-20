package Manifests.Weapons.Mk01.Stats;

import DamagePackets.Packets.unitDamagePacket;
import Enums.TickIntervals;
import Manifests.Weapons.Mk01.WeaponStats;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusVar;

public class basicGun extends WeaponStats
{
	private final long fireInterval;
	private final long reloadInterval;
	private final int maxMagSize;
	private final int bulletsConsumedPerTriggerpull;

	private final unitDamagePacket normalDamage;

	private final int baseCritLevel;
	private final double partialCritChance;
	private final double critMult;
	private final int baseExtraBulletLevel;
	private final double partialExtraBulletChance;

	public basicGun(String inName, LotusObject in)
	{
		super(inName, in);

		final LotusVar wrapFireInterval = in.getVarAsVar("fireInterval");
		final long tentFireInterval = Long.parseLong(wrapFireInterval.getValueAsString());

		if (tentFireInterval != -1)
		{
			this.fireInterval = tentFireInterval;
		}
		else
		{
			final double fireRate = in.getVarAsVar("fireRate").getValueAsDouble();
			final double preciseFireRate = Math.ceil(fireRate * 60.0);
			this.fireInterval = (long) ((60.0 / preciseFireRate) * TickIntervals.OneSecond.getValueAsInt());
		}

		this.reloadInterval = (long) (in.getVarAsVar("reloadInterval").getValueAsDouble() * TickIntervals.OneSecond.getValueAsInt());
		this.maxMagSize = in.getVarAsVar("maxMagSize").getValueAsInt();
		this.bulletsConsumedPerTriggerpull = in.getVarAsVar("bulletsConsumedPerTriggerpull").getValueAsInt();
		this.normalDamage = new unitDamagePacket(in.getVarAsObject("normalDamage"));

		final double critChance = sanitizePercentage(in.getVarAsVar("critChance").getValueAsString());

		this.baseCritLevel = (int) critChance;
		this.partialCritChance = critChance - this.baseCritLevel;

		this.critMult = in.getVarAsVar("critMult").getValueAsDouble();

		final double extraBulletChance =  sanitizePercentage(in.getVarAsVar("extraBulletChance").getValueAsString());;

		this.baseExtraBulletLevel = (int) extraBulletChance;
		this.partialExtraBulletChance = extraBulletChance - this.baseExtraBulletLevel;
	}
	
	private static double sanitizePercentage(String raw)
	{
		final int lastIndex = raw.length() - 1;
		final double result;
		
		if(raw.charAt(lastIndex) == '%')
		{
			result = Double.parseDouble(raw.substring(0, lastIndex)) / 100.0;
		}
		else
		{
			result = Double.parseDouble(raw);
		}
		
		return result;
	}

	public long getFireInterval()
	{
		return this.fireInterval;
	}

	public long getReloadInterval()
	{
		return this.reloadInterval;
	}

	public int getMaxMagSize()
	{
		return this.maxMagSize;
	}

	public int getBulletsConsumedPerTriggerpull()
	{
		return this.bulletsConsumedPerTriggerpull;
	}

	public unitDamagePacket getNormalDamage()
	{
		return this.normalDamage;
	}

	public int getBaseCritLevel()
	{
		return this.baseCritLevel;
	}

	public double getPartialCritChance()
	{
		return this.partialCritChance;
	}

	public double getCritMult()
	{
		return this.critMult;
	}

	public int getBaseExtraBulletLevel()
	{
		return this.baseExtraBulletLevel;
	}

	public double getPartialExtraBulletChance()
	{
		return this.partialExtraBulletChance;
	}
}
