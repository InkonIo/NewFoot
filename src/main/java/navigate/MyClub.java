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
    private JButton clearButton;  // Кнопка для очистки

    private String favoriteClub;
    private String clubLogoUrl;
    private List<String> favoritePlayers;
    private JLabel statusLabel;  // Для отображения статуса работы с базой данных

    public MyClub(String club, String clubLogoUrl) {
        this.favoriteClub = club;
        this.clubLogoUrl = clubLogoUrl;
        this.favoritePlayers = new ArrayList<>();
        FootballDatabase.insertClubsAndPlayers();
        List<String> players = FootballDatabase.getPlayersByClub("");
        initialize();
    }

    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30));


        JPanel clubPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        clubPanel.setBackground(new Color(30, 30, 30));

        clubLogoLabel = new JLabel();
        favoriteClubLabel = new JLabel("Любимый клуб: " + (favoriteClub != null ? favoriteClub : "Не выбран"));
        favoriteClubLabel.setFont(new Font("Arial", Font.BOLD, 16));
        favoriteClubLabel.setForeground(Color.WHITE);

        clubPanel.add(clubLogoLabel);
        clubPanel.add(favoriteClubLabel);
        add(clubPanel);

        // Список игроков клуба
        playersListModel = new DefaultListModel<>();
        playersList = new JList<>(playersListModel);
        playersList.setBackground(new Color(50, 50, 50));
        playersList.setForeground(Color.WHITE);
        playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Одновременный выбор одного элемента
        playersList.setFixedCellHeight(40);  // Увеличим высоту строки для лучшего восприятия

        JScrollPane playersScrollPane = new JScrollPane(playersList);
        playersScrollPane.setPreferredSize(new Dimension(350, 250));  // Установим размер прокрутки

        // Обработчик двойного клика на игрока
        playersList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {  // Проверяем, был ли двойной клик
                    int index = playersList.locationToIndex(evt.getPoint());  // Получаем индекс элемента, по которому кликнули
                    String playerName = playersList.getModel().getElementAt(index);  // Получаем имя игрока

                    // Если метка "Нравится" уже есть, удаляем ее, если нет — добавляем
                    if (playerName.contains(" - Нравится")) {
                        playersListModel.set(index, playerName.replace(" - Нравится", ""));
                    } else {
                        playersListModel.set(index, playerName + " - Нравится");
                    }
                }
            }
        });

        // Размещение списка в центральной части
        add(playersScrollPane);

        // Панель для добавления игроков
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(new Color(30, 30, 30));

        playerInputField = new JTextField(15);
        addPlayerButton = new JButton("Добавить игрока");

        addPlayerButton.addActionListener(e -> addFavoritePlayer());

        // Панель для кнопки очистки
        JPanel clearButtonPanel = new JPanel(new FlowLayout());
        clearButtonPanel.setBackground(new Color(30, 30, 30));

        // Кнопка для очистки статуса "Нравится"
        clearButton = new JButton("Очистить");
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setBackground(Color.RED);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setPreferredSize(new Dimension(100, 30));

        clearButton.addActionListener(e -> clearFavoriteStatus());

        inputPanel.add(playerInputField);
        inputPanel.add(addPlayerButton);

        // Добавляем панель с кнопкой очистки
        clearButtonPanel.add(clearButton);

        // Размещаем внизу
        add(inputPanel);
        add(clearButtonPanel);

        // Добавим статусную метку
        statusLabel = new JLabel("Проверка базы данных...");
        statusLabel.setForeground(Color.WHITE);
        add(statusLabel);

        // Обновляем логотип и информацию о клубе
        updateFavoriteClub();

        // Проверка работы базы данных
        if (FootballDatabase.isDatabaseWorking()) {
            statusLabel.setText("");
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

    private void clearFavoriteStatus() {
        // Очищаем статус всех игроков
        for (int i = 0; i < playersListModel.getSize(); i++) {
            String playerName = playersListModel.getElementAt(i);
            if (playerName.contains(" - Нравится")) {
                // Убираем пометку "Нравится"
                playersListModel.set(i, playerName.replace(" - Нравится", ""));
            }
        }
        favoritePlayers.clear();  // Очищаем список любимых игроков
    }
}
