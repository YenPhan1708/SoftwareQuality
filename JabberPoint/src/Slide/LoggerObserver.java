package Slide;

public class LoggerObserver implements SlideObserver
{
    @Override
    public void update(Slide slide)
    {
        System.out.println("[LoggerObserver] Slide updated: Title = \"" + slide.getTitle() + "\"");
    }
}
