Családfa Kutatás Applikáció Leírása

Ez az alkalmazás egy családfakutató eszköz, amely lehetővé teszi a felhasználók számára, hogy rögzítsék és kezeljék 
családi kapcsolatokat. Az alkalmazás főbb funkciói közé tartozik a bejelentkezés és regisztráció Firebase 
autentikációval, családtagok adatainak kezelése, valamint az adatok lokális mentése és értesítések kezelése.

Fő Funkciók és Felhasználásuk
1. Bejelentkezés és Regisztráció

   LoginActivity: Ez az activity jelenik meg az alkalmazás indításakor. A felhasználók bejelentkezhetnek e-mail 
   címükkel és jelszavukkal. Ha nincs még fiókjuk, akkor a regisztrációs oldalra navigálhatnak.
   Bejelentkezés: Adja meg az e-mail címét és jelszavát, majd kattintson a "Bejelentkezés" gombra.
   Regisztráció: Kattintson a "Regisztráció" gombra, amely átirányítja a regisztrációs oldalra.

   RegisterActivity: Itt lehet új felhasználói fiókot létrehozni. Az e-mail cím és jelszó megadása után kattintson a "Regisztráció" gombra.
   Felhasználói adatok megadása: E-mail és jelszó megadása, majd a "Regisztráció" gombra kattintva hozza létre a fiókot.
   Kilépés: A regisztrációs oldalon van egy kilépési gomb is, amely visszavisz a bejelentkezési oldalra.

2. Családtagok Kezelése

   MainActivity: Ez az activity felelős a családtagok adatainak kezeléséért. Itt lehet új családtagokat hozzáadni, meglévőket módosítani, keresni és törölni.
   Új családtag hozzáadása: Töltse ki a szükséges mezőket (név, születési hely, dátum stb.), majd kattintson az "Új családtag hozzáadása" gombra.
   Családtag módosítása: Adja meg a módosítandó családtag ID-ját és az új adatokat, majd kattintson a "Módosítás" gombra.
   Családtag törlése: Adja meg a törölni kívánt családtag ID-ját, majd kattintson a "Törlés" gombra.
   Keresés: Adja meg a keresett családtag nevét vagy ID-ját, majd kattintson a "Keresés" gombra.
   Kilépés: A főoldalon található kilépési gomb visszavisz a bejelentkezési oldalra.
   Beállítások: A Beállítások gomb megnyomásával a felhasználó átirányítódik a SettingsActivity-re, ahol különböző beállításokat végezhet.

3. Beállítások és Adatok Mentése

   SettingsActivity: Itt lehet bekapcsolni az adatok lokális mentését és az értesítéseket.
   Adatok Lokális Mentése: Bekapcsolva az alkalmazás menti a családtagok adatait az eszközre.
   Értesítések Bekapcsolása: Bekapcsolva az alkalmazás értesítéseket küld, ha valaki módosítja a felhasználó által létrehozott családtagnak az adatait.


Firebase autentikáció megvalósítva (bejelentkezés és regisztráció):

    LoginActivity és RegisterActivity: Ezekben az activity-kben történik a felhasználói bejelentkezés és regisztráció a Firebase segítségével.
    Kód helye: LoginActivity.java és RegisterActivity.java

Adatmodell definiálása (class vagy interfész formájában):

    FamilyMember osztály: Az adatmodell a FamilyMember osztályban van definiálva, amely a családtagok adatait tartalmazza.
    Kód helye: FamilyMember.java

Legalább 3 különböző activity vagy fragment használata:

    LoginActivity, RegisterActivity, MainActivity, és SettingsActivity: Az alkalmazás több activity-t is használ.
    Kód helye: LoginActivity.java, RegisterActivity.java, MainActivity.java, SettingsActivity.java

Beviteli mezők beviteli típusa megfelelő:

    Email mezők: inputType="textEmailAddress" használata.
    Jelszó mezők: inputType="textPassword" használata.
    Kód helye: activity_login.xml és activity_register.xml

ConstraintLayout és még egy másik layout típus használata:

    ConstraintLayout: A fő layout az alkalmazásban.
    LinearLayout: Használatos például a regisztrációs oldalon.
    Kód helye: activity_main.xml és activity_register.xml

Reszponzív dizájn:

    Az alkalmazás különböző kijelzőméreteken és elforgatás esetén is jól jelenik meg.
    Kód helye: Minden layout XML fájl (activity_main.xml, activity_login.xml, activity_register.xml).

Legalább 2 különböző animáció használata:

    Animációk az activity váltások során.
    Kód helye: Például MainActivity.java és LoginActivity.java között váltáskor.

Intentek használata:

    Navigáció: Intentek használata az activity-k közötti navigációhoz.
    Kód helye: LoginActivity.java, RegisterActivity.java, MainActivity.java, SettingsActivity.java

Legalább egy Lifecycle Hook használata:

    onStart: Értesítések figyelése a Firestore adatbázisban.
    Kód helye: MainActivity.java

Legalább egy androidos erőforrás használata, amihez kell android permission:

    Írási engedély: Az adatok lokális mentéséhez.
    Értesítési engedély: Értesítésekhez.
    Kód helye: MainActivity.java és SettingsActivity.java

Legalább egy notification vagy alarm manager vagy job scheduler használata:

    Értesítések kezelése: Adatbázis változásainak figyelésére értesítések küldése.
    Kód helye: MainActivity.java és SettingsActivity.java

CRUD műveletek mindegyike megvalósult:

    Adatbázis műveletek: Adatok hozzáadása, olvasása, frissítése és törlése.
    Kód helye: MainActivity.java

Legalább 2 komplex Firestore lekérdezés megvalósítása:

    Lekérdezések: Két komplex Firestore lekérdezés (where feltétel és rendezés).
    Kód helye: MainActivity.java

Összefoglalás

Ez az alkalmazás egy teljes körű megoldást kínál a családfakutatásra, amely tartalmazza a szükséges autentikációs, 
adatkezelési és értesítési funkciókat, biztosítva a felhasználók számára a kényelmes és hatékony használatot. 
Az alkalmazás reszponzív és megfelel a modern androidos alkalmazások követelményeinek.
