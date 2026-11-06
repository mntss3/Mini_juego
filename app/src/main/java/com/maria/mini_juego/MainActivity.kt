package com.maria.mini_juego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ---------- COLORES ----------

private val Oscuro = Color(0xFF2D1B4E)
private val Amarillo = Color(0xFFFFD43B)
private val AmarilloClaro = Color(0xFFFFE680)
private val AzulReloj = Color(0xFF5AA9FF)
private val GrisTexto = Color(0xFF8A7FA8)
private val RosaVida = Color(0xFFFF6B9D)
private val LilaVacio = Color(0xFFEBDDFF)
private val VerdeTablero = Color(0xFF5DD36F)
private val Tierra = Color(0xFFB8743C)
private val Hoyo = Color(0xFF5A3417)
private val NarizRosa = Color(0xFFE5486F)
private val Moflete = Color(0x66FF6B9D)
private val RosaMartillo = Color(0xFFFF5C8A)
private val NaranjaPuntos = Color(0xFFFF8A00)

private val FondoDegradado = listOf(
    Color(0xFF7C5CFA),
    Color(0xFFC98AE0),
    Color(0xFFFFB27A),
)

// ---------- DATOS DEL TABLERO ----------

private data class ColorTopo(val cuerpo: Color, val barriga: Color)

private val TopoRosa = ColorTopo(Color(0xFFFF7EB6), Color(0xFFFFC7DF))
private val TopoAzul = ColorTopo(Color(0xFF4DA3FF), Color(0xFFBADDFF))
private val TopoAmarillo = ColorTopo(Color(0xFFFFC93C), Color(0xFFFFE9A8))
private val TopoMorado = ColorTopo(Color(0xFF9B6BFF), Color(0xFFD7C5FF))
private val TopoTurquesa = ColorTopo(Color(0xFF2EC4B6), Color(0xFFB8F0EA))
private val TopoNaranja = ColorTopo(Color(0xFFFF9248), Color(0xFFFFD2B3))

private data class Celda(
    val topo: ColorTopo? = null,
    val corona: Boolean = false,
    val golpeado: Boolean = false,
)

private val celdas = listOf(
    Celda(TopoRosa), Celda(), Celda(TopoAzul),
    Celda(), Celda(TopoAmarillo, corona = true), Celda(TopoMorado, golpeado = true),
    Celda(TopoTurquesa), Celda(TopoNaranja), Celda(),
)

// ---------- ACTIVITY ----------

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantallaTopoLoco()
        }
    }
}

// ---------- PANTALLA ----------

@Composable
fun PantallaTopoLoco() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(FondoDegradado)),
    ) {
        Confeti(Modifier.matchParentSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Titulo()

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Marcador(
                    titulo = "PUNTOS",
                    valor = "1.250",
                    colorIcono = Amarillo,
                    modifier = Modifier.weight(1f),
                ) { IconoEstrella() }

                Marcador(
                    titulo = "TIEMPO",
                    valor = "0:45",
                    colorIcono = AzulReloj,
                    modifier = Modifier.weight(1f),
                ) { IconoReloj() }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Corazon(lleno = true)
                Spacer(Modifier.width(4.dp))
                Corazon(lleno = true)
                Spacer(Modifier.width(4.dp))
                Corazon(lleno = false)
                Spacer(Modifier.weight(1f))
                Nivel(numero = 3)
            }

            Spacer(Modifier.height(16.dp))

            Tablero(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        }
    }
}

// ---------- CABECERA ----------

@Composable
private fun Titulo() {
    Box {
        Text(                      // borde negro letras del título
            text = "TOPO LOCO",
            style = TextStyle(
                fontSize = 46.sp,
                fontWeight = FontWeight.Black,
                color = Oscuro,
                drawStyle = Stroke(width = 14f, join = StrokeJoin.Round),
            ),
        )
        Text(                      // relleno amarillo letras del título
            text = "TOPO LOCO",
            style = TextStyle(
                fontSize = 46.sp,
                fontWeight = FontWeight.Black,
                color = Amarillo,
            ),
        )
    }
}

