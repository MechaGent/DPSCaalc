package Manifests.Enemy.Mk01.FactionEnemies;

import Manifests.Enemy.Mk01.EnemiesManifest;
import Manifests.Enemy.Mk01.EnemyStats;

public enum GrineerEnemies
{
	Lancer("Grineer_Lancer"),
	Elite_Lancer("Grineer_Lancer_Elite"),
	Butcher("Grineer_Butcher"),
	Flameblade("Grineer_Flameblade"),
	Powerfist("Grineer_Powerfist"),
	Scorpion("Grineer_Scorpion"),
	Shield_Lancer("Grineer_Lancer_Shield"),
	Guardsman("Grineer_Guardsman"),
	;
	
	private final String mangledName;
	
	private GrineerEnemies(String inMangledName)
	{
		this.mangledName = inMangledName;
	}
	
	public EnemyStats getStats()
	{
		return EnemiesManifest.getEnemyStats(this.mangledName);
	}
}
