import java.io.IOException;

public abstract class PresentationManager
{
    protected Presentation presentation;

    public PresentationManager(Presentation presentation)
    {
        this.presentation = presentation;
    }

    public Presentation getPresentation()
    {
        return this.presentation;
    }

    public void setPresentation(Presentation presentation)
    {
        this.presentation = presentation;
    }

    public abstract Accessor createAccessor();

    public void loadFile(String filename) throws IOException
    {
        Accessor accessor = createAccessor();
        presentation.clear();
        accessor.loadFile(presentation, filename);
    }

    public void saveFile(String filename) throws IOException
    {
        Accessor accessor = createAccessor();
        accessor.saveFile(presentation, filename);
    }

}
