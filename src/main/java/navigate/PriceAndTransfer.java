package navigate;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PriceAndTransfer extends JPanel {
    private JButton loadButton;  // Кнопка для загрузки игроков и трансферов
    private DefaultListModel<String> playersModel;  // Модель для списка игроков
    private JList<String> playersList;  // Список игроков

    // Вручную добавленные данные (ценники и трансферы)
    private static final Map<String, String[]> TEAM_TRANSFERS = new HashMap<>();

    public PriceAndTransfer() {
        // Инициализация панели
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        // Панель для кнопки загрузки данных
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(30, 30, 30));

        loadButton = new JButton("Загрузить игроков и трансферы");  // Кнопка для загрузки

        // Обработчик кнопки для загрузки данных вручную
        loadButton.addActionListener(e -> loadTransferData());

        inputPanel.add(loadButton);

        // Добавляем панель ввода на верхнюю часть панели
        add(inputPanel, BorderLayout.NORTH);

        // Модель и список для отображения игроков, за которыми следят
        playersModel = new DefaultListModel<>();
        playersList = new JList<>(playersModel);
        playersList.setBackground(new Color(50, 50, 50));
        playersList.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(playersList);
        add(scrollPane, BorderLayout.CENTER);  // Добавляем список на центральную часть

        // Заполнение вручную добавленных трансферов
        TEAM_TRANSFERS.put("Derby County FC", new String[] {
                "Player 1 - Transfer Date: 2024-01-01",
                "Player 2 - Transfer Date: 2024-02-01"
        });
        TEAM_TRANSFERS.put("RCD Mallorca", new String[] {
                "Player 3 - Transfer Date: 2024-01-15",
                "Player 4 - Transfer Date: 2024-03-01"
        });
    }

    // Метод для загрузки данных игроков вручную
    private void loadTransferData() {
        try {
            // Вместо API, просто используем вручную добавленные данные
            String team1Name = "Derby County FC";
            String team2Name = "RCD Mallorca";

            // Получаем трансферы для этих команд
            String transfersHome = getTransfersByTeamName(team1Name);
            String transfersAway = getTransfersByTeamName(team2Name);

            // Добавляем трансферы в список
            playersModel.addElement(team1Name + ": " + transfersHome);
            playersModel.addElement(team2Name + ": " + transfersAway);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Метод для получения трансферов по названию команды
    private String getTransfersByTeamName(String teamName) {
        // Используем вручную добавленные данные
        if (TEAM_TRANSFERS.containsKey(teamName)) {
            String[] transfers = TEAM_TRANSFERS.get(teamName);
            StringBuilder transferDetails = new StringBuilder();

            for (String transfer : transfers) {
                transferDetails.append(transfer).append("\n");
            }
            return transferDetails.toString();  // Возвращаем строку с информацией о трансферах
        } else {
            return "Трансферы не найдены для команды: " + teamName;
        }
    }
}
