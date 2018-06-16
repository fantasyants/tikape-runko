package tikape.runko;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaakaAineReseptiDao implements Dao<RaakaAineResepti, Integer> {

    private Database database;

    public RaakaAineReseptiDao(Database database) {
        this.database = database;
    }

    public RaakaAineResepti findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<RaakaAineResepti> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RaakaAineResepti save(RaakaAineResepti object) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO RaakaAineResepti (resepti_id, raakaAine_nimi, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, object.getReseptiId());
            stmt.setString(2, object.getRaakaAineNimi());
            stmt.setInt(3, object.getJarjestys());
            stmt.setString(4, object.getMaara());
            stmt.setString(5, object.getOhje());
            stmt.executeUpdate();
        }

        return null;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAineResepti WHERE resepti_id = ?");

        stmt.setInt(1, key);
        stmt.executeUpdate();

        stmt.close();
        conn.close();

    }

    public List<RaakaAineResepti> findOthersForResepti(Integer reseptiId) throws SQLException {
        String query = "SELECT id, resepti_id, raakaAine_nimi, jarjestys, maara, ohje FROM RaakaAineResepti\n"
                + "WHERE resepti_id = ?\n"
                + "ORDER BY jarjestys;";

        List<RaakaAineResepti> muut = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, reseptiId);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                muut.add(new RaakaAineResepti(result.getInt("id"), result.getInt("resepti_id"), result.getString("raakaAine_nimi"), result.getInt("jarjestys"), result.getString("maara"), result.getString("ohje")));
            }
        }

        return muut;

    }
   

}
