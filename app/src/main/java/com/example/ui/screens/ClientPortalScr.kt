package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Inquiry
import com.example.data.ProjectUpdate
import com.example.ui.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientPortalScreen(viewModel: AppViewModel) {
    val isDark by viewModel.isDarkTheme.collectAsState()
    val projectUpdates by viewModel.projectUpdates.collectAsState()
    val inquiries by viewModel.inquiries.collectAsState()
    
    val textPrimary = if (isDark) Color.White else Color(0xFF151515)
    val textSecondary = if (isDark) Color(0xFFB0B0B0) else Color(0xFF707070)
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val accentColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355)

    var activeSubTab by remember { mutableStateOf(0) } // 0 for Project Tracker, 1 for Chat, 2 for Documents, 3 for Get Quote

    Column(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)) {
        // Portal sub navigation bar
        ScrollableTabRow(
            selectedTabIndex = activeSubTab,
            containerColor = if (isDark) Color(0xFF151515) else Color(0xFFF9F6F0),
            contentColor = accentColor,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeSubTab]),
                    color = accentColor
                )
            }
        ) {
            Tab(selected = activeSubTab == 0, onClick = { activeSubTab = 0 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.TrackChanges, contentDescription = "Track")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Tracker", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = activeSubTab == 1, onClick = { activeSubTab = 1 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ChatBubble, contentDescription = "Chat")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat with Vinod", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = activeSubTab == 2, onClick = { activeSubTab = 2 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.FolderZip, contentDescription = "Documents")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Drawings & PDF", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Tab(selected = activeSubTab == 3, onClick = { activeSubTab = 3 }) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AddCard, contentDescription = "New Quote")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Request Visit", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        when (activeSubTab) {
            0 -> TrackerTab(projectUpdates = projectUpdates, inquiries = inquiries, isDark = isDark, textPrimary = textPrimary, textSecondary = textSecondary, accentColor = accentColor, surfaceColor = surfaceColor)
            1 -> ChatTab(viewModel = viewModel, isDark = isDark, textPrimary = textPrimary, textSecondary = textSecondary, accentColor = accentColor, surfaceColor = surfaceColor)
            2 -> DocumentsTab(isDark = isDark, textPrimary = textPrimary, textSecondary = textSecondary, accentColor = accentColor, surfaceColor = surfaceColor)
            3 -> GetQuoteTab(viewModel = viewModel, isDark = isDark, textPrimary = textPrimary, textSecondary = textSecondary, accentColor = accentColor, surfaceColor = surfaceColor)
        }
    }
}

