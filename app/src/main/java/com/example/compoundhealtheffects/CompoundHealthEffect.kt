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
    val state: String?,
    @SerialName("annotation_quality") val annotationQuality: String,
    val description: String,
    @SerialName("cas_number") val casNumber: String,
    @SerialName("moldb_smiles") val moldbSmiles: String,
    @SerialName("moldb_inchi") val moldbInchi: String,
    @SerialName("moldb_mono_mass") val moldbMonoMass: String,
    @SerialName("moldb_inchikey") val moldbInchikey: String,
    @SerialName("moldb_iupac") val moldbIupac: String,
    val kingdom: String?,
    val superklass: String?,
    val klass: String?,
    val subklass: String?
)

@Serializable
class HealthEffect(
    val id: Int,
    val name: String,
    val description: String?,
    @SerialName("chebi_name") val chebiName: String?,
    @SerialName("chebi_id") val chebiId: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("creator_id") val creatorId: String?,
    @SerialName("updater_id") val updaterId: String?,
    @SerialName("chebi_definition") val chebiDefinition: String?
)