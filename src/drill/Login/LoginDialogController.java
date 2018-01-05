package drill.Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginDialogController {

    private Stage dialogStage;
    private boolean correct_user = false;

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
    }

    public boolean isCorrectUser() {
        return correct_user;
    }

    @FXML private Text actiontarget;

    @FXML protected void handleSubmitButtonAction(ActionEvent event)
    {
        actiontarget.setText("Sign in button pressed");

        correct_user = true;
        dialogStage.close();
    }
}
