package tikape.runko;

import java.util.ArrayList;
import spark.Spark;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        Database database = new Database("jdbc:sqlite:drinkit.db");
        ReseptiDao reseptit = new ReseptiDao(database);
        RaakaAineDao raakaAineet = new RaakaAineDao(database);
        RaakaAineReseptiDao raakaAineReseptit = new RaakaAineReseptiDao(database);
        TilastoDao tilastot = new TilastoDao(database);

        database.init();

        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkit", reseptit.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/ainekset", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaAineet", raakaAineet.findAll());

            return new ModelAndView(map, "ainekset");
        }, new ThymeleafTemplateEngine());

        Spark.post("/ainekset", (req, res) -> {
            String raakaAineenNimi = req.queryParams("nimi");
            raakaAineenNimi = raakaAineenNimi.toLowerCase();
            raakaAineenNimi = raakaAineenNimi.trim();
            if (raakaAineenNimi.isEmpty() || raakaAineenNimi.length() > 50) {
                System.out.println("virheellinen nimi");
                res.redirect("/virhe");
            } else {
                RaakaAine raakaAine = new RaakaAine(-1, raakaAineenNimi);
                raakaAineet.save(raakaAine);
            }

            res.redirect("/ainekset");
            return "";
        });

        Spark.post("/ainekset/:id/delete", (req, res) -> {
            String id = req.params(":id");
            raakaAineet.delete(Integer.parseInt(id));

            res.redirect("/ainekset");
            return "";
        });

        Spark.get("/drinkit", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkit", reseptit.findAll());
            map.put("raakaAineet", raakaAineet.findAll());

            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());

        Spark.post("/drinkit", (req, res) -> {
            String reseptinNimi = req.queryParams("nimi");
            reseptinNimi = reseptinNimi.toLowerCase();
            reseptinNimi = reseptinNimi.trim();
            if (reseptinNimi.isEmpty() || reseptinNimi.length() > 50) {
                System.out.println("virheellinen nimi");
                res.redirect("/virhe");
            } else {
                Resepti resepti = new Resepti(-1, reseptinNimi);
                reseptit.save(resepti);
            }

            res.redirect("/drinkit");
            return "";
        });

        Spark.post("/drinkit/:id/delete", (req, res) -> {
            String id = req.params(":id");
            reseptit.delete(Integer.parseInt(id));
            raakaAineReseptit.delete(Integer.parseInt(id));

            res.redirect("/drinkit");
            return "";
        });

        Spark.post("/drinkit/lisaa", (Request req, Response res) -> {
            String raakaAineNimi = req.queryParams("raakaAineenNimi");
            Integer reseptiId = Integer.parseInt(req.queryParams("drinkinNimi"));
            Integer jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
            if (raakaAineNimi.isEmpty() || reseptiId == null || maara.length() > 50 || ohje.length() > 500) {
                res.redirect("/virhe");
            } else {
                RaakaAineResepti rar = new RaakaAineResepti(-1, reseptiId, raakaAineNimi, jarjestys, maara, ohje);
                raakaAineReseptit.save(rar);
            }

            res.redirect("/drinkit");
            return "";
        });

        Spark.get("/drinkki/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer reseptiId = Integer.parseInt(req.params(":id"));
            map.put("resepti", reseptit.findOne(reseptiId));
            map.put("raakaAine", raakaAineet.findForResepti(reseptiId));
            map.put("raakaAineResepti", raakaAineReseptit.findOthersForResepti(reseptiId));

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());

        Spark.get("/virhe", (req, res) -> {
            HashMap map = new HashMap<>();

            return new ModelAndView(map, "virhe");
        }, new ThymeleafTemplateEngine());

        Spark.get("/tilasto", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("tilasto", tilastot.findCount());

            return new ModelAndView(map, "tilasto");
        }, new ThymeleafTemplateEngine());

    }
}
