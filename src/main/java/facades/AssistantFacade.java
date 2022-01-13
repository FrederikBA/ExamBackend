package facades;

import dtos.Assistant.AssistantDTO;
import dtos.Assistant.AssistantsDTO;
import entities.Assistant;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.util.List;

public class AssistantFacade {
    private static EntityManagerFactory emf;
    private static AssistantFacade instance;

    private AssistantFacade() {
    }

    public static AssistantFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AssistantFacade();
        }
        return instance;
    }

    public AssistantDTO getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Assistant assistant = em.find(Assistant.class, id);
            return new AssistantDTO(assistant);
        } finally {
            em.close();
        }
    }

    public AssistantsDTO getAllAssistants() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Assistant> query = em.createQuery("SELECT a FROM Assistant a", Assistant.class);
            List<Assistant> result = query.getResultList();
            return new AssistantsDTO(result);
        } finally {
            em.close();
        }
    }

    public AssistantDTO createAssistant(AssistantDTO assistantDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            if (assistantDTO.getPricePrHour() <= 49) {
                throw new WebApplicationException("The amount is below the minimum wage of 50,- DKK");
            } else {
                Assistant assistant = new Assistant(assistantDTO.getName(), assistantDTO.getLanguage(), assistantDTO.getExperience(), assistantDTO.getPricePrHour());

                em.getTransaction().begin();
                em.persist(assistant);
                em.getTransaction().commit();

                return new AssistantDTO(assistant);
            }
        } finally {
            em.close();
        }
    }

    public AssistantDTO editAssistant(AssistantDTO assistantDTO) {
        EntityManager em = emf.createEntityManager();

        if (assistantDTO.getName().length() == 0 || assistantDTO.getLanguage().length() == 0) {
            throw new WebApplicationException("Inputs are missing, please make sure to fill out the formular");
        }

        try {
            em.getTransaction().begin();
            Assistant assistant = em.find(Assistant.class, assistantDTO.getId());
            if (assistant == null) {
                throw new WebApplicationException("No assistant found matching the id");
            } else {
                assistant.setName(assistantDTO.getName());
                assistant.setLanguage(assistantDTO.getLanguage());
                assistant.setExperience(assistantDTO.getExperience());
                if (assistantDTO.getPricePrHour() <= 49) {
                    throw new WebApplicationException("The amount is below the minimum wage of 50,- DKK");
                } else {
                    assistant.setPricePrHour(assistantDTO.getPricePrHour());
                }
            }
            em.getTransaction().commit();
            return new AssistantDTO(assistant);
        } finally {
            em.close();
        }
    }
}
