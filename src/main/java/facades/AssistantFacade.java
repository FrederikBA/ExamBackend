package facades;

import javax.persistence.EntityManagerFactory;

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
}
