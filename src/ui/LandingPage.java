package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LandingPage extends BorderPane {

    private static final String BG = "#1a1a2e";
    private static final String PANEL = "#16213e";

    public LandingPage() {
        setStyle("-fx-background-color: " + BG + ";");
        setTop(createHeader());
        setCenter(createContent());
        setBottom(createFooter());
    }

    private VBox createHeader() {
        Label title = new Label("The Feline Graph Chronicles");
        title.setStyle("-fx-text-fill: #e94560; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Pola \u2022 Minerva \u2022 Nina \u2022 Lim\u00f3n");
        subtitle.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");

        VBox header = new VBox(5, title, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30, 0, 20, 0));
        return header;
    }

    private VBox createContent() {
        Label sectionTitle = new Label("Misiones");
        sectionTitle.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox buttons = new HBox(15,
            createMissionButton("Misi\u00f3n 1 \u2014 Pola", "#3498db", "BFS & DFS"),
            createMissionButton("Misi\u00f3n 2 \u2014 Minerva", "#9b59b6", "Dijkstra"),
            createMissionButton("Misi\u00f3n 3 \u2014 Nina", "#e74c3c", "Floyd-Warshall & Bellman-Ford"),
            createMissionButton("Misi\u00f3n 4 \u2014 Lim\u00f3n", "#2ecc71", "Kruskal")
        );
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(20, sectionTitle, buttons);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        return content;
    }

    private VBox createMissionButton(String title, String color, String algo) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label algoLabel = new Label(algo);
        algoLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-font-size: 11px;");

        VBox box = new VBox(4, titleLabel, algoLabel);
        box.setAlignment(Pos.CENTER);
        box.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20 30;" +
            "-fx-cursor: hand;"
        );
        box.setMinWidth(200);
        box.setOnMouseClicked(e -> {
            // TODO: Implementar navegacion a cada mision
        });

        return box;
    }

    private Label createFooter() {
        Label footer = new Label("Scaffolding inicial \u2014 Pr\u00f3ximamente se implementar\u00e1n las misiones");
        footer.setStyle("-fx-text-fill: #34495e; -fx-font-size: 11px;");
        BorderPane.setAlignment(footer, Pos.CENTER);
        BorderPane.setMargin(footer, new Insets(10));
        return footer;
    }
}
