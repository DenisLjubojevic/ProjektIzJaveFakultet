# Aplikacija za upravljanje procesima zoološkog vrta

Ovaj projekt napravljen je u edukativne svrhe u sklopu fakulteta vezano za projektni zadatak te je kasnije bio podloga za završni rad. Sami projekt napravljen je u programskom jeziku Java
te je osmišljen kao desktop aplikacija koja bi trebala pomoći zaposlenicima zoološkog vrta da na brži i jednostavniji način upravljaju svim procesima unutar zoološkog vrta.

## Korišteni alati za izradu projekta:

  1. IntelliJ IDEA
  2. JavaFX
  3. h2 baza podataka

## Sigurnost

Kako bi aplikacija bila što sigurnija implementirane su pripremljene naredbe (engl. _prepared statements_) i postupak _hashiranja_ lozinke.
Ove značajke osiguravaju zaštitu od "ubrizgavanja SQL-a" (engl. _SQL injection_) te zaštitu vlastitih lozinki unutar baze podataka.

## Moguća proširenja

Pošto se aplikacija bazira isključivo za korištenje zaposlenika unutar zoološkog vrta te nema veliku ponudu za posjetitelje, može se napraviti android verzija iste aplikacije.
Aplikacija bi se bazirala na lokalnoj bazi podataka **Room** kako bi posjetitelji mogli pregledavati informacije prilikom posjete zoološkog vrta. Za prikaz elemenata unutar liste
može se koristiti **RecyclerView** komponenta te za interaktivnu kartu aplikacija se može povezati s **Google Maps**.

## Prikaz aplikacije

### Forma za prijavu
Prilikom otvaranja aplikacije pojavljuje se forma za prijavu koja osigurava da naša aplikacija ispunjava principe autentifikacije i autorizacije. Svi dijelovi teksta unutar aplikacije složeni
su da budu responzivni, odnosno da se prilagođavaju velićini ekrana.

![loginScreen](https://github.com/user-attachments/assets/08b88133-fa1e-44ab-bab1-e96c5a81c9aa)

### Pregled podataka
Unutar aplikacije u obliku tablice su prikazane sve informacije vezano za životinje. Osim pregleda moguće je i pretraživati podatke te dodavati, mijenjati ili brisati postojeće podatke.

![pretragaZivotinja](https://github.com/user-attachments/assets/cd518a7e-4eaf-488e-95bd-20af27dcbadd)

### Karte
Kako aplikacija nebi imala samo mogućnost pretraživanja podataka dodane su i interaktivna karta kao i karta staništa. Interaktivna karta omogućuje da korisnici pregledavaju lokaciju
zoološkog vrta kao i njeinu okolinu. Karta staništa je napravljena kao slika koja na sebi ima gumbe koji predstavljaju na kojem mjestu se nalazi koje stanište. Prilikom odabira gumba
prikazuju se detalji tog staništa.

**Interaktivna karta**
![interaktivnaKarta](https://github.com/user-attachments/assets/2d39646e-5044-43b1-bad8-6fa119aefe00) 

**Karta staništa**
![kartaStanista](https://github.com/user-attachments/assets/d78d8e33-15cb-44e1-ba2b-882a98c27cc6)

### Rasporedi
Unutar aplikacije još se može naći raspored rada za svakog zaposlenika te raspored hranjenja svakog staništa.

**Raspored rada**
![RasporedRada](https://github.com/user-attachments/assets/8c127057-8d6d-40a0-8c71-4e443f54838b)

**Raspored hranjenja**
![RasporedHranjenja](https://github.com/user-attachments/assets/71498ef8-e466-40cc-a4f0-654bb5f0a3cc)

