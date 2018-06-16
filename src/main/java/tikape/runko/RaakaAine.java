package tikape.runko;

public class RaakaAine {

    private int id;
    private String nimi;
    

    RaakaAine(Integer id, String nimi) {
        this.id = id;
        this.nimi = nimi;

    }

    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

}
