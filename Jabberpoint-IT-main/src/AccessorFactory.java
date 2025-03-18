public class AccessorFactory
{
    public static Accessor getAccessor(String type)
    {
        switch (type.toLowerCase())
        {
            case "demo":
                return new DemoPresentation();
            case "xml":
                return new XMLAccessor();
            default:
                throw new IllegalArgumentException("Unknown accessor type: " + type);
        }
    }
}
