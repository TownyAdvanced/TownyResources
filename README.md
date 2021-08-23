# TownyResources
TownyResources adds value to individual towns, by giving each one a unique set of automatically-extracted resources (*e.g. Oak Log, Coal, Wheat etc.*), and then protecting that economic value with moderate limits to player resource extraction.

# Installation Guide
1. Ensure you have Towny 0.97.1.0+
2. Edit your townyperms.yml file, and add the following perms:
   > Mayor, Assistant, Treasurer:  ... townyresources.command.towncollect                                                            
   > King, Assistant, Treasurer: ... townyresources.command.nationcollect     
3. If you have the TownyDynmap plugin installed, add this to the 'infowindow' section of your Dynmap-Towny config file
   > `<br/><span style="font-weight:bold;">Resources&colon; %town_resources%</span>`                                                                                                      
4. Stop your server
5. Download the latest TownyResources jar from [here](https://github.com/TownyAdvanced/TownyResources/releases)
6. Drop the TownyResources jar into your plugins folder
7. Start yor server

# Player Guide
### Town Production
###### Information
- Town production information is shown on the town screen. Example:
  > Resources:
  <br> > Daily Production: 64 Oak Log, 32 Emerald
###### Surveys
- Before resources can be be produced by a town, they must first be discovered.
- Resources can only be discovered by doing Surveys.
- Each Survey discovers a resource of a new level and type: 
  - Survey **1**: Reveals Level **1** Resource. Cost **250**. Minimum num townblocks **10**.
  - Survey **2**: Reveals Level **2** Resource. Cost **1000**. Minimum num townblocks **50**.
  - Survey **3**: Reveals Level **3** Resource. Cost **5000**. Minumum num townblocks **100**.
  - Survey **4**: Reveals Level **4** Resource. Cost **20000**. Minimum num townblocks **200**.  
- To do a Survey, enter the target town and run: `/tr survey`.
- If the Survey succeeds, a global success message will be generated. Example:
  > Goosius1 has discovered coal deposits at Rome!. Daily Production: 64 Coal.
###### Daily Production
- On each new Towny New Day, towns automatically produce resources.
- Production is modified by the resource level:
  - Resource Level **1**: Production bonus **N/A**. Minimum town level **1**.
  - Resource Level **2**: Production bonus **+100%**. Minimum town level **4**.
  - Resource Level **3**: Production bonus **+200%**. Minimum town level **6**.
  - Resource Level **4**: Production bonus **+300%**. Minimum town level **8**.
- After resources are produced, they are shown as available for collection on the town screen. Example:
  > Resources:
  <br> > Daily Production: 64 Oak Log, 32 Emerald
  <br> > Available For Collection: 64 Oak Log, 32 Emerald
- For each resource, a town can store a maximum of 5x the production amount. When stores are full, subsequent production is lost.                                                                                                      
###### Collecting Town Resources
- To collect town resources, as a mayor/assistant/treasurer, enter your town and run `/tr towncollect`
- The available resources will then be dropped at your position.
### Nation Production
###### Information
- Nation production & collection information is shown on the nation screen. Example:
  > Resources:
  <br> > Daily Production: 64 Oak Log 64 Coal, 32 Emerald
  <br> > Available For Collection: 128 Oak Log 128 Coal, 64 Emerald
###### Daily Production
- If a town belongs to, or is occupied by, a nation, then 50% of the town production is diverted to the nation.
###### Collecting Nation Resources
- To collect nation resources, as a king/assistant/treasurer, enter your capital and run `/tr nationcollect`
- The available resources will then be dropped at your position.
### Player Resource Extraction Limits
###### Resource Categories
- Extraction is limited per resource category.
- Example: The resource category of "Common Rocks" contains both EARTH and COBBLESTONE. Extracting either of those items counts towards the daily limit (in this case 6 stacks).
###### Limit Schedule
- Extraction is limited per day.
- The extraction records of all players are reset at Towny New Day.
###### Information
- When a player hits their daily extraction limit for a particular category of material, they will see an information bar message. Example:
  > Daily Extraction limit reached for Beetroot (64)
###### Limit Mechanisms
- Resources can only be extracted in the following circumstances:
  - Block Drops:
    - Before a player's daily limit is reached, if they break a block and it drops an item, this counts towards their daily limit for that item's category.
    - After a player's daily limit is reached:
      - Ancient Debris: Block does not break; Warning message is sent.
      - Dirt, Rock, Cobblestone: Block breaks; No warning message is sent.
      - All other blocks: Block breaks; Warning message is sent.
  - Mob Death Drops:
    - Before a player's daily limit is reached, if they kill a mob and it drops an item, this counts towards their daily limit for that item's category.
    - After a player's daily limit is reached, if they kill a mob which would otherwise drop an item in that category, it does not and a warning message is sent.
  - Fishing: 
    - Before a player's daily limit is reached, if they catch an item while fishing, this counts towards their daily limit for that item's category.
    - After a player's daily limit is reached, if a fishing action would catch an item in that category, it does not and a warning message is sent.
  - Sheep Shearing: 
    - Before a player's daily limit is reached, if they shear a sheep, this counts +2 towards the player's daily limit for the wool category.
    - After a player's daily limit is reached, if they try to shear a sheep they cannot, and a warning message is sent.
  - Egg Laying: 
    - Before a player's daily limit is reached, eggs dropped within their owned town plots, count towards their daily limit for eggs.
    - After a player's daily limit is reached, chickens will not drop eggs in their owned town plots. 
# Admin Guide
###### Reload
- `/tra reload` - Reload TownyResouces
- Configure Daily Extraction Limits
  - Daily Extraction Limits for each resources are are configured in the *TownyResources* config.yml file.
  - *NOTE*: After you make change to the daily extraction limits, individual player records do not change until new Towny New Day.
- Configure Offers
  - Resource Offers are configured in the *TownyResources* config.yml file.
  - ***WARNING***: If you reconfigure the offers list, it will change not only future discoveries, but also existing discoveries. Take particular care when removing an offer category, as this will remove the correponding discovery from any any towns towns which have already paid for it. 

# F.A.Q:
###### Question: 
How will *TownyResources* benefit my server ? 
###### Answer: 
#### 6 Benefits:
1. Provides a critical part of the *SiegeWar* experience - **A Good Reason To Start A War:**
   - There are surprisingly few good reasons to start a war in *SiegeWar*:
     - Resources: **NO**, because sieging does not provide resources. 
     - Expansion: **NO**, because sieging does not provide land control. 
     - Equipment: **NO**, because SiegeWar does not allow soldiers to steal the equipment of killed enemy soldiers (*for muliple important reasons as explained [here](https://github.com/TownyAdvanced/SiegeWar/wiki/Siege-War-FAQ)*).       
     - Automatically-Assigned Military Objectives: **NO**, because SiegeWar does not have this type of feature.
     - Roleplaying: **NO**, because most players are not roleplaying to any reasonable standard (*making new characters, adopting new personalities while in game, separating in-game & out-of-game conversations, following character-driven story arcs within the gameworld etc.)*.  
     - Personal: **NO**, because it creates toxicity.
     - PVP: **YES**. However, such sieges usually upset players who want to treat the game as strategy and/or roleplay-lite, as it "breaks their bubble".
     - Plunder Money: **YES**. However, money is usually regarded by players as less satifying & less useful than the acquisition of material resources.
   - *TownyResources* fixes this problem by transforming Resources into a good reason to start a war. 
      - The wisdom of this is demonstrated by the success of the *Civilization* series of games, which also has generated-terrain & sandbox geopolitical strategy, and in which: 
 <br> "*Resources are special commodities found in limited quantities on the map....are extremely important in the game, and the main reason for expansion and territorial wars ([Civ V Wiki](https://civilization.fandom.com/wiki/Resources_(Civ5))"*
2. Assists **Town Building:**
    - Town resources are automatically produced by the town itself. 
    - However it is the mayors / assistants / treasurers who get to actually collect the resources.
    - This dynamic naturally encourages the development of town governance, to determine how the valuable town resources are to be distributed.
3. Assists **Nation Building:**
    - Nation loyalty becomes a much more important decision for a town. Whichever nation they are in will get a good share of their daily resource production. Thus mayors will be much more motivated to be in a nation which is active and helpful.
    - Correspondingly, nations will have to work harder to keep towns loyal to them, including devising fair and efficient systems of resource distribution.
    - These dynamics will naturally encourage the development of active and competitive nations.
4. Assists **Trading**
    - By turning individual towns into centres for the production of specific goods, natural trading activities are encouraged.
5. Reduces **Grind**
    - Before, a multi-hour grind could grant a player a significant economic advantage over other players. *TownyResources* puts a hard cap on how much can be gained by each grind, thus facilitating a server a economy where extensive grinding simply has no part.
    - It is still possible to expend extra effort for a resource advantage over other players, but more varied and interesting activities are required to achieve it e.g. farming multiple varieties of crops, cave explorations, ruins exploration, trading, war. 
5. Assists **Roleplaying**
    - By giving each town a "signature" set of resources, this helps to develop the character of each town.
 
-----------
###### Question:
Won't players complain..... ? 
###### Answer:
Yes of course. As a server admin, you are lucky if your player's don't complain continually about basically everything.

----------
###### Question: 
Is is ok that players take longer to do things (*such as creating god sets or building epic monuments*) ?
###### Answer:
Yes.
<br>
- Consider 2 groups of players:
  - **Group A**: 
    - Are given daily gifts of money, any blocks they ask for, enhanced mining speed, and the flying ability to help them build faster.
    - Within a few days, manufacture expensive items such as god sets, shulker boxes, and beacons.
    - Within a few weeks, construct gigantic monuments to honour themselves.
    - Within a month, their riches are almost limitless.
    - Trading/diplomacy/sieges are now quite meaningless for them.
  - **Group B** 
    - Are given moderate daily limits to extracting resources, slowing down all their activities.
    - Set long term goals to manufacture expensive items such as god sets, shulker boxes, and beacons.
    - Set long term goals to construct gigantic monuments to honour themselves.
    - At first, even minor magical items, such as Efficiency II picks, are a great achievement.
    - Within a month, they are still working towards their goals, improving their governments, trading actively, co-operating with allies, and competing with dangerous enemies who could deal serious setbacks to their goals.   
<br>
- Which group of players do you think is more likely to stay on this server ?

----------
###### Question:
Is is ok to prevent block-breaking in an "unnatural" way ?
###### Answer: 
Yes.
<br>
- Just as *Towny* prevents-block breaking to protect the **Architectural** value of towns, *TownyResources* prevents block-breaking to protect the **Economic** value of towns.
- Both preventions are entirely unnatural, and are used for the same overall reason - to protect town value.
- Thus if a player believes truly, that preserving natural Minecraft block operations is more important than preserving the value of towns, then to avoid accusations of hypocrecy, they should first disable all *Towny* block protections in their own town, prior to making any complaints about the unnaturalness of *Towny Resources* protections.
----------