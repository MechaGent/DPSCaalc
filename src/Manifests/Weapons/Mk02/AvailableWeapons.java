package Manifests.Weapons.Mk02;

public enum AvailableWeapons
{
	Braton("Rifle_Tenno_Braton");
	
	private final String mangledName;
	
	private AvailableWeapons(String inMangledName)
	{
		this.mangledName = inMangledName;
	}

	public String getMangledName()
	{
		return this.mangledName;
	}
	
	public GunWeapon getWeaponStats()
	{
		return WeaponsManifest.getGunWeapon(this.mangledName);
	}
	
	public static void main(String[] args)
	{
		final GunWeapon test = AvailableWeapons.Braton.getWeaponStats();
		System.out.println(test.toCharList("\r\n"));
	}
}
