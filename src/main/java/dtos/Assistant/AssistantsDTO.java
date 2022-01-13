package dtos.Assistant;

import entities.Assistant;

import java.util.List;
import java.util.Objects;

public class AssistantsDTO {
    private List<AssistantDTO> assistants;

    public AssistantsDTO(List<Assistant> assistants) {
        this.assistants = AssistantDTO.getFromList(assistants);
    }

    public List<AssistantDTO> getAssistants() {
        return assistants;
    }

    public void setAssistants(List<AssistantDTO> assistants) {
        this.assistants = assistants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantsDTO that = (AssistantsDTO) o;
        return Objects.equals(assistants, that.assistants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assistants);
    }

    @Override
    public String toString() {
        return "AssistantsDTO{" +
                "assistants=" + assistants +
                '}';
    }
}
