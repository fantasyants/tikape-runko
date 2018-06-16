package tikape.runko;

import java.sql.SQLException;
import java.util.List;

public class RaakaAineResepti {

    private Integer id;
    private Integer reseptiId;
    private String raakaAineNimi;
    private Integer jarjestys;
    private String maara;
    private String ohje;
  

    public RaakaAineResepti(Integer id, Integer reseptiId, String raakaAineNimi, Integer jarjestys, String maara, String ohje) {
        this.id = id;
        this.raakaAineNimi = raakaAineNimi;
        this.reseptiId = reseptiId;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
        
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public String getMaara() {
        return maara;
    }

    public String getOhje() {
        return ohje;
    }

    public Integer getId() {
        return id;
    }

    public Integer getReseptiId() {
        return reseptiId;
    }

    public String getRaakaAineNimi() {
        return raakaAineNimi;
    }



}

