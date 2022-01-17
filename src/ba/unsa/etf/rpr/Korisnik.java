package ba.unsa.etf.rpr;

public class Korisnik {
    private String ime, prezime, nadimak;

    public Korisnik(String ime, String prezime, String nadimak) throws IllegalArgumentException {
        if (ime.isBlank() || prezime.isBlank() || nadimak.isBlank())
            throw new IllegalArgumentException("Ime prezime i nadimak ne smiju biti prazni!");

        if (ime == null || prezime == null || nadimak == null)
            throw new IllegalArgumentException("Ime, prezime i nadimak ne smiju biti null!");

        if (nadimak.length() < 5)
            throw new IllegalArgumentException("Nadimak mora imati 5 ili više karaktera!");

        this.ime = ime;
        this.prezime = prezime;
        this.nadimak = nadimak;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getNadimak() {
        return nadimak;
    }

    public void setNadimak(String nadimak) {
        this.nadimak = nadimak;
    }

    @Override
    public String toString() {
        return ime + ' ' + prezime + '(' + nadimak + ')';
    }
}
