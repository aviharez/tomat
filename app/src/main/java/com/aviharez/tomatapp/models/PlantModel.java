package com.aviharez.tomatapp.models;

/**
 * Created by Admin on 12/5/2017.
 */

public class PlantModel {
    private String plantId;
    private String plantDesc;

    public PlantModel(){}

    public PlantModel(String plantId, String plantDesc){
        this.plantId = plantId;
        this.plantDesc = plantDesc;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getPlantDesc() {
        return plantDesc;
    }

    public void setPlantDesc(String plantDesc) {
        this.plantDesc = plantDesc;
    }

}
