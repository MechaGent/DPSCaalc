package Manifests.Weapons.Mk02;

import java.util.Arrays;

import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.TickIntervals;
import Manifests.Mods.Mk01.ModCard.ModEffect;
import Manifests.Mods.Mk01.ModFolio;
import Manifests.Mods.Mk01.RankedModCard;
import Manifests.Weapons.Mk02.Bullet.BulletBuilder;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFormat;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;

public class GunWeapon
{
	private final String name;
	private final String mangledName;
	private final String type;

	private final long fireInterval;
	private final long reloadInterval;
	private final int maxMagSize;

	private final Bullet[] BulletCharacteristics;

	public GunWeapon(String inMangledName, LotusFormat in)
	{
		this(inMangledName, (LotusObject) in);
	}

	public GunWeapon(String inMangledName, LotusObject in)
	{
		this.name = in.getVarAsVar("name").getValueAsString();
		this.mangledName = inMangledName;
		this.type = in.getVarAsVar("type").getValueAsString();

		this.fireInterval = resolveFireInterval(in);
		this.reloadInterval = (long) (in.getVarAsVar("reloadInterval").getValueAsDouble() * TickIntervals.OneSecond.getValueAsInt());
		this.maxMagSize = in.getVarAsVar("maxMagSize").getValueAsInt();
		this.BulletCharacteristics = Bullet.parseArray(in.getVarAsArray("BulletCharacteristics"));
	}

	private GunWeapon(String inName, String inMangledName, String inType, long inFireInterval, long inReloadInterval, int inMaxMagSize, Bullet[] inBulletCharacteristics)
	{
		super();
		this.name = inName;
		this.mangledName = inMangledName;
		this.type = inType;
		this.fireInterval = inFireInterval;
		this.reloadInterval = inReloadInterval;
		this.maxMagSize = inMaxMagSize;
		this.BulletCharacteristics = inBulletCharacteristics;
	}

	private static long resolveFireInterval(LotusObject in)
	{
		final long result;
		final int test = in.getVarAsVar("fireInterval").getValueAsInt();

		if (test != -1)
		{
			result = test;
		}
		else
		{
			final double givenFireRate = in.getVarAsVar("fireRate").getValueAsDouble();
			final double estimatedRoundsPerMinute = Math.ceil(givenFireRate * 60.0);
			result = (long) ((60.0 / estimatedRoundsPerMinute) * ((double) TickIntervals.OneSecond.getValueAsInt()));
		}

		return result;
	}

	public String getName()
	{
		return this.name;
	}

	public String getMangledName()
	{
		return this.mangledName;
	}

