<div align="center">
    <h1>VersionBlocked</h1>
    <p>Заблокируй вход на сервер с неподдерживаемых версий</p>
</div>

### <a href="https://github.com/ilezzov-code/VersionBlocked/tree/main"><img src="img/flags/en.svg" width=15px> Select English README.md</a>

##  <a>Оглавление</a>

- [Описание](#about)
- [Особенности](#features)
- [Config.yml](#config)
- [Команды](#commands)
- [Ссылки](#links)
- [Поддержать разработчика](#donate)
- [Сообщить об ошибке](https://github.com/ilezzov-code/VersionBlocked/issues)


## <a id="about">Описание</a>

**VersionBlocked** — это уникальный плагин, который позволяет ограничить вход игрокам с неподдерживаемых версий клиента.

## <a id="features">Особенности</a>
* Возможность указать диапазон (от минимальной версии до максимальной версии)
* Возможность выборочно блокировать версии
* Настраиваемый экран кика с сервера
* Поддержка MiniMessages

## <a id="config">Config.yml</a>

<details>
    <summary>Посмотреть config.yml</summary>

```yaml
# ██╗░░░██╗███████╗██████╗░░██████╗██╗░█████╗░███╗░░██╗██████╗░██╗░░░░░░█████╗░░█████╗░██╗░░██╗███████╗██████╗░
# ██║░░░██║██╔════╝██╔══██╗██╔════╝██║██╔══██╗████╗░██║██╔══██╗██║░░░░░██╔══██╗██╔══██╗██║░██╔╝██╔════╝██╔══██╗
# ╚██╗░██╔╝█████╗░░██████╔╝╚█████╗░██║██║░░██║██╔██╗██║██████╦╝██║░░░░░██║░░██║██║░░╚═╝█████═╝░█████╗░░██║░░██║
# ░╚████╔╝░██╔══╝░░██╔══██╗░╚═══██╗██║██║░░██║██║╚████║██╔══██╗██║░░░░░██║░░██║██║░░██╗██╔═██╗░██╔══╝░░██║░░██║
# ░░╚██╔╝░░███████╗██║░░██║██████╔╝██║╚█████╔╝██║░╚███║██████╦╝███████╗╚█████╔╝╚█████╔╝██║░╚██╗███████╗██████╔╝
# ░░░╚═╝░░░╚══════╝╚═╝░░╚═╝╚═════╝░╚═╝░╚════╝░╚═╝░░╚══╝╚═════╝░╚══════╝░╚════╝░░╚════╝░╚═╝░░╚═╝╚══════╝╚═════╝░

# Socials / Ссылки:
# • Contact with me / Связаться: https://t.me/ilezovofficial
# • Telegram Channel / Телеграм канал: https://t.me/
# • GitHub: https://github.com/ilezzov-code

# By me coffee / Поддержать разработчика:
# • YooMoney: https://yoomoney.ru/to/4100118180919675
# • Telegram Gift: https://t.me/ilezovofficial
# • TON: UQCInXoHOJAlMpZ-8GIHqv1k0dg2E4pglKAIxOf3ia5xHmKV
# • BTC: 1KCM1QN9TNYRevvQD63UF81oBRSK67vCon
# • Card: 2200 7007 3348 7101 (T-Bank)

# Check for updates / Проверять наличие обновлений
check-updates: true

# Языковой файл. Поддерживаемые (ru-RU, en-US), вы можете загрузить свой перевод, создав файл в папке messages/custom.yml
# Language file. Supported (ru-RU, en-US), you can upload your own translation by creating a file in the messages/custom.yml folder
lang: "en-US"

# Версии протокола
# Protocol versions
# Версия Minecraft | Номер протокола
# Minecraft Version | Protocol Number
# 1.7.2-1.7.3-1.7.4-1.7.5 | 4
# 1.7.6-1.7.7-1.7.8-1.7.9-1.7.10 | 5
# 1.8-1.8.1-1.8.2-1.8.3-1.8.4-1.8.5-1.8.6-1.8.7-1.8.8-1.8.9 | 47
# 1.9 | 107
# 1.9.1,108
# 1.9.2 | 109
# 1.9.3-1.9.4 | 110
# 1.10-1.10.1-1.10.2 | 210
# 1.11 | 315
# 1.11.1-1.11.2 | 316
# 1.12 | 335
# 1.12.1 | 338
# 1.12.2 | 340
# 1.13 | 393
# 1.13.1 | 401
# 1.13.2 | 404
# 1.14 | 477
# 1.14.1 | 480
# 1.14.2 | 485
# 1.14.3 | 490
# 1.14.4 | 498
# 1.15 | 573
# 1.15.1 | 575
# 1.15.2 | 578
# 1.16 | 735
# 1.16.1 | 736
# 1.16.2 | 751
# 1.16.3 | 753
# 1.16.4-1.16.5 | 754
# 1.17 | 755
# 1.17.1 | 756
# 1.18-1.18.1 | 757
# 1.18.2 | 758
# 1.19 | 759
# 1.19.1-1.19.2 | 760
# 1.19.3 | 761
# 1.19.4 | 762
# 1.20-1.20.1 | 763
# 1.20.2 | 764
# 1.20.3-1.20.4 | 765
# 1.20.5-1.20.6 | 766
# 1.21-1.21.1 | 767
# 1.21.2-1.21.3 | 768
# 1.21.4 | 769
# 1.21.5 | 770
# 1.21.6 | 771
# 1.21.7-1.21.8 | 772
# 1.21.9-1.21.10 | 773
# 1.21.11 | 774

# Настройки фильтрации версий
# Version filtering settings
version-filter:
  # Минимальная версия протокола, с которой можно подключиться к серверу
  # Minimum protocol version allowed to connect to the server
  min-version-protocol: 4
  # Используйте -1, если не хотите ставить ограничение на последнюю версию игры
  # Use -1 if you do not want to set a limit on the latest game version
  max-version-protocol: -1
  # Заблокированные версии
  # Blocked versions
  blocked-protocols:
    - 355
    - 393
  # Отключить игроков с неподдерживаемой версией после /versblock reload
  # Disconnect players with unsupported versions after /versblock reload
  kick-connected: true

# Логирование в консоль
# Console logging
logging:
  enabled: true
  # Игрок <player> пытался зайти с версии <protocol> (<minecraft-version>)
  # Player <player> tried to join with version <protocol> (<minecraft-version>)
  format: "Player <player> tried to join with version <protocol> (<minecraft-version>)"

# Internal configuration version. Do not modify!
# Внутренняя версия конфигурации. Не редактируйте!
config-version: ${project.version}
```

</details>

## <a id="commands">Команды (/команда → /псевдоним1, /псевдоним2, ... ※ `право`)</a>

### /versionblocked → /vb ※ `versionblocked.main_comamnd` 

* Доступ к главной команде плагина

### /versionblocked reload → /vb reload ※ `versionblocked.main_comamnd.reload`

* Перезагрузить конфигурацию плагина

## <a id="minimessage-support">Поддержка MiniMessage</a>

Плагин поддерживает любые виды форматирования текста в Minecraft

- **LEGACY** — Цвет через & / § и HEX через &#rrggbb / §#rrggbb или &x&r&r&g&g&b&b / §x§r§r§g§g§b§b
- **LEGACY_ADVANCED** — Цвет и HEX через &##rrggbb / §##rrggbb
- **MINI_MESSAGE** — Цвет через <цвет> Подробнее — https://docs.advntr.dev/minimessage/index.html

И все форматы доступные на https://www.birdflop.com/resources/rgb/
Вы можете использовать все форматы одновременно в одном сообщении.

## <a id="links">Ссылки</a>

* Связаться: https://t.me/ilezovofficial
* Telegram Channel: https://t.me/ilezzov

## <a id="donate">Поддержать разработчика</a>

* DA: https://www.donationalerts.com/r/ilezov
* YooMoney: https://yoomoney.ru/to/4100118180919675
* Telegram Gift: https://t.me/ilezovofficial
* TON: UQCInXoHOJAlMpZ-8GIHqv1k0dg2E4pglKAIxOf3ia5xHmKV
* BTC: 1KCM1QN9TNYRevvQD63UF81oBRSK67vCon
* Card: 5536914188326494

## Found an issue or have a question? Create a new issue — https://github.com/ilezzov-code/VersionBlocked/issues/new

## <a id="license">Лицензия</a>

Этот проект распространяется под лицензией `GPL-3.0 License`. Подробнее см. в файле [LICENSE](LICENSE).
