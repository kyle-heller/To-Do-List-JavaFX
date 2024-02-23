import java.io.Serializable;
public class toggleHighlightCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L;
    private Task task;
    private boolean previousHighlight;

    public toggleHighlightCommand(Task task) {
        this.task = task;
        this.previousHighlight = task.isImportantHighlight();
    }

    @Override
    public void execute() {
        task.setImportantHighlight(!task.isImportantHighlight());
    }

    @Override
    public void undo() {
        task.setImportantHighlight(previousHighlight);
    }
}
