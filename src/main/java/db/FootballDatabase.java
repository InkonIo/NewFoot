package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FootballDatabase {

    private static final String DB_URL = "jdbc:sqlite:database.db";

    // Метод для проверки соединения с базой данных
    public static boolean isDatabaseWorking() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            // Выполняем простой запрос, чтобы проверить соединение
            String checkSQL = "SELECT 1";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(checkSQL);
                return rs.next();  // Если результат есть, значит база работает
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // В случае ошибки соединения возвращаем false
        }
    }

    // Метод для получения списка игроков клуба
    public static List<String> getPlayersByClub(String clubName) {
        List<String> players = new ArrayList<>();
        String sql = "SELECT p.player_name FROM players p" +
                " JOIN clubs c ON p.club_id = c.id" +
                " WHERE c.club_name = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, clubName);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                players.add(resultSet.getString("player_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    // Создание таблиц для клубов и игроков, если они не существуют
    private static void createTablesIfNotExists() {
        String createClubsTableSQL = "CREATE TABLE IF NOT EXISTS clubs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "club_name TEXT NOT NULL UNIQUE" +
                ");";

        String createPlayersTableSQL = "CREATE TABLE IF NOT EXISTS players (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "player_name TEXT NOT NULL, " +
                "club_id INTEGER, " +
                "FOREIGN KEY (club_id) REFERENCES clubs(id)" +
                ");";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(createClubsTableSQL);
            statement.execute(createPlayersTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Очищаем таблицы
            stmt.executeUpdate("DELETE FROM players");
            stmt.executeUpdate("DELETE FROM clubs");

            // Сбрасываем автоинкрементные значения
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name = 'players'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name = 'clubs'");

            System.out.println("Database reset successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertClubsAndPlayers() {
        resetDatabase();  // Сбрасываем данные перед вставкой новых

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Добавляем клубы (если их нет)
            insertClubIfNotExists(conn, "Arsenal");
            insertClubIfNotExists(conn, "Manchester City");
            insertClubIfNotExists(conn, "Liverpool");
            insertClubIfNotExists(conn, "Chelsea");
            insertClubIfNotExists(conn, "Manchester United");
            insertClubIfNotExists(conn, "Tottenham Hotspur");
            insertClubIfNotExists(conn, "Newcastle United");
            insertClubIfNotExists(conn, "Aston Villa");
            insertClubIfNotExists(conn, "West Ham United");
            insertClubIfNotExists(conn, "Brighton & Hove Albion");
            insertClubIfNotExists(conn, "Everton");
            insertClubIfNotExists(conn, "Wolverhampton Wanderers");
            insertClubIfNotExists(conn, "Brentford");
            insertClubIfNotExists(conn, "Crystal Palace");
            insertClubIfNotExists(conn, "Fulham");
            insertClubIfNotExists(conn, "Nottingham Forest");
            insertClubIfNotExists(conn, "Bournemouth");
            insertClubIfNotExists(conn, "Burnley");
            insertClubIfNotExists(conn, "Sheffield United");
            insertClubIfNotExists(conn, "Luton Town");
            insertClubIfNotExists(conn, "Real Madrid");
            insertClubIfNotExists(conn, "Barcelona");
            insertClubIfNotExists(conn, "Atletico Madrid");
            insertClubIfNotExists(conn, "Valencia");
            insertClubIfNotExists(conn, "Sevilla");
            insertClubIfNotExists(conn, "Villarreal");
            insertClubIfNotExists(conn, "Real Sociedad");
            insertClubIfNotExists(conn, "Athletic Bilbao");
            insertClubIfNotExists(conn, "Real Betis");
            insertClubIfNotExists(conn, "Getafe");
            insertClubIfNotExists(conn, "Celta Vigo");
            insertClubIfNotExists(conn, "Osasuna");
            insertClubIfNotExists(conn, "Mallorca");
            insertClubIfNotExists(conn, "Almeria");
            insertClubIfNotExists(conn, "Cadiz");
            insertClubIfNotExists(conn, "Las Palmas");
            insertClubIfNotExists(conn, "Granada");
            insertClubIfNotExists(conn, "Girona");
            insertClubIfNotExists(conn, "Alaves");
            insertClubIfNotExists(conn, "Bayern Munich");
            insertClubIfNotExists(conn, "Borussia Dortmund");
            insertClubIfNotExists(conn, "RB Leipzig");
            insertClubIfNotExists(conn, "Bayer Leverkusen");
            insertClubIfNotExists(conn, "Eintracht Frankfurt");
            insertClubIfNotExists(conn, "Union Berlin");
            insertClubIfNotExists(conn, "Freiburg");
            insertClubIfNotExists(conn, "Hoffenheim");
            insertClubIfNotExists(conn, "Wolfsburg");
            insertClubIfNotExists(conn, "Stuttgart");
            insertClubIfNotExists(conn, "Borussia Mönchengladbach");
            insertClubIfNotExists(conn, "Werder Bremen");
            insertClubIfNotExists(conn, "Augsburg");
            insertClubIfNotExists(conn, "Bochum");
            insertClubIfNotExists(conn, "Juventus");
            insertClubIfNotExists(conn, "Inter Milan");
            insertClubIfNotExists(conn, "AC Milan");
            insertClubIfNotExists(conn, "Napoli");
            insertClubIfNotExists(conn, "Roma");
            insertClubIfNotExists(conn, "Lazio");
            insertClubIfNotExists(conn, "Atalanta");
            insertClubIfNotExists(conn, "Fiorentina");
            insertClubIfNotExists(conn, "Torino");
            insertClubIfNotExists(conn, "Bologna");
            insertClubIfNotExists(conn, "Udinese");
            insertClubIfNotExists(conn, "Sassuolo");
            insertClubIfNotExists(conn, "Empoli");
            insertClubIfNotExists(conn, "Hellas Verona");
            insertClubIfNotExists(conn, "Cagliari");
            insertClubIfNotExists(conn, "Genoa");
            insertClubIfNotExists(conn, "Frosinone");
            insertClubIfNotExists(conn, "Lecce");
            insertClubIfNotExists(conn, "Salernitana");
            insertClubIfNotExists(conn, "Monza");
            insertClubIfNotExists(conn, "PSG");
            insertClubIfNotExists(conn, "Marseille");
            insertClubIfNotExists(conn, "Lyon");
            insertClubIfNotExists(conn, "Monaco");
            insertClubIfNotExists(conn, "Lille");
            insertClubIfNotExists(conn, "Nice");
            insertClubIfNotExists(conn, "Rennes");
            insertClubIfNotExists(conn, "Lens");
            insertClubIfNotExists(conn, "Strasbourg");
            insertClubIfNotExists(conn, "Nantes");
            insertClubIfNotExists(conn, "Toulouse");
            insertClubIfNotExists(conn, "Montpellier");
            insertClubIfNotExists(conn, "Brest");
            insertClubIfNotExists(conn, "Reims");
            insertClubIfNotExists(conn, "Metz");
            insertClubIfNotExists(conn, "Lorient");
            insertClubIfNotExists(conn, "Clermont Foot");
            insertClubIfNotExists(conn, "Le Havre");


            // Добавляем игроков (если их нет)
            insertPlayerIfNotExists(conn, "David de Gea", "Manchester United");
            insertPlayerIfNotExists(conn, "Cristiano Ronaldo", "Manchester United");
            insertPlayerIfNotExists(conn, "Paul Pogba", "Manchester United");
            insertPlayerIfNotExists(conn, "Marcus Rashford", "Manchester United");
            insertPlayerIfNotExists(conn, "Bruno Fernandes", "Manchester United");
            insertPlayerIfNotExists(conn, "Harry Maguire", "Manchester United");
            insertPlayerIfNotExists(conn, "Luke Shaw", "Manchester United");
            insertPlayerIfNotExists(conn, "Raphaël Varane", "Manchester United");
            insertPlayerIfNotExists(conn, "Casemiro", "Manchester United");
            insertPlayerIfNotExists(conn, "Jadon Sancho", "Manchester United");
            insertPlayerIfNotExists(conn, "Anthony Martial", "Manchester United");

            insertPlayerIfNotExists(conn, "Aaron Ramsdale", "Arsenal");
            insertPlayerIfNotExists(conn, "William Saliba", "Arsenal");
            insertPlayerIfNotExists(conn, "Ben White", "Arsenal");
            insertPlayerIfNotExists(conn, "Oleksandr Zinchenko", "Arsenal");
            insertPlayerIfNotExists(conn, "Gabriel Magalhães", "Arsenal");
            insertPlayerIfNotExists(conn, "Declan Rice", "Arsenal");
            insertPlayerIfNotExists(conn, "Martin Ødegaard", "Arsenal");
            insertPlayerIfNotExists(conn, "Bukayo Saka", "Arsenal");
            insertPlayerIfNotExists(conn, "Gabriel Martinelli", "Arsenal");
            insertPlayerIfNotExists(conn, "Leandro Trossard", "Arsenal");
            insertPlayerIfNotExists(conn, "Gabriel Jesus", "Arsenal");

            insertPlayerIfNotExists(conn, "Lionel Messi", "Barcelona");
            insertPlayerIfNotExists(conn, "Sergio Busquets", "Barcelona");
            insertPlayerIfNotExists(conn, "Gerard Piqué", "Barcelona");
            insertPlayerIfNotExists(conn, "Ansu Fati", "Barcelona");
            insertPlayerIfNotExists(conn, "Marc-André ter Stegen", "Barcelona");
            insertPlayerIfNotExists(conn, "Ronald Araújo", "Barcelona");
            insertPlayerIfNotExists(conn, "Jordi Alba", "Barcelona");
            insertPlayerIfNotExists(conn, "Pedri", "Barcelona");
            insertPlayerIfNotExists(conn, "Gavi", "Barcelona");
            insertPlayerIfNotExists(conn, "Frenkie de Jong", "Barcelona");
            insertPlayerIfNotExists(conn, "Ousmane Dembélé", "Barcelona");


            insertPlayerIfNotExists(conn, "Ederson", "Manchester City");
            insertPlayerIfNotExists(conn, "Kyle Walker", "Manchester City");
            insertPlayerIfNotExists(conn, "Rúben Dias", "Manchester City");
            insertPlayerIfNotExists(conn, "Nathan Aké", "Manchester City");
            insertPlayerIfNotExists(conn, "John Stones", "Manchester City");
            insertPlayerIfNotExists(conn, "Rodri", "Manchester City");
            insertPlayerIfNotExists(conn, "Kevin De Bruyne", "Manchester City");
            insertPlayerIfNotExists(conn, "Bernardo Silva", "Manchester City");
            insertPlayerIfNotExists(conn, "Jack Grealish", "Manchester City");
            insertPlayerIfNotExists(conn, "Phil Foden", "Manchester City");
            insertPlayerIfNotExists(conn, "Erling Haaland", "Manchester City");

            insertPlayerIfNotExists(conn, "Alisson Becker", "Liverpool");
            insertPlayerIfNotExists(conn, "Trent Alexander-Arnold", "Liverpool");
            insertPlayerIfNotExists(conn, "Virgil van Dijk", "Liverpool");
            insertPlayerIfNotExists(conn, "Ibrahima Konaté", "Liverpool");
            insertPlayerIfNotExists(conn, "Andrew Robertson", "Liverpool");
            insertPlayerIfNotExists(conn, "Alexis Mac Allister", "Liverpool");
            insertPlayerIfNotExists(conn, "Dominik Szoboszlai", "Liverpool");
            insertPlayerIfNotExists(conn, "Curtis Jones", "Liverpool");
            insertPlayerIfNotExists(conn, "Mohamed Salah", "Liverpool");
            insertPlayerIfNotExists(conn, "Luis Díaz", "Liverpool");
            insertPlayerIfNotExists(conn, "Darwin Núñez", "Liverpool");

            insertPlayerIfNotExists(conn, "Robert Sánchez", "Chelsea");
            insertPlayerIfNotExists(conn, "Reece James", "Chelsea");
            insertPlayerIfNotExists(conn, "Thiago Silva", "Chelsea");
            insertPlayerIfNotExists(conn, "Levi Colwill", "Chelsea");
            insertPlayerIfNotExists(conn, "Ben Chilwell", "Chelsea");
            insertPlayerIfNotExists(conn, "Enzo Fernández", "Chelsea");
            insertPlayerIfNotExists(conn, "Moises Caicedo", "Chelsea");
            insertPlayerIfNotExists(conn, "Conor Gallagher", "Chelsea");
            insertPlayerIfNotExists(conn, "Mykhailo Mudryk", "Chelsea");
            insertPlayerIfNotExists(conn, "Raheem Sterling", "Chelsea");
            insertPlayerIfNotExists(conn, "Nicolas Jackson", "Chelsea");


            insertPlayerIfNotExists(conn, "Guglielmo Vicario", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Pedro Porro", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Cristian Romero", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Micky van de Ven", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Destiny Udogie", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Yves Bissouma", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Pape Sarr", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "James Maddison", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Heung-min Son", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Richarlison", "Tottenham Hotspur");
            insertPlayerIfNotExists(conn, "Dejan Kulusevski", "Tottenham Hotspur");


            insertPlayerIfNotExists(conn, "Neymar", "PSG");
            insertPlayerIfNotExists(conn, "Kylian Mbappé", "PSG");
            insertPlayerIfNotExists(conn, "Lionel Messi", "PSG");
            insertPlayerIfNotExists(conn, "Marquinhos", "PSG");
            insertPlayerIfNotExists(conn, "Gianluigi Donnarumma", "PSG");
            insertPlayerIfNotExists(conn, "Achraf Hakimi", "PSG");
            insertPlayerIfNotExists(conn, "Sergio Ramos", "PSG");
            insertPlayerIfNotExists(conn, "Marco Verratti", "PSG");
            insertPlayerIfNotExists(conn, "Presnel Kimpembe", "PSG");
            insertPlayerIfNotExists(conn, "Danilo Pereira", "PSG");
            insertPlayerIfNotExists(conn, "Carlos Soler", "PSG");

            insertPlayerIfNotExists(conn, "Robert Lewandowski", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Thomas Müller", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Joshua Kimmich", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Serge Gnabry", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Manuel Neuer", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Leroy Sané", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Leon Goretzka", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Alphonso Davies", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Kingsley Coman", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Dayot Upamecano", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Matthijs de Ligt", "Bayern Munich");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Добавляет клуб только если его нет
    private static void insertClubIfNotExists(Connection conn, String clubName) throws SQLException {
        String sql = "INSERT INTO clubs (club_name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM clubs WHERE club_name = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, clubName);
            stmt.setString(2, clubName);
            stmt.executeUpdate();
        }
    }

    // ✅ Добавляет игрока только если его нет
    private static void insertPlayerIfNotExists(Connection conn, String playerName, String clubName) throws SQLException {
        String sql = "INSERT INTO players (player_name, club_id) " +
                "SELECT ?, (SELECT id FROM clubs WHERE club_name = ?) " +
                "WHERE NOT EXISTS (SELECT 1 FROM players WHERE player_name = ? AND club_id = (SELECT id FROM clubs WHERE club_name = ?))";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.setString(2, clubName);
            stmt.setString(3, playerName);
            stmt.setString(4, clubName);
            stmt.executeUpdate();
        }
    }

    public static void insertPlayer(String playerName, String clubName) {
        String sql = "INSERT INTO players (player_name, club_id) " +
                "SELECT ?, c.id FROM clubs c WHERE c.club_name = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            pstmt.setString(2, clubName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
