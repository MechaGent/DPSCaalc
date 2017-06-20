package Manifests.Weapons.Mk01;

import java.util.Map.Entry;

import DataStructures.Maps.HalfByteRadix.Mk02.HalfByteRadixMap;
import HandyStuff.FileParser;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFactory;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFormat;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;

public class WeaponsManifest
{
	private static String WeaponsManifestFileDir = "H:/Users/Thrawnboo/workspace - Java/DPSCaalc/src/Manifests/DataFiles/weaponManifest.txt";
	private static HalfByteRadixMap<WeaponStats> weapons = initWeapons(WeaponsManifestFileDir);
	
	private static HalfByteRadixMap<WeaponStats> initWeapons(String dir)
	{
		final HalfByteRadixMap<WeaponStats> result = new HalfByteRadixMap<WeaponStats>();
		final String raw = FileParser.parseFileAsString(dir);
		final LotusObject wrap = LotusFactory.createNewLotusObject(raw).getVarAsObject("weapons");
		
		for(Entry<String, LotusFormat> entry: wrap.getEntrySet())
		{
			final String curKey = entry.getKey();
			final WeaponStats curStats = WeaponStats.parseWeapon(curKey, entry.getValue());
			//System.out.println(curStats.toString());
			result.add(curKey, curStats);
		}
		
		return result;
	}
	
	public static WeaponStats getWeaponStats(String enemyName)
	{
		return weapons.get(enemyName);
	}
}
