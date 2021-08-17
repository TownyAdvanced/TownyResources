package io.github.townyadvanced.townyresources.objects;

public class ResourceOffer {

    private final String category;              //Category of the resource
    private final String material;              //Name of the resource material
    private final int baseAmount;               //Base amount of the resource    
    private final int discoveryProbabilityWeight;    //Indicates the probability that this offer will be discovered
    private final int discoveryId;                   //Used during discovery, to identify the offer within the set of all offers

    public ResourceOffer(String category, String material, int baseAmount, int discoveryProbabilityWeight, int discoveryId) {
        this.category = category;
        this.material = material;
        this.baseAmount = baseAmount;
        this.discoveryProbabilityWeight = discoveryProbabilityWeight;
        this.discoveryId = discoveryId;
    }

    public String getCategory() {
        return category;
    }

    public String getMaterial() {
        return material;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public int getDiscoveryProbabilityWeight() {
        return discoveryProbabilityWeight;
    }

    public int getDiscoveryId() {
        return discoveryId;
    }
}
