package com.example.pasteleriamilsabores.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.pasteleriamilsabores.utils.ImageUtils
import com.example.pasteleriamilsabores.viewmodel.AuthViewModel
import com.example.pasteleriamilsabores.viewmodel.ProductViewModel
import java.io.File

@Composable
fun AddProductScreen(
    productViewModel: ProductViewModel,
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var currentPhotoFile by remember { mutableStateOf<File?>(null) }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val productState by productViewModel.productState.collectAsState()
    val user by authViewModel.currentUser.collectAsState()

    // 1. PRIMERO: Launcher para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoFile != null) {
            val savedPath = ImageUtils.saveImageToInternalStorage(
                context,
                ImageUtils.getUriForFile(context, currentPhotoFile!!)
            )
            if (savedPath != null) {
                imagePath = savedPath
                imageUri = Uri.fromFile(File(savedPath))
            }
        }
    }

    // 2. SEGUNDO: Launcher para seleccionar de galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val savedPath = ImageUtils.saveImageToInternalStorage(context, it)
            if (savedPath != null) {
                imagePath = savedPath
                imageUri = Uri.fromFile(File(savedPath))
            }
        }
    }

    // 3. TERCERO: Launcher para pedir permisos (usa takePictureLauncher)
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            currentPhotoFile = ImageUtils.createImageFile(context)
            currentPhotoFile?.let { file ->
                val uri = ImageUtils.getUriForFile(context, file)
                takePictureLauncher.launch(uri)
            }
        }
    }

    LaunchedEffect(productState) {
        when (val state = productState) {
            is ProductViewModel.ProductState.Success -> {
                showError = false
                onNavigateBack()
            }
            is ProductViewModel.ProductState.Error -> {
                errorMessage = state.message
                showError = true
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Agregar Producto",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AnimatedVisibility(visible = showError) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Vista previa de imagen
        if (imageUri != null) {
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Imagen del producto",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Botones de imagen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (hasCameraPermission) {
                        currentPhotoFile = ImageUtils.createImageFile(context)
                        currentPhotoFile?.let { file ->
                            val uri = ImageUtils.getUriForFile(context, file)
                            takePictureLauncher.launch(uri)
                        }
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Tomar Foto")
            }

            OutlinedButton(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Galería")
            }
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val currentUser = user
                if (currentUser == null) {
                    errorMessage = "Error: Usuario no autenticado"
                    showError = true
                } else {
                    productViewModel.addProduct(title, price, description, imagePath, currentUser.id)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}