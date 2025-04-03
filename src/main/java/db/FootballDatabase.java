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
            insertClubIfNotExists(conn, "Manchester United");
            insertClubIfNotExists(conn, "Barcelona");
            insertClubIfNotExists(conn, "Manchester City");
            insertClubIfNotExists(conn, "Liverpool");
            insertClubIfNotExists(conn, "PSG");
            insertClubIfNotExists(conn, "Bayern Munich");

            // Добавляем игроков (если их нет)
            insertPlayerIfNotExists(conn, "David de Gea", "Manchester United");
            insertPlayerIfNotExists(conn, "Cristiano Ronaldo", "Manchester United");
            insertPlayerIfNotExists(conn, "Paul Pogba", "Manchester United");
            insertPlayerIfNotExists(conn, "Marcus Rashford", "Manchester United");

            insertPlayerIfNotExists(conn, "Lionel Messi", "Barcelona");
            insertPlayerIfNotExists(conn, "Sergio Busquets", "Barcelona");
            insertPlayerIfNotExists(conn, "Gerard Piqué", "Barcelona");
            insertPlayerIfNotExists(conn, "Ansu Fati", "Barcelona");

            insertPlayerIfNotExists(conn, "Kevin De Bruyne", "Manchester City");
            insertPlayerIfNotExists(conn, "Riyad Mahrez", "Manchester City");
            insertPlayerIfNotExists(conn, "Phil Foden", "Manchester City");
            insertPlayerIfNotExists(conn, "Jack Grealish", "Manchester City");

            insertPlayerIfNotExists(conn, "Mohamed Salah", "Liverpool");
            insertPlayerIfNotExists(conn, "Sadio Mane", "Liverpool");
            insertPlayerIfNotExists(conn, "Virgil van Dijk", "Liverpool");
            insertPlayerIfNotExists(conn, "Alisson Becker", "Liverpool");

            insertPlayerIfNotExists(conn, "Neymar", "PSG");
            insertPlayerIfNotExists(conn, "Kylian Mbappé", "PSG");
            insertPlayerIfNotExists(conn, "Lionel Messi", "PSG");
            insertPlayerIfNotExists(conn, "Marquinhos", "PSG");

            insertPlayerIfNotExists(conn, "Robert Lewandowski", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Thomas Müller", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Joshua Kimmich", "Bayern Munich");
            insertPlayerIfNotExists(conn, "Serge Gnabry", "Bayern Munich");

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
