# TownyResources
This plugin provides towns and nations with valuable natural resources.

## Introduction:
This plugin is essential for a good SiegeWar experience, because without it, there are (*surprisingly*) few good reasons to go to war:
 - **Expansion:** No Reason, because sieging does not provide land control. 
 - **Resources:** No Reason, because sieging does not provide resources (*emeralds, wood, wheat, iron etc.*). 
 - **Automatic Matchmaking and Objectives:** No Reason, because SiegeWar does not have this type of feature (*although it is being investigated [here](https://github.com/TownyAdvanced/SiegeWar/issues/335)*).
 - **Roleplaying:** No Reason, because most players are not doing roleplaying to any reasonable standard (*creating new characters & character-sheets, separating in-game & out-of-game personalities/culture/conversations/knowledge, following character-driven story arcs within the gameworld etc.)*.  
 - **Personal:** No Good Reason, because it creates toxicity.
 - **PVP:** Good Reason. *However*, such sieges usually upset those who want to treat the game as strategy and/or roleplay-lite, as it "breaks their bubble".
 - **Plunder Money:** Good Reason. *However*, money is usually regarded by players as less satifying & less useful than the acquisition of material resources.
 
This plugin solve this problem by transforming **Resources** into a good reason for war. The wisdom of this is demonstrated by the success of the **Civlization** series of games, which also has generated-terrain & sandbox geopolitical strategy, and in which: 
 <br> "*Resources are special commodities found in limited quantities on the map....are extremely important in the game, and the main reason for expansion and territorial wars ([Civ V Wiki](https://civilization.fandom.com/wiki/Resources_(Civ5))"*
#### 
 
## Mechanics: 
#### Step 1: Make resources valuable.
- Limit the amount of resources which players can extract each day.
- The limit is high enough to allow players to feed themselves, and provide enough materials for medieval-scale consumption, building and trading.
- However the limit is deliberately low enough to prevent ***INDUSTRIAL-SCALE FACTORIES***, which would flood the market & degrade the value of any towns which have that resource.

#### Step 2: Give resources to towns.
- Provide each town with a handful of automatically-extracted resources:
  - Level 1 Resource - 100% daily productivity, requires town level 1 to extract.
  - Level 2 Resource - 200% daily productivity, requires town level 4 to extract.
  - Level 3 Resource - 300% daily productivity, requires town level 6 to extract.
  - Level 4 Resource - 400% daily productivity, requires town level 8 to extract.
- Before these resources can be extracted, they must be discovered via **surveys**. Survey are done by running `/t survey`, and each survey has a cost & num-townblocks requirement:
    - Level 1 Resource - 250 cost, requires 10 townblocks.
    - Level 2 Resource - 1000 cost, requires 50 townblocks.
    - Level 3 Resource - 5000 cost, requires 100 townblocks.
    - Level 4 Resource - 20000 cost, requires 200 townblocks.  
- The mayor can collect the extracted resources at any time by running `/tr collect`.

#### Step 3: Give town owners a cut of the resources.
- 75% of the town resources are extracted by the town's owner nation (*natural or occupier*). 
- 25% of the town resources are extracted by the town.
- The owner king can collect the nation's cut at any time by running `/tr collect`.
    
## Other Benefits:
- Besides providing a good reason for wars, this plugin also provides a good reason for trade.
- This is achieved because individual towns and nations become hot-spots for the production of specific goods, which motivates natural trading activities.

## Installation Instructions:
1. Ensure you have Towny 0.97.1.0+
2. Edit your townyperms.yml file, and add the following perms:
   > Mayor, Assistant, Treasurer:  ... townyresources.town.collect                                                            
   > King, Assistant, Treasurer: ... townyresources.nation.collect                                                                                                                       
2. Stop your server
3. Download the latest TownyResources jar from [here](https://github.com/TownyAdvanced/TownyResources/releases)
4. Drop the TownyResources jar into your plugins folder
5. Start yor server