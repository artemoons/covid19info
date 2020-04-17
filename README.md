# **CoVID-19 Info bot for Telegram**

## About

![](https://s423sas.storage.yandex.net/rdisk/dee6e4d917dfa2d3834d94e51830106d9e439cb0efeb8c28d78202001bc76327/5e9950ad/-szdBF0zXYottYN909ERDeo7pfCPxlBES8Yqzg1wlY0VQpER7p7Hd-fhi94imO0GeX0Z75Aut0TUpFBMPFD3IA==?uid=35573294&filename=%D0%90%D0%BD%D0%BD%D0%BE%D1%82%D0%B0%D1%86%D0%B8%D1%8F+2020-04-17+094437.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&tknv=v2&owner_uid=35573294&fsize=11403&media_type=image&etag=8dc78907c0553fad1d769329d578f4f1&hid=4a333388f660b602ac3af3fb39072c77&rtoken=P2PwEqwAowRB&force_default=yes&ycrid=na-af2d5b2183da60227d533d0ced409ef1-downloader20e&ts=5a376e743c540&s=19190855b4486c3bdfa570979b1b50596a0770ac5da0fe244c276fcb1bedf647&pb=U2FsdGVkX18qMpNpOuMAY-GOBjeCKik4A2dWuyJ8Qk2jBSoEfW7PQPMbTUhgOE564YvYEdW5wm5dUhPEOyljvfmToeUdj6PwDl44YcHvQEY)

This is a bot for Telegram written on Java with help of Spring Boot Framework. 
It parse JSON that's based on information from стопкоронавирус.рф (https://xn--80aesfpebagmfblc0a.xn--p1ai/) 
and send obtained information to Telegram chat @coronavirus19statistic.

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

## Known bugs
1. All help files should present in parent directory (on one level with executable .jar file).
2. If internet connection is lost completely, bot with crash after some time

After this, you can run bot by command `mvn spring-boot:run` in parent directory or 
`java -jar <jar file name>`.
