package Manifests.Weapons.Mk02;

import java.util.Map.Entry;

import DataStructures.Maps.HalfByteRadix.Mk02.HalfByteRadixMap;
import HandyStuff.FileParser;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFactory;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFormat;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;

public class WeaponsManifest
{
	private static String WeaponsManifestFileDir = "H:/Users/Thrawnboo/workspace - Java/DPSCaalc/src/Manifests/DataFiles/weaponManifest2.txt";
	private static HalfByteRadixMap<GunWeapon> weapons = initWeapons(WeaponsManifestFileDir);
	
	private static HalfByteRadixMap<GunWeapon> initWeapons(String dir)
	{
		final HalfByteRadixMap<GunWeapon> result = new HalfByteRadixMap<GunWeapon>();
		final String raw = FileParser.parseFileAsString(dir);
		final LotusObject wrap = LotusFactory.createNewLotusObject(raw).getVarAsObject("weapons");
		
		for(Entry<String, LotusFormat> entry: wrap.getEntrySet())
		{
			final String curKey = entry.getKey();
			final GunWeapon curStats = new GunWeapon(curKey, entry.getValue());
			//System.out.println(curStats.toString());
			result.add(curKey, curStats);
		}
		
		return result;
	}
	
	public static GunWeapon getGunWeapon(String enemyName)
	{
		return weapons.get(enemyName);
	}
}