@Composable
private fun Marcador(
    titulo: String,
    valor: String,
    colorIcono: Color,
    modifier: Modifier = Modifier,
    icono: @Composable () -> Unit,
) {
    val forma = RoundedCornerShape(50)

    Row(
        modifier = modifier
            .height(64.dp)
            .background(Color.White, forma)
            .border(3.dp, Oscuro, forma)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(colorIcono, CircleShape)
                .border(3.dp, Oscuro, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            icono()
        }

        Spacer(Modifier.width(8.dp))

        Column {
            Text(text = titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GrisTexto)
            Text(text = valor, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Oscuro)
        }
    }
}

@Composable
private fun IconoEstrella() {
    Canvas(Modifier.size(28.dp)) {
        val estrella = poligonoEstrella(center, size.minDimension / 2, size.minDimension * 0.22f, 5)
        drawPath(estrella, AmarilloClaro)
        drawPath(estrella, Oscuro, style = Stroke(2.5.dp.toPx(), join = StrokeJoin.Round))
    }
}

@Composable
private fun IconoReloj() {
    Canvas(Modifier.size(28.dp)) {
        val radio = size.minDimension / 2
        val grosor = 2.5.dp.toPx()
        drawCircle(Color.White, radio)
        drawCircle(Oscuro, radio, style = Stroke(grosor))
        drawLine(Oscuro, center, Offset(center.x, center.y - radio * 0.6f), grosor, StrokeCap.Round)
        drawLine(Oscuro, center, Offset(center.x + radio * 0.45f, center.y), grosor, StrokeCap.Round)
    }
}

@Composable
private fun Corazon(lleno: Boolean) {
    Canvas(Modifier.size(30.dp)) {
        val w = size.width
        val h = size.height
        val corazon = Path().apply {
            moveTo(w * 0.5f, h * 0.88f)
            cubicTo(w * 0.1f, h * 0.6f, -w * 0.05f, h * 0.25f, w * 0.25f, h * 0.12f)
            cubicTo(w * 0.4f, h * 0.05f, w * 0.5f, h * 0.2f, w * 0.5f, h * 0.27f)
            cubicTo(w * 0.5f, h * 0.2f, w * 0.6f, h * 0.05f, w * 0.75f, h * 0.12f)
            cubicTo(w * 1.05f, h * 0.25f, w * 0.9f, h * 0.6f, w * 0.5f, h * 0.88f)
            close()
        }
        drawPath(corazon, if (lleno) RosaVida else LilaVacio)
        drawPath(corazon, Oscuro, style = Stroke(2.5.dp.toPx(), join = StrokeJoin.Round))
    }
}

