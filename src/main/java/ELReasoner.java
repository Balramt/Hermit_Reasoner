/*
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
            System.out.println("ontology"+ontology);
            pm = new DefaultPrefixManager(null, null, "http://example.com/owlapi/ontologies/ontology#");
            System.out.println("pm"+pm);

            OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
            reasoner = reasonerFactory.createReasoner(ontology);

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

    public void addConcept(String child, String parent) {
        OWLClass parentClass = factory.getOWLClass(IRI.create(pm.getDefaultPrefix() + parent));
        OWLClass childClass = factory.getOWLClass(IRI.create(pm.getDefaultPrefix() + child));
        OWLAxiom axiom = factory.getOWLSubClassOfAxiom(childClass, parentClass);

*//*System.out.println("parentClass"+parentClass);
        System.out.println("childClass"+childClass);
        System.out.println("axiom"+axiom);*//*

        manager.addAxiom(ontology, axiom);
        reasoner.flush();
    }

    public void addInstanceOfConcept(String individual, String concept) {
        OWLNamedIndividual ind = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + individual));
        OWLClass cls = factory.getOWLClass(IRI.create(pm.getDefaultPrefix() + concept));
        OWLAxiom axiom = factory.getOWLClassAssertionAxiom(cls, ind);

*//*   System.out.println("ind"+ind);
        System.out.println("cls"+cls);
        System.out.println("axiom1"+axiom);*//*

        manager.addAxiom(ontology, axiom);
        reasoner.flush();
    }

    public void classify() {
        reasoner.precomputeInferences();
        //System.out.println("Concept Hierarchy:");
        //System.out.println("Concept of all classes:"+ ontology.getClassesInSignature());
        for (OWLClass cls : ontology.getClassesInSignature()) {
            Set<OWLClass> superClasses = reasoner.getSuperClasses(cls, true).getFlattened();
          //  System.out.println(" superClasses classify 3 "+superClasses);
            for (OWLClass superClass : superClasses) {
                System.out.println(" superClass classify 1 "+superClass);
                System.out.println(" superClasses classify 2 "+superClasses);
                System.out.println(factory.getOWLClass(superClass.getIRI()).getIRI().getShortForm() + " ⊑ " + factory.getOWLClass(cls.getIRI()).getIRI().getShortForm());

            }
        }
    }

    public void checkInstances() {
       // System.out.println("\nInstance Checking:");
       // System.out.println("All the indivudal"+ontology.getIndividualsInSignature());
        for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()) {
            for (OWLClassAssertionAxiom axiom : ontology.getClassAssertionAxioms(ind)) {
               // System.out.println("indivudal name"+ind + "indivudal's  class"+ axiom);
                System.out.println(factory.getOWLNamedIndividual(ind.getIRI()).getIRI().getShortForm() + " ∈ " + factory.getOWLClass(axiom.getClassExpression().asOWLClass().getIRI()).getIRI().getShortForm());
            }
        }
    }



*//*  public void propagate() {
        try {
            System.out.println("\nPropagation:");

            // Ensure ontology and reasoner are properly initialized and loaded
            reasoner.precomputeInferences();  // Ensure inferences are precomputed

            for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {
                // Print subject IRI for debugging
              //  System.out.println("Subject: " + subject.getIRI());

                // Retrieve object property assertion axioms for the subject
                Set<OWLObjectPropertyAssertionAxiom> axioms = ontology.getObjectPropertyAssertionAxioms(individual);
                System.out.println("Number of axioms for " + individual.getIRI().getShortForm() + ": " + axioms.size());

                // Iterate over object property assertion axioms for the subject
                for (OWLObjectPropertyAssertionAxiom axiom : axioms) {
                    OWLNamedIndividual object = axiom.getObject().asOWLNamedIndividual();
                    System.out.println(individual.getIRI().getShortForm() + " hasChild " + object.getIRI().getShortForm());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*//**//*


    public void propagate() {
        reasoner.precomputeInferences(); // Ensure inferences are computed
        System.out.println("Axiom1"+ontology.getIndividualsInSignature());
        for (OWLNamedIndividual individual : ontology.getIndividualsInSignature()) {
            Set<OWLObjectPropertyAssertionAxiom> axioms = ontology.getObjectPropertyAssertionAxioms(individual);
            System.out.println("Axioms for individual " + individual.getIRI().getShortForm() + ": " + axioms.size());
            System.out.println("Axiom"+axioms);
            for (OWLObjectPropertyAssertionAxiom axiom : axioms) {
                System.out.println("Assertion: " + axiom);
            }
        }
    }







    *//*
*//*   public void propagate() {
        System.out.println("\nPropagation:");
        System.out.println("All the indivudal in propogation "+ontology.getIndividualsInSignature());
        for (OWLNamedIndividual subject : ontology.getIndividualsInSignature()) {
            System.out.println(" subject in propogation "+subject);
            for (OWLObjectPropertyAssertionAxiom axiom : ontology.getObjectPropertyAssertionAxioms(subject)) {
                System.out.println(" axiom in propogation "+axiom);
                OWLNamedIndividual object = axiom.getObject().asOWLNamedIndividual();
                System.out.println(subject.getIRI().getShortForm() + " hasChild " + object.getIRI().getShortForm());
            }
        }
    }*//**//*


  *//*
*//*  public void propagate() {
        // This method can be adapted to use OWL API as needed for your specific propagation logic.
        // Here is a simplified example:
        System.out.println("\nPropagation:");
        for (OWLObjectPropertyAssertionAxiom axiom : ontology.getObjectPropertyAssertionAxioms()) {
            OWLNamedIndividual subject = axiom.getSubject().asOWLNamedIndividual();
            OWLNamedIndividual object = axiom.getObject().asOWLNamedIndividual();
            System.out.println(subject.getIRI().getShortForm() + " hasChild " + object.getIRI().getShortForm());
        }
    }*//*


    public static void main(String[] args) {
        ELReasoner reasoner = new ELReasoner();

        // Adding concepts and roles
        reasoner.addConcept("Human", "Animal");
        reasoner.addConcept("Human", "Parent");
        reasoner.addConcept("Parent", "Father");

        // Adding individuals and their relationships
        reasoner.addInstanceOfConcept("John", "Human");
        reasoner.addInstanceOfConcept("Mary", "Human");
        reasoner.addInstanceOfConcept("John", "Father");



        // Reasoning steps
        reasoner.classify();
        reasoner.checkInstances();
        reasoner.propagate();
    }
}*/









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
        System.out.println("Axiom : "+axiom);
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

  /*  public void classify() {
         System.out.println("\nclassify:");
        reasoner.precomputeInferences();
        for (OWLClass cls : ontology.getClassesInSignature()) {
            Set<OWLClass> superClasses = reasoner.getSuperClasses(cls, true).getFlattened();
            for (OWLClass superClass : superClasses) {
                System.out.println(factory.getOWLClass(superClass.getIRI()).getIRI().getShortForm() + " ⊑ " + factory.getOWLClass(cls.getIRI()).getIRI().getShortForm());
            }
        }
    }*/

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

     /*   // Adding concepts and roles
        reasoner.addConcept("Animal", "Human"); // Animal ⊑ Human
        reasoner.addConcept("Father", "Parent"); // Father ⊑ Parent
        reasoner.addConcept("Human", "Parent"); // Human ⊑ Parent
*/
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

