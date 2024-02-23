import java.io.Serializable;

/**
 * Command to toggle the highlight status of a task.
 * This command supports undoing the action, reverting the task's highlight status to its previous state.
 */
public class toggleHighlightCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L; // Serial version UID for serialization

    private Task task; // The task whose highlight status is to be toggled
    private boolean previousHighlight; // The previous highlight status of the task, for undo functionality

    /**
     * Constructs a toggleHighlightCommand for a given task.
     * @param task The task whose highlight status will be toggled.
     */
    public toggleHighlightCommand(Task task) {
        this.task = task;
        this.previousHighlight = task.isImportantHighlight();
    }

    /**
     * Executes the command to toggle the task's highlight status.
     */
    @Override
    public void execute() {
        task.setImportantHighlight(!task.isImportantHighlight());
    }

    /**
     * Undoes the toggle highlight command, reverting the task's highlight status to its previous state.
     */
    @Override
    public void undo() {
        task.setImportantHighlight(previousHighlight);
    }
}
