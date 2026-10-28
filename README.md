# About the mod
This is an achievement gui mod, it:
- Adds new gui with dynamic size, now more achievements could be seen
- Adds tab system, now achievements could be separated and sorted into tabs, which adds infinite free space

Tabs are meant to be added by other mods, there are no new tabs by default.

## For mod developers
Look into AchievementLibExampleMod (WOULD BE PUBLISHED LATER).

Basically, to add new tab you need to:
1. Write TabCustom class which extends TabAbstract
2. In TabCustom construct you must add achievements. This construct is called on every gui initialization
3. In your mod registry add TabCustom "TabRegistry.register(TabCustom.class);"

### Further development
I'm not a professional, and I am free for feedback and especially critique. If you have anything to say about this mod, contact me on discord (@vb1dra) or via GitHub's issues page (https://github.com/MinecraftIsTooEasy/AchievementLib/issues)

AchievementLibExampleMod would be published soon.

Things to think about for further updates:
- Is it better to completely remove vanilla gui? At this moment it's still in game.

Update plan:
- Tabs must have hidden achievements based on their parents and children. For example - locked only unlocked, first locked achievements and their children must be visible, so far locked achievements are invisible
- Registry for achievements that would be excluded from TabDefault. Right now all achievements are represented in TabDefault as they're represented in vanilla gui

