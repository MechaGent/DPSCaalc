package Mk01.Weapons;

import java.util.Arrays;

import DamagePackets.Packets.crittableDamagePacket;
import Enums.locDamTypes;
import Manifests.Enemy.Mk01.BodyPart;
import Mk01.Procs.BulletDamageProcToken;

public abstract class ShotRecord
{
	private static final ShotRecord emptyRecord = new ShotRecord_NoProc(crittableDamagePacket.getEmptyPacket(), BodyPart.getMissedPart());
	
	private final crittableDamagePacket damage;
	private final BodyPart partHit;
	
	private ShotRecord(crittableDamagePacket inDamage, BodyPart inPartHit)
	{
		this.damage = inDamage;
		this.partHit = inPartHit;
	}
	
	public static ShotRecord getInstance(crittableDamagePacket inDamage, BodyPart inPartHit)
	{
		//System.out.println("new non-proc shotRecord");
		return new ShotRecord_NoProc(inDamage, inPartHit);
	}
	
	public static ShotRecord getInstance(crittableDamagePacket inDamage, BodyPart inPartHit, locDamTypes inProc)
	{
		//System.out.println("new with-proc shotRecord");
		return new ShotRecord_WithProc(inDamage, inPartHit, inProc);
	}
	
	public static ShotRecord getInstance(crittableDamagePacket inDamage, BodyPart inPartHit, locDamTypes[] inProc)
	{
		if(inProc.length == 0)
		{
			return new ShotRecord_NoProc(inDamage, inPartHit);
		}
		else
		{
			return new ShotRecord_WithProc(inDamage, inPartHit, inProc);
		}
	}

	public crittableDamagePacket getDamage()
	{
		return this.damage;
	}

	public BodyPart getPartHit()
	{
		return this.partHit;
	}
	
	public BulletDamageProcToken getBulletDamageProcToken(long currentTime)
	{
		return new BulletDamageProcToken(this, currentTime);
	}
	
	@Override
	public String toString()
	{
		return "ShotRecord [damage=" + this.damage + ", partHit=" + this.partHit + "]";
	}

	public abstract boolean hasProc();
	public abstract locDamTypes[] getProcFlavors();
	
	public static ShotRecord getEmptyRecord()
	{
		return emptyRecord;
	}

	private static class ShotRecord_NoProc extends ShotRecord
	{
		private static locDamTypes[] NoProcArr = new locDamTypes[0];
		
		private ShotRecord_NoProc(crittableDamagePacket inDamage, BodyPart inPartHit)
		{
			super(inDamage, inPartHit);
		}

		@Override
		public boolean hasProc()
		{
			return false;
		}

		@Override
		public locDamTypes[] getProcFlavors()
		{
			return NoProcArr;
		}
	}
	
	private static class ShotRecord_WithProc extends ShotRecord
	{
		private final locDamTypes[] proc;

		private ShotRecord_WithProc(crittableDamagePacket inDamage, BodyPart inPartHit, locDamTypes inProc)
		{
			super(inDamage, inPartHit);
			this.proc = new locDamTypes[]{inProc};
		}

		private ShotRecord_WithProc(crittableDamagePacket inDamage, BodyPart inPartHit, locDamTypes[] inProc)
		{
			super(inDamage, inPartHit);
			this.proc = inProc;
		}

		@Override
		public boolean hasProc()
		{
			return true;
		}

		@Override
		public locDamTypes[] getProcFlavors()
		{
			return this.proc;
		}

		@Override
		public String toString()
		{
			return "ShotRecord_WithProc [proc=" + Arrays.toString(this.proc) + "]";
		}
	}
}
