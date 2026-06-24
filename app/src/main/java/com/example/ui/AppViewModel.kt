package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    val repository = AppRepository(application)

    // Language state: "EN" (English) or "HI" (Hindi)
    private val _selectedLanguage = MutableStateFlow("EN")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    // Theme state: true for Dark, false for Light
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Wishlist of project IDs
    private val _wishlistIds = MutableStateFlow<Set<String>>(emptySet())
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    // Database Flows
    val savedDesigns: StateFlow<List<SavedDesign>> = repository.getAllSavedDesigns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inquiries: StateFlow<List<Inquiry>> = repository.getAllInquiries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projectUpdates: StateFlow<List<ProjectUpdate>> = repository.getProjectUpdates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.getChatMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Simulated typing state for chat
    private val _isTeamTyping = MutableStateFlow(false)
    val isTeamTyping: StateFlow<Boolean> = _isTeamTyping.asStateFlow()

    init {
        viewModelScope.launch {
            // Seed sample data for the My Projects/Chat dashboard if db is empty
            repository.seedDatabaseIfEmpty()
        }
    }

    // Actions
    fun toggleLanguage() {
        _selectedLanguage.value = if (_selectedLanguage.value == "EN") "HI" else "EN"
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun toggleWishlistProject(projectId: String) {
        val current = _wishlistIds.value
        _wishlistIds.value = if (current.contains(projectId)) {
            current - projectId
        } else {
            current + projectId
        }
    }

    fun saveDesign(
        furnitureType: String,
        woodType: String,
        finishType: String,
        dimensions: String,
        hardwareType: String,
        estimatedPrice: Double
    ) {
        viewModelScope.launch {
            repository.insertSavedDesign(
                SavedDesign(
                    furnitureType = furnitureType,
                    woodType = woodType,
                    finishType = finishType,
                    dimensions = dimensions,
                    hardwareType = hardwareType,
                    estimatedPrice = estimatedPrice
                )
            )
        }
    }

    fun deleteDesign(design: SavedDesign) {
        viewModelScope.launch {
            repository.deleteSavedDesign(design)
        }
    }

    fun submitInquiry(
        spaceType: String,
        requirements: String,
        budgetRange: String,
        clientName: String,
        clientPhone: String
    ) {
        viewModelScope.launch {
            repository.insertInquiry(
                Inquiry(
                    spaceType = spaceType,
                    requirements = requirements,
                    budgetRange = budgetRange,
                    clientName = clientName,
                    clientPhone = clientPhone
                )
            )
            // Trigger automatic acknowledgment message in chat after submission
            delay(1000)
            sendAutomatedResponse("Hello $clientName! We received your quote inquiry for a '$spaceType' with budget '$budgetRange'. Our lead designer, Vinod Bhatia, is reviewing your details and will call you on $clientPhone today.")
        }
    }

    fun sendUserMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.insertChatMessage(ChatMessage(message = text, isFromUser = true))
            generateAutoResponse(text)
        }
    }

    private fun sendAutomatedResponse(text: String) {
        viewModelScope.launch {
            _isTeamTyping.value = true
            delay(1500)
            repository.insertChatMessage(ChatMessage(message = text, isFromUser = false))
            _isTeamTyping.value = false
        }
    }

    private fun generateAutoResponse(userText: String) {
        val query = userText.lowercase()
        val reply = when {
            query.contains("hello") || query.contains("hi") || query.contains("hey") -> {
                "Hi there! Thanks for reaching out to Bhatia Interior. I am Vinod. How can we help you plan your Mumbai home today?"
            }
            query.contains("price") || query.contains("cost") || query.contains("rate") || query.contains("budget") -> {
                "Our custom woodwork starts from ₹450/sqft for standard laminates, going up to ₹850/sqft for premium Burmese Teak. Would you like us to schedule a free site survey?"
            }
            query.contains("wood") || query.contains("material") || query.contains("teak") || query.contains("ply") -> {
                "We use 100% genuine IS:710 Marine Waterproof Ply for structures to protect your furniture from Mumbai monsoons. Veneers and solid Teak are hand-polished by our craftsmen."
            }
            query.contains("location") || query.contains("office") || query.contains("where") || query.contains("mumbai") -> {
                "Our main experience showroom is located on Linking Road, Bandra West, Mumbai. Drop by any time between 10 AM to 8 PM!"
            }
            query.contains("progress") || query.contains("status") || query.contains("update") || query.contains("my project") -> {
                "Your ongoing project 'Bandra Sea-Facing Apartment' is currently at 75% progress (Carpentry Stage). We are on track for handover by Aug 15, 2026."
            }
            query.contains("book") || query.contains("appointment") || query.contains("visit") || query.contains("schedule") -> {
                "Perfect! Please share your address or preferred time, or tap 'Book Appointment' in our Contact tab. We'll send an experienced surveyor."
            }
            else -> {
                "Thank you for your message! Our team on Linking Road, Bandra is active. We are reviewing your request and will get back to you shortly."
            }
        }
        sendAutomatedResponse(reply)
    }

    // Helper translation string keys for multi-language dictionary (EN/HI)
    fun translate(key: String): String {
        val isEn = _selectedLanguage.value == "EN"
        return if (isEn) {
            when (key) {
                "app_title" -> "Bhatia Interior"
                "app_subtitle" -> "Mumbai's Premium Custom Woodwork & Full Interiors"
                "nav_home" -> "Home"
                "nav_browse" -> "Browse"
                "nav_customize" -> "Customize"
                "nav_projects" -> "My Projects"
                "nav_account" -> "Contact"
                "categories" -> "Categories"
                "residential" -> "Residential"
                "commercial" -> "Commercial"
                "furniture" -> "Custom Furniture"
                "renovations" -> "Renovations"
                "recent_projects" -> "Recent Mumbai Projects"
                "view_gallery" -> "View Full Gallery"
                "materials_library" -> "Materials & Swatches"
                "about_team" -> "Our Legacy"
                "get_quote" -> "Get Quick Quote"
                "ar_preview" -> "Interactive AR Placer"
                "wood" -> "Wood Type"
                "finish" -> "Finish Style"
                "dimensions" -> "Custom Dimensions"
                "hardware" -> "Hardware Latch"
                "estimate" -> "Estimated Price"
                "save_design" -> "Save Custom Design"
                "saved_success" -> "Design Saved to Wishlist!"
                "requirement_prompt" -> "Tell us about your space and requirements..."
                "full_name" -> "Your Full Name"
                "phone" -> "Phone Number"
                "budget" -> "Select Budget"
                "submit" -> "Submit Inquiry"
                "chat_hint" -> "Type your message to Vinod Bhatia..."
                "active_job" -> "Active Project Progress"
                "invoice" -> "Invoice & Estimates"
                "contact_info" -> "Contact Showroom"
                "address" -> "Address: Linking Road, Bandra West, Mumbai"
                "whatsapp" -> "WhatsApp Chat"
                "call_now" -> "Call Vinod Bhatia"
                "book_site" -> "Book Showroom Site Visit"
                "mumbai_homes" -> "Mumbai Home Tips & Guides"
                "materials_header" -> "Showroom Material Swatches"
                else -> key
            }
        } else {
            when (key) {
                "app_title" -> "भाटिया इंटीरियर"
                "app_subtitle" -> "मुंबई का प्रीमियम कस्टम वुडवर्क और पूर्ण इंटीरियर"
                "nav_home" -> "होम"
                "nav_browse" -> "गैलरी"
                "nav_customize" -> "कस्टमाइज़"
                "nav_projects" -> "मेरे प्रोजेक्ट"
                "nav_account" -> "संपर्क करें"
                "categories" -> "श्रेणियां"
                "residential" -> "आवासीय"
                "commercial" -> "व्यावसायिक"
                "furniture" -> "कस्टम फर्नीचर"
                "renovations" -> "नवीनीकरण"
                "recent_projects" -> "हालिया मुंबई प्रोजेक्ट्स"
                "view_gallery" -> "पूरी गैलरी देखें"
                "materials_library" -> "सामग्री और नमूने"
                "about_team" -> "हमारी विरासत"
                "get_quote" -> "त्वरित कोट प्राप्त करें"
                "ar_preview" -> "इंटरैक्टिव एआर प्लेसर"
                "wood" -> "लकड़ी का प्रकार"
                "finish" -> "फिनिश स्टाइल"
                "dimensions" -> "कस्टम आयाम"
                "hardware" -> "हार्डवेयर लैच"
                "estimate" -> "अनुमानित मूल्य"
                "save_design" -> "डिज़ाइन सहेजें"
                "saved_success" -> "डिज़ाइन विशलिस्ट में सहेजा गया!"
                "requirement_prompt" -> "हमें अपनी जगह और आवश्यकताओं के बारे में बताएं..."
                "full_name" -> "आपका पूरा नाम"
                "phone" -> "फ़ोन नंबर"
                "budget" -> "बजट चुनें"
                "submit" -> "पूछताछ भेजें"
                "chat_hint" -> "विकास भाटिया को संदेश लिखें..."
                "active_job" -> "सक्रिय प्रोजेक्ट प्रगति"
                "invoice" -> "चालान और अनुमान"
                "contact_info" -> "शोरूम से संपर्क करें"
                "address" -> "पता: लिंकिंग रोड, बांद्रा वेस्ट, मुंबई"
                "whatsapp" -> "व्हाट्सएप चैट"
                "call_now" -> "विकास भाटिया को कॉल करें"
                "book_site" -> "साइट विज़िट बुक करें"
                "mumbai_homes" -> "मुंबई होम टिप्स और गाइड"
                "materials_header" -> "शोरूम सामग्री नमूने"
                else -> key
            }
        }
    }
}
