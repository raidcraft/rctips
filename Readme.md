# RCTips - Chat-Tipps für Spieler

Jeden Spieler werden im Chat Tipps angezeigt. Die einzelnen Tipps können dann mit einem Klick auf 'OK' bestätigt werden um neue Tipps anzuzeigen. Der Spieler erhält pro bestätigten Tipp eine Belohnung.

## Befehle
### Alle Tipps anzeigen
``/tipps``

Listet alle konfigurierten Tipps auf und zeigt an ob dieser Tipp durch den Spieler bereits bestätigt wurde.

### Spieler Zurücksetzen
``/rcta purge <Spieler>``

Setzt für den Spieler alle bereits bestätigten Tipps zurück.

Benötigt die Permission ``rctips.admin.purge`` 

## Permissions

Damit Spieler Tipps erhalten muss dieser die Permission ``rctips.tip.receive`` gesetzt haben.

## Konfiguration

```yaml
# Minimum delay between tips in seconds
minimum_tip_delay: 120
# Maximum delay between tips in seconds
maximum_tip_delay: 900
# Added tip delay for each accepted tip in seconds
additional_accepted_tip_delay: 60

# List of all tips
#-----------------
tips:
  # Numeric order of tip.
  # Lowest ordered tips will be showed first
  0:
    # Unique-ID which is used in database to store accepted tips
    id: about-tips
    # Text of this tip which is used in chat message
    # URLs are automatically highlighted (https://google.com)
    # Minecraft Chat-Command are automatically highlighted (/home)
    text: Du bekommst für jeden Tipp eine Belohnung
    # Reward-ID which is credited if player accepts this tip.
    # 'random' if any configured reward should be used.
    reward: random

# List of all rewards
#--------------------
rewards:
  # Unique-ID of this rewards. Could be used in tip configuration
  diamond-ore:
    # Name of reward. Is used in Hover-Box to show reward credit to player
    name: +1 Diamant-Erz
    # Description of reward. Is used inside sentences like:
    # "Als Belohnung wirst Du [...] erhalten"
    description: 1 Diamant-Erz
    # Command which is executed with OP privileges when rewards is credited
    command: give %player% diamond_ore 1
```