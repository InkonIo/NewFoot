package navigate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class CalendarOfMatches extends JPanel {
    private JList<String> matchesList;  // Список матчей
    private DefaultListModel<String> matchesListModel;  // Модель для списка матчей
    private JComboBox<String> monthComboBox;  // Выпадающий список для выбора месяца

    private static final List<String> TEAMS = Arrays.asList(
            "Coventry City FC", "Burnley FC", "Everton FC", "Arsenal FC",
            "Luton Town FC", "Leeds United FC", "West Bromwich Albion FC",
            "Sunderland AFC", "Girona FC", "Deportivo Alavés", "AC Monza",
            "Como 1907", "SC Freiburg", "Borussia Dortmund", "RB Leipzig",
            "TSG 1899 Hoffenheim", "1. FC Heidenheim 1846", "Bayer 04 Leverkusen",
            "Chelsea FC", "Manchester United", "Liverpool FC", "Manchester City",
            "Tottenham Hotspur", "Juventus FC", "Inter Milan", "AS Roma", "AC Milan",
            "Real Madrid", "FC Barcelona", "Atletico Madrid", "Sevilla FC",
            "Bayer Leverkusen", "Eintracht Frankfurt", "VfB Stuttgart", "Wolverhampton Wanderers",
            "Crystal Palace FC", "Newcastle United", "Southampton FC", "Aston Villa",
            "FC Porto", "Benfica", "Sporting CP", "Boca Juniors", "River Plate"
    );


    private static final List<String> LEAGUES = Arrays.asList(
            "Premier League", "Championship", "La Liga", "Serie A", "Bundesliga"
    );

    public CalendarOfMatches() {
        // Настройка панели
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 128));  // Синий фон

        // Панель для заголовка
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 128));  // Синий фон
        JLabel headerLabel = new JLabel("Календарь матчей");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(headerLabel);

        // Панель для выбора месяца
        JPanel monthPanel = new JPanel();
        monthPanel.setBackground(new Color(0, 0, 128));  // Синий фон
        monthComboBox = new JComboBox<>(new String[]{
                "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
        });
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        monthComboBox.setForeground(Color.WHITE);
        monthComboBox.setBackground(new Color(30, 30, 30));
        JButton loadButton = new JButton("Показать матчи");
        loadButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loadButton.setForeground(Color.WHITE);
        loadButton.setBackground(new Color(0, 0, 128));

        // Добавляем слушатель на кнопку
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMonth = (String) monthComboBox.getSelectedItem();
                if ("Апрель".equals(selectedMonth)) {
                    loadMatchesFromAPI();  // Загружаем реальные матчи с API
                } else {
                    loadMatches(selectedMonth);  // Генерируем случайные матчи для других месяцев
                }
            }
        });

        monthPanel.add(monthComboBox);
        monthPanel.add(loadButton);

        // Добавляем заголовок на верхнюю панель
        add(headerPanel, BorderLayout.NORTH);
        add(monthPanel, BorderLayout.SOUTH);

        // Модель и список для отображения матчей
        matchesListModel = new DefaultListModel<>();
        matchesList = new JList<>(matchesListModel);
        matchesList.setBackground(new Color(30, 30, 30));  // Темный фон для списка
        matchesList.setForeground(Color.WHITE);  // Белый текст
        matchesList.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(matchesList);
        add(scrollPane, BorderLayout.CENTER);  // Добавляем список на центральную часть
    }

    private void loadMatches(String month) {
        // Генерация случайных матчей для выбранного месяца
        List<String[]> randomMatches = generateRandomMatches(month);
        updateMatchData(randomMatches);
    }

    private void loadMatchesFromAPI() {
        // Здесь нужно добавить вызов API для получения реальных матчей
        List<String[]> matchData = FootballAPI.getNews();  // Пример использования API
        updateMatchData(matchData);
    }

    private List<String[]> generateRandomMatches(String month) {
        List<String[]> matches = new ArrayList<>();
        Random rand = new Random();

        // Генерация случайных матчей для выбранного месяца
        for (int i = 0; i < 10; i++) {
            String homeTeam = TEAMS.get(rand.nextInt(TEAMS.size()));
            String awayTeam = TEAMS.get(rand.nextInt(TEAMS.size()));
            while (homeTeam.equals(awayTeam)) {  // Убедимся, что команды разные
                awayTeam = TEAMS.get(rand.nextInt(TEAMS.size()));
            }
            String league = LEAGUES.get(rand.nextInt(LEAGUES.size()));
            String date = generateRandomDate();  // Генерируем случайную дату
            String score = rand.nextInt(5) + " - " + rand.nextInt(5);  // Генерация случайного счета

            String[] match = {
                    "Матч: " + homeTeam + " vs " + awayTeam,
                    date,
                    league + ": " + score
            };
            matches.add(match);
        }

        return matches;
    }

    private String generateRandomDate() {
        Random rand = new Random();
        int day = rand.nextInt(28) + 1;  // Случайный день (от 1 до 28)
        int month = rand.nextInt(12) + 1;  // Случайный месяц (от 1 до 12)
        int year = 2025;  // Фиксированный год
        return String.format("%02d-%02d-%dT%02d:%02d:%02dZ", day, month, year, rand.nextInt(24), rand.nextInt(60), rand.nextInt(60));
    }

    // Метод для обновления данных матчей
    private void updateMatchData(List<String[]> matchData) {
        matchesListModel.clear();  // Очищаем список перед добавлением новых данных

        for (String[] match : matchData) {
            String matchInfo = match[0] + "\n" + match[2];  // Форматируем данные
            matchesListModel.addElement(matchInfo);  // Добавляем матч в список
        }
    }

    public static void main(String[] args) {
        // Запускаем приложение
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Футбольный календарь");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.add(new CalendarOfMatches());  // Добавляем панель в окно
            frame.setVisible(true);
        });
    }
}