	public String getType()
	{
		return this.type;
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

	public Bullet getBulletCharacteristics(int i)
	{
		return this.BulletCharacteristics[i];
	}

	public GunWeapon getModdedInstance(ModFolio in)
	{
		if (!in.isNullFolio())
		{
			final GunWeaponBuilder builder = new GunWeaponBuilder(this);
			builder.applyMods(in);
			return builder.build();
		}
		else
		{
			return this;
		}
	}

	/**
	 * auto-generated toString
	 */
	@Override
	public String toString()
	{
		return "GunWeapon [name=" + this.name + ", mangledName=" + this.mangledName + ", type=" + this.type + ", fireInterval=" + this.fireInterval + ", reloadInterval=" + this.reloadInterval + ", maxMagSize=" + this.maxMagSize + ", BulletCharacteristics=" + Arrays.toString(this.BulletCharacteristics) + "]";
	}

	public CharList toCharList()
	{
		return this.toCharList("\t");
	}

	public CharList toCharList(String delim)
	{
		return this.toCharList(new CharList(), delim);
	}

	public CharList toCharList(CharList prior, String delim)
	{
		prior.add("Name: ");
		prior.add(this.name);
		prior.add(delim);
		prior.add("MangledName: ");
		prior.add(this.mangledName);
		prior.add(delim);
		prior.add("Type: ");
		prior.add(this.type);
		prior.add(delim);
		prior.add("FireInterval: ");
		final double dubFireInterval = TickIntervals.fromNanoToSeconds(this.fireInterval);
		prior.addAsString(dubFireInterval);
		prior.add('|');
		prior.add("FireRate: ");
		prior.addAsString(1.0 / dubFireInterval);
		prior.add(delim);
		prior.add("ReloadInterval: ");
		final double dubReloadInterval = TickIntervals.fromNanoToSeconds(this.fireInterval);
		prior.addAsString(dubReloadInterval);
		prior.add('|');
		prior.add("ReloadRate: ");
		prior.addAsString(1.0 / dubReloadInterval);
		prior.add(delim);
		prior.add("MaxMagSize: ");
		prior.addAsString(this.maxMagSize);
		prior.add(delim);
		prior.add("BulletModes:[");

		for (int i = 0; i < this.BulletCharacteristics.length; i++)
		{
			prior.add(delim);
			prior.add('{');
			prior.add(delim);
			this.BulletCharacteristics[i].toCharList(prior, delim);
			prior.add(delim);
			prior.add('}');
		}

		prior.add(delim);
		prior.add(']');

		return prior;
	}

	public static class GunWeaponBuilder
	{
		private final GunWeapon base;

		private double fireRateMult;
		private double reloadSpeedMult;
		private double maxMagSizeMult;
		private final BulletBuilder[] Characteristics;

		public GunWeaponBuilder(GunWeapon in)
		{
			this.base = in;
			this.fireRateMult = 0;
			this.reloadSpeedMult = 0;
			this.maxMagSizeMult = 0;
			this.Characteristics = initBulletBuilders(in.BulletCharacteristics);
		}

		private static BulletBuilder[] initBulletBuilders(Bullet[] in)
		{
			final BulletBuilder[] result = new BulletBuilder[in.length];

			for (int i = 0; i < result.length; i++)
			{
				result[i] = new BulletBuilder(in[i]);
			}

			return result;
		}

		public void applyMods(ModFolio in)
		{
			for (RankedModCard cur : in)
			{
				this.applyMod(cur);
			}
		}

		public void applyMod(RankedModCard in)
		{
			for (ModEffect curr : in.getBaseEffects())
			{
				this.applyEffect(curr, in.getRank());
			}
		}

		public boolean applyEffect(ModEffect in, int rank)
		{
			boolean result = this.applyEffect_Internal(in, rank);

			if (!result)
			{
				for (BulletBuilder cur : this.Characteristics)
				{
					result |= cur.applyEffect(in, rank);
				}
			}

			return result;
		}

		private boolean applyEffect_Internal(ModEffect in, int rank)
		{
			boolean result = true;
			final double delta = in.getRankedValue(rank);

			switch (in.getTarget())
			{
				case fireRate:
				{
					this.fireRateMult += delta;
					break;
				}
				case reloadTime:
				{
					this.reloadSpeedMult += delta;
					break;
				}
				default:
				{
					result = false;
					break;
				}
			}

			return result;
		}

		public GunWeapon build()
		{
			final String name = this.base.name;
			final String mangledName = this.base.mangledName;
			final String type = this.base.type;

			final long fireInterval = (long) (((double) this.base.fireInterval) / (1.0 + this.fireRateMult));
			final long reloadInterval = (long) (((double) this.base.reloadInterval) / (1.0 + this.reloadSpeedMult));
			final int maxMagSize = (int) (((double) this.base.maxMagSize) * (1.0 + this.maxMagSizeMult));

			final Bullet[] BulletCharacteristics = new Bullet[this.Characteristics.length];

			for (int i = 0; i < this.Characteristics.length; i++)
			{
				BulletCharacteristics[i] = this.Characteristics[i].build();
			}

			return new GunWeapon(name, mangledName, type, fireInterval, reloadInterval, maxMagSize, BulletCharacteristics);
		}
	}
}
