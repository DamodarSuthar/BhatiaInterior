package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceCard
import com.example.ui.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(viewModel: AppViewModel) {
    val context = LocalContext.current
    val isDark by viewModel.isDarkTheme.collectAsState()
    
    val textPrimary = if (isDark) Color.White else Color(0xFF151515)
    val textSecondary = if (isDark) Color(0xFFB0B0B0) else Color(0xFF707070)
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val accentColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355)

    // Booking states
    var selectedServiceForBooking by remember { mutableStateOf("Consultation") }
    var preferredDate by remember { mutableStateOf("") }
    var preferredTime by remember { mutableStateOf("11:00 AM") }
    var bookingClientName by remember { mutableStateOf("") }
    var bookingSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Showroom Legacy
        item {
            Text(
                text = "Bhatia Interior Heritage",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Founded in Mumbai over 35 years ago by master woodworker Lalit Bhatia, we deliver premium bespoke carpentry and architectural turnkey interiors. Our Linking Road Bandra showroom serves homes and commercial offices throughout South Mumbai and the western suburbs.",
                fontSize = 13.sp,
                color = textSecondary,
                lineHeight = 18.sp
            )
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val stats = listOf(
                    Pair("35+", "Years in Timber"),
                    Pair("1,500+", "Mumbai Homes"),
                    Pair("100%", "Monsoon Safe")
                )
                stats.forEach { (title, subtitle) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = textSecondary.copy(alpha = 0.05f)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = accentColor)
                            Text(subtitle, fontSize = 10.sp, color = textSecondary, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }

        // Showroom Map & Address
        item {
            Text(
                text = "Bandra Experience Showroom",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.border(1.dp, textSecondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            ) {
                Column {
                    // Custom drawn vector map representing Bandra Linking Road & Arabian Sea
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(Color(0xFFE3F2FD)) // Soft water blue base
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw ocean area on the left
                            drawRect(Color(0xFFBBDEFB), topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(w * 0.25f, h))
                            
                            // Coastal road outline (Carter Road / Linking Road)
                            drawLine(Color.White, start = Offset(w * 0.25f, 0f), end = Offset(w * 0.25f, h), strokeWidth = 8f)
                            drawLine(Color.Gray.copy(alpha = 0.3f), start = Offset(w * 0.25f, 0f), end = Offset(w * 0.25f, h), strokeWidth = 2f)

                            // Main Roads Grid (Bandra)
                            drawLine(Color.White, start = Offset(w * 0.25f, h * 0.4f), end = Offset(w, h * 0.4f), strokeWidth = 10f)
                            drawLine(Color.White, start = Offset(w * 0.55f, 0f), end = Offset(w * 0.55f, h), strokeWidth = 10f)
                            drawLine(Color.White, start = Offset(w * 0.75f, h * 0.7f), end = Offset(w, h * 0.7f), strokeWidth = 8f)

                            // Small block parks / sites
                            drawRect(Color(0xFFC8E6C9), topLeft = Offset(w * 0.35f, h * 0.1f), size = androidx.compose.ui.geometry.Size(60f, 40f))

                            // Showroom Hotspot locator point
                            drawCircle(Color(0xFFC62828), radius = 10f, center = Offset(w * 0.55f, h * 0.4f))
                            drawCircle(Color(0xFFC62828).copy(alpha = 0.3f), radius = 24f, center = Offset(w * 0.55f, h * 0.4f))
                        }

                        Text(
                            text = "Bhatia Interior Showroom",
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = (-25).dp)
                                .background(Color.White, RoundedCornerShape(2.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Filled.Place, contentDescription = "Address", tint = accentColor, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Linking Road, Bandra West, Mumbai - 400050",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Text(
                                    text = "Opposite National College. Timings: 10:00 AM to 8:00 PM (All Days)",
                                    fontSize = 12.sp,
                                    color = textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // WhatsApp / Call Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Call Now
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:+919876543210")
                            }
                            context.startActivity(intent)
                        }
                        .testTag("action_call_showroom"),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    border = BorderStroke(1.dp, textSecondary.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Phone, contentDescription = "Call", tint = accentColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Call Vinod", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    }
                }

                // WhatsApp Chat
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://wa.me/919876543210?text=Hi%20Bhatia%20Interior%2C%20I'm%20interested%20in%20custom%20woodwork!")
                            }
                            context.startActivity(intent)
                        }
                        .testTag("action_whatsapp_chat"),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    border = BorderStroke(1.dp, textSecondary.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Sms, contentDescription = "WhatsApp", tint = Color(0xFF4CAF50))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("WhatsApp Chat", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    }
                }
            }
        }

        // Capabilities / Services List Accordion
        item {
            Text(
                text = "Our Specialized Workmanship Services",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(viewModel.repository.services) { service ->
            var expanded by remember { mutableStateOf(false) }
            Card(
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .testTag("service_accordion_${service.id}"),
                border = BorderStroke(1.dp, textSecondary.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(service.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text("Standard Schedule: ${service.duration}", fontSize = 11.sp, color = accentColor, fontWeight = FontWeight.Bold)
                        }
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "Expand",
                            tint = textSecondary
                        )
                    }
                    AnimatedVisibility(visible = expanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(service.description, fontSize = 12.sp, color = textSecondary)
                            Spacer(modifier = Modifier.height(8.dp))
                            service.details.forEach { d ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.TaskAlt, contentDescription = "Detail", tint = Color(0xFF43A047), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(d, fontSize = 11.sp, color = textPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Appointment / Booking Form
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Book Site Measurement Visit",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.border(1.dp, textSecondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            ) {
                if (bookingSubmitted) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.Verified, contentDescription = "Success", tint = Color(0xFF43A047), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Showroom Visit Booked!", fontWeight = FontWeight.Bold, color = textPrimary, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Thank you $bookingClientName. We have scheduled your '$selectedServiceForBooking' visit on $preferredDate at $preferredTime. Vinod Bhatia will contact you shortly to confirm.",
                            color = textSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Type Selection
                        Text("Select Visit Type", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = textPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Consultation", "Site Survey", "Material Check").forEach { opt ->
                                val isSelected = selectedServiceForBooking == opt
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) accentColor.copy(alpha = 0.12f) else surfaceColor)
                                        .border(1.dp, if (isSelected) accentColor else textSecondary.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                        .clickable { selectedServiceForBooking = opt }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(opt, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Date Pick input
                        Text("Preferred Date (e.g., June 30, 2026)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = textPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = preferredDate,
                            onValueChange = { preferredDate = it },
                            placeholder = { Text("Enter preferred date", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("booking_date_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                unfocusedBorderColor = textSecondary.copy(alpha = 0.2f),
                                focusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6),
                                unfocusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Client Name Input
                        Text("Your Name", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = textPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = bookingClientName,
                            onValueChange = { bookingClientName = it },
                            placeholder = { Text("Enter your full name", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("booking_name_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                unfocusedBorderColor = textSecondary.copy(alpha = 0.2f),
                                focusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6),
                                unfocusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (bookingClientName.isNotBlank() && preferredDate.isNotBlank()) {
                                    bookingSubmitted = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("submit_booking_btn"),
                            enabled = bookingClientName.isNotBlank() && preferredDate.isNotBlank()
                        ) {
                            Text("Confirm Appointment", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
