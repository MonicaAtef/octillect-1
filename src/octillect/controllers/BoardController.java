package octillect.controllers;

import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import octillect.controllers.dialogs.NewBoardDialogController;
import octillect.controllers.dialogs.NewColumnDialogController;
import octillect.controllers.dialogs.RepositoryNameDialogController;
import octillect.controllers.settings.BoardSettingsController;
import octillect.controllers.settings.GitHubRepositoryController;
import octillect.controls.OButton;
import octillect.controls.TasksColumn;
import octillect.models.Board;

import org.kordamp.ikonli.javafx.FontIcon;

public class BoardController implements Injectable<ApplicationController> {

    // Local Fields
    public Board currentBoard;

    // FXML Fields
    @FXML public ListView boardListView;
    @FXML public JFXTextField searchTextField;
    @FXML public FontIcon gitHubIcon;
    @FXML public FontIcon calendarIcon;
    @FXML public FontIcon addColumnIcon;
    @FXML public Label noBoardsLabel;
    @FXML public OButton newBoardOButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private LeftDrawerController leftDrawerController;
    private RightDrawerController rightDrawerController;
    private TitleBarController titleBarController;
    private NewColumnDialogController newColumnDialogController;
    private NewBoardDialogController newBoardDialogController;
    private RepositoryNameDialogController repositoryNameDialogController;
    private BoardSettingsController boardSettingsController;
    private GitHubRepositoryController gitHubRepositoryController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController     = applicationController;
        leftDrawerController           = applicationController.leftDrawerController;
        rightDrawerController          = applicationController.rightDrawerController;
        titleBarController             = applicationController.titleBarController;
        newColumnDialogController      = applicationController.newColumnDialogController;
        newBoardDialogController       = applicationController.newBoardDialogController;
        repositoryNameDialogController = applicationController.repositoryNameDialogController;
        boardSettingsController        = rightDrawerController.boardSettingsController;
        gitHubRepositoryController     = rightDrawerController.gitHubRepositoryController;
    }

    @Override
    public void init() {
        boardSettingsController.resetBoardSettings();
        if (!applicationController.user.getBoards().isEmpty()) {
            loadBoard(applicationController.user.getBoards().get(0));
            leftDrawerController.userBoardsListView.getSelectionModel().selectFirst();
        } else {
            showToolbar(false);
            titleBarController.boardNameLabel.setText("Octillect");
            boardListView.getItems().clear();
        }
    }

    public void loadBoard(Board board) {

        currentBoard = board;

        showToolbar(true);
        titleBarController.boardNameLabel.setText(board.getName());
        boardSettingsController.loadBoardSettings();

        // Populate Board Columns
        boardListView.setItems(board.getChildren());
        boardListView.setCellFactory(param -> {
            TasksColumn tasksColumn = new TasksColumn();
            tasksColumn.inject(applicationController);
            return tasksColumn;
        });

    }

    @FXML
    public void handleGitHubIconMouseClicked(MouseEvent mouseEvent) {
        if (currentBoard.getRepositoryName() != null) {
            gitHubRepositoryController.loadGitHubRepository();
            rightDrawerController.show(rightDrawerController.gitHubRepository);
            applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
        } else {
            repositoryNameDialogController.repositoryNameDialog.show(applicationController.rootStackPane);
        }
    }

    @FXML
    public void handleCalendarIconMouseClicked(MouseEvent mouseEvent) {
    }

    @FXML
    public void handleAddColumnIconMouseClicked(MouseEvent mouseEvent) {
        newColumnDialogController.newColumnDialog.show(applicationController.rootStackPane);
    }

    private void showToolbar(boolean show) {
        if (show) {
            searchTextField.setOpacity(1);
            searchTextField.setDisable(false);
            gitHubIcon.setOpacity(1);
            gitHubIcon.setDisable(false);
            calendarIcon.setOpacity(1);
            calendarIcon.setDisable(false);
            addColumnIcon.setOpacity(1);
            addColumnIcon.setDisable(false);
            noBoardsLabel.setOpacity(0);
            newBoardOButton.setDisable(true);
            newBoardOButton.setOpacity(0);
        } else {
            searchTextField.setOpacity(0);
            searchTextField.setDisable(true);
            gitHubIcon.setOpacity(0);
            gitHubIcon.setDisable(true);
            calendarIcon.setOpacity(0);
            calendarIcon.setDisable(true);
            addColumnIcon.setOpacity(0);
            addColumnIcon.setDisable(true);
            noBoardsLabel.setOpacity(1);
            newBoardOButton.setDisable(false);
            newBoardOButton.setOpacity(1);
        }
    }

    @FXML
    public void handleNewBoardButtonAction(ActionEvent actionEvent) {
        newBoardDialogController.newBoardDialog.show(applicationController.rootStackPane);
    }

}