package ba.unsa.etf.rpr;

import java.time.LocalDateTime;

public class Poruka {
    private Korisnik posiljalac, primalac;
    private String tekst;
    private LocalDateTime datumSlanja;
    private StatusPoruke statusPoruke = StatusPoruke.NEPROCITANA;

    public Poruka(Korisnik posiljalac, Korisnik primalac,LocalDateTime datumSlanja, String tekst) throws NeispravanFormatPoruke {
        if (posiljalac == null)
            throw new NeispravanFormatPoruke("Pošiljalac ne smije biti null!");
        if (primalac == null)
            throw new NeispravanFormatPoruke("Primalac ne smije biti null!");
        if (datumSlanja == null)
            throw new NeispravanFormatPoruke("Datum slanja ne smije biti null!");
        if (tekst == null)
            throw new NeispravanFormatPoruke("Tekst ne smije biti null!");

        this.posiljalac = posiljalac;
        this.primalac = primalac;
        this.tekst = tekst;
        this.datumSlanja = datumSlanja;
    }

    public Korisnik getPosiljalac() {
        return posiljalac;
    }

    public void setPosiljalac(Korisnik posiljalac) {
        this.posiljalac = posiljalac;
    }

    public Korisnik getPrimalac() {
        return primalac;
    }

    public void setPrimalac(Korisnik primalac) {
        this.primalac = primalac;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public LocalDateTime getDatumSlanja() {
        return datumSlanja;
    }

    public void setDatumSlanja(LocalDateTime datumSlanja) {
        this.datumSlanja = datumSlanja;
    }

    public StatusPoruke getStatusPoruke() {
        return statusPoruke;
    }

    public void setStatusPoruke(StatusPoruke statusPoruke) {
        this.statusPoruke = statusPoruke;
    }

    @Override
    public String toString() {
        return "[od: " + posiljalac +
                " za: " + primalac +
                " tekst: " + tekst + ']';
    }
}
