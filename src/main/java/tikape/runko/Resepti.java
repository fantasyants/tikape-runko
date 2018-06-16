package tikape.runko;

import java.util.ArrayList;
import java.util.List;

public class Resepti {

    private int id;
    private String nimi;

    public Resepti(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;

    }

    public String getNimi() {
        return nimi;
    }

    public int getId() {
        return id;
    }

}
