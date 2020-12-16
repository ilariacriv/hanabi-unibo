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

## Miglioramenti
In esecuzione, il framework risulta pesante. Al termine di una partita il processo di un giocatore avrà allocato circa 200MB di ram; il serverhost ne chiede circa 150MB.
Probabilmente la richiesta di spazio dipende dall'accumulo degli stati della partita, rappresentati tramite JSONObject e classi derivate dei moduli json-v2 e hanabi-api.
Un'ottimizzazione di queste rappresentazioni dovrebbe permettere di guadagnare memoria. Il modulo json-v2 non è stato progettato per occupare la minor memoria possibile. Tuttavia è possibile anche che copie degli stati della partita create durante l'esplorazione delle mosse non siano eliminate dal garbage collector, quindi, forse, si può limitare la memoria usata senza compromettere l'esecuzione avviando gli eseguibili da riga di comando e inserendo i parametri di impostazione della memoria disponibile alla jvm. 
