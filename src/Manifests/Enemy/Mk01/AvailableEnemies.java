package Manifests.Enemy.Mk01;

import Enums.Factions;

public enum AvailableEnemies
{
	Lancer(Factions.Grineer, "Grineer_Lancer"),
	Elite_Lancer(Factions.Grineer, "Grineer_Lancer_Elite"),
	Butcher(Factions.Grineer, "Grineer_Butcher"),
	Flameblade(Factions.Grineer, "Grineer_Flameblade"),
	Powerfist(Factions.Grineer, "Grineer_Powerfist"),
	Scorpion(Factions.Grineer, "Grineer_Scorpion"),
	Shield_Lancer(Factions.Grineer, "Grineer_Lancer_Shield"),
	Guardsman(Factions.Grineer, "Grineer_Guardsman"),;
	
	private final Factions faction;
	private final String mangledName;
	
	private AvailableEnemies(Factions inFaction, String inMangledName)
	{
		this.faction = inFaction;
		this.mangledName = inMangledName;
	}

	public EnemyStats getStats()
	{
		return EnemiesManifest.getEnemyStats(this.mangledName);
	}
}
