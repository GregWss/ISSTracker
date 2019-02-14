package uqac.ca.isstracker.model;

public class Astro
{
    private String name;
    private String craft;

    public Astro(String name, String craft)
    {
        this.name = name;
        this.craft = craft;
    }

    public String getName()
    {
        return name;
    }

    public String getCraft()
    {
        return craft;
    }
}
