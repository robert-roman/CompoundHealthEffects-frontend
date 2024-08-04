package com.example.compoundhealtheffects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CompoundHealthEffect(
    val compound: Compound,
    @SerialName("health_effects") val healthEffects: Map<String, HealthEffect>
)

@Serializable
class Compound(
    val id: Int,
    @SerialName("public_id") val publicId: String,
    val name: String,
    val state: String? = "Info Currently Not Avaiable",
    @SerialName("annotation_quality") val annotationQuality: String? = "Info Currently Not Avaiable",
    val description: String? = "Info Currently Not Avaiable",
    @SerialName("cas_number") val casNumber: String? = "Info Currently Not Avaiable",
    @SerialName("moldb_smiles") val moldbSmiles: String? = "Info Currently Not Avaiable",
    @SerialName("moldb_inchi") val moldbInchi: String? = "Info Currently Not Avaiable",
    @SerialName("moldb_mono_mass") val moldbMonoMass: String? = "Info Currently Not Avaiable",
    @SerialName("moldb_inchikey") val moldbInchikey: String? = "Info Currently Not Avaiable",
    @SerialName("moldb_iupac") val moldbIupac: String? = "Info Currently Not Avaiable",
    val kingdom: String? = "Info Currently Not Avaiable",
    val superklass: String? = "Info Currently Not Avaiable",
    val klass: String? = "Info Currently Not Avaiable",
    val subklass: String? = "Info Currently Not Avaiable"
)

@Serializable
class HealthEffect(
    val id: Int,
    val name: String,
    val description: String? = "Info Currently Not Avaiable",
    @SerialName("chebi_name") val chebiName: String? = "Info Currently Not Avaiable",
    @SerialName("chebi_id") val chebiId: String? = "Info Currently Not Avaiable",
    @SerialName("created_at") val createdAt: String? = "Info Currently Not Avaiable",
    @SerialName("updated_at") val updatedAt: String? = "Info Currently Not Avaiable",
    @SerialName("creator_id") val creatorId: String? = "Info Currently Not Avaiable",
    @SerialName("updater_id") val updaterId: String? = "Info Currently Not Avaiable",
    @SerialName("chebi_definition") val chebiDefinition: String? = "Info Currently Not Avaiable"
)