@Composable
private fun Nivel(numero: Int) {
    Box(
        modifier = Modifier
            .background(Oscuro, RoundedCornerShape(50))
            .padding(horizontal = 28.dp, vertical = 8.dp),
    ) {
        Text(text = "NIVEL $numero", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
    }
}

// ---------- TABLERO ----------

@Composable
private fun Tablero(modifier: Modifier = Modifier) {
    val forma = RoundedCornerShape(32.dp)

    Column(
        modifier = modifier
            .background(VerdeTablero, forma)
            .border(4.dp, Oscuro, forma)
            .padding(8.dp),
    ) {
        celdas.chunked(3).forEach { fila ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                fila.forEach { celda ->
                    Agujero(
                        celda = celda,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
            }
        }
    }
}

@Composable
private fun Agujero(celda: Celda, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(bottom = 10.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AgujeroFondo(Modifier.size(width = 92.dp, height = 38.dp))

        if (celda.topo != null) {
            Topo(
                colores = celda.topo,
                corona = celda.corona,
                aturdido = celda.golpeado,
                modifier = Modifier.offset(y = (-14).dp),
            )
            AgujeroDelante(Modifier.size(width = 92.dp, height = 38.dp))
        }

        if (celda.golpeado) {
            Golpe(Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun AgujeroFondo(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val grosor = 3.dp.toPx()
        val margen = 10.dp.toPx()
        drawOval(Tierra)
        drawOval(Oscuro, style = Stroke(grosor))
        drawOval(
            color = Hoyo,
            topLeft = Offset(margen, margen * 0.6f),
            size = Size(size.width - margen * 2, size.height - margen * 1.2f),
        )
    }
}

@Composable
private fun AgujeroDelante(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        drawArc(Tierra, startAngle = 0f, sweepAngle = 180f, useCenter = true)
        drawArc(Oscuro, startAngle = 0f, sweepAngle = 180f, useCenter = false, style = Stroke(3.dp.toPx()))
    }
}

// ---------- TOPO ----------

@Composable
private fun Topo(
    colores: ColorTopo,
    corona: Boolean,
    aturdido: Boolean,
    modifier: Modifier = Modifier,
) {
    val formaCuerpo = RoundedCornerShape(topStart = 34.dp, topEnd = 34.dp)

    Box(modifier.size(width = 76.dp, height = 104.dp)) {
        Oreja(colores.cuerpo, Modifier.align(Alignment.TopStart).offset(x = 6.dp, y = 14.dp))
        Oreja(colores.cuerpo, Modifier.align(Alignment.TopEnd).offset(x = (-6).dp, y = 14.dp))

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(width = 68.dp, height = 84.dp)
                .background(colores.cuerpo, formaCuerpo)
                .border(3.dp, Oscuro, formaCuerpo),
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 3.dp)
                .size(width = 44.dp, height = 32.dp)
                .background(colores.barriga, RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 10.dp, y = 60.dp)
                .size(width = 12.dp, height = 7.dp)
                .background(Moflete, CircleShape),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-10).dp, y = 60.dp)
                .size(width = 12.dp, height = 7.dp)
                .background(Moflete, CircleShape),
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 38.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Ojo(aturdido)
                Ojo(aturdido)
            }

            Spacer(Modifier.height(2.dp))

            Box(
                modifier = Modifier
                    .size(width = 16.dp, height = 10.dp)
                    .background(NarizRosa, CircleShape)
                    .border(2.dp, Oscuro, CircleShape),
            )

            Row {
                Diente()
                Diente()
            }
        }

        if (corona) {
            Corona(Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun Oreja(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(20.dp)
            .background(color, CircleShape)
            .border(3.dp, Oscuro, CircleShape),
    )
}

@Composable
private fun Ojo(aturdido: Boolean) {
    if (aturdido) {
        Canvas(Modifier.size(18.dp)) {
            val grosor = 3.dp.toPx()
            val m = 3.dp.toPx()
            drawLine(Oscuro, Offset(m, m), Offset(size.width - m, size.height - m), grosor, StrokeCap.Round)
            drawLine(Oscuro, Offset(size.width - m, m), Offset(m, size.height - m), grosor, StrokeCap.Round)
        }
    } else {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(Color.White, CircleShape)
                .border(2.dp, Oscuro, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .background(Oscuro, CircleShape),
            )
        }
    }
}

@Composable
private fun Diente() {
    Box(
        modifier = Modifier
            .size(width = 8.dp, height = 10.dp)
            .background(Color.White)
            .border(2.dp, Oscuro),
    )
}

@Composable
private fun Corona(modifier: Modifier = Modifier) {
    Canvas(modifier.size(width = 40.dp, height = 26.dp)) {
        val w = size.width
        val h = size.height
        val corona = Path().apply {
            moveTo(0f, h)
            lineTo(0f, h * 0.3f)
            lineTo(w * 0.25f, h * 0.6f)
            lineTo(w * 0.5f, 0f)
            lineTo(w * 0.75f, h * 0.6f)
            lineTo(w, h * 0.3f)
            lineTo(w, h)
            close()
        }
        drawPath(corona, Amarillo)
        drawPath(corona, Oscuro, style = Stroke(2.5.dp.toPx(), join = StrokeJoin.Round))
    }
}

// ---------- GOLPE: martillo, ¡POW! y +50 ----------

@Composable
private fun Golpe(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp),
    ) {
        Text(
            text = "+50",
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-8).dp, y = 6.dp),
            color = NaranjaPuntos,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 10.dp, y = 0.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(Modifier.size(66.dp)) {
                val explosion = poligonoEstrella(center, size.minDimension / 2, size.minDimension * 0.34f, 10)
                drawPath(explosion, AmarilloClaro)
                drawPath(explosion, Oscuro, style = Stroke(2.5.dp.toPx(), join = StrokeJoin.Round))
            }
            Text(text = "¡POW!", color = NarizRosa, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }

        Martillo(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = 24.dp, y = (-52).dp),
        )
    }
}

