package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MaterialSwatch
import com.example.data.ProjectItem
import com.example.ui.AppViewModel
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    viewModel: AppViewModel,
    selectedProjectFromHome: ProjectItem?,
    onClearSelectedProject: () -> Unit,
    onSelectProject: (ProjectItem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedRoomType by remember { mutableStateOf("All") }
    var selectedStyle by remember { mutableStateOf("All") }
    
    // Bottom sheet details trigger
    var detailedProject by remember { mutableStateOf<ProjectItem?>(null) }
    
    // Sync with project passed from home screen
    LaunchedEffect(selectedProjectFromHome) {
        if (selectedProjectFromHome != null) {
            detailedProject = selectedProjectFromHome
            onClearSelectedProject()
        }
    }

    val isDark by viewModel.isDarkTheme.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    
    val textPrimary = if (isDark) Color.White else Color(0xFF151515)
    val textSecondary = if (isDark) Color(0xFFB0B0B0) else Color(0xFF707070)
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    
    var activeSectionTab by remember { mutableStateOf(0) } // 0 for Projects, 1 for Material Swatches

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        // Mode selector (Projects vs Materials)
        TabRow(
            selectedTabIndex = activeSectionTab,
            containerColor = if (isDark) Color(0xFF151515) else Color(0xFFF9F6F0),
            contentColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeSectionTab]),
                    color = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355)
                )
            }
        ) {
            Tab(
                selected = activeSectionTab == 0,
                onClick = { activeSectionTab = 0 },
                text = { Text("Mumbai Portfolio", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
            )
            Tab(
                selected = activeSectionTab == 1,
                onClick = { activeSectionTab = 1 },
                text = { Text(viewModel.translate("materials_header"), fontWeight = FontWeight.Bold, fontSize = 14.sp) }
            )
        }

        if (activeSectionTab == 0) {
            // Portfolio Search & Filters List
            Column(modifier = Modifier.padding(16.dp)) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search location, room, materials...", fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("portfolio_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
                        unfocusedBorderColor = textSecondary.copy(alpha = 0.2f),
                        focusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF0EBE0),
                        unfocusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF0EBE0)
                    ),
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear")
                            }
                        }
                    } else null,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable filters
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Category Filters
                    item {
                        FilterDropdown(
                            label = "Category: $selectedCategory",
                            options = listOf("All", "Residential", "Commercial", "Renovations"),
                            selected = selectedCategory,
                            onSelect = { selectedCategory = it }
                        )
                    }
                    // Room Type Filters
                    item {
                        FilterDropdown(
                            label = "Room: $selectedRoomType",
                            options = listOf("All", "Living Room", "Bedroom", "Office", "Kitchen"),
                            selected = selectedRoomType,
                            onSelect = { selectedRoomType = it }
                        )
                    }
                    // Style Filters
                    item {
                        FilterDropdown(
                            label = "Style: $selectedStyle",
                            options = listOf("All", "Modern Minimalist", "Rustic Modern", "Luxury Contemporary"),
                            selected = selectedStyle,
                            onSelect = { selectedStyle = it }
                        )
                    }
                }
            }

            // Projects List
            val filteredProjects = viewModel.repository.projects.filter { proj ->
                (selectedCategory == "All" || proj.category == selectedCategory) &&
                (selectedRoomType == "All" || proj.roomType == selectedRoomType) &&
                (selectedStyle == "All" || proj.style == selectedStyle) &&
                (searchQuery.isEmpty() || 
                 proj.title.contains(searchQuery, ignoreCase = true) ||
                 proj.location.contains(searchQuery, ignoreCase = true) ||
                 proj.description.contains(searchQuery, ignoreCase = true) ||
                 proj.materials.any { it.contains(searchQuery, ignoreCase = true) })
            }

            if (filteredProjects.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.ContentPasteOff,
                            contentDescription = "No project found",
                            tint = textSecondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No matching Mumbai projects found.", color = textSecondary)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredProjects) { proj ->
                        val isSaved = wishlistIds.contains(proj.id)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { detailedProject = proj }
                                .testTag("portfolio_item_${proj.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = surfaceColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column {
                                // Draw architectural schematic thumbnail
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .background(Color(android.graphics.Color.parseColor("#" + proj.colorPrimaryHex)))
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val w = size.width
                                        val h = size.height
                                        // Draw architectural grid layout background
                                        for (x in 0..w.toInt() step 50) {
                                            drawLine(Color.Black.copy(alpha = 0.03f), Offset(x.toFloat(), 0f), Offset(x.toFloat(), h), 1f)
                                        }
                                        for (y in 0..h.toInt() step 50) {
                                            drawLine(Color.Black.copy(alpha = 0.03f), Offset(0f, y.toFloat()), Offset(w, y.toFloat()), 1f)
                                        }
                                        // Draw simple elevation outline of cabinet / bed
                                        drawRect(
                                            color = Color(android.graphics.Color.parseColor("#" + proj.colorSecondaryHex)).copy(alpha = 0.3f),
                                            topLeft = Offset(w * 0.2f, h * 0.4f),
                                            size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.45f)
                                        )
                                        drawRect(
                                            color = Color.Black.copy(alpha = 0.2f),
                                            topLeft = Offset(w * 0.2f, h * 0.4f),
                                            size = androidx.compose.ui.geometry.Size(w * 0.6f, h * 0.45f),
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                                        )
                                        // Split line to represent doors
                                        drawLine(
                                            color = Color.Black.copy(alpha = 0.2f),
                                            start = Offset(w * 0.5f, h * 0.4f),
                                            end = Offset(w * 0.5f, h * 0.85f),
                                            strokeWidth = 2.5f
                                        )
                                        // Round handles
                                        drawCircle(Color.Black.copy(alpha = 0.6f), radius = 5f, center = Offset(w * 0.48f, h * 0.62f))
                                        drawCircle(Color.Black.copy(alpha = 0.6f), radius = 5f, center = Offset(w * 0.52f, h * 0.62f))
                                    }
                                    
                                    // Bookmark/Wishlist toggle button
                                    IconButton(
                                        onClick = { viewModel.toggleWishlistProject(proj.id) },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(12.dp)
                                            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                    ) {
                                        Icon(
                                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                            contentDescription = "Save project",
                                            tint = if (isSaved) Color(0xFFEEDC82) else Color.White
                                        )
                                    }

                                    Text(
                                        text = "${proj.sizeSqFt} sqft • ${proj.style}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(12.dp)
                                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = proj.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = proj.location,
                                        fontSize = 12.sp,
                                        color = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = proj.description,
                                        fontSize = 13.sp,
                                        color = textSecondary,
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        proj.materials.take(3).forEach { mat ->
                                            Box(
                                                modifier = Modifier
                                                    .background(textSecondary.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                            ) {
                                                Text(mat, fontSize = 10.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Materials Library with swatches + 360° Rotator Tool
            var selectedSwatchFor360 by remember { mutableStateOf(viewModel.repository.materialSwatches.first()) }
            var swatchRotationAngle by remember { mutableStateOf(180f) }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Interactive 360° visual swatch viewer
                item {
                    Text(
                        text = "360° Material Grain Viewer",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Drag slider to rotate texture grain and inspect glossy sheen under dynamic light.",
                        fontSize = 12.sp,
                        color = textSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = surfaceColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(1.dp, textSecondary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Custom Box representing rotated material with grains and shiny highlights using stock images
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(100.dp))
                                    .border(2.dp, if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355), RoundedCornerShape(100.dp))
                                    .background(if (isDark) Color(0xFF151515) else Color(0xFFEFECE6)),
                                contentAlignment = Alignment.Center
                            ) {
                                // Load the actual material stock image
                                coil.compose.AsyncImage(
                                    model = selectedSwatchFor360.imageUrl,
                                    contentDescription = selectedSwatchFor360.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val r = size.minDimension / 2.5f
                                    val cx = size.width / 2f
                                    val cy = size.height / 2f

                                    // Draw texture grains reflecting rotation angle
                                    val rad = Math.toRadians(swatchRotationAngle.toDouble())
                                    val grainSpacing = 22f
                                    val grainAlpha = 0.2f
                                    
                                    for (i in -4..4) {
                                        val offset = i * grainSpacing
                                        // Vector math to curve grains on the sphere
                                        val x1 = cx + offset * cos(rad).toFloat() - (r * 0.5f * sin(rad).toFloat())
                                        val y1 = cy + offset * sin(rad).toFloat() + (r * 0.5f * cos(rad).toFloat())
                                        val x2 = cx + offset * cos(rad).toFloat() + (r * 0.5f * sin(rad).toFloat())
                                        val y2 = cy + offset * sin(rad).toFloat() - (r * 0.5f * cos(rad).toFloat())
                                        
                                        // Clip lines within sphere boundaries
                                        drawLine(
                                            color = Color.Black.copy(alpha = grainAlpha),
                                            start = Offset(x1, y1),
                                            end = Offset(x2, y2),
                                            strokeWidth = 3f
                                        )
                                    }

                                    // Light highlight source (fixed at top-left to simulate 3D volume gloss reflection)
                                    drawCircle(
                                        color = Color.White.copy(alpha = 0.45f),
                                        radius = r * 0.35f,
                                        center = Offset(cx - r * 0.4f, cy - r * 0.4f)
                                    )
                                    drawCircle(
                                        color = Color.White.copy(alpha = 0.8f),
                                        radius = r * 0.12f,
                                        center = Offset(cx - r * 0.45f, cy - r * 0.45f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Selected Material Meta Info
                            Text(
                                text = selectedSwatchFor360.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Showroom Code: BI-${selectedSwatchFor360.id.uppercase()} • ${selectedSwatchFor360.estimatedPriceUnit}",
                                fontSize = 12.sp,
                                color = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Angle slider
                            Slider(
                                value = swatchRotationAngle,
                                onValueChange = { swatchRotationAngle = it },
                                valueRange = 0f..360f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("material_rotation_slider"),
                                colors = SliderDefaults.colors(
                                    thumbColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
                                    activeTrackColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355)
                                )
                            )
                            Text(
                                text = "Rotation Angle: ${swatchRotationAngle.toInt()}°",
                                fontSize = 11.sp,
                                color = textSecondary
                            )
                        }
                    }
                }
                           // Swatch Catalog Grid
                item {
                    Text(
                        text = "Browse Swatch Library",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Chunk swatches dynamically into responsive grid rows depending on available width!
                item {
                    val swatches = viewModel.repository.materialSwatches
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        val availableWidth = maxWidth
                        val columnCount = when {
                            availableWidth > 800.dp -> 3
                            availableWidth > 480.dp -> 2
                            else -> 1
                        }
                        
                        val chunkedSwatches = swatches.chunked(columnCount)
                        
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            chunkedSwatches.forEach { rowSwatches ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowSwatches.forEach { swatch ->
                                        val isSelected = selectedSwatchFor360.id == swatch.id
                                        Card(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedSwatchFor360 = swatch }
                                                .testTag("catalog_swatch_${swatch.id}"),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSelected) {
                                                    if (isDark) Color(0xFF332E27) else Color(0xFFF4EFE6)
                                                } else surfaceColor
                                            ),
                                            border = if (isSelected) {
                                                BorderStroke(1.dp, if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355))
                                            } else null
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                                                ) {
                                                    coil.compose.AsyncImage(
                                                        model = swatch.imageUrl,
                                                        contentDescription = swatch.name,
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                                        error = androidx.compose.ui.graphics.painter.ColorPainter(Color(android.graphics.Color.parseColor("#" + swatch.colorHex)))
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = swatch.name,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = textPrimary,
                                                        maxLines = 1,
                                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = swatch.description,
                                                        fontSize = 11.sp,
                                                        color = textSecondary,
                                                        maxLines = 1,
                                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "Details: ${swatch.details}",
                                                        fontSize = 10.sp,
                                                        color = textSecondary,
                                                        maxLines = 1,
                                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                                    )
                                                }
                                                Icon(
                                                    imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Filled.ChevronRight,
                                                    contentDescription = "Active Swatch",
                                                    tint = if (isSelected) {
                                                        if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355)
                                                    } else textSecondary.copy(alpha = 0.5f),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                    // Fill up empty spots in row
                                    if (rowSwatches.size < columnCount) {
                                        repeat(columnCount - rowSwatches.size) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Detailed Project Case Study Overlay Dialog
    detailedProject?.let { proj ->
        AlertDialog(
            onDismissRequest = { 
                detailedProject = null 
                onClearSelectedProject()
            },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .padding(16.dp),
            confirmButton = {
                TextButton(
                    onClick = { 
                        detailedProject = null
                        onClearSelectedProject()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355))
                ) {
                    Text("Close Case Study", fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = "Bhatia Case Study", 
                    fontSize = 14.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355)
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = proj.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = proj.location + " • " + proj.style,
                            fontSize = 12.sp,
                            color = textSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Drawn schematic header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(android.graphics.Color.parseColor("#" + proj.colorPrimaryHex)))
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height
                                drawLine(Color.Black.copy(alpha = 0.08f), Offset(w * 0.1f, h * 0.5f), Offset(w * 0.9f, h * 0.5f), 2f)
                                drawCircle(Color.Black.copy(alpha = 0.15f), radius = 35f, center = Offset(w * 0.3f, h * 0.5f))
                                drawCircle(Color.Black.copy(alpha = 0.15f), radius = 25f, center = Offset(w * 0.7f, h * 0.5f))
                            }
                            Text(
                                text = "Architectural Floor Plan Review",
                                color = Color.Black.copy(alpha = 0.4f),
                                fontSize = 10.sp,
                                modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp)
                            )
                        }
                    }

                    item {
                        Text("The Challenge & Brief", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(proj.description, fontSize = 13.sp, color = textSecondary)
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Before state card
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isDark) Color(0xFF2B2B2B) else Color(0xFFF0EBE0)
                                ),
                                shape = RoundedCornerShape(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("BEFORE STATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(proj.beforeState, fontSize = 12.sp, color = textPrimary)
                                }
                            }
                            // After state card
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isDark) Color(0xFF2E332A) else Color(0xFFE2EFE0)
                                ),
                                shape = RoundedCornerShape(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("BHATIA INTERIOR (AFTER)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color(0xFF7CB342) else Color(0xFF43A047))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(proj.afterState, fontSize = 12.sp, color = textPrimary)
                                }
                            }
                        }
                    }

                    item {
                        Text("Showroom Material Specification", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        proj.materials.forEach { mat ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.Verified, 
                                    contentDescription = "Specified", 
                                    tint = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(mat, fontSize = 13.sp, color = textPrimary)
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(textSecondary.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("TIMELINE", fontSize = 10.sp, color = textSecondary)
                                Text("${proj.timelineWeeks} Weeks", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("TOTAL AREA", fontSize = 10.sp, color = textSecondary)
                                Text("${proj.sizeSqFt} SQFT", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("LOCATION", fontSize = 10.sp, color = textSecondary)
                                Text(proj.location.split(",").first(), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            }
                        }
                    }
                }
            }
        )
    }
}

// Custom drop-down filter chip component
@Composable
fun FilterDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        AssistChip(
            onClick = { expanded = true },
            label = { Text(label, fontSize = 12.sp) },
            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expand") }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = {
                        onSelect(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}
