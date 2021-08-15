package io.github.townyadvanced.townyresources.objects;

public class CandidateResource {
    
    //The name of the resource material
    private final String resourceMaterialName;  
    
    //The category of the material
    private final String resourceCategory;

    /*
     * The ID of the candidate
     * During resource discovery, a list of all candidate resources is compiled, and a random number is generated.
     * If the random number is OVER this candidateID, and UNDER the candidateID of the next candidate on the list, 
     * this this candidate will win and be selected for discovery.
     */
    private final int candidateID;
    
    public CandidateResource(String resourceMaterialName, String resourceCategory, int candidateID) {
        this.resourceMaterialName = resourceMaterialName;
        this.resourceCategory = resourceCategory;
        this.candidateID = candidateID;
        
    }
      
}
