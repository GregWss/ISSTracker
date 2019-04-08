package uqac.ca.isstracker.model;

import android.support.annotation.NonNull;

/**
 * Class of type Astronaut. Every single astronaut gets one.
 */
public class Astronaut implements Comparable<Astronaut>
{
    // Data needed to represent a single astronaut
    private String name, image, countryLink, launchDate, role, location, bio, wiki, twitter;

    /**
     * Instantiates a new Astronaut.
     *
     * @param name        Name of astronaut
     * @param image       Profile image
     * @param countryLink Country flag uri image
     * @param launchDate  Date of launch
     * @param role        The role in the ISS (Ingeener)
     * @param location    Location in ISS
     * @param bio         Biography of astronaut
     * @param wiki        If exist link to astronaut's wikipedia
     * @param twitter     If exist link to astronaut's twitter
     */
    public Astronaut(String name, String image, String countryLink, String launchDate, String role, String location, String bio, String wiki, String twitter)
    {
        this.name = name;
        this.image = image;
        this.countryLink = countryLink;
        this.launchDate = launchDate;
        this.role = role;
        this.location = location;
        this.bio = bio;
        this.wiki = wiki;
        this.twitter = twitter;
    }

    /**
     * Gets name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets image.
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Gets flag image.
     * @return the country flag link
     */
    public String getCountryLink() {
        return countryLink;
    }

    /**
     * Gets launch date.
     * @return the launch date
     */
    public String getLaunchDate() {
        return launchDate;
    }

    /**
     * Gets role.
     * @return the role of the astronaut
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets location.
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets bioggraphy.
     * @return the description
     */
    public String getBio() {
        return bio;
    }

    /**
     * Gets wiki.
     * @return the wiki
     */
    public String getWiki() {
        return wiki;
    }

    /**
     * Gets twitter.
     * @return the twitter
     */
    public String getTwitter() {
        return twitter;
    }

    /**
     * Sorts astronauts by location then by role
     * @param astronaut
     * @return Smaller, equal, or bigger? to sort array
     */
    @Override
    public int compareTo(@NonNull Astronaut astronaut) {
        if (this.getLocation().compareTo(astronaut.getLocation()) != 0) {
            return this.getLocation().compareTo(astronaut.getLocation());
        } else {
            return this.getRole().compareTo(astronaut.getRole());
        }
    }
}
