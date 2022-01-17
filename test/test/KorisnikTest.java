package test;

import ba.unsa.etf.rpr.Korisnik;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KorisnikTest {

    @Test
    void konstruktorTest1() throws IllegalArgumentException {
        Korisnik korisnik = new Korisnik("Miki", "Maus", "miki123");
        assertAll(
                () -> assertEquals("Miki", korisnik.getIme()),
                () -> assertEquals("Maus", korisnik.getPrezime()),
                () -> assertEquals("miki123", korisnik.getNadimak())
        );
    }

    @Test
    void konstruktorTest2() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Korisnik(null, "Maus", "miki123"));

        assertEquals("Ime, prezime i nadimak ne smiju biti null!", exception.getMessage());
    }

    @Test
    void konstruktorTest3() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Korisnik("Miki", null, "miki123"));

        assertEquals("Ime, prezime i nadimak ne smiju biti null!", exception.getMessage());
    }

    @Test
    void konstruktorTest4() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Korisnik("Miki", "Maus", ""));

        assertEquals("Ime, prezime i nadimak ne smiju biti prazni!", exception.getMessage());
    }

    @Test
    void konstruktorTest5() throws IllegalArgumentException {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new Korisnik("Miki", "Maus", "miki"));

        assertEquals("Nadimak mora imati 5 ili više karaktera!", exception.getMessage());
    }

    @Test
    void testSetteri1(){
        Korisnik korisnik = new Korisnik("Miki", "Maus", "miki123");
        assertAll(
                () -> {
                    korisnik.setIme("Paja");
                    assertEquals("Paja", korisnik.getIme());
                },
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class,
                            () ->  korisnik.setIme(""));

                    assertEquals("Ime, prezime i nadimak ne smiju biti prazni!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class,
                            () ->  korisnik.setIme(null));

                    assertEquals("Ime, prezime i nadimak ne smiju biti null!", exception.getMessage());
                }
        );
    }

    @Test
    void testSetteri2(){
        Korisnik korisnik = new Korisnik("Miki", "Maus", "miki123");
        assertAll(
                () -> {
                    korisnik.setPrezime("Patak");
                    assertEquals("Patak", korisnik.getPrezime());
                },
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class,
                            () ->  korisnik.setPrezime(""));

                    assertEquals("Ime, prezime i nadimak ne smiju biti prazni!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class,
                            () ->  korisnik.setPrezime(null));

                    assertEquals("Ime, prezime i nadimak ne smiju biti null!", exception.getMessage());
                }
        );
    }

    @Test
    void testSetteri3(){
        Korisnik korisnik = new Korisnik("Miki", "Maus", "miki123");
        assertAll(
                () -> {
                    korisnik.setNadimak("paja123");
                    assertEquals("paja123", korisnik.getNadimak());
                },
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class,
                            () ->  korisnik.setNadimak(""));

                    assertEquals("Ime, prezime i nadimak ne smiju biti prazni!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class,
                            () ->  korisnik.setNadimak(null));

                    assertEquals("Ime, prezime i nadimak ne smiju biti null!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(IllegalArgumentException.class,
                            () ->  korisnik.setNadimak("paja"));

                    assertEquals("Nadimak mora imati 5 ili više karaktera!", exception.getMessage());
                }

        );
    }

    @Test
    public void testToString(){
        Korisnik korisnik1 = new Korisnik("Miki", "Maus", "miki123");
        Korisnik korisnik2 = new Korisnik("Paja", "Patak", "paja123");

        String korisnik1Strng = "Miki Maus (miki123)";
        String korisnik2String = "Paja Patak (paja123)";

        assertAll(
                () -> assertEquals(korisnik1Strng, korisnik1.toString()),
                () -> assertEquals(korisnik2String, korisnik2.toString())
        );
    }
}