@Composable
private fun Martillo(modifier: Modifier = Modifier) {
    Canvas(modifier.size(90.dp)) {
        val grosor = 3.dp.toPx()
        val radio = CornerRadius(8.dp.toPx())

        rotate(degrees = -40f) {
            val mangoPos = Offset(center.x - 6.dp.toPx(), center.y)
            val mangoTam = Size(12.dp.toPx(), 40.dp.toPx())
            drawRoundRect(Amarillo, mangoPos, mangoTam, radio)
            drawRoundRect(Oscuro, mangoPos, mangoTam, radio, style = Stroke(grosor))

            val cabezaPos = Offset(center.x - 34.dp.toPx(), center.y - 26.dp.toPx())
            val cabezaTam = Size(68.dp.toPx(), 30.dp.toPx())
            val banda = Size(8.dp.toPx(), cabezaTam.height)
            drawRoundRect(RosaMartillo, cabezaPos, cabezaTam, radio)
            drawRect(Amarillo, Offset(cabezaPos.x + 8.dp.toPx(), cabezaPos.y), banda)
            drawRect(Amarillo, Offset(cabezaPos.x + cabezaTam.width - 16.dp.toPx(), cabezaPos.y), banda)
            drawRoundRect(Oscuro, cabezaPos, cabezaTam, radio, style = Stroke(grosor))
        }
    }
}

// ---------- DECORACIÓN ----------

@Composable
private fun Confeti(modifier: Modifier = Modifier) {
    val trozos = listOf(
        Triple(0.08f, 0.05f, Color(0xFFFFD43B)),
        Triple(0.22f, 0.12f, Color(0xFF5AA9FF)),
        Triple(0.40f, 0.03f, Color(0xFFFF6B9D)),
        Triple(0.62f, 0.10f, Color(0xFF2EC4B6)),
        Triple(0.80f, 0.04f, Color(0xFFFFD43B)),
        Triple(0.92f, 0.14f, Color(0xFFFF6B9D)),
        Triple(0.15f, 0.22f, Color(0xFF2EC4B6)),
        Triple(0.55f, 0.21f, Color(0xFFFFFFFF)),
        Triple(0.86f, 0.24f, Color(0xFF5AA9FF)),
    )

    Canvas(modifier) {
        trozos.forEachIndexed { i, (x, y, color) ->
            val posicion = Offset(size.width * x, size.height * y)
            rotate(degrees = if (i % 2 == 0) 30f else -25f, pivot = posicion) {
                drawRoundRect(
                    color = color,
                    topLeft = posicion,
                    size = Size(12.dp.toPx(), 4.dp.toPx()),
                    cornerRadius = CornerRadius(2.dp.toPx()),
                )
            }
        }
    }
}

// ---------- UTILIDADES ----------

private fun poligonoEstrella(centro: Offset, radioFuera: Float, radioDentro: Float, puntas: Int): Path {
    return Path().apply {
        for (i in 0 until puntas * 2) {
            val radio = if (i % 2 == 0) radioFuera else radioDentro
            val angulo = PI * i / puntas - PI / 2
            val x = centro.x + radio * cos(angulo).toFloat()
            val y = centro.y + radio * sin(angulo).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
}

// ---------- PREVIEW ----------

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun PantallaTopoLocoPreview() {
    PantallaTopoLoco()
}