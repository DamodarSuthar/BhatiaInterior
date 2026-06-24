package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BlogArticle
import com.example.data.ProjectItem
import com.example.ui.AppViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onNavigateToBrowse: () -> Unit,
    onNavigateToCustomize: () -> Unit,
    onNavigateToContact: () -> Unit,
    onSelectProject: (ProjectItem) -> Unit,
    onSelectArticle: (BlogArticle) -> Unit
) {
    val scrollState = rememberScrollState()
    val isDark by viewModel.isDarkTheme.collectAsState()
    
    val textPrimary = if (isDark) Color.White else Color(0xFF151515)
    val textSecondary = if (isDark) Color(0xFFB0B0B0) else Color(0xFF707070)
    val containerColor = if (isDark) Color(0xFF222222) else Color(0xFFF4EFE6)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp) // Leave space for bottom nav
    ) {
        // Hero Carousel
        HeroProjectCarousel(
            projects = viewModel.repository.projects,
            onProjectClick = { onSelectProject(it) },
            isDark = isDark
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Categories
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = viewModel.translate("categories"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val categories = listOf(
                    Triple(viewModel.translate("residential"), Icons.Outlined.Home, onNavigateToBrowse),
                    Triple(viewModel.translate("commercial"), Icons.Outlined.Business, onNavigateToBrowse),
                    Triple(viewModel.translate("furniture"), Icons.Outlined.Chair, onNavigateToCustomize),
                    Triple(viewModel.translate("renovations"), Icons.Outlined.Build, onNavigateToBrowse)
                )
                
                categories.forEach { (title, icon, action) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { action() }
                            .testTag("category_card_${title.lowercase()}"),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Dynamic Materials Library Snippet
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewModel.translate("materials_library"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(
                    text = viewModel.translate("view_gallery"),
                    fontSize = 12.sp,
                    color = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToBrowse() }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.repository.materialSwatches.take(4)) { swatch ->
                    Card(
                        modifier = Modifier
                            .width(135.dp)
                            .testTag("material_swatch_${swatch.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            // Swatch visual rendering - stock image with premium gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            ) {
                                coil.compose.AsyncImage(
                                    model = swatch.imageUrl,
                                    contentDescription = swatch.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    error = androidx.compose.ui.graphics.painter.ColorPainter(Color(android.graphics.Color.parseColor("#" + swatch.colorHex)))
                                )
                                // Ambient gradient overlay to ensure text legibility
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                                            )
                                        )
                                )
                                Text(
                                    text = swatch.estimatedPriceUnit,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = swatch.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = swatch.category,
                                    fontSize = 10.sp,
                                    color = textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Blog / Mumbai Guides
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = viewModel.translate("mumbai_homes"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            viewModel.repository.articles.forEach { article ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onSelectArticle(article) }
                        .testTag("blog_card_${article.id}"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF222222) else Color(0xFFF9F6F0)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = article.date,
                                fontSize = 11.sp,
                                color = textSecondary
                            )
                            Text(
                                text = article.readTime,
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.dashPathEffectModifier())
                        Text(
                            text = article.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = article.summary,
                            fontSize = 12.sp,
                            color = textSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fast Action Quote Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onNavigateToContact() }
                .testTag("cta_quote_banner"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF9E845A) else Color(0xFF8B7355)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = viewModel.translate("get_quote"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Schedule standard home layout survey across Mumbai.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Forward",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun HeroProjectCarousel(
    projects: List<ProjectItem>,
    onProjectClick: (ProjectItem) -> Unit,
    isDark: Boolean
) {
    var currentIndex by remember { mutableStateOf(0) }
    
    // Auto rotation delay
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIndex = (currentIndex + 1) % projects.size
        }
    }

    val activeProject = projects[currentIndex]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onProjectClick(activeProject) }
            .testTag("hero_carousel"),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // High fidelity schematic representation of the room instead of plain photo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor("#" + activeProject.colorPrimaryHex)))
        ) {
            // Stylized vector architectural line art to represent interior
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                // Draw floor line
                drawLine(
                    color = Color.Black.copy(alpha = 0.08f),
                    start = androidx.compose.ui.geometry.Offset(0f, h * 0.8f),
                    end = androidx.compose.ui.geometry.Offset(w, h * 0.8f),
                    strokeWidth = 3f
                )
                // Draw fluted wood panel representation
                for (i in 0..12) {
                    val x = w * 0.15f + (i * 18f)
                    if (x < w * 0.45f) {
                        drawLine(
                            color = Color.Black.copy(alpha = 0.05f),
                            start = androidx.compose.ui.geometry.Offset(x, 0f),
                            end = androidx.compose.ui.geometry.Offset(x, h * 0.8f),
                            strokeWidth = 4f
                        )
                    }
                }
                // Draw custom floating platform bed / couch outline
                drawRect(
                    color = Color(android.graphics.Color.parseColor("#" + activeProject.colorSecondaryHex)).copy(alpha = 0.2f),
                    topLeft = androidx.compose.ui.geometry.Offset(w * 0.35f, h * 0.55f),
                    size = androidx.compose.ui.geometry.Size(w * 0.5f, h * 0.25f)
                )
                // Draw frame
                drawRect(
                    color = Color.Black.copy(alpha = 0.15f),
                    topLeft = androidx.compose.ui.geometry.Offset(w * 0.35f, h * 0.55f),
                    size = androidx.compose.ui.geometry.Size(w * 0.5f, h * 0.25f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                )
                // Draw ceiling lamp wire and light cone
                drawLine(
                    color = Color.Black.copy(alpha = 0.2f),
                    start = androidx.compose.ui.geometry.Offset(w * 0.6f, 0f),
                    end = androidx.compose.ui.geometry.Offset(w * 0.6f, h * 0.25f),
                    strokeWidth = 1.5f
                )
                drawCircle(
                    color = Color(0xFFEEDC82).copy(alpha = 0.3f),
                    radius = 45f,
                    center = androidx.compose.ui.geometry.Offset(w * 0.6f, h * 0.27f)
                )
            }

            // Dark subtle overlay gradient simulated
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            // Category tag on top left
            Text(
                text = activeProject.category + " • " + activeProject.location,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )

            // Info on bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = activeProject.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activeProject.description,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Indicators Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    projects.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentIndex) 18.dp else 6.dp, 6.dp)
                                .clip(CircleShape)
                                .background(if (index == currentIndex) Color.White else Color.White.copy(alpha = 0.4f))
                        )
                    }
                }
            }
        }
    }
}

// Extension to simulate elegant dash division line
fun Modifier.dashPathEffectModifier(): Modifier = this
    .padding(vertical = 12.dp)
    .fillMaxWidth()
    .height(1.dp)
    .background(Color.Black.copy(alpha = 0.05f))
