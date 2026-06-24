package com.example.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class AppRepository(private val context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val dao = database.appDao()

    // Room Database CRUD
    fun getAllSavedDesigns(): Flow<List<SavedDesign>> = dao.getAllSavedDesigns()
    suspend fun insertSavedDesign(design: SavedDesign) = dao.insertSavedDesign(design)
    suspend fun deleteSavedDesign(design: SavedDesign) = dao.deleteSavedDesign(design)

    fun getAllInquiries(): Flow<List<Inquiry>> = dao.getAllInquiries()
    suspend fun insertInquiry(inquiry: Inquiry) = dao.insertInquiry(inquiry)

    fun getProjectUpdates(): Flow<List<ProjectUpdate>> = dao.getProjectUpdates()
    suspend fun insertProjectUpdate(update: ProjectUpdate) = dao.insertProjectUpdate(update)

    fun getChatMessages(): Flow<List<ChatMessage>> = dao.getChatMessages()
    suspend fun insertChatMessage(message: ChatMessage) = dao.insertChatMessage(message)

    // Seed initial project tracker state if database is empty
    suspend fun seedDatabaseIfEmpty() {
        val existingUpdates = dao.getProjectUpdates().firstOrNull()
        if (existingUpdates.isNullOrEmpty()) {
            dao.insertProjectUpdate(
                ProjectUpdate(
                    projectTitle = "Bandra Sea-Facing Apartment Renovations",
                    statusMessage = "Teak wardrobe structures completed. Polishing begins today under master carpenter Lalit.",
                    progressPercent = 75,
                    currentStepName = "Carpentry Works",
                    totalSteps = 6,
                    currentStepIndex = 4,
                    clientName = "Damodar Suthar",
                    siteAddress = "Bandra West, Mumbai",
                    designerName = "Vinod Suthar",
                    estimatedHandover = "Aug 15, 2026"
                )
            )
            dao.insertChatMessage(ChatMessage(message = "Welcome to Bhatia Interior, Damodar! I am Vinod Bhatia. Tracking details for your Bandra home are live here.", isFromUser = false, timestamp = System.currentTimeMillis() - 86400000))
            dao.insertChatMessage(ChatMessage(message = "Thank you, Vinod. The material choices look incredible.", isFromUser = true, timestamp = System.currentTimeMillis() - 43200000))
            dao.insertChatMessage(ChatMessage(message = "Our carpentry team has started shaping the master bedroom bed frame today. Standard updates will post daily.", isFromUser = false, timestamp = System.currentTimeMillis() - 100000))
        }
    }

    // Static Showroom Datasets
    val projects = listOf(
        ProjectItem(
            id = "proj_bandra",
            title = "Bandra Contemporary Penthouse",
            category = "Residential",
            roomType = "Living Room",
            style = "Modern Minimalist",
            sizeSqFt = 2400,
            description = "A gorgeous transformation of a high-rise flat in Bandra overlooking the Arabian Sea. Focused on heavy negative space, warm organic textures, and custom concealed cabinets that hide clutter.",
            beforeState = "Cluttered dark 3BHK with traditional partition walls and outdated heavy mahogany woodwork blocking light.",
            afterState = "Sun-drenched, open-plan sanctuary wrapped in light oak fluted panels and limestone floors.",
            materials = listOf("European White Oak", "Travertine Limestone", "Waterproof Marine Ply", "Brushed Brass Finishes"),
            timelineWeeks = 16,
            location = "Bandra West, Mumbai",
            colorPrimaryHex = "F4EFE6",
            colorSecondaryHex = "9E845A"
        ),
        ProjectItem(
            id = "proj_juhu",
            title = "Juhu Beachside Minimal Bedroom",
            category = "Residential",
            roomType = "Bedroom",
            style = "Modern Minimalist",
            sizeSqFt = 450,
            description = "Designed for a serene beachside feel, this bedroom features a custom floating bed frame made of solid Teak, paired with a floor-to-ceiling wardrobe with integrated touch-to-open latch hardware.",
            beforeState = "Standard rectangular room with poor storage and a bulky steel bed taking up 60% of the walking area.",
            afterState = "Airy minimalist layout with smart wall-mounted storage and floating bedside tables in natural polish.",
            materials = listOf("Burmese Teak Wood", "Linen Wall Upholstery", "Charcoal Matte Laminate", "Warm LED Cove Lights"),
            timelineWeeks = 6,
            location = "Juhu, Mumbai",
            colorPrimaryHex = "EFECE6",
            colorSecondaryHex = "8B7355"
        ),
        ProjectItem(
            id = "proj_colaba",
            title = "Colaba Heritage Design Studio Loft",
            category = "Commercial",
            roomType = "Office",
            style = "Rustic Modern",
            sizeSqFt = 1800,
            description = "A commercial office design within a historic 100-year-old high-ceiling brick warehouse. Restored original brick walls and integrated large metal-framed glass dividers and a custom solid teak conference deck.",
            beforeState = "Damp, abandoned storage attic with crumbling plaster and old non-functional industrial pipes.",
            afterState = "Inspiring double-height workspace featuring custom oak desks, architectural black metalwork, and cozy lounge nooks.",
            materials = listOf("Reclaimed Teak Deck", "Mild Steel Sections", "Exposed Red Bricks", "Raw Micro-cement"),
            timelineWeeks = 12,
            location = "Colaba, Mumbai",
            colorPrimaryHex = "F5EBE6",
            colorSecondaryHex = "151515"
        ),
        ProjectItem(
            id = "proj_parel",
            title = "Lower Parel Luxury Corporate Lounge",
            category = "Commercial",
            roomType = "Office",
            style = "Luxury Contemporary",
            sizeSqFt = 1200,
            description = "A premium corporate greeting workspace inside a modern high-rise glass building. Focuses on premium luxury lounge furniture, high-end acoustic wood ceilings, and stone counters.",
            beforeState = "Uninspired cold glass corporate cabin with plain white drywall and blue tiles.",
            afterState = "Warm, high-contrast reception oasis featuring Italian marble clad desk, curved wool sofas, and fluted panel acoustic backdrops.",
            materials = listOf("Carrara Marble", "Acoustic Walnut Slats", "Bouclé Wool Upholstery", "Anodized Black Aluminum"),
            timelineWeeks = 8,
            location = "Lower Parel, Mumbai",
            colorPrimaryHex = "ECECEC",
            colorSecondaryHex = "151515"
        )
    )

    val materialSwatches = listOf(
        MaterialSwatch(
            id = "mat_burmese_teak",
            name = "Premium Burmese Teak",
            category = "Woods",
            description = "Rich golden-brown hardwood with beautiful straight grains. Famous for durability and water-resistance.",
            colorHex = "C49A5F",
            imageUrl = "https://images.unsplash.com/photo-1541123437800-1bb1317badc2?w=400&auto=format&fit=crop&q=80",
            estimatedPriceUnit = "₹850 / sqft",
            details = "Perfect for heavy furniture, structural partitions, and outdoor decks. Handpicked from direct timber imports."
        ),
        MaterialSwatch(
            id = "mat_american_oak",
            name = "White American Oak",
            category = "Woods",
            description = "Elegant pale-colored grain with outstanding strength. Excellent for modern Scandinavian and Japandi styles.",
            colorHex = "E5D5C1",
            imageUrl = "https://images.unsplash.com/photo-1533090161767-e6ffed986c88?w=400&auto=format&fit=crop&q=80",
            estimatedPriceUnit = "₹720 / sqft",
            details = "Our most requested choice for custom living room cabinetry, paneling, and floating shelves."
        ),
        MaterialSwatch(
            id = "mat_marine_ply",
            name = "Waterproof Marine Ply (IS:710)",
            category = "Woods",
            description = "Boiling water resistant ply designed to withstand Mumbai's heavy high-humidity coastal monsoon climate.",
            colorHex = "A0522D",
            imageUrl = "https://images.unsplash.com/photo-1626806787461-102c1bfaaea1?w=400&auto=format&fit=crop&q=80",
            estimatedPriceUnit = "₹160 / sqft",
            details = "Core structure of all modular kitchens, bathroom vanities, and base cabinet frames."
        ),
        MaterialSwatch(
            id = "mat_linen_boucle",
            name = "Textured Linen Bouclé",
            category = "Fabrics",
            description = "Sophisticated cream upholstery fabric made of looped wool and organic linen for an ultra-premium feel.",
            colorHex = "F5EFE6",
            imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=400&auto=format&fit=crop&q=80",
            estimatedPriceUnit = "₹1,200 / meter",
            details = "Superb touch-texture choice for living room accent chairs, headboard padding, and custom pillows."
        ),
        MaterialSwatch(
            id = "mat_fluted_laminate",
            name = "Charcoal Fluted Laminate",
            category = "Laminates",
            description = "Modern matte acrylic fluted texture laminate sheet for dramatic 3D accent wall designs.",
            colorHex = "2E2E2E",
            imageUrl = "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=400&auto=format&fit=crop&q=80",
            estimatedPriceUnit = "₹250 / sqft",
            details = "Zero maintenance, scratch-resistant sheets ideal for TV backdrops and wardrobe shutters."
        ),
        MaterialSwatch(
            id = "mat_velvet_sheen",
            name = "Velvet Sheen Luxury Paint",
            category = "Paints",
            description = "Premium zero-VOC emulsion with a subtle velvet finish that diffuses warm lighting softly.",
            colorHex = "EFECE1",
            imageUrl = "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=400&auto=format&fit=crop&q=80",
            estimatedPriceUnit = "₹45 / sqft",
            details = "Creates smooth, clean luxury walls that are completely washable and scrub-resistant."
        )
    )

    val services = listOf(
        ServiceCard(
            id = "srv_residential",
            title = "Residential Interior Design",
            description = "Turn-key luxury homes custom designed to suit your daily lifestyle. Includes layouts, 3D renderings, and end-to-end execution.",
            details = listOf("Custom Modular Kitchens", "Modern Wardrobes & Storage", "Premium false ceiling and dynamic lighting layouts", "Monsoon-safe woodwork selection"),
            duration = "12 - 16 Weeks"
        ),
        ServiceCard(
            id = "srv_commercial",
            title = "Commercial & Office Spaces",
            description = "Acoustically optimized, ergonomic and inspiring offices built to maximize modern workflow efficiency.",
            details = listOf("Executive Conference Cabins", "Smart collaborative workspaces", "Custom lobby reception desks", "Glass partition and electrical planning"),
            duration = "8 - 12 Weeks"
        ),
        ServiceCard(
            id = "srv_carpentry",
            title = "Bespoke Carpentry Works",
            description = "Our legendary heritage team manufactures bespoke premium beds, dining sets, cabinets, and doors in-house.",
            details = listOf("Solid Teak Wood furniture", "Soft-close Blum/Hettich hardware integration", "Premium veneer book-matching", "Handmade dining structures"),
            duration = "4 - 8 Weeks"
        ),
        ServiceCard(
            id = "srv_renovation",
            title = "Structural Renovations",
            description = "Full home structural transformations, waterproofing, tiling, and wall rearrangements.",
            details = listOf("Anti-leak monsoon waterproofing", "Premium marble & tile laying", "Internal brick walls reconfiguration", "Plumbing and electrical rewiring"),
            duration = "6 - 10 Weeks"
        )
    )

    val articles = listOf(
        BlogArticle(
            id = "art_1",
            title = "Surviving the Monsoon: Best Waterproof Materials for Mumbai Homes",
            summary = "Mumbai's legendary rains demand strict material choices. Learn why Marine Ply IS:710 and proper Edge-Banding are non-negotiable.",
            readTime = "4 min read",
            date = "June 18, 2026",
            body = "In coastal cities like Mumbai, humidity rises above 90% during monsoon months. Traditional commercial ply absorbs water and swells, causing laminates to peel off and cabinets to warp. At Bhatia Interior, we enforce a strict IS:710 Marine Ply-only core rule for all kitchens and bathroom spaces. Additionally, using polyurethane-based adhesive (PUR) edge banding prevents moisture from seeping into raw board edges."
        ),
        BlogArticle(
            id = "art_2",
            title = "Maximizing Space: Smart Modular Designs for Mumbai Apartments",
            summary = "1BHK and 2BHK apartments require architectural cleverness. Discover folding beds, hidden sliding panels, and height-extending loft wardobes.",
            readTime = "5 min read",
            date = "May 29, 2026",
            body = "With sky-high real estate prices in South Mumbai and Suburbs, maximizing every square inch of your apartment is vital. Custom wardrobes with hydraulic lift lofts provide storage up to the slab level. Multi-functional furniture like wall-mounted study desks that fold into sleek mirror frames and Murphy beds help turn guest rooms into work studios during the day."
        )
    )
}
