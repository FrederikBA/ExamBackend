package dtos.Assistant;

import entities.Assistant;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AssistantDTO {
    private Integer id;
    private String name;
    private String language;
    private int experience;
    private double pricePrHour;

    public static List<AssistantDTO> getFromList(List<Assistant> assistants) {
        return assistants.stream()
                .map(assistant -> new AssistantDTO(assistant))
                .collect(Collectors.toList());
    }

    public AssistantDTO(Assistant assistant) {
        this.id = assistant.getId();
        this.name = assistant.getName();
        this.language = assistant.getLanguage();
        this.experience = assistant.getExperience();
        this.pricePrHour = assistant.getPricePrHour();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getPricePrHour() {
        return pricePrHour;
    }

    public void setPricePrHour(double pricePrHour) {
        this.pricePrHour = pricePrHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantDTO that = (AssistantDTO) o;
        return experience == that.experience && Double.compare(that.pricePrHour, pricePrHour) == 0 && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, language, experience, pricePrHour);
    }

    @Override
    public String toString() {
        return "AssistantDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", experience=" + experience +
                ", pricePrHour=" + pricePrHour +
                '}';
    }
}
