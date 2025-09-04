# Prestanda- och skalbarhetsfrågor
1. Vad händer när en användare söker efter 'red collars under $25' i en databas
med 100 000 produkter. Vilken data flyttas från MongoDB till vår Java-applikation?

## Kommentar:
Steg-för-steg:
**FilterProductService.filterProducts() kör:**
- Konverterar "collar" → "collarProduct"
- Anropar `fetchProductsByType("collarProduct")`

**MongoDB Query**: `db.products.find({"_class": "collarProduct"})`
- Hittar ALLA collars (låt oss säga 15 000 av totalt 100 000 produkter)
- **DATA FLYTTAD FRÅN MONGODB**: 15 000 kompletta kragedokument
- **Överförda byte**: ~15 000 × 200 byte = ~3 MB JSON-data
- **Vad som ingår**: ALLA collars (blå, dyra, allt)

**WASTE**: fruktansvärt mycket onödig data i minnet....

2. Om vpr produktkatalog växer till 1 miljon produkter, vad händer med vår
filtreringsprestanda? Var kan flaskhalsar uppstå?

## Kommentar:
Skalningsproblem:
- **Collar-subset växer**: Kanske 150 000 collar-dokument istället för 15 000
- **Nätverksflaskhals**: 150 000 × 200 byte = ~30 MB överförs per sökning
- **Minnesflaskhals**: JVM-heap innehåller 150 000 temporära objekt
- Konstant skapande/förstörelse av stora objektsamlingar
- **CPU-slöseri**: Java bearbetar 149 950 objekt som borde filtreras av MongoDB

# Arkitektur- och designfrågor
Tänk på dataflödet när ni diskuterar dessa frågor.

1. Vi har filtreringslogik på två olika ställen - var och varför? Vilka är kompromisserna?

## Kommentar:
Två platser:
1. **Databasnivå** (`ProductRepository.findByProductType`):
- Filtrerar endast efter produkttyp
- Snabb, indexerad fråga

2. **Applikationsnivå** (`FilterRegistry` + Java-streams):
- Filtrerar efter alla andra kriterier
- Långsam bearbetning i minnet

Kostnad/kompromiss:
- **Fördel**: Flexibla filterkombinationer i Java
- **Kostnad**: Prestandaförlust, minnesslöseri, nätverksoverhead, komplex arkitektur

2. Vad händer vid programstart med vår FilterRegistry-klass? Hur skalas detta när du lägger till fler produkttyper?

## Kommentar:
```java
static {
// Hela detta block körs vid class loading
Map<String, ProductFilter<Produkt>> commonFilters = new HashMap<>();
// ... bygger filter för Produkt, Halsband, Koppel, Leksak, Skål
// Allt lagras permanent i statiskt minne
}
```
Skalningsproblem:
- **Minne**: Varje filter för varje produkttyp laddas permanent
- **Starttid**: Statiskt initialiseringsblock växer med varje produkttyp
- **Underhållbarhet**: Att lägga till `Bed`-klassen kräver att detta massiva statiska block modifieras
- **Dynamiska filter**: Omöjligt att lägga till filter vid körning


3. Om vi vill lägga till en ny produkttyp som heter 'Bed' med filter för 'size' och 'washable', hur många filer behöver vi modifiera?

## Kommentar:
Nödvändiga ändringar:
1. **`FilterProductService.getProductType()`**:
```java
case "bed": return "bedProduct";
```

2. **`FilterProductService.getProductClassFromAlias()`**:
```java
case "bedProduct": return Bed.class;
```

3. **`FilterRegistry` static block**:
```java
Map<String, ProductFilter<Product>> bedFilters = new HashMap<>(commonFilters);

bedFilters.put("size", value -> product -> { /* bed size logic */ });
bedFilters.put("washable", value -> product -> { /* washable logic */ });
FILTERS.put(Bed.class, bedFilters);
```

Totalt: 3 filer ändrade - Bryter mot öppen/stängd-principen.

# Frågor om databasanvändning

1. MongoDB är riktigt bra på att filtrera, sortera och indexera. Drar vi full nytta av dessa funktioner? Var kan vi kanske inte ha utnyttjat dem fullt ut?

## Kommentar:
MongoDB-funktioner går till spillo:
- **Intervallfrågor**: `{"price": {$gte: 10, $lte: 25}}`
- **Textsökning**: `{"name": {$regex: "leather", $options: "i"}}`
- **Sammansatta frågor**: `{"_class": "collarProduct", "color": "red", "price": {$lt: 25}}`
- **Index**: Prisindex, färgindex, sammansatta index - inga används för filtrering
- **Aggregeringspipelines**: Komplex filtrering, gruppering, sortering - oanvänd

# Frågor om kodkvalitet
Titta på "maintainability".

1. Vad händer när vi vill lägga till komplexa filter som "pris mellan X och Y" eller "namn innehåller flera ord"? Vart skulle denna logik hamna?

## Kommentar:
Problem:
- Statiskt block blir massivt och oläsligt
- Felhanteringen blir komplex (parsningsfel)
- Inget sätt att utnyttja MongoDBs textsökningsfunktioner

2. Titta på antalet ställen där vi konverterar mellan strängprodukttyper och Java-klasser. Vad säger detta om vår design?

## Kommentar:
Totalt: 4 olika platser som gör liknande mappningar

Vad detta indikerar:
- **Tät koppling**: Produkttypkunskap utspridd över flera klasser
- **Duplikation**: Samma mappningslogik upprepas i olika former
- **Skör design**: Att lägga till nya produkttyper kräver ändringar på flera ställen
- **Ingen enskild sanningskälla** för produkttypmappningar

# Övergripande diskussion
1. Om ni skulle bygga om det här filtreringssystemet från grunden, vad skulle ni göra annorlunda?

## Kommentar:
Eliminera tvånivåmetoden helt
Eliminera FilterRegistry
Centralisera typmappning

2. Vilken är den största svagheten ni kan identifiera i den här nuvarande implementationen?

## Kommentar:
Den grundläggande bristen: Vi utför fruktansvärt dyrt arbete på helt fel plats.

Vi använder MongoDB som en "dum lagring" och Java som ett
"smart filter", när det borde vara tvärtom.

