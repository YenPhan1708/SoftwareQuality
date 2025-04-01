import java.io.IOException;

public abstract class PresentationManager implements Accessor
{
    public Presentation presentation;

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

    @Override
    public void loadFile(Presentation presentation, String filename) throws IOException
    {
        Accessor accessor = createAccessor();
        presentation.clear();
        accessor.loadFile(presentation, filename);
    }

    @Override
    public void saveFile(Presentation presentation, String filename ) throws IOException
    {
        Accessor accessor = createAccessor();
        accessor.saveFile(presentation, filename);
    }

}
