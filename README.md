# ATTIVITÀ PROGETTUALE DI FONDAMENTI DI INTELLIGENZA ARTIFICIALE M
Il progetto consiste nell'ampliamento del server di gioco con un modulo che permette di giocare con una rete neurale.

E' stato aggiunto il modulo hanabi-neural-network, eseguibile solo nella versione a 2 giocatori, calcola l'azione da eseguire tramite una rete neurale.

Per dettagli sul funzionamento della rete, leggere il report: [Hanabi_Report](https://github.com/ilariacriv/hanabi-unibo/blob/master/Hanabi_Report.pdf)

## Come giocare
- Si lancia lo script python che apre la socket e si mette in attesa.
- Si lancia il server di gioco GameServer
- Si seleziona NeuralNetwork come giocatore nel server di gioco.
- Bot instaura la connessione con NN su una socket TCP.
- Per ogni turno di NN:
  - Bot riceve dal server di gioco lo stato in formato JSON
  - Bot costruisce la stringa di double che rappresenta lo stato (FinalState)
  - Bot invia la stringa a NN
  - NN calcola l'azione da effettuare e restituisce un intero da 0 a 19 che rappresenta il codice dell'azione
  - Bot esegue il parsing del risultato e restituisce al server l'azione richiesta
- Al termine della partita Bot chiude la connessione e termina. In caso di partite multiple il server Pyhton riapre una socket per la partita successiva.

## Miglioramenti
Il modulo creato funziona solamente nel caso di partite a due giocatori, si potrebbe pensare di estendere il modulo con il supporto a 3 e/o 4 giocatori.
Il dataset utilizzato per l'allenamento contiene i dati generati da 36.000 partite e probabilmente la rete funzionerebbe meglio se avesse a disposizione più stati per allenarsi. In fase di addestramento
si è infatti notato come l'accuracy raggiunta con 1/4 del dataset fosse del 65%, mentre con il dataset
completo è stato possibile raggiungere il 79.4% finale.

La versione con ordinamento dei colori ha performance migliori per le azioni di suggerimento,
mentre gioca e scarta molto male. Si potrebbe approfondire questa formattazione provando a
capire se ci sono eventuali errori nell'ordinamento delle carte ed in caso affermativo risolverli per
verificare un effettivo miglioramento nelle performance rispetto all'utilizzo del dataset standard.

Per concludere, al fine di migliorare ulteriormente la stabilità e le performance della rete neurale,
si potrebbe ricorrere all'utilizzo di tecniche come cross-validation.
