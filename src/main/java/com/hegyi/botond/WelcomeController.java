package com.hegyi.botond;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WelcomeController {

	public void handleStartButtonAction(ActionEvent actionEvent) {
		Pane root = new Pane();
		Scene scene = new Scene( root );

		Game game = new Game(1024, 1000, root);

		handler(scene, game);

		root.getChildren().add(game);

		Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.centerOnScreen();
		window.show();
	}

	private void handler(Scene scene, Game game) {
		scene.setOnKeyPressed(e -> {
			Ship player = game.getPlayer();
			switch (e.getCode()){
				case RIGHT:
					if (player.getPositionX() <= game.getWidth()-player.getWidth() && game.isInGame()) {
						player.setMovingRight(true);
					}
					break;
				case LEFT:
					if (player.getPositionX() >= 0 && game.isInGame()) {
						player.setMovingLeft(true);
					}
					break;
				case UP:
					if (game.isInGame()) {
						player.shoot();
					}
					break;
				case ESCAPE:
					Game.myTimer timer = game.getTimer();

					if (game.isInGame()) {
						timer.stop();
						game.setInGame(false);
					} else {
						timer.start();
						game.setInGame(true);
					}
					break;
			}
		});

		scene.setOnKeyPressed(f -> {
			Ship player2 = game.getPlayer2();
			switch (f.getCode()){
				case D:
					if (player2.getPositionX() <= game.getWidth()-player2.getWidth() && game.isInGame()) {
						player2.setMovingRight(true);
					}
					break;
				case Q:
					if (player2.getPositionX() >= 0 && game.isInGame()) {
						player2.setMovingLeft(true);
					}
					break;
				case Z:
					if (game.isInGame()) {
						player2.shoot();
					}
					break;
				case ESCAPE:
					Game.myTimer timer = game.getTimer();

					if (game.isInGame()) {
						timer.stop();
						game.setInGame(false);
					} else {
						timer.start();
						game.setInGame(true);
					}
					break;
			}
		});



		scene.setOnKeyReleased(e -> {
			MovingGameObject player = game.getPlayer();
			switch (e.getCode()){
				case RIGHT:
					player.setMovingRight(false);
					break;
				case LEFT:
					player.setMovingLeft(false);
					break;
			}
		});

		scene.setOnKeyReleased(f -> {
			MovingGameObject player2 = game.getPlayer2();
			switch (f.getCode()){
				case D:
					player2.setMovingRight(false);
					break;
				case Q:
					player2.setMovingLeft(false);
					break;
			}
		});
	}


	public void handleExitButtonAction(ActionEvent actionEvent) {
		System.exit(0);
	}

	public void handleInstructionButtonAction(ActionEvent actionEvent) {
		Alert info = new Alert(Alert.AlertType.INFORMATION);
		info.setTitle("Instruction");
		info.setHeaderText(null);
		info.setContentText("Moving with Left and Right arrow.\nShoot with Space.");
		Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/invader.png").toString()));
		info.showAndWait();
	}
}
