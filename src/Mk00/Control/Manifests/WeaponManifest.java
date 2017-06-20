package Mk00.Control.Manifests;

import Enums.TickIntervals;
import Enums.locDamTypes;
import Mk00.Damage.Packets.ScaledDamagePacket;
import Mk00.Weapons.basicWeapon;

public class WeaponManifest
{
	public static enum Weapons
	{
		Braton,
		Braton_Prime,
		Grakata,
		Tigris_Sancti;
	}

	public static basicWeapon getWeapon(Weapons choice)
	{
		final basicWeapon result;

		switch (choice)
		{
			case Tigris_Sancti:
			{
				//final int shotsPerMin = 1200;
				final double reloadTime = 1.5;
				final int inMaxMagSize = 2;
				final int bulletsConsumedPerTriggerPull = 1;
				final long inFireInterval = (long) (0.5 * TickIntervals.OneSecond.getValueAsInt());
				final long inReloadInterval = (long) (reloadTime * TickIntervals.OneSecond.getValueAsInt());

				double inMultishotChance = 6.0;
				final double damImpact = 126.0 / inMultishotChance;
				final double damPuncture = 126.0 / inMultishotChance;
				final double damSlash = 1008.0 / inMultishotChance;
				final double totalDamage = damImpact + damPuncture + damSlash;
				ScaledDamagePacket inBaseDamage = new ScaledDamagePacket(totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Impact, damImpact / totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Puncture, damPuncture / totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Slash, damSlash / totalDamage);

				double inCritChance = 0.15;
				double inCritMult = 1.5;
				
				double inProcChance = calculateShotgunBaseProcChance(0.20, (int) inMultishotChance);

				result = new basicWeapon(inFireInterval, inReloadInterval, inMaxMagSize, bulletsConsumedPerTriggerPull, inBaseDamage, inCritChance, inCritMult, inMultishotChance, inProcChance);
				break;
			}
			case Grakata:
			{
				final int shotsPerMin = 1200;
				final double reloadTime = 2.37;
				final int inMaxMagSize = 60;
				final long inFireInterval = (long) ((60.0 / shotsPerMin) * TickIntervals.OneSecond.getValueAsInt());
				final long inReloadInterval = (long) (reloadTime * TickIntervals.OneSecond.getValueAsInt());

				final double damImpact = 4.4;
				final double damPuncture = 3.67;
				final double damSlash = 2.93;
				final double totalDamage = damImpact + damPuncture + damSlash;
				ScaledDamagePacket inBaseDamage = new ScaledDamagePacket(totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Impact, damImpact / totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Puncture, damPuncture / totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Slash, damSlash / totalDamage);

				double inCritChance = 0.25;
				double inCritMult = 2.0;
				double inProcChance = 0.20;

				result = new basicWeapon(inFireInterval, inReloadInterval, inMaxMagSize, inBaseDamage, inCritChance, inCritMult, inProcChance);
				break;
			}
			case Braton:
			{
				final int shotsPerMin = 525;
				final double reloadTime = 2;
				final int inMaxMagSize = 45;
				final long inFireInterval = (long) ((60.0 / shotsPerMin) * TickIntervals.OneSecond.getValueAsInt());
				final long inReloadInterval = (long) (reloadTime * TickIntervals.OneSecond.getValueAsInt());

				final double damImpact = 6.6;
				final double damPuncture = 6.6;
				final double damSlash = 6.8;
				final double totalDamage = damImpact + damPuncture + damSlash;
				ScaledDamagePacket inBaseDamage = new ScaledDamagePacket(totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Impact, damImpact / totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Puncture, damPuncture / totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Slash, damSlash / totalDamage);

				double inCritChance = 0.1;
				double inCritMult = 1.5;
				double inProcChance = 0.05;

				result = new basicWeapon(inFireInterval, inReloadInterval, inMaxMagSize, inBaseDamage, inCritChance, inCritMult, inProcChance);
				break;
			}
			case Braton_Prime:
			{
				final int shotsPerMin = 575;
				final double reloadTime = 2.15;
				final int inMaxMagSize = 75;
				final long inFireInterval = (long) ((60.0 / shotsPerMin) * TickIntervals.OneSecond.getValueAsInt());
				final long inReloadInterval = (long) (reloadTime * TickIntervals.OneSecond.getValueAsInt());

				final double damImpact = 1.75;
				final double damPuncture = 12.25;
				final double damSlash = 21.0;
				final double totalDamage = damImpact + damPuncture + damSlash;
				ScaledDamagePacket inBaseDamage = new ScaledDamagePacket(totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Impact, damImpact / totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Puncture, damPuncture / totalDamage);
				inBaseDamage.setDamagePortion(locDamTypes.Slash, damSlash / totalDamage);

				double inCritChance = 0.1;
				double inCritMult = 2.0;
				double inProcChance = 0.2;

				result = new basicWeapon(inFireInterval, inReloadInterval, inMaxMagSize, inBaseDamage, inCritChance, inCritMult, inProcChance);
				break;
			}
			default:
			{
				throw new IllegalArgumentException();
			}
		}

		return result;
	}
	
	private static double calculateShotgunBaseProcChance(double sum, int numPellets)
	{
		final double inverseProb = 1.0 - sum;
		final double chancePerPellet = 1.0 - Math.pow(inverseProb, 1.0 / ((double) numPellets));
		return chancePerPellet;
	}
	
	/*
	public static void main(String[] args)
	{
		final double testProb = 0.2;
		final int numPellets = 6;
		final double pelletProb = calculateShotgunBaseProcChance(testProb, numPellets);
		final double check = 1.0 - Math.pow(1.0 - pelletProb, numPellets);
		System.out.println(pelletProb + "\r\nwith check:\r\n" + check);
	}
	*/
}
