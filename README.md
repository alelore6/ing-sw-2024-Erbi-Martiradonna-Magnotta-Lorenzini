# CodexNaturalis in Java

![CodexNaturalis icon](https://github.com/Alessandro-Lorenzini/ing-sw-2024-Erbi-Martiradonna-Magnotta-Lorenzini/blob/main/CodexNaturalis/src/main/resources/assets/images/rulebook/01.png)

## Team
Filippo Erbì [@mrmarfi](https://github.com/mrmarfi)<br>
Gianluca Martiradonna [@gianluspng](https://github.com/gianluspng)<br>
Michele Magnotta [@MicheleMagnotta](https://github.com/MicheleMagnotta)<br>
Alessandro Lorenzini [@Alessandro-Lorenzini](https://github.com/Alessandro-Lorenzini)<br>

## Functionalities

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| Complete rules | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| Socket | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| RMI | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| TUI | ![#c5f015](https://placehold.it/15/44bb44/44bb44)|
| GUI | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| Chat | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| Disconnections | ![#c5f015](https://placehold.it/15/44bb44/44bb44) |
| Multiple games | ![#c5f015](https://placehold.it/15/f03c15/f03c15) |
| Persistence | ![#c5f015](https://placehold.it/15/f03c15/f03c15) |

## Setup
It is recommended to read first the [rulebook](CodexNaturalis/src/main/resources/assets/images/rulebook/CODEX_Rulebook_EN.pdf).

In [JARs](CodexNaturalis/deliverables/JARs) folder, there are two executable jars: one for the server and the other for clients. To start them, enter the following:
```shell
> java -jar Server.jar
```
```shell
> java -jar Client.jar [options]
```
where options could be one or more of the following:
```
> -tui -rmi -local
```
If you insert the first one, the client will start on TUI instead of GUI.
If you insert the second one, the client will start with RMI type connection instead of the socket one.
If you insert the third one, the client will try to connect on localhost.

## TUI commands
While playing with TUI, i.e., with command line, there are a few commands that are useful for playing:
```shell
> CARD [ID]
```
to print a card with the specified ID (they will appear while playing). Notice that it is permit if and only if you have the card on your played ones or it's a public or private objective card.
Another one is
```shell
> CHAT [P nickname] [message]
```
and it sends to all the message using the chat functionality. If P and an existing nickname is inserted, it sends a private message only to that specified player.

The last one is
```shell
> EXIT
```
to close the game.

## Disconnections
You can rejoin a game if you have closed the app with the same nickname and password inserted at the start. Notice that you can even change the play mode, e.g, you can switch from TUI to GUI or the connection type.

## Softwares

* [Lucid](https://lucid.app/): UML and sequence diagrams.
* [IntelliJ](https://www.jetbrains.com/idea/): Main IDE for project development.
* [Maven](https://maven.apache.org/): Package and dependency management.

## License

[**CodexNaturalis**](https://www.craniocreations.it/prodotto/codex-naturalis) is property of [_Cranio Creations_] and all of the copyrighted graphical assets used in this project were supplied by [**Politecnico di Milano**] in collaboration with their rights' holders.

[_Cranio Creations_]: https://www.craniocreations.it/
[**Politecnico di Milano**]: https://www.polimi.it/
