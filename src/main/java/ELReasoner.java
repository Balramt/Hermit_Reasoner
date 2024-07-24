import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.Set;

public class ELReasoner {
    private OWLOntologyManager manager;
    private OWLDataFactory factory;
    private OWLOntology ontology;
    private PrefixManager pm;
    private OWLReasoner reasoner;

    public ELReasoner() {
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

        //reasoner.flush();
    }

    public void addInstanceOfConcept(String individual, String concept) {

        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + individual));
        OWLClass cls = factory.getOWLClass(IRI.create(pm.getDefaultPrefix() + concept));
        OWLAxiom axiom = factory.getOWLClassAssertionAxiom(cls, ind);
        manager.addAxiom(ontology, axiom);
        //reasoner.flush();
    }

    public void addObjectProperty(String property, String subject, String object) {
        OWLObjectProperty prop = factory.getOWLObjectProperty(IRI.create(pm.getDefaultPrefix() + property));
        OWLNamedIndividual subj = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + subject));
        OWLNamedIndividual obj = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + object));
        OWLAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(prop, subj, obj);
        manager.addAxiom(ontology, axiom);
        //reasoner.flush();
    }

    public void initializeReasoner() {
        OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        reasoner = reasonerFactory.createReasoner(ontology);
        reasoner.flush();
    }


    public void classify() {
        System.out.println("\nclassify:");
        reasoner.precomputeInferences();
        for (OWLClass cls : ontology.getClassesInSignature()) {
            // Skip OWLThing
            if (cls.isOWLThing()) {
                continue;
            }
            Set<OWLClass> superClasses = reasoner.getSuperClasses(cls, true).getFlattened();
            Set<OWLClass> subClasses = reasoner.getSubClasses(cls, true).getFlattened();

           // System.out.println("superClasses  "+ superClasses);
            //System.out.println("subClasses  "+ subClasses);
            for (OWLClass superClass : superClasses) {
                // Skip OWLThing
                if (superClass.isOWLThing()) {
                    continue;
                }
                System.out.println(cls.getIRI().getShortForm() + " ⊑ " + superClass.getIRI().getShortForm());
            }
        }
    }

    public void checkInstances() {
        System.out.println("\nInstance Checking:");
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
                System.out.println(individual.getIRI().getShortForm() + " hasChild " + object.getIRI().getShortForm());
            }
        }
    }

    public static void main(String[] args) {
        ELReasoner reasoner = new ELReasoner();

        // Adding concepts and roles
        reasoner.addConcept("Human", "Animal");
        reasoner.addConcept("Parent", "Human");
        reasoner.addConcept("Father", "Parent");


        // Adding individuals and their relationships
        reasoner.addInstanceOfConcept("John", "Human");
        reasoner.addInstanceOfConcept("Mary", "Human");
        reasoner.addInstanceOfConcept("John", "Father");

        // Adding object properties and assertions
        reasoner.addObjectProperty("hasChild", "John", "Mary");

        // Initialize the reasoner after all axioms have been added
        reasoner.initializeReasoner();

        // Reasoning steps
        reasoner.classify();
        reasoner.checkInstances();
        reasoner.propagate();
    }
}






