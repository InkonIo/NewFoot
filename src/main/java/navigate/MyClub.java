package navigate;

import db.FootballDatabase;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyClub extends JPanel {
    private JLabel favoriteClubLabel;
    private JLabel clubLogoLabel;
    private DefaultListModel<String> playersListModel;
    private JList<String> playersList;
    private JTextField playerInputField;
    private JButton addPlayerButton;

    private String favoriteClub;
    private String clubLogoUrl;
    private List<String> favoritePlayers;
    private JLabel statusLabel;  // Для отображения статуса работы с базой данных

    public MyClub(String club, String clubLogoUrl) {
        this.favoriteClub = club;
        this.clubLogoUrl = clubLogoUrl;
        this.favoritePlayers = new ArrayList<>();

        System.out.println("Вызываем insertClubsAndPlayers()...");
        FootballDatabase.insertClubsAndPlayers();  // Заполняем БД

        // Проверяем, загружены ли клубы
        List<String> players = FootballDatabase.getPlayersByClub("Manchester United");
        System.out.println("Игроки Манчестер Юнайтед: " + players);

        System.out.println("Метод insertClubsAndPlayers() вызван");

        initialize();
    }


    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        // Панель для клуба
        JPanel clubPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        clubPanel.setBackground(new Color(30, 30, 30));

        clubLogoLabel = new JLabel();
        favoriteClubLabel = new JLabel("Любимый клуб: " + (favoriteClub != null ? favoriteClub : "Не выбран"));
        favoriteClubLabel.setFont(new Font("Arial", Font.BOLD, 16));
        favoriteClubLabel.setForeground(Color.WHITE);

        clubPanel.add(clubLogoLabel);
        clubPanel.add(favoriteClubLabel);
        add(clubPanel, BorderLayout.NORTH);

        // Список игроков клуба
        playersListModel = new DefaultListModel<>();
        playersList = new JList<>(playersListModel);
        playersList.setBackground(new Color(50, 50, 50));
        playersList.setForeground(Color.WHITE);

        JScrollPane playersScrollPane = new JScrollPane(playersList);
        add(playersScrollPane, BorderLayout.CENTER);

        // Панель для добавления игроков
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(new Color(30, 30, 30));

        playerInputField = new JTextField(15);
        addPlayerButton = new JButton("Добавить игрока");

        addPlayerButton.addActionListener(e -> addFavoritePlayer());

        inputPanel.add(playerInputField);
        inputPanel.add(addPlayerButton);

        add(inputPanel, BorderLayout.SOUTH);

        // Добавим статусную метку
        statusLabel = new JLabel("Проверка базы данных...");
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel, BorderLayout.SOUTH);

        // Обновляем логотип и информацию о клубе
        updateFavoriteClub();

        // Проверка работы базы данных
        if (FootballDatabase.isDatabaseWorking()) {
            statusLabel.setText("База данных работает нормально.");
            // Загружаем игроков выбранного клуба
            loadClubPlayers();
        } else {
            statusLabel.setText("Ошибка подключения к базе данных.");
        }
    }

    private void updateFavoriteClub() {
        SwingUtilities.invokeLater(() -> {
            favoriteClubLabel.setText("Любимый клуб: " + (favoriteClub != null ? favoriteClub : "Не выбран"));

            if (clubLogoUrl != null && !clubLogoUrl.isEmpty()) {
                try {
                    ImageIcon clubIcon = new ImageIcon(new URL(clubLogoUrl));
                    clubLogoLabel.setIcon(new ImageIcon(clubIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
                } catch (Exception e) {
                    System.out.println("Ошибка загрузки логотипа: " + e.getMessage());
                    clubLogoLabel.setText("Ошибка загрузки логотипа");
                }
            } else {
                clubLogoLabel.setText("Нет логотипа");
            }
        });
    }

    private void loadClubPlayers() {
        // Получаем список игроков клуба через FootballDatabase
        List<String> players = FootballDatabase.getPlayersByClub(favoriteClub);

        // Очищаем старые данные
        playersListModel.clear();

        // Добавляем игроков в список
        if (players != null && !players.isEmpty()) {
            for (String player : players) {
                playersListModel.addElement(player);
            }
        } else {
            playersListModel.addElement("Нет игроков для данного клуба");
        }
    }

    private void addFavoritePlayer() {
        String playerName = playerInputField.getText().trim();
        if (!playerName.isEmpty() && !favoritePlayers.contains(playerName)) {
            favoritePlayers.add(playerName);
            playersListModel.addElement(playerName + " - Нравится");
            playerInputField.setText("");
            // Добавим игрока в базу данных
            FootballDatabase.insertPlayer(playerName, favoriteClub);
        }
    }
}
