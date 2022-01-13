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
}
