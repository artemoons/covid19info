# **CoVID-19 Info bot for Telegram**

## About

![](https://s217iva.storage.yandex.net/rdisk/05021467f0f909857438f023c3e5c1f10f8bf4ca5387a142a7e7b1a4cf9955b6/5e874574/-szdBF0zXYottYN909ERDcu-FEykhuHAOtutgyqG8JfQaGt3QX5b1NnBpN5Jo0ulve8PkOBEY62Cb198ok4HNA==?uid=35573294&filename=screen.PNG&disposition=inline&hash=&limit=0&content_type=image%2Fpng&owner_uid=35573294&fsize=7933&hid=520ecc8fc2cd15ce11c254a5f5d014ee&media_type=image&tknv=v2&etag=8e8bc4ccb919be32ef867657a1e9b31d&rtoken=HfW6kvxLyFDI&force_default=yes&ycrid=na-ba72468efee616b41ec65e9d48a659fb-downloader22e&ts=5a26393854500&s=a383fcbd61fce73bee5e56c9223e37dc03bf11f6d080f22eaec502a4d08519f2&pb=U2FsdGVkX19WyOqERt6Q-iaZhAOhwIujH_HRRoPYYyxKCwphpzVHzatVMTe4_OoQcvgUEWh1Eqdc0QZVuGKlzCjnNPe63HGafOu9je6MUHo)

This is a bot for Telegram written on Java with help of Spring Boot Framework. 
It parse web page at стопкоронавирус.рф (https://xn--80aesfpebagmfblc0a.xn--p1ai/) and send obtained
information to Telegram chat @coronavirus19statistic.

## How to build
To build this bot you need to have Maven and run  command `mvn clean install` in the
directory with root pom.xml file.

## How to run
In order to run bot first of all you need to set up all properties in application.yml.

| Parameter               | Description                                    |
|-------------------------|------------------------------------------------|
| bot.token               | Token for bot (get yours at @BotFather)        |
| bot.username            | Bot username (get yours at @BotFather)         |
| telegram.proxy.port     | Proxy port                                     |
| telegram.proxy.host     | Proxy host                                     |
| telegram.proxy.type     | Proxy type                                     |
| telegram.channel-name   | Channel name where bot will send messages      |
| telegram.sync-file-name | Sync file name with last parse result hashcode |
| website.url             | Website URL to parse from                      |
| scheduler.delay         | Delay between checks for update                |

After this, you can run bot by command `mvn spring-boot:run` in parent directory or 
`java -jar <jar file name>`.
