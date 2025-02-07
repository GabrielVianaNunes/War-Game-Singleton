package war_singleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class WarGame extends JFrame {

    private java.util.List<Player> players;
    private JTextArea displayArea;
    private Player currentPlayer;
    private JPanel mapPanel;
    private Map<String, java.awt.Color> playerColors;
    private Set<String> playerNames;

    public WarGame() {
        players = new ArrayList<>();
        playerColors = new HashMap<>();
        playerNames = new HashSet<>();
        initializePlayers();
        initializeUI();
        showShuffleDialog(); // Mostra a janela de sorteio
    }

    private void initializePlayers() {
        int numPlayers;
        do {
            String input = customInputDialog("Quantos jogadores irão participar? (Mínimo de 2, Máximo de 5)", "Configuração de Jogo");
            if (input == null) { // Se o usuário cancelar ou fechar a janela
                System.exit(0);
            }
            numPlayers = (input != null && !input.trim().isEmpty()) ? Integer.parseInt(input) : 0;
        } while (numPlayers < 2 || numPlayers > 5);

        java.awt.Color[] distinctColors = {new Color(255, 0, 0), new Color(0, 0, 255), new Color(0, 255, 0), new Color(255, 165, 0), new Color(128, 0, 128)};

        for (int i = 0; i < numPlayers; i++) {
            String playerName;
            do {
                playerName = customInputDialog("Insira o nome do Jogador " + (i + 1) + " (até 20 caracteres, nome único):", "Nome do Jogador");
                if (playerName == null) { // Se o usuário cancelar ou fechar a janela
                    System.exit(0);
                }
                if (playerNames.contains(playerName.trim())) {
                    JOptionPane.showMessageDialog(null, "O nome '" + playerName + "' já está em uso. Por favor, escolha outro.", "Nome Duplicado", JOptionPane.WARNING_MESSAGE);
                }
            } while (playerName == null || playerName.length() > 20 || playerName.trim().isEmpty() || playerNames.contains(playerName.trim()));

            playerNames.add(playerName.trim());
            Player player = new Player(playerName.trim(), distinctColors[i]);
            players.add(player);
            playerColors.put(player.getName(), player.getColor());
        }

        int numTerritories = numPlayers * 3; // Por exemplo, 3 territórios por jogador
        GameBoard.getInstance().initializeBoard(numTerritories);
    }

    private void showShuffleDialog() {
        JFrame shuffleFrame = new JFrame("Sorteio da Ordem de Jogada");
        shuffleFrame.setSize(400, 200);
        shuffleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        shuffleFrame.setLayout(new BorderLayout());

        JLabel message = new JLabel("Clique no botão abaixo para sortear a ordem de jogada.", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 16));
        message.setForeground(Color.WHITE);
        shuffleFrame.getContentPane().setBackground(new Color(34, 49, 34));
        shuffleFrame.add(message, BorderLayout.NORTH);

        JButton shuffleButton = new JButton("SORTEIO");
        shuffleButton.setFont(new Font("Arial", Font.BOLD, 16));
        shuffleButton.setBackground(new Color(44, 62, 44));
        shuffleButton.setForeground(Color.WHITE);
        shuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shufflePlayerOrder();
                shuffleFrame.dispose(); // Fecha a janela de sorteio
            }
        });

        shuffleFrame.add(shuffleButton, BorderLayout.CENTER);
        shuffleFrame.setLocationRelativeTo(null); // Centraliza a janela na tela
        shuffleFrame.setVisible(true);
    }

    private void shufflePlayerOrder() {
        Collections.shuffle(players); // Embaralha a ordem dos jogadores
        displayArea.append("\nOrdem de jogo sorteada:\n");
        for (Player player : players) {
            displayArea.append(player.getName() + "\n");
        }
        currentPlayer = players.get(0); // Define o primeiro jogador após o sorteio
        displayArea.append("\nPrimeiro a jogar: " + currentPlayer.getName() + "\n");
    }

    private String customInputDialog(String message, String title) {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(34, 49, 34)); // Fundo verde escuro
        textField.setForeground(Color.WHITE); // Texto branco
        textField.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(34, 49, 34)); // Fundo verde escuro
        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);

        UIManager.put("OptionPane.background", new Color(34, 49, 34));
        UIManager.put("Panel.background", new Color(34, 49, 34));
        UIManager.put("Button.background", new Color(44, 62, 44));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));

        int result = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
            System.exit(0); // Fecha o programa se o usuário cancelar ou fechar a janela
        }
        return textField.getText();
    }

    private void initializeUI() {
        setTitle("WAR - Jogo de Estratégia");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(34, 49, 34)); // Fundo verde escuro

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.BOLD, 16));
        displayArea.setForeground(Color.WHITE);
        displayArea.setBackground(new Color(44, 62, 44));
        add(new JScrollPane(displayArea), BorderLayout.EAST);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(24, 39, 24));
        JLabel titleLabel = new JLabel("WAR - Jogo de Estratégia");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        menuBar.add(titleLabel);
        setJMenuBar(menuBar);

        mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(0, 3));
        mapPanel.setBackground(new Color(34, 49, 34));
        updateMapDisplay();
        add(mapPanel, BorderLayout.CENTER);

        updateDisplay();
    }

    private void occupyTerritory(int territoryIndex) {
        GameBoard board = GameBoard.getInstance();
        String owner = board.getTerritoryOwner(territoryIndex);

        if (owner.equals(currentPlayer.getName())) {
            displayArea.append("Você já ocupa este território!\n");
        } else if (!owner.isEmpty()) {
            initiateBattle(territoryIndex, owner);
        } else {
            board.occupyTerritory(territoryIndex, currentPlayer.getName());
            displayArea.append(currentPlayer.getName() + " ocupou " + board.getTerritories().get(territoryIndex) + "\n");
            updateMapDisplay();
            switchPlayer();
        }
    }

    private void initiateBattle(int territoryIndex, String defender) {
        int attackingArmies = getNumberOfArmies("Ataque", currentPlayer.getName());
        int defendingArmies = getNumberOfArmies("Defesa", defender);

        int[] attackRolls = rollDice(attackingArmies);
        int[] defenseRolls = rollDice(defendingArmies);

        Arrays.sort(attackRolls);
        Arrays.sort(defenseRolls);

        int battles = Math.min(attackRolls.length, defenseRolls.length);
        int attackerLosses = 0;
        int defenderLosses = 0;

        for (int i = 0; i < battles; i++) {
            if (attackRolls[attackRolls.length - 1 - i] > defenseRolls[defenseRolls.length - 1 - i]) {
                defenderLosses++;
            } else {
                attackerLosses++;
            }
        }

        displayArea.append(currentPlayer.getName() + " perdeu " + attackerLosses + " exércitos.\n");
        displayArea.append(defender + " perdeu " + defenderLosses + " exércitos.\n");

        if (defenderLosses >= defendingArmies) {
            displayArea.append(currentPlayer.getName() + " conquistou o território!\n");
            GameBoard.getInstance().occupyTerritory(territoryIndex, currentPlayer.getName());
        } else {
            displayArea.append(defender + " manteve o território.\n");
        }

        updateMapDisplay();
        switchPlayer();
    }

    private int getNumberOfArmies(String tipo, String playerName) {
        JTextField textField = new JTextField();
        textField.setBackground(new Color(34, 49, 34)); // Fundo verde escuro
        textField.setForeground(Color.WHITE); // Texto branco
        textField.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(34, 49, 34));

        JLabel label = new JLabel(playerName + ": Quantos exércitos deseja usar para " + tipo + "? (1 a 3)");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);

        UIManager.put("OptionPane.background", new Color(34, 49, 34));
        UIManager.put("Panel.background", new Color(34, 49, 34));
        UIManager.put("Button.background", new Color(44, 62, 44));
        UIManager.put("Button.foreground", Color.WHITE);

        int result = JOptionPane.showConfirmDialog(null, panel, "Batalha", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
            return 1; // Valor padrão caso o jogador cancele
        }

        try {
            int numArmies = Integer.parseInt(textField.getText());
            if (numArmies >= 1 && numArmies <= 3) {
                return numArmies;
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, insira um número entre 1 e 3.", "Entrada Inválida", JOptionPane.WARNING_MESSAGE);
                return getNumberOfArmies(tipo, playerName); // Repetir a entrada
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida. Por favor, insira um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            return getNumberOfArmies(tipo, playerName); // Repetir a entrada
        }
    }

    private int[] rollDice(int numDice) {
        Random random = new Random();
        int[] rolls = new int[numDice];
        for (int i = 0; i < numDice; i++) {
            rolls[i] = random.nextInt(6) + 1;
        }
        return rolls;
    }

    private void switchPlayer() {
        int currentIndex = players.indexOf(currentPlayer);
        currentPlayer = players.get((currentIndex + 1) % players.size());
        displayArea.append("Agora é a vez de: " + currentPlayer.getName() + "\n");
    }

    private void updateMapDisplay() {
        mapPanel.removeAll();
        GameBoard board = GameBoard.getInstance();

        for (int i = 0; i < board.getTerritories().size(); i++) {
            final int territoryIndex = i;
            String territory = board.getTerritories().get(i);
            JLabel territoryLabel = new JLabel(territory, SwingConstants.CENTER);
            territoryLabel.setOpaque(true);
            territoryLabel.setFont(new Font("Arial", Font.BOLD, 14));

            if (board.isTerritoryOccupied(i)) {
                String owner = board.getTerritoryOwner(i);
                territoryLabel.setBackground(playerColors.get(owner));
                territoryLabel.setText(territory + " - " + owner);
            } else {
                territoryLabel.setBackground(new Color(44, 62, 44));
            }

            territoryLabel.setBorder(BorderFactory.createLineBorder(new Color(24, 39, 24)));
            territoryLabel.setForeground(Color.WHITE);
            territoryLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    occupyTerritory(territoryIndex);
                }
            });
            mapPanel.add(territoryLabel);
        }

        mapPanel.revalidate();
        mapPanel.repaint();
    }

    private void updateDisplay() {
        displayArea.setText("Jogadores:\n");
        for (Player player : players) {
            displayArea.append(player.getName() + "\n");
        }
        displayArea.append("\nClique em um território para ocupá-lo ou atacar.\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WarGame game = new WarGame();
            game.setVisible(true);
        });
    }
}
