package DamagePackets;

import DamagePackets.Packets.crittableDamagePacket;
import DataStructures.Linkages.CharList.Mk03.CharList;
import Enums.locDamTypes;
import Manifests.Enemy.Mk01.BodyPart;

public abstract class ShotReport
{
	private final crittableDamagePacket damage;
	private final BodyPart partHit;

	public ShotReport(crittableDamagePacket inDamage, BodyPart inPartHit)
	{
		this.damage = inDamage;
		this.partHit = inPartHit;
	}
	
	public static ShotReport getInstance(crittableDamagePacket inDamage, BodyPart inPartHit)
	{
		return new ShotReport_noProc(inDamage, inPartHit);
	}
	
	public static ShotReport getInstance(crittableDamagePacket inDamage, BodyPart inPartHit, locDamTypes procFlavor)
	{
		return new ShotReport_withProc(inDamage, inPartHit, procFlavor);
	}

	public crittableDamagePacket getDamage()
	{
		return this.damage;
	}

	public BodyPart getPartHit()
	{
		return this.partHit;
	}

	public abstract boolean hasProc();

	public abstract locDamTypes getProcFlavor();

	public CharList toCharList()
	{
		final CharList result = new CharList();

		result.add("Damage(");
		result.add(this.damage.toCharList(), true);
		result.add(')');
		result.add('\t');
		result.add("partHit(");
		result.add(this.partHit.toCharList(), true);
		result.add(')');

		return result;
	}

	private static class ShotReport_noProc extends ShotReport
	{
		public ShotReport_noProc(crittableDamagePacket inDamage, BodyPart inPartHit)
		{
			super(inDamage, inPartHit);
		}

		@Override
		public boolean hasProc()
		{
			return false;
		}

		@Override
		public locDamTypes getProcFlavor()
		{
			return null;
		}

		@Override
		public CharList toCharList()
		{
			final CharList result = super.toCharList();

			result.push("ShotReport_NoProc: ");

			return result;
		}
	}

	private static class ShotReport_withProc extends ShotReport
	{
		public final locDamTypes procFlavor;

		public ShotReport_withProc(crittableDamagePacket inDamage, BodyPart inPartHit, locDamTypes inProcFlavor)
		{
			super(inDamage, inPartHit);
			this.procFlavor = inProcFlavor;
		}

		@Override
		public boolean hasProc()
		{
			return true;
		}

		@Override
		public locDamTypes getProcFlavor()
		{
			return this.procFlavor;
		}

		@Override
		public CharList toCharList()
		{
			final CharList result = super.toCharList();

			result.push("ShotReport_Proc: ");
			result.add('\t');
			result.add("Proc(");
			result.add(this.procFlavor.toString());
			result.add(')');

			return result;
		}
	}
}