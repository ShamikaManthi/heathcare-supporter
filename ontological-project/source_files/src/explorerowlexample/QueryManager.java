/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorerowlexample;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;

public class QueryManager {

    private static QueryManager instance;

    private QueryManager() {
        System.out.println("QueryManager constructor");
    }

    public static QueryManager getInstance() {
        if (instance == null) {

            System.out.println("QueryManager getInstance");
            org.apache.log4j.BasicConfigurator.configure();
            return new QueryManager();

        } else {
            return instance;
        }
    }

    public List<Hospital> filteredQuery(Map<String, String> filter) {

        System.out.println("QueryManager filteredQuery 111111");
        List<Hospital> list = new ArrayList<>();
        Map<String, Hospital> found = new TreeMap<>();

        Model model = ModelFactory.createDefaultModel();
        model.read("hospitalv1.owl");
        ParameterizedSparqlString queryStr = new ParameterizedSparqlString();

        queryStr.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        queryStr.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        queryStr.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
        queryStr.setNsPrefix("", "http://www.hospital.com/ontologies/hospital.owl#");
        
        System.out.println("QueryManager filteredQuery 22222222222222");
        
        queryStr.append("SELECT distinct *");
        queryStr.append(" WHERE {");
        queryStr.append(" ?bd rdfs:subClassOf :Hospital. ?hospital rdf:type ?bd. \n");
        System.out.println("QueryManager filteredQuery 333333333333");
        processFilter(filter, queryStr);

        queryStr.append("?hospital :hasImage ?image.\n");

        queryStr.append("?hospital rdfs:label ?label.");
        queryStr.append("OPTIONAL{");
        
        System.out.println("QueryManager filteredQuery 444444444444");
        
        queryStr.append("?hospital :hasAddress ?addressStr.");
             
        queryStr.append("}");

        queryStr.append("OPTIONAL{");
        
        queryStr.append("?hospital :hasRating ?rating.");   
                
        queryStr.append("}");

        queryStr.append("OPTIONAL{");
        
        queryStr.append("?hospital :hasTelephoneNo ?tele."); 
        
        System.out.println(queryStr);
        
        queryStr.append("}");

        queryStr.append("OPTIONAL{");
               
        queryStr.append("}");
        

        queryStr.append("} ");
        queryStr.append("LIMIT 50 ");
        Query q = queryStr.asQuery();
        QueryExecution qExec = QueryExecutionFactory.create(q, model);
        try {
            ResultSet results = qExec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                RDFNode name = soln.get("?label");
                RDFNode imgurl = soln.get("?image");
                RDFNode address = soln.get("?addressStr");
                RDFNode rating = soln.get("?rating");
                RDFNode telephone = soln.get("?tele");

                String nameStr = name.asLiteral().toString().replace("\"", "");
                String imgurlStr = imgurl.asLiteral().toString().replace("\"", "");
                String addressH = address == null ? null : address.asLiteral().toString().replace("\"", "");
                Double ratingVal = rating == null ? null : rating.asLiteral().getDouble();
                String teleNo = telephone.asLiteral().toString().replace("\"", "");
                
                if (found.containsKey(nameStr)) {
                    if (addressH != null) {
                        found.get(nameStr).getAddresses().add(addressH);
                    }
                    if (ratingVal != null) {
                        found.get(nameStr).setRating(ratingVal);
                    }
                    if (teleNo != null) {
                        found.get(nameStr).setTelephoneNo(teleNo);
                    }
                    
                } else {
                    Hospital hospital = new Hospital();
                    hospital.setImageUrl(imgurlStr);
                    hospital.setName(nameStr);
                    if (addressH != null) {
                        hospital.getAddresses().add(addressH);
                    }
                    if (ratingVal != null) {
                        hospital.setRating(ratingVal);
                    }
                    if (teleNo != null) {
                        hospital.setTelephoneNo(teleNo);
                    }
                    found.put(nameStr, hospital);

                }

            }
        } finally {
            qExec.close();
        }
        found.values().stream().forEach((d) -> {
            list.add(d);
        });
        return list;
    }

    private void processFilter(Map<String, String> filter, ParameterizedSparqlString queryStr) {
        System.out.println("filter");
        System.out.println(filter);
        System.out.println("queryStr first");
        System.out.println(queryStr);

        if (filter.containsKey("Type")) {
            String value = filter.get("Type");
            switch (value) {
                case "Private":
                    queryStr.append("?hospital rdf:type :Private.");
                    break;
                case "SemiGovernment":
                    queryStr.append("?hospital rdf:type :SemiGovernment.");
                    break;
                case "Government":
                    queryStr.append("?hospital rdf:type :Government.");
                    break;
            }
        }
        System.out.println("queryStr Second");
        System.out.println(queryStr);
        if (filter.containsKey("Quality")) {
            String value = filter.get("Quality");
            System.out.println("value");
            System.out.println(value);
            String level = "";

            switch (value) {
                case "High":
                    level = "High";
                    break;
                case "Average":
                    level = "Medium";
                    break;
                case "Satisfactory":
                    level = "Low";
                    break;
            }

            queryStr.append("  ?hospital :hasQuality :" + level 
                    + " . ");

        }
        System.out.println("queryStr Third");
        System.out.println(queryStr);
        
        if (filter.containsKey("EventType")) {
            String value = filter.get("EventType");

            String level = "";
            switch (value) {
                case "Appointment":
                    level = "Appointment";
                    break;
                case "Prescription":
                    level = "Prescription";
                    break;
                case "Test":
                    level = "Test";
                    break;
                case "Treatment":
                    level = "Treatment";
                    break;
                case "E-Channelling":
                    level = "EChannelling";
                    break;
            }
            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :" + level + " . ");
            queryStr.append(" } UNION");
            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :Event . ");
            queryStr.append(" } ");
        }

        if (filter.containsKey("District")) {
            String value = filter.get("District");
            String level = "";
            switch (value) {
                case "Colombo":
                    level = "Colombo";
                    break;
                case "Gampaha":
                    level = "Gampaha";
                    break;
                case "Kandy":
                    level = "Kandy";
                    break;
                case "Galle":
                    level = "Galle";
                    break;
                case "Kurunegala":
                    level = "Kurunegala";
                    break;
                case "Kalutara":
                    level = "Kalutara";
                    break;
                case "Matara":
                    level = "Matara";
                    break;
            }
            queryStr.append(" ?hospital :hasLocation ?location. \n"
                    + "  ?location rdf:type :" + level + " . ");
        }
        System.out.println(queryStr);
        if (filter.containsKey("Pharmacy")) {
            String value = filter.get("Pharmacy");
            String level = "";
            switch (value) {
                case "Needed":
                    level = "Available";
                    queryStr.append("  ?hospital rdf:type :" + level + " . ");    
                    break;
                case "Not Needed":
                    level = "NotAvailable";
//                    queryStr.append("  ?hospital rdf:type :" + level + " . ");
                    break;
            }

            
        }
        

        if (filter.containsKey("Laboratory")) {
            String value = filter.get("Laboratory");
            String level = "";
            switch (value) {
                case "Chemical":
                    level = "Chemical";
                    break;
                case "MicroBiology":
                    level = "MicroBiology";
                    break;
                case "Radiology":
                    level = "Radiology";
                    break;
                case "Scanning":
                    level = "Scanning";
                    break;
            }

            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :" + level + " . ");
            queryStr.append(" } UNION");
            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :Laboratory . ");
            queryStr.append(" } ");
        }

        if (filter.containsKey("Department")) {
            String value = filter.get("Department");
            String level = "";
            switch (value) {
                case "Cardiology":
                    level = "Cardiology";
                    break;
                case "Dental":
                    level = "Dental";
                    break;
                case "ER":
                    level = "ER";
                    break;
                case "ICU":
                    level = "ICU";
                    break;
                case "OPD":
                    level = "OPD";
                    break;
                case "Paediatric":
                    level = "Paediatric";
                    break;
                case "Physiotherapy":
                    level = "Physiotherapy";
                    break;
                case "Surgery":
                    level = "Surgery";
                    break;
            }

            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :" + level + " . ");
            queryStr.append(" } UNION");
            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :Department . ");
            queryStr.append(" } ");
        }

        if (filter.containsKey("TreatmentType")) {
            String value = filter.get("TreatmentType");
            String level = "";
            switch (value) {
                case "Consultation":
                    level = "Consultation";
                    break;
                case "Inpatient":
                    level = "Inpatient";
                    break;
                case "Surgical":
                    level = "Surgical";
                    break;
            }

            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :" + level + " . ");
            queryStr.append(" } UNION");
            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :Treatment . ");
            queryStr.append(" } ");
            queryStr.append(" UNION");
            queryStr.append(" { ");
            queryStr.append("  ?hospital rdf:type :Event . ");
            queryStr.append(" } ");
        }
    }
}
