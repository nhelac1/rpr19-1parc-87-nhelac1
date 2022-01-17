package test;

import ba.unsa.etf.rpr.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MessengerTest {
    private static Messenger messenger;
    private static List<Poruka> poruke = new ArrayList<>();
    private static Korisnik korisnik1, korisnik2, korisnik3, korisnik4;
    private static LocalDateTime datum;

    @BeforeAll
    static void setUp() throws NeispravanFormatPoruke {
        korisnik1 = new Korisnik("Miki", "Maus", "miki123");
        korisnik2 = new Korisnik("Paja", "Patak", "paja123");
        korisnik3 = new Korisnik("Mini", "Maus", "minica");
        korisnik4 = new Korisnik("Betty", "Boop", "bettyb");
        datum = LocalDateTime.of(
                LocalDate.of(2020, 9, 12),
                LocalTime.of(9,0,0,0)
        );

        poruke.add(new Poruka(korisnik1, korisnik2, datum, "Kako si?"));
        poruke.add(new Poruka(korisnik1, korisnik3, datum.plusHours(1), "Šta ima?"));
        poruke.add(new Poruka(korisnik3, korisnik2, datum.plusHours(4), "Ispit je bio jako lagan."));
        poruke.add(new Poruka(korisnik2, korisnik3, datum.plusHours(5), "Slažem se :)"));
        poruke.add(new Poruka(korisnik1, korisnik4, datum.minusHours(3), "Nećeš nikada pogoditi šta se desilo!!! :o"));
        poruke.add(new Poruka(korisnik4, korisnik2, datum.minusHours(1), "Ima li nešto novo?"));
        poruke.add(new Poruka(korisnik3, korisnik4, datum.plusHours(4), "Imaš li za prolaza?"));
    }

    @BeforeEach
    void setUpTest(){
        messenger = new Messenger();
        poruke.forEach(poruka -> poruka.setStatusPoruke(StatusPoruke.NEPROCITANA));
    }

    @Test
    public void konstruktorTest1(){
        messenger = new Messenger();
        assertTrue(messenger.dajSvePoruke().isEmpty());
    }

    @Test
    public void posaljiPorukuTest1(){
        messenger.posaljiPoruku(poruke.get(0));
        assertEquals(List.of(poruke.get(0)), messenger.dajSvePoruke());
    }

    @Test
    public void posaljiPorukuTest2() throws NeispravanFormatPoruke {
        messenger.posaljiPoruku(poruke.get(0).getPosiljalac(), poruke.get(0).getPrimalac(), "Sve je super.");
        List<Poruka> svePoruke = messenger.dajSvePoruke();
        assertAll(
                () -> assertEquals(1, svePoruke.size()),
                () -> assertEquals("Sve je super.", svePoruke.get(0).getTekst()),
                () -> Assertions.assertEquals(StatusPoruke.NEPROCITANA, svePoruke.get(0).getStatusPoruke())
        );
    }

    @Test
    public void  posaljiPorukeTest() {
        messenger.posaljiPoruke(poruke);
        assertEquals(poruke, messenger.dajSvePoruke());
    }

    @Test
    public void ponistiSlanjeTest1(){
        messenger.posaljiPoruke(poruke);

        Exception exception = assertThrows(NeispravnaAkcija.class,
                () -> messenger.ponistiSlanje(new Poruka(korisnik1, korisnik2, datum, "Ne postoji!")));

        assertEquals("Nije moguće poništiti slanje poruke koja nije nikada poslana!", exception.getMessage());
    }

    @Test
    public void ponistiSlanjeTest2() throws NeispravanFormatPoruke, NeispravnaAkcija {
        messenger.posaljiPoruke(poruke);
        Poruka poruka = new Poruka(korisnik1, korisnik2, datum, "Procitana poruka!");
        messenger.posaljiPoruku(poruka);
        messenger.procitajPoruku(poruka);

        Exception exception = assertThrows(NeispravnaAkcija.class,
                () -> messenger.ponistiSlanje(poruka));

        assertEquals("Nije moguće poništiti slanje poruke koja je već pročitana!", exception.getMessage());
    }

    @Test
    public void ponistiSlanjeTest3() throws NeispravanFormatPoruke, NeispravnaAkcija {
        messenger.posaljiPoruke(poruke);
        Poruka poruka = new Poruka(korisnik1, korisnik2, datum, "Neprocitana poruka!");
        messenger.posaljiPoruku(poruka);

        messenger.ponistiSlanje(poruka);
        assertFalse(messenger.dajSvePoruke().contains(poruka));
    }

    @Test
    public void procitajPorukuTest1(){
        Exception exception = assertThrows(NeispravnaAkcija.class,
                () -> messenger.procitajPoruku(new Poruka(korisnik1, korisnik2, datum, "Ne postoji!")));

        assertEquals("Nije moguće pročitati poruku koja nije nikada poslana!", exception.getMessage());
    }

    @Test
    public void procitajPorukuTest2() throws NeispravanFormatPoruke, NeispravnaAkcija {
        Poruka poruka = new Poruka(korisnik1, korisnik2, datum, "Procitana poruka!");
        messenger.posaljiPoruku(poruka);
        messenger.procitajPoruku(poruka);
        List<Poruka> svePoruke = messenger.dajSvePoruke();

        int index = svePoruke.indexOf(poruka);
        assertEquals(StatusPoruke.PROCITANA, svePoruke.get(index).getStatusPoruke());
    }
    
    @Test
    public void oznaciKaoNeprocitanoTest1() throws NeispravnaAkcija {
        messenger.posaljiPoruke(poruke);
        messenger.procitajPoruku(poruke.get(0));
        messenger.procitajPoruku(poruke.get(2));
        messenger.procitajPoruku(poruke.get(3));

        messenger.oznaciKaoNeprocitano(List.of(poruke.get(2), poruke.get(3)));
        List<Poruka> svePoruke = messenger.dajSvePoruke();

        assertAll(
                () -> {
                    int index = svePoruke.indexOf(poruke.get((2)));
                    assertEquals(StatusPoruke.NEPROCITANA, svePoruke.get(index).getStatusPoruke());
                },
                () -> {
                    int index = svePoruke.indexOf(poruke.get((3)));
                    assertEquals(StatusPoruke.NEPROCITANA, svePoruke.get(index).getStatusPoruke());
                },
                () -> {
                    int index = svePoruke.indexOf(poruke.get((0)));
                    assertEquals(StatusPoruke.PROCITANA, svePoruke.get(index).getStatusPoruke());
                }
        );
    }

    @Test
    public void oznaciKaoNeprocitanoTest2() throws NeispravnaAkcija {
        messenger.posaljiPoruku(poruke.get(0));
        messenger.posaljiPoruku(poruke.get(1));
        messenger.procitajPoruku(poruke.get(0));

        Exception exception = assertThrows(NeispravnaAkcija.class,
                () -> messenger.oznaciKaoNeprocitano(List.of(poruke.get(2), poruke.get(0))));

        List<Poruka> svePoruke = messenger.dajSvePoruke();
        assertAll(
                () -> assertEquals("Neke od poruka koje pokušavate označiti kao nepročitane nisu poslane!",
                        exception.getMessage()),
                () -> {
                    int index = svePoruke.indexOf(poruke.get((0)));
                    assertEquals(StatusPoruke.PROCITANA, svePoruke.get(index).getStatusPoruke());
                }
        );
    }

    @Test
    public void dajNeprocitanePorukeTest() throws NeispravnaAkcija {
        messenger.posaljiPoruke(poruke);
        messenger.procitajPoruku(poruke.get(1));
        messenger.procitajPoruku(poruke.get(3));
        messenger.procitajPoruku(poruke.get(4));
        messenger.procitajPoruku(poruke.get(6));

        Map<Korisnik, List<Poruka>> mapa = messenger.dajNeprocitanePoruke();

        assertAll(
                () -> assertEquals(3, mapa.size()),
                () -> assertTrue(mapa.containsKey(korisnik2)),
                () -> assertTrue(mapa.containsKey(korisnik3)),
                () -> assertTrue(mapa.containsKey(korisnik4)),
                () -> assertTrue(mapa.get(korisnik3).isEmpty()),
                () -> assertEquals(3, mapa.get(korisnik2).size()),
                () -> assertTrue(mapa.get(korisnik4).isEmpty())
        );
    }

    @Test
    public void dajNeprocitanePorukeTest2() throws NeispravnaAkcija {
        messenger.posaljiPoruke(poruke);
        messenger.procitajPoruku(poruke.get(1));
        messenger.procitajPoruku(poruke.get(2));
        messenger.procitajPoruku(poruke.get(5));

        Map<Korisnik, List<Poruka>> mapa = messenger.dajNeprocitanePoruke();

        assertAll(
                () -> assertEquals(3, mapa.size()),
                () -> assertTrue(mapa.containsKey(korisnik2)),
                () -> assertTrue(mapa.containsKey(korisnik3)),
                () -> assertTrue(mapa.containsKey(korisnik4)),
                () -> assertTrue(mapa.get(korisnik2).contains(poruke.get(0))),
                () -> assertFalse(mapa.get(korisnik2).contains(poruke.get(2))),
                () -> assertFalse(mapa.get(korisnik2).contains(poruke.get(5))),
                () -> assertTrue(mapa.get(korisnik3).contains(poruke.get(3))),
                () -> assertFalse(mapa.get(korisnik3).contains(poruke.get(1))),
                () -> assertEquals(2, mapa.get(korisnik4).size())
        );
    }

    @Test
    public void dajPristiglePorukeZaKorisnikaTest1(){
        messenger.posaljiPoruke(poruke);

        List<Poruka> porukeZaKorisnika1 = messenger.dajPristiglePorukeZaKorisnika(korisnik1);

        assertTrue(porukeZaKorisnika1.isEmpty());
    }

    @Test
    public void dajPristiglePorukeZaKorisnikaTest2(){
        messenger.posaljiPoruke(poruke);

        List<Poruka> porukeZaKorisnika3 = messenger.dajPristiglePorukeZaKorisnika(korisnik3);

        assertAll(
                () -> assertEquals(2, porukeZaKorisnika3.size()),
                () -> assertTrue(porukeZaKorisnika3.contains(poruke.get(1))),
                () -> assertTrue(porukeZaKorisnika3.contains(poruke.get(3)))
        );
    }

    @Test
    public void dajPorukeZaKorisnikaTest1(){
        messenger.posaljiPoruke(poruke);

        List<Poruka> porukeZaKorisnika3 = messenger.dajPorukeZaKorisnika(korisnik3, StatusPoruke.PROCITANA);

        assertTrue(porukeZaKorisnika3.isEmpty());
    }

    @Test
    public void dajPorukeZaKorisnikaTest2(){
        messenger.posaljiPoruke(poruke);

        List<Poruka> porukeZaKorisnika1 = messenger.dajPorukeZaKorisnika(korisnik1, StatusPoruke.NEPROCITANA);

        assertTrue(porukeZaKorisnika1.isEmpty());
    }

    @Test
    public void dajPorukeZaKorisnikaTest3() throws NeispravnaAkcija {
        messenger.posaljiPoruke(poruke);
        messenger.procitajPoruku(poruke.get(4));

        List<Poruka> porukeZaKorisnika4 = messenger.dajPorukeZaKorisnika(korisnik4, StatusPoruke.NEPROCITANA);

        assertAll(
                () -> assertEquals(1, porukeZaKorisnika4.size()),
                () -> assertTrue(porukeZaKorisnika4.contains(poruke.get(6)))
        );
    }

    @Test
    public void dajPorukeZaKorisnikaTest4() throws NeispravnaAkcija {
        messenger.posaljiPoruke(poruke);
        messenger.procitajPoruku(poruke.get(4));

        List<Poruka> porukeZaKorisnika4 = messenger.dajPorukeZaKorisnika(korisnik4, StatusPoruke.PROCITANA);

        assertAll(
                () -> assertEquals(1, porukeZaKorisnika4.size()),
                () -> assertTrue(porukeZaKorisnika4.contains(poruke.get(4)))
        );
    }

    @Test
    public void filtrirajPorukeTest1(){
        messenger.posaljiPoruke(poruke);

        List<Poruka> filtrirane = messenger.filtrirajPoruke(poruka -> poruka.getPosiljalac().equals(korisnik1));

        assertEquals(3, filtrirane.size());
    }

    @Test
    public void filtrirajPorukeTest2(){
        messenger.posaljiPoruke(poruke);

        List<Poruka> filtrirane = messenger.filtrirajPoruke(poruka -> poruka.getTekst().contains("?"));

        assertEquals(4, filtrirane.size());
    }

    @Test
    public void dajStarijeOdTest1(){
        messenger.posaljiPoruke(poruke);

        List<Poruka> filtrirane  = messenger.dajStarijeOd(korisnik1, datum);

        assertAll(
                () -> assertEquals(2, filtrirane.size()),
                () -> assertTrue(filtrirane.contains(poruke.get(0))),
                () -> assertTrue(filtrirane.contains(poruke.get(4)))
        );
    }

    @Test
    public void dajStarijeOdTest2(){
        messenger.posaljiPoruke(poruke);

        List<Poruka> filtrirane  = messenger.dajStarijeOd(korisnik3, datum.plusHours(2));

        assertTrue(filtrirane.isEmpty());
    }

    @Test
    public void toStringTest1(){
        messenger.posaljiPoruke(List.of(poruke.get(0), poruke.get(1)));

        String rezultat =
                "[od: Miki Maus (miki123) za: Paja Patak (paja123) tekst: Kako si?]\n" +
                        "[od: Miki Maus (miki123) za: Mini Maus (minica) tekst: Šta ima?]";

        Messenger messenger2 = new Messenger();
        messenger2.posaljiPoruke(List.of(poruke.get(2), poruke.get(3)));

        String rezultat2 =
                "[od: Mini Maus (minica) za: Paja Patak (paja123) tekst: Ispit je bio jako lagan.]\n"+
                "[od: Paja Patak (paja123) za: Mini Maus (minica) tekst: Slažem se :)]";

        assertAll(
                () -> assertEquals(rezultat, messenger.toString()),
                () -> assertEquals(rezultat2, messenger2.toString())
        );
    }

}