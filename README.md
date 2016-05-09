# ClueCapstone
Remade the classic board game Clue in Java for my college Capstone project. 

Played with your phone connecting to your host computer's local IP address port 8000 (or port 80 if open). 
Example URL: 192.168.1.100:8000

You can find your local IP address by:
Windows: open command prompt, type in ipconfig, look for the IPv4 address of your current connection
Linux/Mac: open terminal, type ifconfig, look for the IPv4 address of your current connection

How to play:
1. Connect to the server WITH ALL YOUR DEVICES FIRST. DO NOT PRESS START GAME UNTIL ALL ARE IN. Common problem with people, see the shiny button and must click.
2. When all players are connected, click start game. 
3. You will be assigned a player depending on when you joined. First to join will be Prof. Plum and the rest are assigned clockwise around the board (Plum, Scarlett, Mustard, White, Green, Peacock). Your remote will match the color piece that you are playing
4. Move using the directional arrows. A die will be rolled at the beginning of the turn and a 'Moves left' counter will be displayed near the top
5. When inside a room, you can suggest, accuse, use a secret passage (if available), and exit the room.
 a. Suggest is the normal guess. You are constrained by only being able to suggest within your current room, but can choose any player or weapon. Players will be teleported to your room when suggested.
 b. Accuse is like suggest but final. This should be a last move for you as you will lose if you are wrong. The benefit is that you don't have to be in the room you are accusing.
 c. Secret passages are available Lounge <-> Conservatory and Study <-> Kitchen. In normal clue, you can't use the secret passage when you enter a room and you can only use it once. In this version, that isn't the case.
 d. Exit room allows you to re-enter the passages between rooms. When you exit, you can choose the room by toggling through doors and choosing exit. Single door rooms will still prompt you for this.

In case of disconnect:
Wait a few seconds for your session to expire, then refresh. If you get another player's screen, exit the browser, let the session expire and have the other player reconnect.
You can see the session expire when the number of connections (top right of the game board) decreases by 1.

Note: The Android version of Chrome actually closes the Websocket session when exiting the tab, so you usually don't have to worry about bad sessions from it. All other mobile browsers I've tried had problems so I had to make a watchdog for the sessions which will time out after 3 seconds.
