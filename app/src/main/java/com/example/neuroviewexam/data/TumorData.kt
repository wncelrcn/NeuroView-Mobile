package com.example.neuroviewexam.data

import androidx.annotation.DrawableRes

data class TumorType(
    val name: String,
    val shortDescription: String,
    val detailedDescription: String,
    val symptoms: List<String>,
    val treatment: String,
    @DrawableRes val imageResource: Int
)

object TumorData {
    fun getAllTumors(): List<TumorType> = listOf(
        TumorType(
            name = "Glioma",
            shortDescription = "Most common primary brain tumor",
            detailedDescription = """
                Gliomas are tumors that arise from glial cells, which are the supportive cells in the brain. 
                They are the most common type of primary brain tumor, accounting for about 80% of all 
                malignant brain tumors. Gliomas can vary greatly in their aggressiveness and treatment approach.
                
                Gliomas are classified into different grades (I-IV) based on how abnormal the cells look 
                under a microscope and how quickly they are likely to grow and spread.
            """.trimIndent(),
            symptoms = listOf(
                "Persistent headaches",
                "Seizures",
                "Nausea and vomiting",
                "Vision problems",
                "Speech difficulties",
                "Memory problems",
                "Personality changes",
                "Weakness or numbness in limbs"
            ),
            treatment = """
                Treatment typically involves a combination of surgery, radiation therapy, and chemotherapy. 
                The specific treatment plan depends on the tumor's location, size, grade, and the patient's 
                overall health. Complete surgical removal is often challenging due to the tumor's invasive nature.
            """.trimIndent(),
            imageResource = com.example.neuroviewexam.R.drawable.sample_glioma_image
        ),
        TumorType(
            name = "Meningioma",
            shortDescription = "Tumor arising from brain's protective layers",
            detailedDescription = """
                Meningiomas are tumors that develop from the meninges, the protective membranes that 
                surround the brain and spinal cord. They are typically slow-growing and often benign, 
                though some can be atypical or malignant.
                
                Meningiomas are more common in women than men and their incidence increases with age. 
                They account for about 30% of all primary brain tumors.
            """.trimIndent(),
            symptoms = listOf(
                "Gradual onset headaches",
                "Vision changes or loss",
                "Hearing loss or ringing in ears",
                "Memory loss",
                "Seizures (less common)",
                "Weakness in arms or legs",
                "Difficulty with speech",
                "Changes in smell"
            ),
            treatment = """
                Treatment depends on the tumor's size, location, and growth rate. Small, asymptomatic 
                meningiomas may just be monitored. Larger or symptomatic tumors typically require 
                surgical removal. Radiation therapy may be used for tumors that cannot be completely 
                removed or for recurrent tumors.
            """.trimIndent(),
            imageResource = com.example.neuroviewexam.R.drawable.sample_meningioma_image
        ),
        TumorType(
            name = "Pituitary",
            shortDescription = "Tumor in the pituitary gland",
            detailedDescription = """
                Pituitary tumors develop in the pituitary gland, a small pea-sized gland at the base 
                of the brain that controls several other hormone-producing glands. Most pituitary tumors 
                are benign (non-cancerous) adenomas.
                
                These tumors can be functional (producing excess hormones) or non-functional (not 
                producing hormones). The symptoms depend on whether the tumor produces hormones and 
                which hormones are affected.
            """.trimIndent(),
            symptoms = listOf(
                "Vision problems",
                "Headaches",
                "Unexplained weight gain or loss",
                "Fatigue and weakness",
                "Changes in menstrual periods",
                "Sexual dysfunction",
                "Growth abnormalities",
                "Mood changes",
                "Cold intolerance",
                "Excessive urination and thirst"
            ),
            treatment = """
                Treatment options include medication to control hormone levels, surgery to remove the 
                tumor, and radiation therapy. The choice of treatment depends on the type and size of 
                the tumor, hormone levels, and the patient's symptoms. Many pituitary tumors can be 
                successfully treated with medication alone.
            """.trimIndent(),
            imageResource = com.example.neuroviewexam.R.drawable.sample_pituitary_image
        )
    )

    fun getTumorByName(name: String): TumorType? {
        return getAllTumors().find { it.name.equals(name, ignoreCase = true) }
    }
}