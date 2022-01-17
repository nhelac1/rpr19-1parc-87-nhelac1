package test;

import ba.unsa.etf.rpr.Korisnik;
import ba.unsa.etf.rpr.NeispravanFormatPoruke;
import ba.unsa.etf.rpr.Poruka;
import ba.unsa.etf.rpr.StatusPoruke;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PorukaTest {

    private final Korisnik korisnik1 = new Korisnik("Miki", "Maus", "miki123");
    private final Korisnik korisnik2 = new Korisnik("Paja", "Patak", "paja123");
    private final LocalDateTime datum = LocalDateTime.of(
            LocalDate.of(2020, 9, 12),
            LocalTime.of(9,0,0,0)
    );

    @Test
    void testKonstruktor1() throws NeispravanFormatPoruke {
        Poruka poruka = new Poruka(korisnik1, korisnik2, datum, "Da li si slobodan za kafu danas?");
        assertAll(
                () -> assertEquals(korisnik1, poruka.getPosiljalac()),
                () -> assertEquals(korisnik2, poruka.getPrimalac()),
                () -> assertEquals(datum, poruka.getDatumSlanja()),
                () -> assertEquals("Da li si slobodan za kafu danas?", poruka.getTekst())
        );
    }

    @Test
    void konstruktorTest2() {
        assertAll(
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () -> new Poruka(null, korisnik2, datum, "Da li si slobodan za kafu danas?"));

                    assertEquals("Pošiljalac ne smije biti null!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () -> new Poruka(korisnik1, null, datum, "Da li si slobodan za kafu danas?"));

                    assertEquals("Primalac ne smije biti null!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () -> new Poruka(korisnik1, korisnik2, null, "Da li si slobodan za kafu danas?"));

                    assertEquals("Datum slanja ne smije biti null!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () -> new Poruka(korisnik1, korisnik2, datum, null));

                    assertEquals("Tekst ne smije biti null!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () -> new Poruka(korisnik1, korisnik2, datum, ""));

                    assertEquals("Tekst ne smije biti prazan!", exception.getMessage());
                }
        );
    }

    @Test
    public void setteriTest1() throws NeispravanFormatPoruke {
        Poruka poruka = new Poruka(korisnik1, korisnik2, datum, "Da li si slobodan za kafu danas?");

        assertAll(
                () -> {
                    poruka.setStatusPoruke(StatusPoruke.PROCITANA);
                    assertEquals(StatusPoruke.PROCITANA, poruka.getStatusPoruke());
                },
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () ->poruka.setPrimalac(null));

                    assertEquals("Primalac ne smije biti null!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () ->poruka.setTekst(""));

                    assertEquals("Tekst ne smije biti prazan!", exception.getMessage());
                }
        );
    }

    @Test
    public void setteriTest2() throws NeispravanFormatPoruke {
        Poruka poruka = new Poruka(korisnik1, korisnik2, datum, "Da li si slobodan za kafu danas?");

        assertAll(
                () -> {
                    poruka.setStatusPoruke(StatusPoruke.PROCITANA);
                    assertEquals(StatusPoruke.PROCITANA, poruka.getStatusPoruke());
                },
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () ->poruka.setPosiljalac(null));

                    assertEquals("Pošiljalac ne smije biti null!", exception.getMessage());
                },
                () -> {
                    Exception exception = assertThrows(NeispravanFormatPoruke.class,
                            () ->poruka.setDatumSlanja(null));

                    assertEquals("Datum slanja ne smije biti null!", exception.getMessage());
                },
                () -> {
                    poruka.setTekst("Promijenjeni tekst");
                    assertEquals("Promijenjeni tekst", poruka.getTekst());
                }
        );
    }

    @Test
    public void toStringTest() throws NeispravanFormatPoruke {
        Poruka poruka = new Poruka(korisnik1, korisnik2, datum, "Da li si slobodan za kafu danas?");
        Poruka poruka2 = new Poruka(korisnik2, korisnik1, datum.plusHours(3), "Više mi odgovara sutra.");

        String porukaString = "[od: Miki Maus (miki123) za: Paja Patak (paja123) tekst: Da li si slobodan za kafu danas?]";
        String poruka2String = "[od: Paja Patak (paja123) za: Miki Maus (miki123) tekst: Više mi odgovara sutra.]";

        assertAll(
                () -> assertEquals(porukaString, poruka.toString()),
                () -> assertEquals(poruka2String, poruka2.toString())
        );
    }
}