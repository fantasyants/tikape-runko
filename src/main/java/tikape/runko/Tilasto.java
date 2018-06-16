
package tikape.runko;

public class Tilasto {
    private String nimi;
    private int lkm;

    public Tilasto(String nimi, int lkm) {
        this.nimi = nimi;
        this.lkm = lkm;
    }

    public String getNimi() {
        return nimi;
    }

    public int getLkm() {
        return lkm;
    }
}
