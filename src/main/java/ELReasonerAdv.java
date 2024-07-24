import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.Set;

public class ELReasonerAdv {
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private OWLOntology ontology;
    private PrefixManager pm;
    private OWLReasoner reasoner;

    public ELReasonerAdv() {
        try {
            manager = OWLManager.createOWLOntologyManager();
            factory = manager.getOWLDataFactory();
            ontology = manager.createOntology(IRI.create("http://example.com/owlapi/ontologies/ontology"));
            pm = new DefaultPrefixManager(null, null, "http://example.com/owlapi/ontologies/ontology#");

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

    public void addConcept(String child, String parent) {
        OWLClass childClass = factory.getOWLClass(IRI.create(pm.getDefaultPrefix() + child));
        OWLClass parentClass = factory.getOWLClass(IRI.create(pm.getDefaultPrefix() + parent));
        OWLAxiom axiom = factory.getOWLSubClassOfAxiom(childClass, parentClass);
        manager.addAxiom(ontology, axiom);
    }

    public void addInstanceOfConcept(String individual, String concept) {
        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + individual));
        OWLClass cls = factory.getOWLClass(IRI.create(pm.getDefaultPrefix() + concept));
        OWLAxiom axiom = factory.getOWLClassAssertionAxiom(cls, ind);
        manager.addAxiom(ontology, axiom);
    }

