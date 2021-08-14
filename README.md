# TownyResources
This plugin provides towns and nations with valuable natural resources.

## Introduction:
This plugin is essential for a good SiegeWar experience, because without it, there are surprisingly few reasons to start sieges:
 - **Expansion:** No Reason, because sieging does not provides land control. 
 - **Resources:** No Reason, because sieging does not provides resources. 
 - **Roleplaying:** No Reason, because few players are doing roleplaying to any reasonable standard (*creating new characters & character-sheets, adopting different personalities while in-game, separating in-game & out-of-game conversations, following character-driven story arcs within the gameworld etc.*).  
 - **Personal:** No Good Reason, because it creates toxicity.
 - **PVP:** Good Reason. *However*, unfortunately such sieges upset those who want to treat the game as strategy and/or roleplay-lite, as it "breaks their bubble".
 - **Plunder Money:** Good Reason. *However*, unfortunately money tends to be regarded by players as less satifying & less useful than the acquisition of material resources.
 
 This plugin solves the problem by turning **Resources** into a good reason for sieges. This is achieved in 3 steps:
 - Step 1: Make resources valuable.
 - Step 2: Give towns a handful of auto-collected resources.
 - Step 3: Give occupiers a cut of the auto-collected resources.
 #### 
 
## Mechanics: 
#### Step 1: Make resources valuable.
- Limit the amount of resources which players can extract each day.
- The limit is high enough to allow players to feed themselves, and provide enough materials for medieval-scale building and trading.
- However the limit is made deliberately low enough to prevent ***INDUSTRIAL-SCALE FACTORIES***, which would flood the market & degrade the value of any towns which have that resource.

#### Step 2: Give towns a handful of auto-collected resources.
- Provide each town with a handful of resources:
  - Level 1 Resource - 100% productivity, requires town level 1 to extract.
  - Level 2 Resource - 150% productivity, requires town level 3 to extract.
  - Level 3 Resource - 300% productivity, requires town level 5 to extract.
  - Level 4 Resource - 500% productivity, requires town level 7 to extract.
- These resources are shown publicly on the Town screen `/t`.
- Before these resources can be extracted, they must be discovered via **surveys**. Survey are done by running `/t survey`, and each survey has a cost & num-townblocks requirement:
    - Level 1 Resource - 250 cost, requires 10 townblocks.
    - Level 2 Resource - 500 cost, requires 50 townblocks.
    - Level 3 Resource - 2000 cost, requires 100 townblocks.
    - Level 4 Resource - 5000 cost, requires 200 townblocks.  
- Town resources are automatically extracted on each Towny new-day.
- The mayor can collect the extracted resources at any time by running `/tr collect`.

#### Step 3: Give occupiers a cut of the auto-collected resources.
- 50% of the town resources are extracted by the town.
- 50% of the town resources are extracted by the owner nation (*natural or occupier*). 
- The owner king can collect the nation's cut at any time by running `/tr collect`.
    
## Installation Instructions:
1. Ensure you have Towny 0.97.1.0+
2. Edit your townyperms.yml file, and add the following perms:
   > Mayor: ...    townyresources.town.collect                                                            
   > King: ....    townyresources.nation.collect                                                                                                                       
2. Stop your server
3. Download the latest TownyResources jar from [here](https://github.com/TownyAdvanced/TownyResources/releases)
4. Drop the TownyResources jar into your plugins folder
5. Start yor server