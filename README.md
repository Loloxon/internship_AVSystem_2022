# internship_AVSystem_2022
 
## System kontroli wind w budynku
Uruchomienie programu następuje poprzez odpalenie obiektu GUI.

System umożliwia 
- przywoływanie windy (lewa część), na co składa się:
  - wybranie konkretnego szybu-windy, 
  - wskazania piętra, na którym osoba się znajduje w danym momencie
  - wskazanie piętra docelowego
- obserwację ruchu wind na symulacji (centralna część)
- analizę konkretnych pozycji wind oraz informacji gdzie obecnie zmierzają, a także ile osób przewożą [^1] (prawa część)
- suwak do modyfikacji prędkości symulacji wind (dolna część) [^2]
- przyciski do pauzowania oraz wyjścia z programu

[^1]: Założono, że każde zamówienie windy to dokładnie jedna osoba oraz że w windzie mieści się 10 osób
[^2]: Prędkość wind, oraz czas poświęcany na otwieranie drzwi oparto na rzeczywistych danych, ruch suwaka nie modyfikuje tych wartości
---
#### Dodatkowe założenia i informacje:
- 16 wind jest dyspozycyjnych
- każda winda porusza się w osobnym szybie
- budynek posiada 10 pięter oraz parter
- każda winda porusza się niezależnie oraz wybiera docelowe piętro na podstawie analizy:
  - odległości od pięter, na które już zabrane osoby chcą jechać
  - odległości od pięter, na których znajdują się osoby czekające na windę
  - czasu oczekiwania poszczególnych osób (zarówno tych już zabranych, jak i tych czekających)