    public void addObjectProperty(String property, String subject, String object) {
        OWLObjectProperty prop = factory.getOWLObjectProperty(IRI.create(pm.getDefaultPrefix() + property));
        OWLNamedIndividual subj = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + subject));
        OWLNamedIndividual obj = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + object));
        OWLAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(prop, subj, obj);
        manager.addAxiom(ontology, axiom);
    }

    public void initializeReasoner() {
        OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(ontology);
    }

    public void classify() {
        reasoner.precomputeInferences();
    }

    public void checkInstances() {
        System.out.println("checkInstances:");
        for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()) {
            for (OWLClassAssertionAxiom axiom : ontology.getClassAssertionAxioms(ind)) {
                System.out.println(factory.getOWLNamedIndividual(ind.getIRI()).getIRI().getShortForm() + " ∈ " + factory.getOWLClass(axiom.getClassExpression().asOWLClass().getIRI()).getIRI().getShortForm());
            }
        }
    }

    public void propagate() {
        System.out.println("\npropagate:");
        reasoner.precomputeInferences();
        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {
            Set<OWLObjectPropertyAssertionAxiom> axioms = ontology.getObjectPropertyAssertionAxioms(individual);
            System.out.println("Axioms for individual " + individual.getIRI().getShortForm() + ": " + axioms.size());
            for (OWLObjectPropertyAssertionAxiom axiom : axioms) {
                OWLNamedIndividual object = axiom.getObject().asOWLNamedIndividual();
                System.out.println(individual.getIRI().getShortForm() + " hasProperty " + object.getIRI().getShortForm());
            }
        }
    }

    public static void main(String[] args) {
        ELReasonerAdv reasoner = new ELReasonerAdv();

        // Adding classes
        OWLClass qualified = reasoner.factory.getOWLClass(IRI.create(reasoner.pm.getDefaultPrefix() + "Qualified"));
        OWLClass aiExpert = reasoner.factory.getOWLClass(IRI.create(reasoner.pm.getDefaultPrefix() + "AI-Expert"));
        OWLClass theorist = reasoner.factory.getOWLClass(IRI.create(reasoner.pm.getDefaultPrefix() + "Theorist"));
        OWLClass interviewed = reasoner.factory.getOWLClass(IRI.create(reasoner.pm.getDefaultPrefix() + "Interviewed"));
        OWLClass ai = reasoner.factory.getOWLClass(IRI.create(reasoner.pm.getDefaultPrefix() + "AI"));
        OWLClass theory = reasoner.factory.getOWLClass(IRI.create(reasoner.pm.getDefaultPrefix() + "Theory"));

        // Adding object properties
        OWLObjectProperty specializes = reasoner.factory.getOWLObjectProperty(IRI.create(reasoner.pm.getDefaultPrefix() + "Specializes"));
        OWLObjectProperty publishes = reasoner.factory.getOWLObjectProperty(IRI.create(reasoner.pm.getDefaultPrefix() + "Publishes"));

        // Adding individuals
        OWLNamedIndividual alice = reasoner.factory.getOWLNamedIndividual(IRI.create(reasoner.pm.getDefaultPrefix() + "Alice"));
        OWLNamedIndividual bob = reasoner.factory.getOWLNamedIndividual(IRI.create(reasoner.pm.getDefaultPrefix() + "Bob"));
        OWLNamedIndividual ml = reasoner.factory.getOWLNamedIndividual(IRI.create(reasoner.pm.getDefaultPrefix() + "ML"));
        OWLNamedIndividual complexity = reasoner.factory.getOWLNamedIndividual(IRI.create(reasoner.pm.getDefaultPrefix() + "Complexity"));

        // Define class hierarchy and restrictions
        OWLClassExpression qualifiedAndAIExpert = reasoner.factory.getOWLObjectIntersectionOf(qualified, aiExpert);
        OWLAxiom qualifiedAndAIExpertSubClassOfInterviewed = reasoner.factory.getOWLSubClassOfAxiom(qualifiedAndAIExpert, interviewed);
        reasoner.manager.addAxiom(reasoner.ontology, qualifiedAndAIExpertSubClassOfInterviewed);

        OWLClassExpression specializesAI = reasoner.factory.getOWLObjectSomeValuesFrom(specializes, ai);
        OWLAxiom specializesAISubClassOfAIExpert = reasoner.factory.getOWLSubClassOfAxiom(specializesAI, aiExpert);
        reasoner.manager.addAxiom(reasoner.ontology, specializesAISubClassOfAIExpert);

        OWLClassExpression specializesTheory = reasoner.factory.getOWLObjectSomeValuesFrom(specializes, theory);
        OWLAxiom specializesTheorySubClassOfTheorist = reasoner.factory.getOWLSubClassOfAxiom(specializesTheory, theorist);
        reasoner.manager.addAxiom(reasoner.ontology, specializesTheorySubClassOfTheorist);

        OWLAxiom publishesSubPropertyOfSpecializes = reasoner.factory.getOWLSubObjectPropertyOfAxiom(publishes, specializes);
        reasoner.manager.addAxiom(reasoner.ontology, publishesSubPropertyOfSpecializes);

        // Adding individual assertions
        reasoner.addInstanceOfConcept("Alice", "Qualified");
        reasoner.addInstanceOfConcept("Bob", "Qualified");
        reasoner.addInstanceOfConcept("ML", "AI");
        reasoner.addInstanceOfConcept("Complexity", "Theory");
        reasoner.addObjectProperty("Publishes", "Alice", "ML");
        reasoner.addObjectProperty("Publishes", "Bob", "Complexity");

        // Initialize the reasoner after all axioms have been added
        reasoner.initializeReasoner();

        // Reasoning steps
        reasoner.classify();
        reasoner.checkInstances();
        reasoner.propagate();
        reasoner.checkConsistency();

        // Check if Alice and Bob are inferred to be AI-Expert and Theorist
        reasoner.checkInferredClasses("Alice");
        reasoner.checkInferredClasses("Bob");

    }
    public void checkConsistency() {
        boolean isConsistent = reasoner.isConsistent();
        System.out.println("Ontology is consistent: " + isConsistent);
    }


    private void checkInferredClasses(String individualName) {
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + individualName));
        Set<OWLClass> inferredClasses = reasoner.getTypes(individual, true).getFlattened();
        System.out.println("\nInferred classes for " + individualName + ":");
        for (OWLClass inferredClass : inferredClasses) {
            System.out.println(individualName + " ∈ " + inferredClass.getIRI().getShortForm());
        }
    }
}




