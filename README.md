This is a course project for Intro to Java to implement a popular multiplayer game, Codenames, using Java.

The wordlist is copied from https://github.com/Gullesnuffs/Codenames/blob/master/wordlist-eng.txt

To run this program, please run Server.java to start the server. Then, run ui/Client.java to start the client window. 
You need at least 4 people to start this game. The server architecture theoretically supports running unlimited number of games, each with unlimited number of players.

To understand the logic of the code, I suggest reading the [rules for Codenames.](https://czechgames.com/files/rules/codenames-rules-en.pdf)

Known error:

- In the waiting room, the unassigned text at the top does not update when the person following enters (which should automatically be placed under unassigned first). It would only update when someone else picked a role.
- When playing for the first time, when the red detectives first got a word wrong, it switches to blue detective phase instead of blue spymaster phase.