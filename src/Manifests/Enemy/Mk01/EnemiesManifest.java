package Manifests.Enemy.Mk01;

import java.util.Map.Entry;

import DataStructures.Maps.HalfByteRadix.Mk02.HalfByteRadixMap;
import HandyStuff.FileParser;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFactory;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusFormat;
import Parsers.LotusParser.aTopLayer.Interfaces.LotusObject;

public class EnemiesManifest
{
	private static String EnemiesManifestFileDir = "H:/Users/Thrawnboo/workspace - Java/DPSCaalc/src/Manifests/DataFiles/enemyManifest.txt";
	private static HalfByteRadixMap<EnemyStats> enemies = initEnemies(EnemiesManifestFileDir);
	
	private static HalfByteRadixMap<EnemyStats> initEnemies(String dir)
	{
		final HalfByteRadixMap<EnemyStats> result = new HalfByteRadixMap<EnemyStats>();
		final String raw = FileParser.parseFileAsString(dir);
		final LotusObject wrap = LotusFactory.createNewLotusObject(raw).getVarAsObject("enemies");
		
		for(Entry<String, LotusFormat> entry: wrap.getEntrySet())
		{
			final String curKey = entry.getKey();
			final EnemyStats curStats = new EnemyStats(curKey, entry.getValue());
			//System.out.println(curStats.toString());
			result.add(curKey, curStats);
		}
		
		return result;
	}
	
	public static EnemyStats getEnemyStats(String enemyName)
	{
		return enemies.get(enemyName);
	}
}
