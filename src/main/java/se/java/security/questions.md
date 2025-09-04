# Prestanda- och skalbarhetsfrågor
1. Vad händer när en användare söker efter 'red collars under $25' i en databas
med 100 000 produkter. Vilken data flyttas från MongoDB till vår Java-applikation?

2. Om vpr produktkatalog växer till 1 miljon produkter, vad händer med vår
filtreringsprestanda? Var kan flaskhalsar uppstå?

# Arkitektur- och designfrågor
Tänk på dataflödet när ni diskuterar dessa frågor.

1. Vi har filtreringslogik på två olika ställen - var och varför? Vilka är kompromisserna?

2. Vad händer vid programstart med vår FilterRegistry-klass? Hur skalas detta när du lägger till fler produkttyper?

3. Om vi vill lägga till en ny produkttyp som heter 'Bed' med filter för 'size' och 'washable', hur många filer behöver vi modifiera?

# Frågor om databasanvändning

1. MongoDB är riktigt bra på att filtrera, sortera och indexera. Drar vi full nytta av dessa funktioner? Var kan vi kanske inte ha utnyttjat dem fullt ut?

# Frågor om kodkvalitet
Titta på "maintainability".

1. Vad händer när vi vill lägga till komplexa filter som "pris mellan X och Y" eller "namn innehåller flera ord"? Vart skulle denna logik hamna?

2. Titta på antalet ställen där vi konverterar mellan strängprodukttyper och Java-klasser. Vad säger detta om vår design?

# Övergripande diskussion
1. Om ni skulle bygga om det här filtreringssystemet från grunden, vad skulle ni göra annorlunda?

2. Vilken är den största svagheten ni kan identifiera i den här nuvarande implementationen?