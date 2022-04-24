public class ViewForms {
    private final MainFrame mainFrame;
    private final ChoiceFiled choiceFiled;
    private final InputCodeFiled inputCodeFiled;
    private final OutputFiled outputFiled;

    public ViewForms() {
        this.mainFrame = MainFrame.getInstance();
        this.choiceFiled = new ChoiceFiled();
        this.inputCodeFiled = new InputCodeFiled();
        this.outputFiled = new OutputFiled();
    }


    public void run() {
        mainFrame.initialization();
        viewFormVChoiceFiled();
    }

    private void viewFormVChoiceFiled() {
        mainFrame.setContent(choiceFiled, "Окно выбора");
    }

    private void viewFormOutputFiled() {
        mainFrame.setContent(outputFiled, "Окно декодирования");
    }

    private void viewFormInputCodeFiled() {
        mainFrame.setContent(inputCodeFiled, "Окно кодирования");
    }
}
