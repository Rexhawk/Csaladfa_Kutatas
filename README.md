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

Technikai Megvalósítás

    Firebase autentikáció: A felhasználók regisztrálhatnak és bejelentkezhetnek Firebase autentikációval.
    Adatmodell: A családtagok adatainak kezelésére a FamilyMember osztályt használjuk.
    Különböző activityk használata: Három fő activity van: LoginActivity, RegisterActivity és MainActivity. Ezen kívül egy beállítási activity (SettingsActivity) is elérhető.
    Beviteli mezők: Az email mezők megfelelő billentyűzetet jelenítenek meg, a jelszó mezők csillagozva vannak.
    Layoutok: Az alkalmazás ConstraintLayout-ot és LinearLayout-ot használ.
    Reszponzív design: Az alkalmazás különböző kijelzőméreteken és elforgatás esetén is megfelelően jelenik meg.
    Animációk: Legalább két animáció használata biztosítja a felhasználói élményt (pl. activity váltások során).
    Intentek használata: Navigáció az activityk között intentekkel történik.
    Lifecycle Hook: Az onStart metódusban kezeljük az értesítések figyelését a Firestore adatbázisban.
    Permission használata: Az alkalmazás írási engedélyt kér az adatok lokális mentéséhez és értesítési engedélyt az értesítésekhez.
    Notification: Értesítések kezelése, ha valaki módosítja a felhasználó által létrehozott adatokat.
    Firestore lekérdezések: Két komplex Firestore lekérdezés van megvalósítva, amelyek where feltételt és rendezést használnak.

Összefoglalás

Ez az alkalmazás egy teljes körű megoldást kínál a családfakutatásra, amely tartalmazza a szükséges autentikációs, 
adatkezelési és értesítési funkciókat, biztosítva a felhasználók számára a kényelmes és hatékony használatot. 
Az alkalmazás reszponzív és megfelel a modern androidos alkalmazások követelményeinek.