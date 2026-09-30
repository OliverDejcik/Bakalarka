# Workout Tracker

Android aplikácia na plánovanie tréningových zostáv, zaznamenávanie odcvičených sérií a sledovanie silového progresu. Projekt vznikol ako bakalárska práca a jeho používateľské rozhranie je postavené na Jetpack Compose.

## Čo aplikácia ponúka

- Registráciu a prihlásenie používateľa s ukladaním hesiel vo forme BCrypt hashu.
- Vytváranie tréningových plánov a pridávanie cvikov s počtom sérií.
- Záznam tréningovej jednotky vrátane opakovaní a použitej váhy pre každú sériu.
- Zobrazenie údajov z predchádzajúceho tréningu pri zapisovaní ďalšej jednotky.
- Vyhľadávanie plánov a správu existujúcich plánov a cvikov.
- Štatistiky vybraného cviku s odhadom maxima na jedno opakovanie (1RM) a časovými rozsahmi 14, 30, 180 alebo 360 dní.
- Zmenu hesla v používateľskom profile a základný kalendárny prototyp.

## Technológie

- Kotlin a Android SDK
- Jetpack Compose, Material 3 a Navigation Compose
- Android ViewModel a Kotlin Coroutines
- Supabase PostgREST na vzdialené čítanie a zápis údajov
- Ktor Android klient a Kotlin Serialization
- BCrypt na hashovanie a overovanie hesiel
- MPAndroidChart na zobrazenie grafu progresu

## Architektúra

```text
app/src/main/java/com/example/bakalarka/
├── MainActivity.kt             # vstupný bod aplikácie a navigačný graf
├── data/                       # serializovateľné modely a insert DTO
├── other_classes/
│   ├── AppViewModel.kt         # UI stav, korutiny a operácie s tréningami
│   ├── ElementsGenerator.kt    # zdieľané Compose prvky a graf
│   ├── Screens.kt              # navigačné ciele
│   └── Sizes.kt                # pomocné škálovanie rozhrania
├── screens/                    # obrazovky aplikácie
├── supabase/                   # klient, PostgREST operácie a session holdery
└── ui/theme/                   # farebné schémy a Material tému
```

Obrazovky volajú operácie cez `AppViewModel`; dátová vrstva komunikuje so Supabase. Základné entity v kóde sú používateľ, tréningový plán, cvik, tréningová jednotka a zaznamenané série. Odhad 1RM v štatistikách používa Epleyho vzorec.

## Spustenie

### Požiadavky

- Android Studio s Android SDK 36
- JDK 17 pre Gradle; aplikácia cieli na JVM 11
- Android zariadenie alebo emulátor s Androidom 10 (API 29) alebo novším
- Supabase projekt s databázou pripravenou podľa dátových modelov aplikácie

### Kroky

1. Naklonujte repozitár a otvorte ho v Android Studio.
2. Nastavte URL Supabase projektu a verejný `anon` kľúč v `app/src/main/java/com/example/bakalarka/supabase/SupabaseClient.kt`.
3. V Supabase vytvorte tabuľky zodpovedajúce dátovým modelom a nastavte prístupové pravidlá Row Level Security (RLS).
4. Synchronizujte Gradle a spustite konfiguráciu `app` na emulátore alebo zariadení.

Zostavenie z príkazového riadka vo Windows:

```powershell
.\gradlew.bat assembleDebug
```

Lokálne unit testy:

```powershell
.\gradlew.bat test
```

## Backend a bezpečnosť

Aplikácia pristupuje k Supabase priamo z Android klienta. Súbor `Database Structure.txt` je prázdny, preto definícia schémy a RLS pravidiel nie je súčasťou repozitára; databázu treba pripraviť podľa modelov v `data/` a operácií v `supabase/SupabaseApi.kt`.

Aktuálna registrácia a prihlásenie používajú vlastnú tabuľku `users` a BCrypt, nie Supabase Auth. Pred produkčným nasadením treba navrhnúť a otestovať autentifikáciu a RLS tak, aby používatelia nemohli čítať ani meniť cudzie údaje. V mobilnej aplikácii používajte iba verejný `anon` kľúč s obmedzujúcimi RLS pravidlami; nikdy nevkladajte `service_role` kľúč do klienta ani do repozitára. Pri zverejnení projektu presuňte konfiguračné hodnoty mimo zdrojového kódu a zneplatnite prípadné kľúče, ktoré nemajú byť verejné.

## Stav projektu

Hlavné toky plánovania, zapisovania tréningu a zobrazenia štatistík sú implementované. Kalendár je prototyp a aktuálne všeobecné unit aj instrumentačné testy sú iba východiskové ukážky; automatické testy aplikačnej logiky zatiaľ nie sú doplnené.
