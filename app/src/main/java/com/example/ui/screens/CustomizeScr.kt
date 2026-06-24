package com.example.ui.screens

import android.Manifest
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.example.ui.AppViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(viewModel: AppViewModel) {
    val isDark by viewModel.isDarkTheme.collectAsState()
    val savedDesigns by viewModel.savedDesigns.collectAsState()

    val textPrimary = if (isDark) Color.White else Color(0xFF151515)
    val textSecondary = if (isDark) Color(0xFFB0B0B0) else Color(0xFF707070)
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val accentColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355)

    // Configuration selections
    var selectedFurnitureType by remember { mutableStateOf("Wardrobe") } // Wardrobe, Cabinet, Bed, Table
    var selectedWoodType by remember { mutableStateOf("Burmese Teak") } // Burmese Teak, American Oak, Marine Ply, American Walnut
    var selectedFinishStyle by remember { mutableStateOf("Natural Matte") } // Natural Matte, Satin Polish, Gloss Acrylic, Fluted Veneer
    var selectedHardware by remember { mutableStateOf("Brushed Brass") } // Brushed Brass, Antique Copper, Matte Black, Push-Latch
    
    var widthFt by remember { mutableFloatStateOf(6f) }
    var heightFt by remember { mutableFloatStateOf(7f) }
    var depthFt by remember { mutableFloatStateOf(2f) }

    // AR Simulator toggle
    var showARPreview by remember { mutableStateOf(false) }

    // Live pricing calculations
    val calculatedPrice = remember(selectedFurnitureType, selectedWoodType, selectedFinishStyle, selectedHardware, widthFt, heightFt, depthFt) {
        val basePrice = when (selectedFurnitureType) {
            "Wardrobe" -> 15000.0
            "Cabinet" -> 8000.0
            "Bed" -> 18000.0
            "Table" -> 10000.0
            else -> 5000.0
        }
        
        val woodMultiplier = when (selectedWoodType) {
            "Burmese Teak" -> 2.2
            "American Oak" -> 1.8
            "American Walnut" -> 2.0
            "Marine Ply" -> 1.2
            else -> 1.0
        }

        val finishAdd = when (selectedFinishStyle) {
            "Fluted Veneer" -> 8500.0
            "Gloss Acrylic" -> 5000.0
            "Satin Polish" -> 3000.0
            else -> 0.0
        }

        val hardwareAdd = when (selectedHardware) {
            "Brushed Brass" -> 2500.0
            "Antique Copper" -> 2000.0
            "Matte Black" -> 1200.0
            else -> 0.0
        }

        val volumeMultiplier = (widthFt * heightFt * depthFt) / 36f // Normalized volume factor

        (basePrice * woodMultiplier * volumeMultiplier) + finishAdd + hardwareAdd
    }

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }

    if (showARPreview) {
        // Render AR Simulator View
        ARSimulatorView(
            furnitureType = selectedFurnitureType,
            woodColorHex = when (selectedWoodType) {
                "Burmese Teak" -> "C49A5F"
                "American Oak" -> "E5D5C1"
                "American Walnut" -> "5C4033"
                else -> "A0522D"
            },
            onClose = { showARPreview = false },
            isDark = isDark
        )
    } else {
        // Main Builder Scroll Screen
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp)
        ) {
            // Builder Header Banner (Drawn elevation representation)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(if (isDark) Color(0xFF151515) else Color(0xFFF9F6F0)),
                contentAlignment = Alignment.Center
            ) {
                // Live custom blueprint elevation renderer
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    
                    // Blueprint Grid Lines
                    for (x in 0..w.toInt() step 30) {
                        drawLine(Color.Blue.copy(alpha = 0.04f), Offset(x.toFloat(), 0f), Offset(x.toFloat(), h), 1f)
                    }
                    for (y in 0..h.toInt() step 30) {
                        drawLine(Color.Blue.copy(alpha = 0.04f), Offset(0f, y.toFloat()), Offset(w, y.toFloat()), 1f)
                    }

                    // Render selected furniture shape based on slider proportions
                    val maxDrawWidth = w * 0.5f
                    val maxDrawHeight = h * 0.8f
                    
                    // Normalize slider values (Range: width: 3-10, height: 2-9)
                    val scaleW = (widthFt - 3f) / (10f - 3f) * 0.5f + 0.5f
                    val scaleH = (heightFt - 2f) / (9f - 2f) * 0.5f + 0.5f
                    
                    val fWidth = maxDrawWidth * scaleW
                    val fHeight = maxDrawHeight * scaleH
                    
                    val xPos = (w - fWidth) / 2f
                    val yPos = (h - fHeight) / 2f

                    val fillThemeColor = Color(android.graphics.Color.parseColor("#" + when (selectedWoodType) {
                        "Burmese Teak" -> "C49A5F"
                        "American Oak" -> "E5D5C1"
                        "American Walnut" -> "5C4033"
                        else -> "A0522D"
                    }))

                    when (selectedFurnitureType) {
                        "Wardrobe", "Cabinet" -> {
                            // Main cabinet
                            drawRect(fillThemeColor.copy(alpha = 0.6f), Offset(xPos, yPos), Size(fWidth, fHeight))
                            drawRect(Color.Blue.copy(alpha = 0.3f), Offset(xPos, yPos), Size(fWidth, fHeight), style = Stroke(width = 2.5f))
                            
                            // Splitting shutters
                            val doorsCount = if (widthFt > 6) 3 else 2
                            for (i in 1 until doorsCount) {
                                val dx = xPos + (fWidth / doorsCount) * i
                                drawLine(Color.Blue.copy(alpha = 0.3f), Offset(dx, yPos), Offset(dx, yPos + fHeight), 2f)
                            }
                            // Handles drawing
                            if (selectedHardware != "Push-Latch") {
                                val handleColor = when (selectedHardware) {
                                    "Brushed Brass" -> Color(0xFFD4AF37)
                                    "Antique Copper" -> Color(0xFFB87333)
                                    else -> Color.Black
                                }
                                for (i in 0 until doorsCount) {
                                    val hx = xPos + (fWidth / doorsCount) * i + (fWidth / doorsCount * 0.5f)
                                    drawCircle(handleColor, radius = 4f, center = Offset(hx, yPos + fHeight * 0.5f))
                                }
                            }
                        }
                        "Bed" -> {
                            val bedHeight = fHeight * 0.4f
                            val headboardHeight = fHeight * 0.8f
                            // Headboard
                            drawRect(fillThemeColor.copy(alpha = 0.8f), Offset(xPos, yPos + fHeight - headboardHeight), Size(fWidth * 0.15f, headboardHeight))
                            drawRect(Color.Blue.copy(alpha = 0.3f), Offset(xPos, yPos + fHeight - headboardHeight), Size(fWidth * 0.15f, headboardHeight), style = Stroke(width = 2.5f))
                            // Mattress base
                            drawRect(fillThemeColor.copy(alpha = 0.5f), Offset(xPos + fWidth * 0.15f, yPos + fHeight - bedHeight), Size(fWidth * 0.85f, bedHeight))
                            drawRect(Color.Blue.copy(alpha = 0.3f), Offset(xPos + fWidth * 0.15f, yPos + fHeight - bedHeight), Size(fWidth * 0.85f, bedHeight), style = Stroke(width = 2.5f))
                        }
                        "Table" -> {
                            val tableTopThickness = 12f
                            // Table Top
                            drawRect(fillThemeColor.copy(alpha = 0.8f), Offset(xPos, yPos + fHeight * 0.2f), Size(fWidth, tableTopThickness))
                            drawRect(Color.Blue.copy(alpha = 0.3f), Offset(xPos, yPos + fHeight * 0.2f), Size(fWidth, tableTopThickness), style = Stroke(width = 2f))
                            // Left Leg
                            drawRect(fillThemeColor.copy(alpha = 0.6f), Offset(xPos + fWidth * 0.1f, yPos + fHeight * 0.2f + tableTopThickness), Size(20f, fHeight * 0.8f - tableTopThickness))
                            drawRect(Color.Blue.copy(alpha = 0.3f), Offset(xPos + fWidth * 0.1f, yPos + fHeight * 0.2f + tableTopThickness), Size(20f, fHeight * 0.8f - tableTopThickness), style = Stroke(width = 1.5f))
                            // Right Leg
                            drawRect(fillThemeColor.copy(alpha = 0.6f), Offset(xPos + fWidth * 0.85f, yPos + fHeight * 0.2f + tableTopThickness), Size(20f, fHeight * 0.8f - tableTopThickness))
                            drawRect(Color.Blue.copy(alpha = 0.3f), Offset(xPos + fWidth * 0.85f, yPos + fHeight * 0.2f + tableTopThickness), Size(20f, fHeight * 0.8f - tableTopThickness), style = Stroke(width = 1.5f))
                        }
                    }
                }

                // AR Live Placer trigger button floating
                Button(
                    onClick = { showARPreview = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .testTag("ar_trigger_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Icon(Icons.Filled.Camera, contentDescription = "AR")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(viewModel.translate("ar_preview"), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Interactive selections
                Text(
                    text = "Bespoke Furniture Configurator",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = "Select premium Mumbai timbers and hardware; get dynamic quotes instantly.",
                    fontSize = 12.sp,
                    color = textSecondary
                )
                
                Spacer(modifier = Modifier.height(20.dp))

                // Furniture Category Select
                Text("Furniture Archetype", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Wardrobe", "Cabinet", "Bed", "Table").forEach { type ->
                        val isSelected = selectedFurnitureType == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFurnitureType = type },
                            label = { Text(type, fontSize = 12.sp) },
                            modifier = Modifier.weight(1f).testTag("furniture_type_$type"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = accentColor.copy(alpha = 0.15f),
                                selectedLabelColor = accentColor
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Timber Selection
                Text(viewModel.translate("wood"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Burmese Teak", "American Oak", "American Walnut", "Marine Ply").forEach { wood ->
                        val isSelected = selectedWoodType == wood
                        Card(
                            onClick = { selectedWoodType = wood },
                            modifier = Modifier.fillMaxWidth().testTag("wood_selection_$wood"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) accentColor.copy(alpha = 0.08f) else surfaceColor
                            ),
                            border = BorderStroke(1.dp, if (isSelected) accentColor else textSecondary.copy(alpha = 0.15f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(android.graphics.Color.parseColor("#" + when (wood) {
                                            "Burmese Teak" -> "C49A5F"
                                            "American Oak" -> "E5D5C1"
                                            "American Walnut" -> "5C4033"
                                            else -> "A0522D"
                                        })))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(wood, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    val woodDesc = when (wood) {
                                        "Burmese Teak" -> "Golden premium hardwood. Immune to moisture."
                                        "American Oak" -> "Trendy, pale high-density timber. Stunning modern look."
                                        "American Walnut" -> "Rich luxurious deep chocolate tone. Classy executive aesthetic."
                                        else -> "Waterproof IS:710 Marine grade. Built for Mumbai monsoons."
                                    }
                                    Text(woodDesc, fontSize = 11.sp, color = textSecondary)
                                }
                                if (isSelected) {
                                    Icon(Icons.Filled.Check, contentDescription = "Active", tint = accentColor)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Dimension sliders
                Text(viewModel.translate("dimensions"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                
                // Width
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Width: ${String.format("%.1f", widthFt)} ft", fontSize = 13.sp, color = textPrimary, modifier = Modifier.width(100.dp))
                    Slider(
                        value = widthFt,
                        onValueChange = { widthFt = it },
                        valueRange = 3f..10f,
                        modifier = Modifier.weight(1f).testTag("slider_width"),
                        colors = SliderDefaults.colors(thumbColor = accentColor, activeTrackColor = accentColor)
                    )
                }
                
                // Height
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Height: ${String.format("%.1f", heightFt)} ft", fontSize = 13.sp, color = textPrimary, modifier = Modifier.width(100.dp))
                    Slider(
                        value = heightFt,
                        onValueChange = { heightFt = it },
                        valueRange = 2f..9f,
                        modifier = Modifier.weight(1f).testTag("slider_height"),
                        colors = SliderDefaults.colors(thumbColor = accentColor, activeTrackColor = accentColor)
                    )
                }
                
                // Depth
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Depth: ${String.format("%.1f", depthFt)} ft", fontSize = 13.sp, color = textPrimary, modifier = Modifier.width(100.dp))
                    Slider(
                        value = depthFt,
                        onValueChange = { depthFt = it },
                        valueRange = 1f..3f,
                        modifier = Modifier.weight(1f).testTag("slider_depth"),
                        colors = SliderDefaults.colors(thumbColor = accentColor, activeTrackColor = accentColor)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Finish Select
                Text(viewModel.translate("finish"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Natural Matte", "Satin Polish", "Gloss Acrylic", "Fluted Veneer").forEach { finish ->
                        val isSelected = selectedFinishStyle == finish
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) accentColor.copy(alpha = 0.12f) else surfaceColor)
                                .border(1.dp, if (isSelected) accentColor else textSecondary.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .clickable { selectedFinishStyle = finish }
                                .padding(8.dp)
                                .testTag("finish_$finish"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(finish, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textPrimary, maxLines = 1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hardware Select
                Text(viewModel.translate("hardware"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Brushed Brass", "Antique Copper", "Matte Black", "Push-Latch").forEach { hw ->
                        val isSelected = selectedHardware == hw
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) accentColor.copy(alpha = 0.12f) else surfaceColor)
                                .border(1.dp, if (isSelected) accentColor else textSecondary.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .clickable { selectedHardware = hw }
                                .padding(8.dp)
                                .testTag("hardware_$hw"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(hw, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textPrimary, maxLines = 1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Price estimator summary box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF222222) else Color(0xFFF4EFE6))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Live Showroom Estimate Breakdown", fontSize = 12.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Material Base ($selectedFurnitureType)", fontSize = 13.sp, color = textPrimary)
                            Text(currencyFormatter.format(if (selectedFurnitureType == "Wardrobe") 15000 else 10000), fontSize = 13.sp, color = textPrimary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Timber Grade Multiplier ($selectedWoodType)", fontSize = 13.sp, color = textPrimary)
                            Text("x ${when (selectedWoodType) { "Burmese Teak" -> "2.2" "American Oak" -> "1.8" "American Walnut" -> "2.0" else -> "1.2" }}", fontSize = 13.sp, color = textPrimary)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Sizing Volume Weight (W x H x D)", fontSize = 13.sp, color = textPrimary)
                            Text("x ${String.format("%.2f", (widthFt * heightFt * depthFt) / 36f)}", fontSize = 13.sp, color = textPrimary)
                        }
                        if (selectedFinishStyle != "Natural Matte") {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Premium Finish ($selectedFinishStyle)", fontSize = 13.sp, color = textPrimary)
                                Text("+ " + currencyFormatter.format(if (selectedFinishStyle == "Fluted Veneer") 8500 else 5000), fontSize = 13.sp, color = textPrimary)
                            }
                        }
                        if (selectedHardware != "Push-Latch") {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Hardware Fittings ($selectedHardware)", fontSize = 13.sp, color = textPrimary)
                                Text("+ " + currencyFormatter.format(if (selectedHardware == "Brushed Brass") 2500 else 1500), fontSize = 13.sp, color = textPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = textSecondary.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Estimated Cost (Bandra Showroom)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text(
                                text = currencyFormatter.format(calculatedPrice.toInt()),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Save button
                        var saveMessage by remember { mutableStateOf("") }
                        Button(
                            onClick = {
                                viewModel.saveDesign(
                                    furnitureType = selectedFurnitureType,
                                    woodType = selectedWoodType,
                                    finishType = selectedFinishStyle,
                                    dimensions = "${String.format("%.1f", widthFt)}ft x ${String.format("%.1f", heightFt)}ft x ${String.format("%.1f", depthFt)}ft",
                                    hardwareType = selectedHardware,
                                    estimatedPrice = calculatedPrice
                                )
                                saveMessage = "Design saved to local wishlist!"
                            },
                            modifier = Modifier.fillMaxWidth().testTag("save_custom_design_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Filled.BookmarkAdded, contentDescription = "Save")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Design & Estimate", fontWeight = FontWeight.Bold)
                        }

                        if (saveMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(saveMessage, color = Color(0xFF43A047), fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
                            LaunchedEffect(saveMessage) {
                                delay(3000)
                                saveMessage = ""
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Saved Designs List header
                Text(
                    text = "My Custom Saved Designs (${savedDesigns.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (savedDesigns.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(textSecondary.copy(alpha = 0.04f), RoundedCornerShape(12.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No saved designs yet. Configure and tap Save above!", color = textSecondary, fontSize = 13.sp)
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        savedDesigns.forEach { design ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("saved_design_card_${design.id}"),
                                border = BorderStroke(1.dp, textSecondary.copy(alpha = 0.12f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("${design.furnitureType} - BI#${design.id}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("Timber: ${design.woodType} • Finish: ${design.finishType}", fontSize = 11.sp, color = textSecondary)
                                        Text("Dims: ${design.dimensions} • Hardware: ${design.hardwareType}", fontSize = 11.sp, color = textSecondary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(currencyFormatter.format(design.estimatedPrice.toInt()), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accentColor)
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteDesign(design) },
                                        modifier = Modifier.testTag("delete_saved_design_${design.id}")
                                    ) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
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

// AR placement Simulator Overlay View
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ARSimulatorView(
    furnitureType: String,
    woodColorHex: String,
    onClose: () -> Unit,
    isDark: Boolean
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Local state to track interactive dragging, rotation, and scaling of the furniture
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var scaleFactor by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit) {
        // Automatically request camera permission when opening AR
        cameraPermissionState.launchPermissionRequest()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (cameraPermissionState.status.isGranted) {
            // CameraX Viewport
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().apply {
                            setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Simulated camera room backdrop using Compose Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                // Draw simulated high-end living room walls outline
                drawRect(Color(0xFF2C251C), size = size) // Warm sand backdrop
                // Draw floor angle lines representing 3D perspective
                drawLine(Color.White.copy(alpha = 0.2f), Offset(0f, h * 0.7f), Offset(w, h * 0.7f), 2f)
                drawLine(Color.White.copy(alpha = 0.15f), Offset(0f, h), Offset(w * 0.25f, h * 0.7f), 1.5f)
                drawLine(Color.White.copy(alpha = 0.15f), Offset(w, h), Offset(w * 0.75f, h * 0.7f), 1.5f)
                
                // Draw a nice large window outline with ocean sky view
                drawRect(Color(0xFFE3F2FD).copy(alpha = 0.8f), Offset(w * 0.2f, h * 0.15f), Size(w * 0.6f, h * 0.45f))
                drawRect(Color.White.copy(alpha = 0.6f), Offset(w * 0.2f, h * 0.15f), Size(w * 0.6f, h * 0.45f), style = Stroke(width = 3f))
                drawLine(Color.White.copy(alpha = 0.6f), Offset(w * 0.5f, h * 0.15f), Offset(w * 0.5f, h * 0.6f), 2f)
            }
        }

        // Target Reticle Center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 120.dp),
            contentAlignment = Alignment.Center
        ) {
            // Interactive drag furniture item overlay
            Box(
                modifier = Modifier
                    .offset(x = offsetX.dp, y = offsetY.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x / 2f
                            offsetY += dragAmount.y / 2f
                        }
                    }
                    .size((220 * scaleFactor).dp)
                    .testTag("ar_interactive_furniture_surface"),
                contentAlignment = Alignment.Center
            ) {
                // Interactive Custom drawn 3D-angled wireframe furniture box
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val woodColor = Color(android.graphics.Color.parseColor("#$woodColorHex"))

                    // Rotate drawing block
                    rotate(degrees = rotationAngle, pivot = Offset(w / 2f, h / 2f)) {
                        // Draw perspective cabinet / wardrobe
                        val dW = w * 0.6f
                        val dH = h * 0.75f
                        val dx = (w - dW) / 2f
                        val dy = (h - dH) / 2f

                        // Draw back projection
                        drawRect(
                            color = woodColor.copy(alpha = 0.35f),
                            topLeft = Offset(dx + 25f, dy - 20f),
                            size = Size(dW, dH)
                        )
                        // Draw face projection
                        drawRect(
                            color = woodColor.copy(alpha = 0.85f),
                            topLeft = Offset(dx, dy),
                            size = Size(dW, dH)
                        )
                        // Structural wires
                        drawRect(
                            color = Color.White.copy(alpha = 0.8f),
                            topLeft = Offset(dx, dy),
                            size = Size(dW, dH),
                            style = Stroke(width = 2f)
                        )
                        drawRect(
                            color = Color.White.copy(alpha = 0.4f),
                            topLeft = Offset(dx + 25f, dy - 20f),
                            size = Size(dW, dH),
                            style = Stroke(width = 1f)
                        )
                        // Connecting projection lines
                        drawLine(Color.White.copy(alpha = 0.6f), Offset(dx, dy), Offset(dx + 25f, dy - 20f), 2f)
                        drawLine(Color.White.copy(alpha = 0.6f), Offset(dx + dW, dy), Offset(dx + dW + 25f, dy - 20f), 2f)
                        drawLine(Color.White.copy(alpha = 0.6f), Offset(dx, dy + dH), Offset(dx + 25f, dy + dH - 20f), 2f)
                        drawLine(Color.White.copy(alpha = 0.6f), Offset(dx + dW, dy + dH), Offset(dx + dW + 25f, dy + dH - 20f), 2f)
                    }
                }
            }
        }

        // Transparent control panel bottom overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AR: Placing $furnitureType",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                }
            }
            Text(
                text = "Drag the piece of furniture to reposition. Use sliders below to adjust fit.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Rotation slider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Rotate", fontSize = 12.sp, color = Color.White, modifier = Modifier.width(60.dp))
                Slider(
                    value = rotationAngle,
                    onValueChange = { rotationAngle = it },
                    valueRange = -180f..180f,
                    modifier = Modifier.weight(1f).testTag("ar_rotation_slider"),
                    colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.White)
                )
            }

            // Scale slider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Scale", fontSize = 12.sp, color = Color.White, modifier = Modifier.width(60.dp))
                Slider(
                    value = scaleFactor,
                    onValueChange = { scaleFactor = it },
                    valueRange = 0.5f..1.5f,
                    modifier = Modifier.weight(1f).testTag("ar_scale_slider"),
                    colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.White)
                )
            }
        }
    }
}
