# ATTIVITÀ PROGETTUALE DI FONDAMENTI DI INTELLIGENZA ARTIFICIALE M
Il progetto consiste in un framework java che consenta all'utente di giocare ad Hanabi insieme a bots o a giocatori reali.
Il progetto è suddiviso in moduli, per ognuno dei quali è disponibile sorgente e jar.

- hanabi-api: contiene classi di supporto agli altri moduli
- json-v2: implementa oggetti json, attraverso i quali sono definite le entità di una partita
- hanabi-game: eseguibile, implementa il server host della partita. 
- hanabi-human-player: eseguibile, offre una GUI di gioco per utente reale.
- hanabi-bot1: eseguibile, implementa un bot che segue una strategia predefinita
- hanabi-bot2: eseguibile, implementa un bot che migliora le performance di bot1 adottando una nuova strategia predefinita più aggressiva
- hanabi-bot3: incompleto, implementa un bot che segue una terza strategia che fa uso di convenzioni tra giocatori
- hanabi-bot4: incompleto, implementa un bot che cerca di migliorare la seconda strategia facendo uso di convenzioni 

## Come giocare
Per iniziare una partita avviare il server host hanabi-game.jar e attraverso la GUI impostare il numero e tipo di giocatori. 
Successivamente avviare un jar (human-player o bot) per ogni giocatore definito come "aperto".
Inserire attraverso la GUI del giocatore l'indirizzo ip e la porta della server host.
La partita inizierà automaticamente quando tutti i giocatori saranno connessi.

Se si volesse vedere il log di esecuzione del server host o di un giocatore avviare quel componente da riga di comando.
