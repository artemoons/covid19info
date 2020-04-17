# **CoVID-19 Info bot for Telegram**

## About

![](https://raw.githubusercontent.com/temautkin/covid19info/develop/wiki/pics/message-preview.png)

This is a bot for Telegram written on Java with help of Spring Boot Framework. 
It parse JSON that's based on information from стопкоронавирус.рф (https://xn--80aesfpebagmfblc0a.xn--p1ai/) 
and send obtained information to Telegram chat @coronavirus19statistic.

## Data sources
This bot requires JSON message for its input. And there is no source of it is bundled with this app. Yet. I will 
create second module in future that will produce it, so stay tuned.

```json{
  "Regions": [
    {
      "location": "Москва",
      "sick": "16146",
      "healed": "1394",
      "died": "113"
    },
    {
      "location": "Московская область",
      "sick": "3054",
      "healed": "91",
      "died": "33"
    },
    {
      "location": "Санкт-Петербург",
      "sick": "1083",
      "healed": "110",
      "died": "7"
    }
  ]
}
```

One official datasource that I found around the web is 
https://covid19.rosminzdrav.ru/wp-json/api/mapdata/, but to use it, you have to make some changes to files in DTO package.
In this bot I don't use it because it has delay around 20-30 minutes from СтопКоронавирус.рф.

## How to build
To build this bot you need to run Maven command `mvn clean install` in the
directory with root pom.xml file.

## How to run
In order to run bot first of all you need to set up all properties in application.yml. The simplest proxy for this bot
is opened Tor Browser - just launch it and use default values from application.yml (host - 127.0.0.1, port - 9150,
type - socks5).

| Parameter               | Description                                                    |
|-------------------------|----------------------------------------------------------------|
| bot.token               | Token for bot (get yours at @BotFather)                        |
| bot.username            | Bot username (get yours at @BotFather)                         |
| telegram.proxy.port     | Proxy port                                                     |
| telegram.proxy.host     | Proxy host                                                     |
| telegram.proxy.type     | Proxy type                                                     |
| telegram.channel-name   | Channel name starting with @ where bot will send messages      |
| telegram.sync-file-name | Sync file name with last parse result hashcode                 |
| website.url             | Website URL to parse from                                      |
| scheduler.delay         | Delay between checks for update                                |
| json.url                | URL for source JSON                                            |
| json.date-format        | Date format for JSON message (history)                         |
| json.history.file-name  | File name for history                                          |
| timezone.difference     | Difference in hours with Moscow                                |
| message.footer          | Message footer                                                 |

After this, you can run bot by command `mvn spring-boot:run` in parent directory or 
`java -jar <jar file name>`.

## Known bugs
1. All help files should present in parent directory (on one level with executable .jar file).
2. If internet connection is lost completely, bot with crash after some time