@Composable
fun TrackerTab(
    projectUpdates: List<ProjectUpdate>,
    inquiries: List<Inquiry>,
    isDark: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    accentColor: Color,
    surfaceColor: Color
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (projectUpdates.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, textSecondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Analytics, contentDescription = "No project", tint = textSecondary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No active construction project connected yet.", color = textPrimary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Submit a Quote Request or Contact our showroom to connect your ongoing project.", color = textSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        } else {
            items(projectUpdates) { update ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("active_tracker_card"),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(update.projectTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Box(
                                modifier = Modifier
                                    .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("In Progress", fontSize = 10.sp, color = accentColor, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Site: ${update.siteAddress}", fontSize = 12.sp, color = textSecondary)
                        Text("Designer: ${update.designerName} • Est. Handover: ${update.estimatedHandover}", fontSize = 12.sp, color = textSecondary)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress numeric and bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Current Phase: ${update.currentStepName}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text("${update.progressPercent}%", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = accentColor)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = update.progressPercent / 100f,
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                            color = accentColor,
                            trackColor = textSecondary.copy(alpha = 0.15f)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Progress Step Timelines Vector
                        Text("Progress Milestones", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Spacer(modifier = Modifier.height(12.dp))

                        val steps = listOf("Survey", "3D Design", "Masonry", "Carpentry", "Polish", "Handover")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            steps.forEachIndexed { index, step ->
                                val stepIdx = index + 1
                                val isCompleted = stepIdx < update.currentStepIndex
                                val isActive = stepIdx == update.currentStepIndex
                                val circleColor = when {
                                    isCompleted -> accentColor
                                    isActive -> Color(0xFF43A047)
                                    else -> textSecondary.copy(alpha = 0.3f)
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(circleColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isCompleted) {
                                            Icon(Icons.Filled.Check, contentDescription = "Done", tint = Color.White, modifier = Modifier.size(14.dp))
                                        } else {
                                            Text(stepIdx.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = step,
                                        fontSize = 9.sp,
                                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isActive) Color(0xFF43A047) else textSecondary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = textSecondary.copy(alpha = 0.15f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Status message block
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Filled.Feed, contentDescription = "Status Update", tint = accentColor, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = update.statusMessage,
                                fontSize = 12.sp,
                                color = textPrimary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Submissions header
        if (inquiries.isNotEmpty()) {
            item {
                Text("My Submitted Quote Requests", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(inquiries) { inq ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, textSecondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(inq.spaceType, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE2EFE0), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(inq.status, fontSize = 9.sp, color = Color(0xFF43A047), fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Requirements: ${inq.requirements}", fontSize = 11.sp, color = textSecondary)
                        Text("Budget limit: ${inq.budgetRange}", fontSize = 11.sp, color = textSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatTab(
    viewModel: AppViewModel,
    isDark: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    accentColor: Color,
    surfaceColor: Color
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isTyping by viewModel.isTeamTyping.collectAsState()
    var messageText by remember { mutableStateOf("") }
    
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll chat to end when new messages arrive
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Chat Header Team Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(textSecondary.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Text("VB", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Vinod Bhatia", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text("Active Showroom Designer • Bandra Office", fontSize = 11.sp, color = textSecondary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chat List Box
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(messages) { msg ->
                    val isUser = msg.isFromUser
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Card(
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp,
                                bottomStart = if (isUser) 12.dp else 0.dp,
                                bottomEnd = if (isUser) 0.dp else 12.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUser) accentColor else surfaceColor
                            ),
                            border = if (!isUser) BorderStroke(1.dp, textSecondary.copy(alpha = 0.12f)) else null,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = msg.message,
                                    fontSize = 13.sp,
                                    color = if (isUser) Color.White else textPrimary
                                )
                            }
                        }
                    }
                }

                // Team typing indicator
                if (isTyping) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(accentColor))
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(accentColor.copy(alpha = 0.6f)))
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(accentColor.copy(alpha = 0.3f)))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Vinod is typing...", fontSize = 11.sp, color = textSecondary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chat Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text(viewModel.translate("chat_hint"), fontSize = 13.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = textSecondary.copy(alpha = 0.2f),
                    focusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6),
                    unfocusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6)
                ),
                maxLines = 2,
                singleLine = false
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendUserMessage(messageText)
                        messageText = ""
                    }
                },
                modifier = Modifier
                    .background(accentColor, CircleShape)
                    .testTag("chat_send_button")
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
fun DocumentsTab(
    isDark: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    accentColor: Color,
    surfaceColor: Color
) {
    // Standard visual components for Documents and rating survey
    var userRating by remember { mutableIntStateOf(0) }
    var reviewComment by remember { mutableStateOf("") }
    var ratingSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Client Files & Shared Documents", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Download active architectural blue layouts, material lists, and approved cost sheets.", fontSize = 12.sp, color = textSecondary)
        }

        val documents = listOf(
            Triple("Bandra_Apartment_Floorplan_V2.pdf", "Architectural 2D Layout", "2.4 MB"),
            Triple("Custom_Wardrobe_Materials_Approved.pdf", "Timber Specification Ledger", "840 KB"),
            Triple("Estimate_Proposal_Showroom_Invoice.pdf", "Receipt Invoice & Balance", "1.2 MB")
        )

        items(documents) { (name, type, size) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, textSecondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.PictureAsPdf, contentDescription = "PDF", tint = Color.Red, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text("$type • $size", fontSize = 11.sp, color = textSecondary)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Download, contentDescription = "Download", tint = accentColor)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = textSecondary.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(12.dp))

            Text("Rate Our Workmanship", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text("How satisfied are you with our wood quality and site execution?", fontSize = 12.sp, color = textSecondary)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, textSecondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            ) {
                if (ratingSubmitted) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.ThumbUp, contentDescription = "Success", tint = Color(0xFF43A047), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Thank you for your valuable feedback!", fontWeight = FontWeight.Bold, color = textPrimary)
                        Text("Vinod and the design team will review your remarks.", color = textSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                } else {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (i in 1..5) {
                                val isSelected = i <= userRating
                                Icon(
                                    imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "$i Stars",
                                    tint = if (isSelected) Color(0xFFFBC02D) else textSecondary,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable { userRating = i }
                                        .testTag("feedback_star_$i")
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = reviewComment,
                            onValueChange = { reviewComment = it },
                            placeholder = { Text("Write your comments here...", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("feedback_comment_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                unfocusedBorderColor = textSecondary.copy(alpha = 0.2f),
                                focusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6),
                                unfocusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6)
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { if (userRating > 0) ratingSubmitted = true },
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("submit_feedback_btn"),
                            enabled = userRating > 0
                        ) {
                            Text("Submit Workmanship Review", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GetQuoteTab(
    viewModel: AppViewModel,
    isDark: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    accentColor: Color,
    surfaceColor: Color
) {
    var spaceType by remember { mutableStateOf("Residential Living Room") }
    var requirements by remember { mutableStateOf("") }
    var budgetRange by remember { mutableStateOf("₹2 - ₹5 Lakhs") }
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var formSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (formSubmitted) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = surfaceColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = "Done", tint = Color(0xFF43A047), modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Inquiry Submitted Successfully!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Thank you, $clientName. Vinod Bhatia will review your requirements for '$spaceType' and call you shortly on $clientPhone. We have also started a thread for you in our Chat tab!",
                            color = textSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        } else {
            item {
                Text(viewModel.translate("get_quote"), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text("Schedule an experienced site surveyor to visit your Mumbai home and take measurements.", fontSize = 12.sp, color = textSecondary)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Space Type dropdown list
            item {
                Text("Type of Space", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                val spaceOptions = listOf("Residential Living Room", "Modular Kitchen", "Full 2BHK/3BHK Home", "Commercial Office Cabin", "Bespoke Wardrobe")
                var dropdownExpanded by remember { mutableStateOf(false) }
                
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { dropdownExpanded = true },
                        modifier = Modifier.fillMaxWidth().testTag("quote_space_dropdown_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(spaceType, color = textPrimary)
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown", tint = textSecondary)
                        }
                    }
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        spaceOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    spaceType = opt
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Requirements Details
            item {
                Text("Specific Requirements", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = requirements,
                    onValueChange = { requirements = it },
                    placeholder = { Text(viewModel.translate("requirement_prompt"), fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("quote_req_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = textSecondary.copy(alpha = 0.2f),
                        focusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6),
                        unfocusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6)
                    ),
                    maxLines = 4
                )
            }

            // Budget limits select
            item {
                Text("Estimated Budget Limits", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("₹1 - ₹3 Lakhs", "₹3 - ₹5 Lakhs", "₹5 - ₹10 Lakhs+").forEach { range ->
                        val isSelected = budgetRange == range
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) accentColor.copy(alpha = 0.12f) else surfaceColor)
                                .border(1.dp, if (isSelected) accentColor else textSecondary.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                .clickable { budgetRange = range }
                                .padding(10.dp)
                                .testTag("quote_budget_range_$range"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(range, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        }
                    }
                }
            }

            // Simulated Photo upload placeholder
            item {
                Text("Upload Photo of Space (Optional)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                var hasSelectedPhoto by remember { mutableStateOf(false) }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(textSecondary.copy(alpha = 0.05f))
                        .border(1.dp, textSecondary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .clickable { hasSelectedPhoto = !hasSelectedPhoto }
                        .testTag("quote_photo_upload_zone"),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasSelectedPhoto) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.InsertPhoto, contentDescription = "Photo", tint = Color(0xFF43A047), modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("living_room_raw.png Selected • Tap to remove", color = Color(0xFF43A047), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.CloudUpload, contentDescription = "Upload", tint = textSecondary, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tap to simulate space photo upload", color = textSecondary, fontSize = 12.sp)
                            Text("Accepts PNG, JPEG, PDF plans", color = textSecondary.copy(alpha = 0.6f), fontSize = 10.sp)
                        }
                    }
                }
            }

            // Contact Info
            item {
                Text("Contact Details", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = textPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                
                // Name Input
                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    placeholder = { Text(viewModel.translate("full_name"), fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("quote_name_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = textSecondary.copy(alpha = 0.2f),
                        focusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6),
                        unfocusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6)
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                // Phone Input
                OutlinedTextField(
                    value = clientPhone,
                    onValueChange = { clientPhone = it },
                    placeholder = { Text(viewModel.translate("phone"), fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("quote_phone_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = textSecondary.copy(alpha = 0.2f),
                        focusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6),
                        unfocusedContainerColor = if (isDark) Color(0xFF242424) else Color(0xFFF5F0E6)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        if (clientName.isNotBlank() && clientPhone.isNotBlank()) {
                            viewModel.submitInquiry(
                                spaceType = spaceType,
                                requirements = requirements,
                                budgetRange = budgetRange,
                                clientName = clientName,
                                clientPhone = clientPhone
                            )
                            formSubmitted = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("quote_submit_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    shape = RoundedCornerShape(12.dp),
                    enabled = clientName.isNotBlank() && clientPhone.isNotBlank()
                ) {
                    Text(viewModel.translate("submit"), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
