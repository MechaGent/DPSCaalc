package Mk01.Weapons;

import DamagePackets.Packets.crittableDamagePacket;
import Enums.ModEffectTargets;
import Enums.StatusProcEffectValues;
import Manifests.Enemy.Mk01.BodyPart;
import Manifests.Mods.Mk01.ModFolio;
import Manifests.Weapons.Mk02.Bullet;
import Manifests.Weapons.Mk02.GunWeapon;
import Manifests.Weapons.Mk02.ProcRoller;
import Mk01.Weapons.AccuracyMappers.AccuracyMapper;
import Random.XorShiftStar.Mk02.XorShiftStar;

public class GunWeaponInstance
{
	private final GunWeapon stats;
	private final ModFolio mods;
	private final GunWeapon moddedStats;

	private int BulletMode;
	private int magBullets;

	private long nextFireTime;

	public GunWeaponInstance(GunWeapon inStats, ModFolio inMods)
	{
		this.stats = inStats;
		this.mods = inMods;
		this.moddedStats = inStats.getModdedInstance(inMods);

		this.BulletMode = 0;
		this.magBullets = this.moddedStats.getMaxMagSize();

		this.nextFireTime = 0;
	}
	
	public GunWeaponInstance(GunWeaponInstance old)
	{
		this.stats = old.stats;
		this.mods = old.mods;
		this.moddedStats = old.moddedStats;
		
		this.BulletMode = 0;
		this.magBullets = old.magBullets;

		this.nextFireTime = 0;
	}

	public GunWeapon getStats()
	{
		return this.stats;
	}

	public ModFolio getMods()
	{
		return this.mods;
	}

	public GunWeapon getModdedStats()
	{
		return this.moddedStats;
	}

	public long getNextFireTime()
	{
		return this.nextFireTime;
	}

	private void cycleAndSetNextFireTime()
	{
		this.magBullets--;

		if (this.magBullets == 0)
		{
			this.magBullets = this.moddedStats.getMaxMagSize();
			this.nextFireTime += this.moddedStats.getReloadInterval();
		}
		else
		{
			this.nextFireTime += this.moddedStats.getFireInterval();
		}
	}

	public ShotRecord[] pullTrigger(long currentTime, XorShiftStar dice, AccuracyMapper accMap)
	{
		//System.out.println("triggered");
		final Bullet curBulletStats = this.moddedStats.getBulletCharacteristics(this.BulletMode);
		final ShotRecord[] result = new ShotRecord[curBulletStats.rollForMultishot(dice)];

		for (int i = 0; i < result.length; i++)
		{
			result[i] = this.rollBullet(currentTime, curBulletStats, dice, accMap);
			//System.out.println("shotrecord: " + result[i].toString());
		}

		this.cycleAndSetNextFireTime();
		return result;
	}

	private ShotRecord rollBullet(long currentTime, Bullet curStats, XorShiftStar dice, AccuracyMapper accMap)
	{
		final ShotRecord result;
		final BodyPart partHit = accMap.rollNextPartHit(dice);

		if (!partHit.isMissedPart())
		{
			final int critLevel = curStats.rollForCritLevel(dice);
			final crittableDamagePacket resDam = new crittableDamagePacket(curStats.getNormalDamage(), critLevel, curStats.getCritMult());
			final ProcRoller Proccy = curStats.getProccy();

			if (Proccy.rollToSeeIfProcced(dice))
			{
				result = ShotRecord.getInstance(resDam, partHit, Proccy.rollForProcs(dice));
			}
			else
			{
				result = ShotRecord.getInstance(resDam, partHit);
			}
		}
		else
		{
			result = ShotRecord.getEmptyRecord();
		}

		return result;
	}

	public double getElectricityProcDamageValue()
	{
		return calcProcDamage(this.stats.getBulletCharacteristics(this.BulletMode).getNormalDamageSum(),

				this.mods.getEffectMagnitude(ModEffectTargets.BaseDamage),

				this.mods.getEffectMagnitude(ModEffectTargets.elemMult_Electricity),

				0.5);
	}

	public double getGasProcDamageValue()
	{
		return calcProcDamage(this.stats.getBulletCharacteristics(this.BulletMode).getNormalDamageSum(),

				this.mods.getEffectMagnitude(ModEffectTargets.BaseDamage),

				this.mods.getEffectMagnitude(ModEffectTargets.elemMult_Toxin),

				StatusProcEffectValues.FinalDamageMult_Gas.getValue());
	}
	
	public double getToxinProcViaGasProcDamageValue(double gasDamValue)
	{
		return gasDamValue * (1 + this.mods.getEffectMagnitude(ModEffectTargets.elemMult_Toxin)) * StatusProcEffectValues.FinalDamageMult_Toxin.getValue();
	}

	public double getToxinProcDamageValue()
	{
		return calcProcDamage(this.stats.getBulletCharacteristics(this.BulletMode).getNormalDamageSum(),

				this.mods.getEffectMagnitude(ModEffectTargets.BaseDamage),

				this.mods.getEffectMagnitude(ModEffectTargets.elemMult_Toxin),

				StatusProcEffectValues.FinalDamageMult_Toxin.getValue());
	}
	
	public double getHeatProcDamageValue()
	{
		return calcProcDamage(this.stats.getBulletCharacteristics(this.BulletMode).getNormalDamageSum(),

				this.mods.getEffectMagnitude(ModEffectTargets.BaseDamage),

				this.mods.getEffectMagnitude(ModEffectTargets.elemMult_Heat),

				StatusProcEffectValues.FinalDamageMult_Heat.getValue());
	}
	
	public double getSlashProcDamageValue()
	{
		return calcProcDamage(this.stats.getBulletCharacteristics(this.BulletMode).getNormalDamageSum(),

				this.mods.getEffectMagnitude(ModEffectTargets.BaseDamage),

				0.0,
				//this.mods.getEffectMagnitude(ModEffectTargets.elemMult_Heat),

				StatusProcEffectValues.FinalDamageMult_Slash.getValue());
	}

	private static final double calcProcDamage(double baseDamageSum, double baseMult, double flavMult, double procMult)
	{
		return baseDamageSum * (1.0 + baseMult) * (1.0 + flavMult) * procMult;
	}
}
