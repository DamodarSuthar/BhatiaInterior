package com.example.data

data class ProjectItem(
    val id: String,
    val title: String,
    val category: String, // "Residential", "Commercial", "Custom Furniture", "Renovations"
    val roomType: String, // "Living Room", "Bedroom", "Office", "Kitchen"
    val style: String, // "Modern Minimalist", "Rustic Modern", "Luxury Contemporary"
    val sizeSqFt: Int,
    val description: String,
    val beforeState: String,
    val afterState: String,
    val materials: List<String>,
    val timelineWeeks: Int,
    val location: String, // e.g., "Bandra West", "Juhu", "Colaba", "Lower Parel"
    val colorPrimaryHex: String, // To style beautiful placeholders dynamically
    val colorSecondaryHex: String
)

data class MaterialSwatch(
    val id: String,
    val name: String,
    val category: String, // "Woods", "Fabrics", "Laminates", "Paints"
    val description: String,
    val colorHex: String, // Hex color for the live swatch preview fallback
    val imageUrl: String, // Stock image URL representing the material
    val estimatedPriceUnit: String, // e.g., "₹350/sq.ft"
    val details: String
)

data class ServiceCard(
    val id: String,
    val title: String,
    val description: String,
    val details: List<String>,
    val duration: String
)

data class BlogArticle(
    val id: String,
    val title: String,
    val summary: String,
    val readTime: String,
    val date: String,
    val body: String
)
