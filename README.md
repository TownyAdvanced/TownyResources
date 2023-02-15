# TownyResources
*TownyResources* adds value to towns, by giving each one a unique set of automatically-produced resources which can be collected by players (*e.g. Emeralds, Coal, Oak Log, Wheat etc.*).

The plugin also has an optional feature to protect resource value, via daily player extraction limits. This can be enabled in the config if required.

# Benefits:
- 🏙️ Encourages **Town Building**. 
- 🇺🇳 Encourages **Nation Building**.
- 💰 Encourages **Trading**.
- 💤 Reduces **Grind**.
-  🧚‍♀️ Assists **Roleplaying**.
- ⚔️ Improves the [***SiegeWar***](https://github.com/TownyAdvanced/SiegeWar) experience, by adding **a new non-toxic reason for war**: Capturing Resources.

(*details in the FAQ section below*)

# Installation Guide
1. Download the latest *TownyResources* Jar from [here](https://github.com/TownyAdvanced/TownyResources/releases), and drop it into your plugins folder.
2. Restart your server.
3. Edit the *TownyResources* config.yml file, and set `surveys > enabled` to `false` (*this stops players from discovering resources until your settings are ready*).
4. Run `/ta resources reload`.
5. Edit the *TownyResources* config.yml file, and change any settings you would like.
   - Note: Do not add Eggs, Honeycomb, or Honey Bottle to the daily-limits list, as these limit types are not yet operational.
6. Edit your townyperms.yml file, and add the following perms:
   - Mayor / Assistant / Treasurer:
     `- townyresources.command.towncollect`
   - King / Assistant / Treasurer:
     `- townyresources.command.nationcollect`
7. Using your permissions plugin, give this to any admins who are not already OP:
    - `- townyresources.admin.command.*`
    - Optionally, give your admins `townyresources.bypass` if you would like to never be restricted by extraction limits, without having to use /tra bypass.
8. If you want to show town production on the *Dynmap* (**Recommended**), first ensure you have the [*Dynmap-Towny*](https://github.com/TownyAdvanced/Dynmap-Towny/releases) plugin installed, then add the following to the 'infowindow' section of your *Dynmap-Towny* config file:
    ```
    <br/><span style="font-weight:bold;">Resources&colon; %town_resources%</span>
    ```
9. If you want to use the [*Slimefun*](https://github.com/Slimefun/Slimefun4/releases) plugin with *TownyResources*:
   - Town Production:
     <br>You can add *Slimefun* items to the offers list, simply by using *Slimefun* item ID's. See [here](https://github.com/Slimefun/Slimefun4/blob/master/src/main/java/io/github/thebusybiscuit/slimefun4/implementation/SlimefunItems.java) for *Slimefun* item ID's. Example:
    ```
    ...{BUCKET_OF_OIL, 100, 1 BUCKET_OF_OIL}...{Valuable_Dust, 100, 1, COPPER_DUST, TIN_DUST}...
    # F.Y.I. the first category name can translate to non-english as it is a vanilla Slimefun item ID, however the second one cannot, unless you add a translation string.
    ```
   - Daily Player Limits
     <br>Due to technical limitations, it is currently not possible to have daily-limits for *Slimefun* resources. However, a reasonably good extraction-control configuration can be achieved by first adding the small few raw *Slimefun* resources to the Town Production offers (*Sifted Ore, Oil Bucket, Uranium, Salt, Nether Ice*), and then disabling the small few machines which directly extract these (*gold_pan, oil_pump etc.*).
10. If you want to use the [*MythicMobs*](https://git.mythiccraft.io/mythiccraft/MythicMobs/-/wikis/Home) plugin with *TownyResources*:
    - Town Production:
    <br>You can add *MythicMobs* items to the offers list, simply by using *MythicMobs* item 'internal_name'. See [here](https://git.mythiccraft.io/mythiccraft/MythicMobs/-/wikis/Items). Example:
    ```
    ...{example_category, 100, 1, ExampleItem}...{another_example_category, 100, 1, ExampleItem, ExampleItem2}...
    
    Mythic Mobs item config (plugins/MythicMobs/Items/example_items.yml)
    ExampleItem:
      Id: stone
      Data: 0
      Display: '&3Example Item'
    ExampleItem2:
      Id: dirt
      Data: 0
      Display: '&3Example Item 2'
        ```
11. If you want to use [*MMOItems*](https://www.spigotmc.org/resources/mmoitems-premium.39267/) plugin with *TownyResources*:
    - Town Production:
    <br>You can add *MMOItems* items to the offers list, simply by using the following format: TYPE:ID. Example:
    ```
    categories: '{mmo_items, 100, 0.015625, SWORD:CUTLASS}'
    ```
    The above example gives one cutlass per day to a town.
12. If you want to translate material names into a non-english language, first ensure you have the [*LangUtils*](https://ci.nyaacat.com/job/LanguageUtils/job/1.17/) plugin installed, then set your preferred language in the *TownyResources* Config.yml file.
13. Edit the *TownyResources* config.yml file, and set `surveys > enabled` to `true`.
14. Run `/ta resources reload`, then `/ta reload`.
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
- To do a Survey, enter the target town and run: `/t resources survey`.
- If the Survey succeeds, a global success message will be generated. Example:
  > Goosius1 has discovered coal deposits at Rome!. Daily Production: 64 Coal.
###### Daily Production
- On each new Towny New Day, towns automatically produce resources.
- Production is modified by the resource level:
  - Resource Level **1**: Production bonus **N/A**. Minimum town level **2**.
  - Resource Level **2**: Production bonus **+100%**. Minimum town level **4**.
  - Resource Level **3**: Production bonus **+200%**. Minimum town level **6**.
  - Resource Level **4**: Production bonus **+300%**. Minimum town level **8**.
- After resources are produced, they are shown as available for collection on the town screen. Example:
  > Resources:
  <br> > Daily Production: 64 Oak Log, 32 Emerald
  <br> > Available For Collection: 64 Oak Log, 32 Emerald
- If a town is not high enough level to produce a resource, the amount shows as zero. Example:
  > Resources:
  <br> > Daily Production: 64 Oak Log, 0 Emerald
- For each resource, a town can store a maximum of 5x the production amount. When stores are full, subsequent production is lost.                          
###### Collecting Town Resources
- To collect town resources, as a mayor/assistant/treasurer, enter your town and run `/t resources collect`
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
- To collect nation resources, as a king/assistant/treasurer, enter your capital and run `/n collectresources`
- The available resources will then be dropped at your position.
### Daily Extraction Limits
###### Resource Categories
- Extraction is limited per resource category.
- Example: The resource category of "Common Rocks" contains both STONE and COBBLESTONE. Extracting either of those items counts towards the daily limit.
###### Limit Schedule
- Extraction is limited per *Towny* day.
- The daily extraction limits of all players are reset at *Towny* New Day.
###### Information
- When a player hits their daily extraction limit for a particular category of material, they will see an information bar message. Example:
  > Daily extraction limit reached for Beetroot (64)
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
###### Configuration
- Resource Offers
  - Resource Offers are configured in the *TownyResources* config.yml file.
  - ***WARNING***: If you reconfigure the offers list, it will change not only future discoveries, but also existing discoveries. Take particular care if removing an offer category, as this will remove the correponding discovery from any any towns towns which have already paid for it. 
- Daily Extraction Limits
  - Daily Extraction Limits are configured in the *TownyResources* config.yml file.
###### Commands
- `/ta resources reload` - Reload TownyResouces.
- `/ta resources bypass` - Turns on and off bypass limits for your player.
- `/ta resources reroll_all_resources` - Reroll all already-discovered town resources.

# F.A.Q:
###### Question: 
How will *TownyResources* benefit my server ? 
###### Answer: 
#### 6 Key Benefits:
1. Assists **Town Building**:
    - Town resources are automatically produced by the town itself. 
    - However it is the mayors / assistants / treasurers who get to actually collect the resources.
    - This dynamic naturally encourages the development of good town governance, to determine how the valuable town resources are to be distributed.
2. Assists **Nation Building**:
    - Joining a nation, and staying in it, becomes a much more important decision for a town. Whichever nation they are in will get a good share of their daily resource production. Thus mayors will be much more motivated to be in a nation which is active and helpful.
    - Correspondingly, town loyalty becomes much more important for nations. Nations must work harder to keep their towns, by being more active, more helpful, and developing good governance, including fair and efficient systems of resource distribution.
    - These dynamics will naturally encourage the development of active and competitive nations.
3. Assists **Trading**:
    - By turning individual towns into centres for the production of specific goods, trading activities are naturally encouraged.
4. Reduces **Grind**:
    - Without *TownyResources*, a multi-hour grind can grant a player a significant economic advantage over other players.
    - *TownyResources* puts a hard cap on how much can be gained by each grind, thus facilitating a server a economy where extensive grinding simply has no part.
    - With *TownyResources*, it is still possible to expend extra effort for a resource advantage over other players, but more varied and interesting activities are required to achieve it e.g. farming multiple varieties of crops, cave explorations, ruins explorations, trading, war. 
5. Assists **Roleplaying**
    - By giving each town a unique "signature" set of resources, this helps to develop the character of each town.
6. Improves the [***SiegeWar***](https://github.com/TownyAdvanced/SiegeWar) experience, by adding **a new non-toxic reason for war**: Capturing Resources.
   - Vanilla SiegeWar provides relatively few rewards for victorious attackers - a one-time plunder money steal, climbing the `\n list`, coloring the map, and achieving a higher nation claims bonus
   - Some attacking players may feel that this is not quite enough, that wars 'pointless'; and yet giving them more spoils (e.g. money, sets) would usually result in more punishment for the defender. Although players may selfishly seek to take everything from defeated opponents, clever server owners recognize that this is not a sustainable pattern for the server.
   - *TownyResources* addresses this issue by giving victorious nations a reward which is useful but which defenders will not miss as much as money or sets.
   - The general importance of geo-resources in strategy games is illustrated by this this quote from the *Civilization* series of games:
 <br> "***Resources are special commodities found in limited quantities on the map....are extremely important in the game, and the main reason for expansion and territorial wars" ([Civ V Wiki](https://civilization.fandom.com/wiki/Resources_(Civ5)))***

-----------
###### Question: 
Should I enable the daily-limits feature?
###### Answer:
- It depends on your server.
- Generally, if you don't enable it, you will have a little more daily work to do in balancing the economy.
- Generally in long-running gameworlds, players have stockpiles of resources, and indeed some markets may be flooded. Thus if limits are introduced, it will take a while for the cut in production to restore balance to the economy. In the meantime some town resources may be perceived as worthless. Thus, daily-limits are more suitable for new gameworld than long-established ones.
- Players often have an adverse reaction to the idea of limits.
- In many ways this reaction is cultural in nature. The idea of "living in harmony" with the environment, and not destroying it by extracting resources at the fastest possible reate, is better established in some cultures than others.
- In-game, few servers seem to have attempted this so far, but there is theoretical evidence that it can work:
```
- Consider 2 groups of players:

  - Group A: 
    - Are given daily gifts of money, any blocks they ask for, enhanced mining speed, and the flying ability to help them build faster.
    - Within a few days, manufacture expensive items such as god sets, shulker boxes, and beacons.
    - Within a few weeks, construct gigantic monuments to honour themselves.
    - Within a month, their riches are almost limitless.
    - Trading/diplomacy/sieges are now quite meaningless for them.
    
  - Group B: 
    - Are given moderate daily limits to extracting resources, slowing down all their activities.
    - Set long term goals to manufacture expensive items such as god sets, shulker boxes, and beacons.
    - Set long term goals to construct gigantic monuments to honour themselves.
    - At first, even minor magical items, such as Efficiency III picks, are a great achievement.
    - Within a month, they are still working towards their goals, improving their governments, trading actively, co-operating with allies, and competing with dangerous enemies who could deal serious setbacks to their goals.   
    
  Which group of players do you think is more likely to stay on this server ?
  ```
- Keep in mind that whatever the pros and cons of the feature, it is no less ***Natural*** than *Towny* block protections, i.e. Just as *Towny* magically prevents-block breaking to protect the **Architectural** value of towns, *TownyResources* magically prevents block-breaking to protect the **Economic** value of towns.

-----------
