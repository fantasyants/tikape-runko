package tikape.runko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("CREATE TABLE RaakaAine (id integer PRIMARY KEY, nimi varchar(50));");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('vesi');");
        lista.add("INSERT INTO RaakaAine (nimi) VALUES ('kurkku');");

        lista.add("CREATE TABLE Resepti (id integer PRIMARY KEY, nimi varchar(50));");
        lista.add("INSERT INTO Resepti (nimi) VALUES ('kurkkuvesi')");

        lista.add("CREATE TABLE RaakaAineResepti (id integer PRIMARY KEY, resepti_id integer, raakaAine_nimi varchar(50), jarjestys integer, maara varchar(50), ohje varchar(500), FOREIGN KEY (resepti_id) REFERENCES Resepti(id), FOREIGN KEY (raakaAine_nimi) REFERENCES RaakaAine(nimi));");
        lista.add("INSERT INTO RaakaAineResepti (resepti_id, raakaAine_nimi, jarjestys, maara, ohje) VALUES (1, 'kurkku', 2, '1 kpl', 'Aseta veden syleilyyn.')");
        lista.add("INSERT INTO RaakaAineResepti (resepti_id, raakaAine_nimi, jarjestys, maara, ohje) VALUES (1, 'vesi', 1, '10 litraa', 'Laske vesi sankoon.')");

        return lista;
    }
}
