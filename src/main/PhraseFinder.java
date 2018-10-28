package main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PhraseFinder extends Application {
	private Canvas can;
	private GraphicsContext gc;

	private Timeline tl_draw;

	private int popmax;
	private double mutationRate;
	private Population population;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		tl_draw = new Timeline(new KeyFrame(Duration.millis(1000 / 60), e -> {
			draw();
		}));
		tl_draw.setCycleCount(Timeline.INDEFINITE);
		tl_draw.play();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Pane root = new Pane();
		Scene scene = new Scene(root, 640, 360);

		stage.setTitle("Sample Text");

		can = new Canvas(scene.getWidth(), scene.getHeight());
		gc = can.getGraphicsContext2D();

		root.getChildren().add(can);
		// root.setStyle("-fx-background-color: #000000");

		scene.widthProperty().addListener((obsv, oldVal, newVal) -> {
			can.setWidth(newVal.doubleValue());
		});

		scene.heightProperty().addListener((obsv, oldVal, newVal) -> {
			can.setHeight(newVal.doubleValue());
		});

		stage.setScene(scene);
		stage.show();

		// setup
		final String target = "Hallo";//"Ich denke dieser Satz wird vergleichsweise schnell gefunden werden, oder etwa doch nicht?";//"Rindfleischetikettierungsueberwachungsaufgabenuebertragungsgesetz";
			
		popmax = 2000;
		mutationRate = 0.1 / 100.0;

		// Create a populationation with a target phrase, mutation rate, and
		// populationation max
		population = new Population(target, mutationRate, popmax);
	}

	private void draw() {
		gc.clearRect(0, 0, can.getWidth(), can.getHeight());

		// Generate mating pool
		population.naturalSelection();
		// Create next generation
		population.generate();
		// Calculate fitness
		population.calcFitness();
		displayInfo();

		// If we found the target phrase, stop
		if (population.finished()) {
			tl_draw.stop();
		}
	}

	void displayInfo() {
		// Display current status of populationation
		String answer = population.getBest();

		gc.fillText("Best phrase:", 20, 30);
		gc.fillText(answer, 20, 100);

		gc.fillText("total generations: " + population.getGenerations(), 20, 160);
		gc.fillText("average fitness: " + (int) (population.getAverageFitness() * 100) + "%", 20, 180);
		gc.fillText("total population: " + popmax, 20, 200);
		gc.fillText("mutation rate: " + mutationRate * 100 + "%", 20, 220);

		gc.fillText("All phrases:\n" + population.allPhrases(), 500, 10);
	}
}