// Package 
package explorerowlexample;

//Imports
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saidi Ali-(+216) 22790538 or (+216) 50 590 538 -Tunisia
 */
//Class OpenOWL
class OpenOWL {

    static String s;

    //Connexion
    static OntModel OpenConnectOWL() {

        OntModel mode = null;
        mode = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
        java.io.InputStream in = FileManager.get().open("C:/ABC.owl"); // be sure file into c:\
        if (in == null) {
            throw new IllegalArgumentException("Fichier ontology introuvable");
        }
        return (OntModel) mode.read(in, "");
    }
    // Connecté au OWL File et retourner le Jena ResultSet

    static com.hp.hpl.jena.query.ResultSet ExecSparQl(String Query) {

        com.hp.hpl.jena.query.Query query = QueryFactory.create(Query);
        QueryExecution qe = QueryExecutionFactory.create(query, OpenConnectOWL());
        com.hp.hpl.jena.query.ResultSet results = qe.execSelect();
        return results;

    }

    // Connecté au OWL File et retourner le String
    static String GetResultAsString(String Query) {
        try {
            com.hp.hpl.jena.query.Query query = QueryFactory.create(Query);
            QueryExecution qe = QueryExecutionFactory.create(query, OpenConnectOWL());
            com.hp.hpl.jena.query.ResultSet results = qe.execSelect();
            if (results.hasNext()) {
                ByteArrayOutputStream go = new ByteArrayOutputStream();
                ResultSetFormatter.out((OutputStream) go, results, query);
                //  String s = go.toString();
                // retouner les resultat de recherche (String ) ;)
                s = new String(go.toByteArray(), "UTF-8");
            } else {
                // si rien trouvé => pour le test 
                s = "rien";
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OpenOWL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

}
//End
