package dev.volumepanel.app

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle

/**
 * App que apenas dispara o popup deslizante do seletor de volume de mídia.
 * Ao tocar no ícone, a Activity abre, chama o painel do sistema e fecha-se
 * imediatamente — o popup permanece na tela.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // adjustVolume com ADJUST_SAME + FLAG_SHOW_UI faz o Android desenhar
        // o slider de volume correspondente à stream ativa sem alterar o nível.
        // Para garantir que seja o de MÚSIA, chamamos setStreamVolume na stream
        // STREAM_MUSIC com FLAG_SHOW_UI passando o volume atual.
        try {
            am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_SHOW_UI
            )
        } catch (e: SecurityException) {
            // Em versões onde ajustar volume de mídia requer permissão,
            // faz o fallback via adjustVolume.
            android.util.Log.w("VolumePanel", "setStreamVolume falhou: ${e.message}")
            am.adjustVolume(AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI)
        }

        // Fecha a Activity sem animação — o popup do sistema permanece.
        @Suppress("DEPRECATION")
        overridePendingTransition(0, 0)
        finish()
    }
}
